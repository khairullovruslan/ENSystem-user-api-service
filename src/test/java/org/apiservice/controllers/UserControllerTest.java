package org.apiservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apiservice.DTO.UserDTO;
import org.apiservice.model.User;
import org.apiservice.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
public class UserControllerTest {
    private static final long ID = 1L;

    @MockBean
    private UserService userService;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("GET /users возвращает HTTP-ответ со статусом 200 OK и список пользователей")
    public void testGetAllUsers_ReturnsValidResponseEntity_UserList() throws Exception {
        when(userService.getAllUsers()).thenReturn(new ArrayList<>(List.of(new User(), new User())));
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("POST /users возвращает HTTP-ответ со статусом 201 OK")
    public void testAddNewUser_ReturnsValidResponseEntity() throws Exception {
        UserDTO userDTO = UserDTO.builder().fullName("test1").email("email1@mail.ru").build();
        String userJson = objectMapper.writeValueAsString(userDTO);
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated());
        verify(userService, times(1)).save(userDTO);
    }

    @Test
    @DisplayName("GET /users/{id} возвращает HTTP-ответ со статусом 200 OK и пользователя с индификатором ID")
    public void testGetUserById_ReturnsValidResponseEntityAndUserById() throws Exception {
        User user = User.builder().fullName("test1").email("email1@mail.ru").id(ID).build();
        when(userService.showById(ID)).thenReturn(user);

        mockMvc.perform(get("/users/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.fullName").value("test1"))
                .andExpect(jsonPath("$.email").value("email1@mail.ru"));

        verify(userService, times(1)).showById(ID);
    }

    @Test
    @DisplayName("DELETE /users/{id} возвращает HTTP-ответ со статусом 200 OK")
    public void deleteUser_ReturnsValidResponseEntity() throws Exception {
        doNothing().when(userService).deleteUserById(ID);

        mockMvc.perform(delete("/users/{id}", ID))
                .andExpect(status().isOk());

        verify(userService).deleteUserById(ID);

    }

    @Test
    @DisplayName("POST /users/upload-csv возвращает HTTP-ответ со статусом 200 OK")
    public void uploadCSVFile_ReturnsValidResponseEntity() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/plain", "some data".getBytes());

        when(userService.uploadUsersFromCSVFile(eq("testName"), eq(file)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));


        mockMvc.perform(MockMvcRequestBuilders.multipart("/users/upload-csv")
                        .file(file)
                        .param("name", "testName"))
                .andExpect(status().isOk());
        verify(userService).uploadUsersFromCSVFile("testName", file);

    }
}