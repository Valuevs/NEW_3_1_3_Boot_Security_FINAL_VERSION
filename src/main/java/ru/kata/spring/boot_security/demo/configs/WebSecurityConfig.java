package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import ru.kata.spring.boot_security.demo.service.DetailServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;
    private final DetailServiceImpl detailServiceImpl;

    public WebSecurityConfig(SuccessUserHandler successUserHandler, DetailServiceImpl detailServiceImpl) {
        this.successUserHandler = successUserHandler;
        this.detailServiceImpl = detailServiceImpl;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/user/**").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated()
                .and()
                .formLogin().successHandler(successUserHandler)
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setUserDetailsService(detailServiceImpl);
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}