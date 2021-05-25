package com.oint.poc.auth;

import com.oint.poc.constants.OnyxIntegratorConstants;
import org.glassfish.jersey.logging.LoggingFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author dhyan
 */
@Service
public class KeyclockClient {

    @Value("${keycloak.token.url}")
    private String tokernUrl;

    @Value("${keycloak.resource.client.id}")
    private String clientId;

    public List<GrantedAuthority> getAuthorities(String token) {
        Map<String, String> request = new HashMap<>();

        request.put("grant_type", "urn:ietf:params:oauth:grant-type:uma-ticket");
        request.put("audience", clientId);
        request.put("response_mode", "permissions");

        Response response = sendAndReceive(request, token);

        if (response == null || response.getStatus() != 200) {
            return null;
        }
        Authorization[] autherizations = response.readEntity(Authorization[].class);

        List<GrantedAuthority> grantedAuthoritys = new ArrayList<>();

        if (autherizations == null) {
            return null;
        }

        for (Authorization autherization : autherizations) {
            if (autherization.getScopes() == null) {
                continue;
            }
            for (String scope : autherization.getScopes()) {
                String permission = OnyxIntegratorConstants.ROLE_PREFIX + "USER:" + autherization.getRsname() + ":" + scope;
                grantedAuthoritys.add(new SimpleGrantedAuthority(permission.toUpperCase()));
            }
        }

        return grantedAuthoritys;
    }

    public Response sendAndReceive(Map<String, String> request, String token) {
        Form form = new Form(new MultivaluedHashMap<>(request));

        Client client = ClientBuilder.newBuilder()
                .property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_ANY)
                .property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_CLIENT, "WARNING").build();
        WebTarget WebTarget = client.target(tokernUrl);

        return WebTarget.request().header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON_VALUE).post(Entity.form(form));

    }

}
