package com.oint.poc.filter;

import com.oint.poc.auth.UserDetailsServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;

public class AuthenticationTokenFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String TOKEN_STARTS_WITH_BEARER = "Bearer ";

    private UserDetailsServiceImpl userDetailsService;

    public AuthenticationTokenFilter(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        /* Get token header from request. */
        String tokenWithBearer = request.getHeader(HttpHeaders.AUTHORIZATION);
        logger.info("Token from header = {}", tokenWithBearer);

        if (StringUtils.isNotEmpty(tokenWithBearer) && tokenWithBearer.startsWith(TOKEN_STARTS_WITH_BEARER)) {
            String token = tokenWithBearer.substring(TOKEN_STARTS_WITH_BEARER.length());
            UserDetails userDetails = this.userDetailsService.loadUser(token);
            if (userDetails != null && userDetails.getUsername() != null) {
                for (GrantedAuthority g : userDetails.getAuthorities()) {
                    logger.info("{} = and Permission = {}", userDetails.getUsername(), g.getAuthority());
                }
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, new NullAuthoritiesMapper().mapAuthorities(userDetails.getAuthorities()));
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.info("Authorities for user '{}' from context : {}", userDetails.getUsername(),
                        SecurityContextHolder.getContext().getAuthentication().getAuthorities());

            } else {
                throw new IOException("Not able to get the User");
            }
        }

        chain.doFilter(request, response);
    }
}

