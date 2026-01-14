package com.example.weather.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/Hello")
public class Hello{
    @GetMapping
    public ResponseEntity<String> hello(){
        return  new ResponseEntity<>("hello",HttpStatus.OK);
    }
}
