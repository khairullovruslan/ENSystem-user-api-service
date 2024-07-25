package org.apiservice.services;

import org.apiservice.DTO.GroupDTO;
import org.apiservice.model.Group;
import org.apiservice.repositories.GroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {
    private static final long ID = 1L;

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private GroupService groupService;

    @Test
    @DisplayName("Метод save вызывает метод save репозитория и возвращает сохраненную группу")
    public void save_shouldSaveGroup_whenCalled() {
        final Group group = Group.builder().name("testGroup").id(ID).build();
        when(groupRepository.save(group)).thenReturn(group);

        Group actual = groupService.save(group);

        assertNotNull(actual);
        assertEquals(group, actual);
        verify(groupRepository).save(group);
    }

    @Test
    @DisplayName("Метод save вызывает метод save репозитория и возвращает сохраненную группу DTO")
    public void save_shouldSaveGroup_whenCalledWithGroupDTO() {
        final GroupDTO groupDTO = new GroupDTO("testName");
        final Group group = new Group("testName");
        when(groupRepository.save(argThat(g -> g.getName().equals(groupDTO.getName())))).thenReturn(group);

        final Group actual = groupService.save(groupDTO);

        assertNotNull(actual);
        verify(groupRepository).save(argThat(g -> g.getName().equals(groupDTO.getName())));
    }


    @Test
    @DisplayName("Метод showById  вызывает метод findById репозитория и возвращает найденную  группу, если такой существует")
    public void showById_shouldFindGroup_whenExists() {
        final Group group = mock(Group.class);
        when(groupRepository.findById(ID)).thenReturn(Optional.ofNullable(group));

        Group actual = groupService.showById(ID);

        assertNotNull(actual);
        assertEquals(actual, group);
        verify(groupRepository).findById(ID);
    }

    @Test
    @DisplayName("Метод getAllGroups  вызывает метод findAll репозитория и возвращает все группы")
    public void getAllGroups_shouldFindAllGroups_whenExists() {
        final List<Group> groups = new ArrayList<>(List.of(new Group(), new Group()));
        when(groupRepository.findAll()).thenReturn(groups);

        List<Group> actualList = groupService.getAllGroups();

        assertNotNull(actualList);
        assertEquals(actualList, groups);
        verify(groupRepository).findAll();

    }

    @Test
    @DisplayName("Метод deleteGroupById  вызывает метод deleteById репозитория")
    public void deleteGroupById_shouldInvokeRepositoryDelete_whenGroupExists() {
        doNothing().when(groupRepository).deleteById(ID);

        groupService.deleteGroupById(ID);

        verify(groupRepository).deleteById(ID);

    }
}