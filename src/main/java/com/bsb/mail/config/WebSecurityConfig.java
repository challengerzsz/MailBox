package com.bsb.mail.config;

import com.bsb.mail.authentication.MailAuthenticationFailureHandler;
import com.bsb.mail.authentication.MailAuthenticationSuccessHandler;
import com.bsb.mail.common.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

/**
 * @Author: zeng
 * @Date: 2018/9/29 18:35
 */
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MailAuthenticationSuccessHandler mailAuthenticationSuccessHandler;
    @Autowired
    private MailAuthenticationFailureHandler mailAuthenticationFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpFirewall httpFirewall() {
        return new DefaultHttpFirewall();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(
                        SecurityConstants.LOGIN_PAGE,
                        SecurityConstants.DEFAULT_UNAUTHENTICATED_URL,
                        SecurityConstants.LOGIN_PROCESSING_URL,
                        SecurityConstants.REGISTER_PAGE,
                        SecurityConstants.REGISTER_PAGE_API,
                        SecurityConstants.BAD_URLS).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATED_URL)
                .loginProcessingUrl(SecurityConstants.LOGIN_PROCESSING_URL)
                .successHandler(mailAuthenticationSuccessHandler)
                .failureHandler(mailAuthenticationFailureHandler)
                .and()
                .logout()
                .logoutUrl(SecurityConstants.LOGOUT_URL)
                .logoutSuccessUrl(SecurityConstants.LOGIN_PAGE)
                .invalidateHttpSession(true)
                .and()
                .headers()
                .frameOptions().disable()
                .and()
                .csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/css/**");
    }
}
