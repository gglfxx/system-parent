package com.online.system.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lombok.extern.slf4j.Slf4j;
/**
 * 日期工具类
 */
@Slf4j
public class DateUtil {

    //存放时间格式的Map
    private static  final Map<String, ThreadLocal<SimpleDateFormat>> SDF_MAP = new HashMap<>();

    /**
     * 获取SimpleDateFormat 对象
     * @param  pattern 格式
     * @return SimpleDateFormat
     */
    private static SimpleDateFormat getSimpleDateFormat(final String pattern){
        ThreadLocal<SimpleDateFormat> t = SDF_MAP.get(pattern);
        Lock lock = new ReentrantLock();
        if(t==null){
            t = SDF_MAP.get(pattern);
            if(t==null){
                try{
                    lock.lock();
                    t = ThreadLocal.withInitial(() -> new SimpleDateFormat(pattern));
                    SDF_MAP.put(pattern,t);
                }catch (Exception e){
                    log.warn("获取SimpleDateFormat异常："+e.getMessage());
                }finally {
                    lock.unlock();
                }
            }
        }
        return  t.get();
    }

    /**
     * 日期转字符串
     * @param pattern 格式
     * @param time 时间
     * @return string  日期字符串
     */
    public static String dateFormatToString(String pattern, Date time){
        SimpleDateFormat dateFormat = getSimpleDateFormat(pattern);
        return dateFormat.format(time);
    }

    /**
     * 日期格式化
     * @param pattern 格式
     * @param time 时间
     * @return Date
     */
    public static Date  dateFormatToDate(String pattern, Date time){
        SimpleDateFormat dateFormat = getSimpleDateFormat(pattern);
        Date date  = null;
        try {
            date =  dateFormat.parse(dateFormat.format(time));
        } catch (ParseException e) {
            log.warn("获取dateFormatToDate异常："+e.getMessage());
        }
        return date;
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.dateFormatToDate("yyyy-MM-dd",new Date()));
    }
}
