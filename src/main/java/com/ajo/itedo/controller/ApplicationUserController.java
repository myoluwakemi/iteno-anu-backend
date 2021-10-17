package com.ajo.itedo.controller;

import com.ajo.itedo.dto.ApplicationUserDto;
import com.ajo.itedo.service.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class ApplicationUserController {

    @Autowired
    ApplicationUserService applicationUserService;

    @PostMapping("/register")
    public ResponseEntity<?> registerAdmin(@RequestBody ApplicationUserDto applicationUserDto){
        Map<String,String> response = new HashMap<>();
        String saveAdmin = applicationUserService.save(applicationUserDto);
        if (saveAdmin.endsWith("successfully")){
            response.put("message", "Admin was saved successfully");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        else {
            response.put("message","Unknown Error");
            return  new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginAdmin(@Validated  @RequestBody ApplicationUserDto applicationUserDto){
        Map<String,String> response = new HashMap<>();
        String accessToken = applicationUserService.generateToken(applicationUserDto);
        response.put("accessToken",accessToken);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}
