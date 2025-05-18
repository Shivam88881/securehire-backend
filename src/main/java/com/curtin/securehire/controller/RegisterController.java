package com.curtin.securehire.controller;

import com.curtin.securehire.dto.RegisterRequest;
import com.curtin.securehire.service.db.impl.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(RegisterController.class);


    @PostMapping("")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        logger.info("Received register request: {}", request);
        String message = registerService.register(request);
        logger.info("Register response: {}", message);
        return ResponseEntity.ok(message);
    }
}

