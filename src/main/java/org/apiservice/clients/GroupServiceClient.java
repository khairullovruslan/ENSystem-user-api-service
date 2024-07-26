package org.apiservice.clients;

import org.apiservice.model.Group;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient(name = "group-service", url = "http://localhost:8000/groups")
public interface GroupServiceClient {

    @PostMapping()
    ResponseEntity<Group> addNewGroup(@RequestBody Group group);

    @GetMapping
    ResponseEntity<List<Group>> getAllGroups();


    @PostMapping("/update")
    ResponseEntity<Group> updateGroup(@RequestBody Group group);
}
