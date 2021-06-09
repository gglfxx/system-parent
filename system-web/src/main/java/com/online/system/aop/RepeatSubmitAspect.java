package com.online.system.aop;

import com.online.system.annotation.PreRepeatSubmit;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class RepeatSubmitAspect {

    //拦截多次提交切点
    @Pointcut("@annotation(preRepeatSubmit)")
    public void pointCut(PreRepeatSubmit preRepeatSubmit) {
    }

}
