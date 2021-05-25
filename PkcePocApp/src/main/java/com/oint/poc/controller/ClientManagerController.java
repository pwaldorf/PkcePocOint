package com.oint.poc.controller;

import com.oint.poc.service.CreateClientIdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/client")
@RestController
public class ClientManagerController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CreateClientIdService createClientIdService;

    @PostMapping(value = "/registration", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String registerClientId (@RequestBody String client) {

        return createClientIdService.createClientId(client);
    }
}
