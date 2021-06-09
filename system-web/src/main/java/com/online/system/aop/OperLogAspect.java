package com.online.system.aop;

import com.online.system.annotation.OperLog;
import com.online.system.web.service.LogService;
import com.online.system.web.entity.OperationLog;
import com.online.system.common.utils.IpAddressUtil;
import com.online.system.common.utils.JasonUtil;
import com.online.system.common.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 切面处理类，操作日志异常日志记录处理
 */
@Aspect
@Component
@Slf4j
public class OperLogAspect {

    @Value("${version}")
    private String version;

    @Resource
    private LogService logService;

    //日志层切点
    @Pointcut("@annotation(com.online.system.annotation.OperLog)")
    public void operLogPoinCut() {
    }

    //设置操作异常切入点记录异常日志 扫描所有service包下操作
    @Pointcut("execution(* com.online.system.web.handler..*.*(..))")
    public void operExceptionLogPoinCut() {
    }

    /**
     * @Description 前置通知  用于拦截Controller层记录用户的操作
     */

    @AfterReturning(value = "operLogPoinCut()", returning = "keys")
    public void saveOperLog(JoinPoint joinPoint, Object keys) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //通过shiro读取用户
        //Subject subject = SecurityUtils.getSubject();
        String username = SecurityUtil.getUsername();
        String ip = IpAddressUtil.getIpAdrress(request);
        //获取用户请求方法的参数并序列化为JSON格式字符串
        Map<String,String> params = converMap(request.getParameterMap());
        try {
            OperationLog oper = new OperationLog();
            // 从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
            // 获取操作
            OperLog opLog = method.getAnnotation(OperLog.class);
            if (opLog != null) {
                String module = opLog.module();
                String type = opLog.type();
                String desc = opLog.desc();
                oper.setModule(module); // 操作模块
                oper.setOperType(type); // 操作类型
                oper.setDescription(desc); // 操作描述
            }
            // 获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            // 获取请求的方法名
            String methodName = method.getName();
            methodName = className + "." + methodName;
            oper.setMethod(methodName); // 请求方法
            //*========控制台输出=========*//
            log.info("==============前置通知开始==============");
            log.info("请求方法" + methodName);
            log.info("方法描述：" + getMethodDescription(joinPoint));
            log.info("请求人：" + username);
            log.info("请求ip：" + ip);
            log.info("请求浏览器:" + IpAddressUtil.getBrowser(request));
            log.info("请求操作系统:" + IpAddressUtil.getOs(request));
            log.info("请求参数:" + JasonUtil.obj2String(params));

            //*========数据库日志=========*//
            oper.setReqParam(JasonUtil.obj2String(params)); // 请求参数
            oper.setUsername(username); // 请求用户ID
            oper.setName(username); // 请求用户名称
            oper.setLogType("info");//日志类型
            oper.setIp(ip); // 请求IP
            oper.setBrowser(IpAddressUtil.getBrowser(request));
            oper.setOperSys(IpAddressUtil.getOs(request));
            oper.setUrl(request.getRequestURI()); // 请求URI
            oper.setCreateTime(new Date()); // 创建时间
            oper.setVersion(version); // 操作版本
            //保存到数据库
            logService.add(oper);
        } catch (Exception e) {
            //记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息：{}", e.getMessage());
        }
    }

    /**
     * @Description 异常通知 用于拦截service层记录异常日志
     */
    @AfterThrowing(pointcut = "operExceptionLogPoinCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        //通过shiro读取用户
        String username = SecurityUtil.getUsername();
        //获取请求ip
        String ip = IpAddressUtil.getIpAdrress(request);
        //获取用户请求方法的参数并序列化为JSON格式字符串
        Map<String,String> params = converMap(request.getParameterMap());
        try {
            OperationLog oper = new OperationLog();
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            // 获取切入点所在的方法
            Method method = signature.getMethod();
            // 获取请求的类名
            String className = joinPoint.getTarget().getClass().getName();
            // 获取请求的方法名
            String methodName = method.getName();
            methodName = className + "." + methodName;
            oper.setMethod(methodName); // 请求方法
            /*========控制台输出=========*/
            log.info("=====异常通知开始=====");
            log.info("异常代码:" + e.getClass().getName());
            log.info("异常信息:" + stackTraceToString(e.getClass().getName(), e.getMessage(), e.getStackTrace()));
            log.info("异常方法:" + methodName);
            log.info("请求人:" + username);
            log.info("请求IP:" + ip);
            log.info("请求参数:" + JasonUtil.obj2String(params));

            //保存到数据库
            //*========数据库日志=========*//
            oper.setReqParam(JasonUtil.obj2String(params)); // 请求参数
            oper.setExcName(e.getClass().getName()); // 异常名称
            oper.setExcMessage(stackTraceToString(e.getClass().getName(), e.getMessage(), e.getStackTrace())); // 异常信息
            oper.setUsername(username); // 操作员ID
            oper.setName(username); // 操作员名称
            oper.setUrl(request.getRequestURI()); // 操作URI
            oper.setIp(ip); // 操作员IP
            oper.setLogType("error");//日志类型
            oper.setVersion(version); // 操作版本号
            oper.setCreateTime(new Date()); // 发生异常时间
            //保存到数据库
            logService.add(oper);
        } catch (Exception ex) {
            //记录本地异常日志
            log.error("==异常通知异常==");
            log.error("异常信息:{}", ex.getMessage());
        }
    }

    /**
     * @Description 获取注解中对方法的描述信息
     * @date
     */
    public static String getMethodDescription(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class<?> targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        StringBuffer description = new StringBuffer();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class<?>[] classes = method.getParameterTypes();
                if (classes.length == arguments.length) {
                    description.append(method.getAnnotation(OperLog.class).module())
                            .append(method.getAnnotation(OperLog.class).type())
                            .append(method.getAnnotation(OperLog.class).desc());
                    break;
                }
            }
        }
        return description.toString();
    }

    /**
     * 转换异常信息为字符串
     *
     * @param exceptionName    异常名称
     * @param exceptionMessage 异常信息
     * @param elements         堆栈信息
     */
    private String stackTraceToString(String exceptionName, String exceptionMessage, StackTraceElement[] elements) {
        StringBuffer sb = new StringBuffer();
        for (StackTraceElement element : elements) {
            sb.append(element + "\n");
        }
        String message = exceptionName + ":" + exceptionMessage + "\n\t" + sb.toString();
        return message;
    }

    /**
     * 获取参数
     * @param paramMap
     * @return
     */
    private  Map<String, String> converMap(Map<String, String[]> paramMap) {
        Map<String, String> pMap = new HashMap<>();
        for (String key : paramMap.keySet()) {
            pMap.put(key, paramMap.get(key)[0]);
        }
        return pMap;
    }
}
