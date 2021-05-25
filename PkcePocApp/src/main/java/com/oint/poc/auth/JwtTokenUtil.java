package com.oint.poc.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);

    /**
     * Return username from token.
     *
     * @param token
     * @return username
     */
    public static String getUsernameFromToken(String token) {
        String username = null;

        try {
            DecodedJWT jwt = JWT.decode(token);
            username = jwt.getClaim("preferred_username").asString();

        } catch (Exception e) {
            LOGGER.error("Unable to read Username from token. exception {}", e);
        }
        return username;
    }

}
