package com.thittaiAmman.thittai_backend.controller;

import com.thittaiAmman.thittai_backend.model.Users;
import com.thittaiAmman.thittai_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/test")
    public String greet(){
        return "Hello world";
    }

    @GetMapping("/users")
    public ResponseEntity<List<Users>> getUsers(){
        List<Users> users = service.getUsers();
        if (users != null){
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update-role")
    public ResponseEntity<?> updateRole(@RequestBody Users user){
        boolean userUpgrade = service.updateUserRole(user);
        if (!userUpgrade){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of(
                            "success",false,
                            "message","User Not found"
                    )
            );
        }
        return ResponseEntity.ok(Map.of(
                "success",true,
                "message","User Updated Successfully"
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user) {
        String token = service.verify(user);
        String username = user.getUsername();
        Users userData = service.returnUser(username);

        if (token != null) {
            return ResponseEntity.ok(Map.of(
                    "token",token,
                    "username",userData.getUsername(),
                    "role",userData.getRoles()
            ));
        }

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Invalid username or password");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Users user){
        Users newUser = service.signUpUser(user);
        if (newUser==null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    "Username already exists"
            );
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }

}
