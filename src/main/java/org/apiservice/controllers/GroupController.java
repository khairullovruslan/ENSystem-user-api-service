package org.apiservice.controllers;


import org.apiservice.DTO.GroupDTO;
import org.apiservice.model.Group;
import org.apiservice.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public Group addNewGroup(@RequestBody GroupDTO group){
        return groupService.save(group);
    }

    @GetMapping("{id}")
    public Group getGroupById(@PathVariable("id") long id){
        return groupService.showById(id);
    }

    @GetMapping
    public List<Group> getAllUsers(){
        return groupService.getAllGroups();
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable("id") long id){
        groupService.deleteGroupById(id);
    }
}
