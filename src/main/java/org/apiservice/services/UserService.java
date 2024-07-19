package org.apiservice.services;


import org.apiservice.DTO.UserDTO;
import org.apiservice.model.User;
import org.apiservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(UserDTO user){
        return userRepository.save(User.builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build());
    }
    public User showById(long id){
        return userRepository.findById(id).orElseThrow();
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
}
