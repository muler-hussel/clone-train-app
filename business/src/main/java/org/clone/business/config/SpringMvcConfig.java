package org.clone.business.config;

import jakarta.annotation.Resource;
import org.clone.common.interceptor.LogInterceptor;
import org.clone.common.interceptor.MemberInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

   @Resource
   LogInterceptor logInterceptor;

   @Resource
   MemberInterceptor memberInterceptor;

   @Override
   public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(logInterceptor)
               .addPathPatterns("/**");
      registry.addInterceptor(memberInterceptor)
              .addPathPatterns("/**");

   }
}
