package org.apiservice.services;


import org.apiservice.DTO.GroupDTO;
import org.apiservice.DTO.UserDTO;
import org.apiservice.model.Group;
import org.apiservice.model.User;
import org.apiservice.repositories.GroupRepository;
import org.apiservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {


    private final GroupRepository groupRepository;


    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }


    public Group save(GroupDTO group){
        return groupRepository.save(Group.builder()
                .name(group.getName())
                .build());
    }

    public Group save(Group group){
        return groupRepository.save(group);
    }


    public Group showById(long id){
        return groupRepository.findById(id).orElseThrow();
    }

    public List<Group> getAllGroups(){
        return groupRepository.findAll();
    }

    public void deleteGroupById(long id){
        groupRepository.deleteById(id);
    }
}
