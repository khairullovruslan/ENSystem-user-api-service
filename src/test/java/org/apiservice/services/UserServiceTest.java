package org.apiservice.services;


import org.apiservice.DTO.UserDTO;
import org.apiservice.model.User;
import org.apiservice.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final long ID = 1L;

    @InjectMocks
    private UserService userService;

    @Mock
    private GroupService groupService;

    @Mock
    private EmailValidationService emailValidationService;

    @Mock
    private UserRepository userRepository;


    @Test
    @DisplayName("Метод save вызывает метод save репозитория и возвращает сохраненного пользователя")
    public void save_shouldSaveUser_whenCalled() {
        final User user = new User();
        user.setId(ID);
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.save(user);

        assertNotNull(savedUser);
        assertEquals(ID, savedUser.getId());
        verify(userRepository).save(user);
    }


    @Test
    @DisplayName("Метод save вызывает метод save репозитория и возвращает сохраненного пользователя DTO")
    void save_shouldSaveUser_whenCalledWithUserDTO() {
        final UserDTO userDTO = new UserDTO("testName", "test@mail.ru");
        final User user = new User("testName", "test@mail.ru");
        when(userRepository.save(user)).thenReturn(user);

        final User savedUser = userService.save(userDTO);

        assertNotNull(savedUser);
        assertEquals(savedUser.getEmail(), userDTO.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Метод showById  вызывает метод findById репозитория и возвращает найденного  пользователя, если такой существует")
    void showById_shouldFindUser_whenExists() {
        final User user = mock(User.class);
        when(userRepository.findById(ID)).thenReturn(Optional.ofNullable(user));

        final User actual = userService.showById(ID);

        assertNotNull(actual);
        assertEquals(user, actual);
        verify(userRepository).findById(ID);

    }

    @Test
    @DisplayName("Метод getAllUsers  вызывает метод findAll репозитория и возвращает всех пользователей")
    void getAllUsers_shouldFindAllUsers_whenExists() {
        final List<User> users = new ArrayList<>(List.of(new User()));
        when(userRepository.findAll()).thenReturn(users);

        final List<User> actual = userService.getAllUsers();

        assertNotNull(actual);
        assertEquals(users, actual);
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Метод deleteUserById  вызывает метод deleteById репозитория")
    void deleteUserById_shouldInvokeRepositoryDelete_whenUserExists() {
        doNothing().when(userRepository).deleteById(ID);

        userService.deleteUserById(ID);

        verify(userRepository).deleteById(ID);
    }

    @Test
    @DisplayName("Метод  uploadUsersFromCSVFile должен выкинуть  exception  empty file")
    void uploadUsersFromCSVFile_shouldUploadUsersFromCSVFileAndCreateGroup_InvalidFile() {
        MultipartFile emptyFile = mock(MultipartFile.class);
        when(emptyFile.isEmpty()).thenReturn(true);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.uploadUsersFromCSVFile("Test Group", emptyFile));

        assertEquals(HttpStatus.valueOf(400), exception.getStatusCode());
        assertEquals("File is empty", exception.getReason());
    }

    @Test
    @DisplayName("Метод uploadUsersFromCSVFile обрабатывает корректный файл успешно ")
    void uploadUsersFromCSVFile_shouldUploadUsersFromCSVFileAndCreateGroup_ValidFile() throws IOException {
        MultipartFile validFile = mock(MultipartFile.class);
        Group group = mock(Group.class);
        when(validFile.isEmpty()).thenReturn(false);
        when(emailValidationService.isValidEmail(anyString())).thenReturn(true);
        when(validFile.getInputStream()).thenReturn(new ByteArrayInputStream((
                """
                        test1,test1@example.com
                        test2,test2@example.com
                        test3,test3@example.com
                        test4,test4@example.com
                        """).getBytes()));


        when(groupService.save((Group) any())).thenReturn(group);

        ResponseEntity<String> response = userService.uploadUsersFromCSVFile("Test Group", validFile);

        verify(emailValidationService, times(4)).isValidEmail(anyString());
        verify(groupService).save((Group) any());
        assertEquals("File processed successfully", response.getBody());
    }
}