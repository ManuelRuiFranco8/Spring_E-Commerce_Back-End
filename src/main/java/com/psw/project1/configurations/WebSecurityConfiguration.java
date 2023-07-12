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
@EnableGlobalMethodSecurity(prePostEnabled=true) //enables global method-level security
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter { //provides default security configuration

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
    protected void configure(HttpSecurity http) throws Exception { //configures http security
        http.cors(); //enables Cross-Origin Resource Sharing (CORS) support.
        http.csrf().disable() //disables CSRF (Cross-Site Request Forgery) protection
                //endpoints where token-based authentication is not applied
                .authorizeRequests().antMatchers("/authenticate", "/signIn").permitAll()
                .antMatchers(HttpHeaders.ALLOW).permitAll() //allows all requests with the "ALLOW" header
                .anyRequest().authenticated() //applies token-based authentication to all other endpoints
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthEntry)//sets JWT authentication entry point.
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //the back-end server do
                                                                                             //not store session info
        http.addFilterBefore(jwtReqFilter, UsernamePasswordAuthenticationFilter.class); //adds the JWT filter
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