package com.oint.poc.config;

import com.oint.poc.auth.UserDetailsServiceImpl;
import com.oint.poc.constants.OnyxIntegratorConstants;
import com.oint.poc.filter.AuthenticationTokenFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        logger.info("Inside configure");

        AuthenticationTokenFilter authenticationTokenFilter = new AuthenticationTokenFilter(userDetailsService);
        http
                .csrf().disable().cors().and().addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests(authz -> authz.antMatchers("/client/**")
                        .hasAnyAuthority(OnyxIntegratorConstants.LIST_VIEW).anyRequest().denyAll());
    }


}
