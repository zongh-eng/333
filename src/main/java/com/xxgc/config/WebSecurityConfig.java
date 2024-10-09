package com.xxgc.config;

import com.xxgc.service.impl.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
/**
 * @Description: 权限配置类
 */
@Configuration
@EnableWebSecurity //开启spring security功能
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    SessionRegistry sessionRegistry;
    @Bean
    public SessionRegistry getSessionRegistry() {
        SessionRegistry sessionRegistry = new SessionRegistryImpl();
        return sessionRegistry;
    }
    //使用注解在配置类中直接实例化对象
    @Bean
    UserDetailsService customUserService() {
        return new CustomUserService();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//解决Refused to display in a frame because it set 'X-Frame-Options' to'deny'.
                http.headers().frameOptions().disable();
        http.csrf().disable(); //解决跨域的问题
        http
                .authorizeRequests() //验证请求
                .antMatchers("/assets/**","/js/**","/public/**","/front/**").permitAll() //放行部分合法路径
                .antMatchers("/f/**").permitAll() //放行部分合法路径
                .anyRequest().authenticated() //任何请求,登录后可以访问
                .and()
                .formLogin()
                .loginPage("/a/user/login")
                .defaultSuccessUrl("/a/index",true) //登录成功的默认跳转路径
                .failureUrl("/a/user/login?error") //登录失败的跳转路径
                .permitAll() //登录页面用户任意访问
                .and()
                .logout().permitAll().invalidateHttpSession(true)
                .and().sessionManagement().maximumSessions(10).expiredUrl("/a/user/login"); //注销行为任意访问
    }
    //AuthenticationManagerBuilder验证前台提交的数据
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws
            Exception {
//BCryptPasswordEncoder指定密码的验证规则
        auth.userDetailsService(customUserService()).passwordEncoder(new
                BCryptPasswordEncoder());
    }
}
