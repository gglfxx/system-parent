package com.online.system.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 日期工具类
 */
public class DateUtil {

	//日志变量
    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);
    //存放时间格式的Map
    private static Map<String, ThreadLocal<SimpleDateFormat>> SDFMAP = new HashMap<>();

    /**
     * 获取SimpleDateFormat 对象
     * @param pattern
     * @return
     */
    private static SimpleDateFormat getSimpleDateFormat(final String pattern){
        ThreadLocal<SimpleDateFormat> t = SDFMAP.get(pattern);
        Lock lock = new ReentrantLock();
        if(t==null){
            t = SDFMAP.get(pattern);
            if(t==null){
                try{
                    lock.lock();
                    t = new ThreadLocal<SimpleDateFormat>(){
                        @Override
                        protected SimpleDateFormat initialValue() {
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    SDFMAP.put(pattern,t);
                }catch (Exception e){
                    logger.warn("获取SimpleDateFormat异常："+e.getMessage());
                }finally {
                    lock.unlock();
                }
            }
        }
        return  t.get();
    }

    /**
     * 日期转字符串
     * @param pattern
     * @param time
     * @return
     */
    public static String dateFormatToString(String pattern, Date time){
        SimpleDateFormat dateFormat = getSimpleDateFormat(pattern);
        return dateFormat.format(time);
    }

    /**
     * 日期格式化
     * @param pattern
     * @param time
     * @return
     */
    public static Date  dateFormatToDate(String pattern, Date time){
        SimpleDateFormat dateFormat = getSimpleDateFormat(pattern);
        Date date  = null;
        try {
            date =  dateFormat.parse(dateFormat.format(time));
        } catch (ParseException e) {
            logger.warn("获取dateFormatToDate异常："+e.getMessage());
        }
        return date;
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.dateFormatToDate("yyyy-MM-dd",new Date()));
    }
}
