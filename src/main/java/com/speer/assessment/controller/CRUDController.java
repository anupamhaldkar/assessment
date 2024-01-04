package com.speer.assessment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class CRUDController {

    @PostMapping(value ="/test")
    public ResponseEntity<Object> test(){
        return ResponseEntity.ok("Access granted");
    }
    
}
