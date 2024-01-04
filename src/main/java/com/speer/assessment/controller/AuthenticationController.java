package com.speer.assessment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.speer.assessment.dto.AuthRequestDTO;
import com.speer.assessment.dto.AuthenticationResponse;
import com.speer.assessment.service.UserService;
import com.speer.assessment.util.JwtUtil;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;
    

    @PostMapping(value ="/signup")
    public ResponseEntity<Object> signup(@RequestBody AuthRequestDTO signup) {
    
            userService.signUp(signup);
        return ResponseEntity.ok("User signed up successfully");
    }

    @PostMapping(value ="/login")
    public ResponseEntity<Object> login(@RequestBody AuthRequestDTO authenticationRequest) throws Exception {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
    
    
}
