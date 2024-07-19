package org.apiservice.controllers;

import org.apiservice.DTO.UserDTO;
import org.apiservice.model.User;
import org.apiservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public User addNewUser(@RequestBody UserDTO user){
        return userService.save(user);
    }

    @GetMapping("{id}")
    public User getUserById(@PathVariable("id") long id){
        System.out.println(id);
        return userService.showById(id);
    }

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable("id") long id){
        userService.deleteUserById(id);
    }
}
