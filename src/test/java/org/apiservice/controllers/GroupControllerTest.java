package org.apiservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apiservice.DTO.GroupDTO;
import org.apiservice.model.Group;
import org.apiservice.services.GroupService;
import org.apiservice.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class GroupControllerTest {
    private static final long ID = 1L;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GroupService groupService;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("POST /groups/ возвращает HTTP-ответ со статусом 201 OK")
    void testAddNewGroup_ReturnsValidResponseEntity() throws Exception {
        GroupDTO group = GroupDTO.builder().name("testName").build();
        String groupJson = objectMapper.writeValueAsString(group);

        mockMvc.perform(post("/groups", group)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(groupJson))
                .andExpect(status().isCreated());

        verify(groupService).save(group);
    }

    @Test
    @DisplayName("GET /groups/{id} возвращает HTTP-ответ со статусом 200 OK и группу с индификатором ID")
    void getGroupById() throws Exception {
        Group group = Group.builder().name("testName").id(ID).build();
        when(groupService.showById(ID)).thenReturn(group);

        mockMvc.perform(get("/groups/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.name").value("testName"));

        verify(groupService).showById(ID);
    }

    @Test
    @DisplayName("GET /groups возвращает HTTP-ответ со статусом 200 OK и список групп")
    void getAllGroups() throws Exception {
        List<Group> groupList = new ArrayList<>(List.of(new Group(), new Group()));
        when(groupService.getAllGroups()).thenReturn(groupList);

        mockMvc.perform(get("/groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(groupService).getAllGroups();
    }

    @Test
    @DisplayName("DELETE /groups/{id} возвращает HTTP-ответ со статусом 200 OK")
    void deleteGroup() throws Exception {
        doNothing().when(groupService).deleteGroupById(ID);

        mockMvc.perform(delete("/groups/{id}", ID))
                .andExpect(status().isOk());

        verify(groupService).deleteGroupById(ID);
    }
}