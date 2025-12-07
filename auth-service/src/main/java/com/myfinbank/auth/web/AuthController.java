package com.myfinbank.auth.web;

import com.myfinbank.auth.dto.CustomerLoginRequest;
import com.myfinbank.auth.dto.CustomerRegisterRequest;
import com.myfinbank.auth.dto.JwtResponse;
import com.myfinbank.auth.jwt.JwtTokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/customer")
public class AuthController {

    private final JwtTokenUtil jwtTokenUtil;

    public AuthController(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody CustomerRegisterRequest request) {
        // later: call customer-service to actually create the customer
        return ResponseEntity.ok("Customer registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody CustomerLoginRequest request) {
        // later: validate against customer-service
    	if (request.getEmail() == null || request.getPassword() == null) {
    	    return ResponseEntity.badRequest().build();
    	}
    	String token = jwtTokenUtil.generateToken(request.getEmail());
    	return ResponseEntity.ok(new JwtResponse(token));

        
    }
}
