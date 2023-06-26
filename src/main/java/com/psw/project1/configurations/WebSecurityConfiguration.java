package com.psw.project1.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthEntry;
    @Autowired
    private JwtRequestFilter jwtReqFilter;
    @Autowired
    private UserDetailsService JwtServ;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }//authenticationManagerBean

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable()
                //endpoints where token-based authentication is not applied
                .authorizeRequests().antMatchers("/authenticate", "/signIn").permitAll()
                .antMatchers(HttpHeaders.ALLOW).permitAll()
                .anyRequest().authenticated() //applies token-based authentication to the other endpoints
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthEntry)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //to not store
                                                                                             //authentication info
        http.addFilterBefore(jwtReqFilter, UsernamePasswordAuthenticationFilter.class);
    }//configure

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }//econder for users' passwords

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder AuthBuilder) throws Exception {
        AuthBuilder.userDetailsService(JwtServ).passwordEncoder(encoder()); //service handling token-related
                                                                            //business logic
    }//configureGlobal
}//WebSecurityConfiguration