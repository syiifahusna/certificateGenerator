package com.example.certificateGenerator.controller;

import com.example.certificateGenerator.errorhandling.PathNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UnknowPathController {

    @RequestMapping("/**")
    public ResponseEntity unknownPath(){
        throw new PathNotFoundException("Path does not exist");
    }

}
