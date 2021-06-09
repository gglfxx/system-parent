package com.online.system.common.config;

import javax.servlet.DispatcherType;

import com.online.system.web.filter.CharacterEncodingPerFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 字符拦截
 * @author guigl
 *
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<CharacterEncodingPerFilter> xssFilterRegistration() {
        FilterRegistrationBean<CharacterEncodingPerFilter> registration = new FilterRegistrationBean<>();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new CharacterEncodingPerFilter());
        registration.addUrlPatterns("/*");
        registration.setName("characterEncodingPerFilter");
        registration.setOrder(Integer.MAX_VALUE);
        return registration;
    }
}
