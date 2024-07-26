package org.apiservice.services;


import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apiservice.DTO.GroupDTO;
import org.apiservice.DTO.UserDTO;
import org.apiservice.clients.GroupServiceClient;
import org.apiservice.model.Group;
import org.apiservice.model.User;
import org.apiservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final GroupServiceClient groupService;
    private final EmailValidationService emailValidationService;

    @Autowired
    public UserService(UserRepository userRepository, GroupServiceClient groupService, EmailValidationService emailValidationService) {
        this.userRepository = userRepository;
        this.groupService = groupService;
        this.emailValidationService = emailValidationService;
    }

    public User save(UserDTO user) {
        return userRepository.save(User.builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build());
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User showById(long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUserById(long id) {
        userRepository.deleteById(id);
    }


    public ResponseEntity<String> uploadUsersFromCSVFileWithBatchUpdate(String groupName, MultipartFile file) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<User> users = getAllUsers();

        List<User> usersToSave = new ArrayList<>();

        Group group = groupService.addNewGroup(new Group(groupName)).getBody();

        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty");
        } else {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
                 CSVReader csvReader = new CSVReader(reader)) {

                String[] nextLine;
                while ((nextLine = csvReader.readNext()) != null) {
                    if (!emailValidationService.isValidEmail(nextLine[1])) {
                        continue;
                    }
                    if (group.getUsers() == null) {
                        group.setUsers(new HashSet<>());
                    }

                    User user = new User(nextLine[0], nextLine[1]);
                    if (!users.contains(user)) {
                        usersToSave.add(user);
                    } else {
                        group.getUsers().add(users.stream().filter(us -> us.getEmail().equals(user.getEmail())).findAny().orElse(null));
                    }
                }
            } catch (IOException | CsvValidationException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading file", e);
            }
        }
        if (!usersToSave.isEmpty()) {
            List<User> users1 = userRepository.saveAll(usersToSave);
            group.getUsers().addAll(users1);
        }

        groupService.updateGroup(group);
        stopWatch.stop();
        System.out.println("Time has passed with batch update, ms: " + stopWatch.getTotalTimeSeconds());

        return ResponseEntity.ok("File processed successfully");
    }


}
