package org.apiservice.controllers;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apiservice.DTO.UserDTO;
import org.apiservice.model.Group;
import org.apiservice.model.User;
import org.apiservice.services.EmailValidationService;
import org.apiservice.services.GroupService;
import org.apiservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final GroupService groupService;
    private final EmailValidationService emailValidationService;


    @Autowired
    public UserController(UserService userService, GroupService groupService, EmailValidationService emailValidationService) {
        this.userService = userService;
        this.groupService = groupService;
        this.emailValidationService = emailValidationService;
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

    @PostMapping("/upload-csv")
    public ResponseEntity<String> uploadCSVFile(@RequestParam("file") MultipartFile file,
                                                @RequestParam("name") String groupName) {
        List<UserDTO> data = new ArrayList<>();
        List<User> users  = userService.getAllUsers();
        Group group = new Group();
        group.setName(groupName);
//        group.setGroupOwner("admin");

        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty");
        } else {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
                 CSVReader csvReader = new CSVReader(reader)) {

                String[] nextLine;
                while ((nextLine = csvReader.readNext()) != null) {
                    if (!emailValidationService.isValidEmail(nextLine[1])){
                        continue;
                    }
                    User user = new User(nextLine[0], nextLine[1]);
                    if (!users.contains(new User(nextLine[0], nextLine[1]))){
                        group.getUsers().add(userService.save(user));
                    }
                    else {
                        group.getUsers().add(users.stream().filter(us -> us.getEmail().equals(user.getEmail())).findAny().orElse(null));
                    }
                }
            } catch (IOException | CsvValidationException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading file", e);
            }
        }
        groupService.save(group);

        return ResponseEntity.ok("File processed successfully");
    }
}
