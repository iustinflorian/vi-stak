package com.gifprojects.vistak.service;

import com.gifprojects.vistak.mapping.taskDTO.TaskCreateDTO;
import com.gifprojects.vistak.mapping.taskDTO.TaskUpdateDTO;
import com.gifprojects.vistak.model.Task;
import com.gifprojects.vistak.model.User;
import com.gifprojects.vistak.repository.TaskRepository;
import com.gifprojects.vistak.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepositoryMock;

    @Mock
    private UserRepository userRepositoryMock;

    @InjectMocks
    private TaskService taskServiceMock;

    @Test
    public void createTaskTest_Success() {
        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setTitle("test_task");
        dto.setDesc("test_desc");
        dto.setDeadline(LocalDateTime.now().plusDays(1));

        User mockUser = User.builder().id(1L).taskList(new ArrayList<>()).build();

        when(userRepositoryMock.findById(1L)).thenReturn(Optional.of(mockUser));

        taskServiceMock.createTask(dto, 1L);

        verify(taskRepositoryMock).save(any(Task.class));
        assertEquals(1, mockUser.getTaskList().size());
        assertEquals("test_task", mockUser.getTaskList().getFirst().getTitle());
    }

    @Test
    public void createTaskTest_ShouldThrowException_WhenUserNotFound() {
        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setTitle("test_task");
        
        when(userRepositoryMock.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskServiceMock.createTask(dto, 99L));
        verify(taskRepositoryMock, never()).save(any(Task.class));
    }

    @Test
    public void createTaskTest_ShouldThrowException_WhenInvalidDeadline() {
        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setTitle("test_task");
        dto.setDeadline(LocalDateTime.now().minusDays(1)); // Invalid deadline (in the past)

        User mockUser = User.builder().id(1L).taskList(new ArrayList<>()).build();

        when(userRepositoryMock.findById(1L)).thenReturn(Optional.of(mockUser));

        assertThrows(RuntimeException.class, () -> taskServiceMock.createTask(dto, 1L));
        verify(taskRepositoryMock, never()).save(any(Task.class));
    }

    @Test
    public void fetchAllTasksTest() {
        List<Task> mockTasks = new ArrayList<>();
        mockTasks.add(Task.builder().id(1L).title("task_1").build());
        mockTasks.add(Task.builder().id(2L).title("task_2").build());

        when(taskRepositoryMock.getAllByUserId(1L)).thenReturn(mockTasks);

        List<Task> result = taskServiceMock.fetchAllTasks(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("task_1", result.get(0).getTitle());
        assertEquals("task_2", result.get(1).getTitle());
    }

    @Test
    public void fetchTaskTest_Success() {
        User mockUser = User.builder().id(1L).build();
        Task mockTask = Task.builder().id(1L).title("task_1").user(mockUser).build();

        when(taskRepositoryMock.findById(1L)).thenReturn(Optional.of(mockTask));

        Task result = taskServiceMock.fetchTask(1L, 1L);

        assertNotNull(result);
        assertEquals("task_1", result.getTitle());
    }

    @Test
    public void fetchTaskTest_ShouldThrowException_WhenTaskNotFound() {
        when(taskRepositoryMock.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskServiceMock.fetchTask(99L, 1L));
    }

    @Test
    public void fetchTaskTest_ShouldThrowException_WhenUserUnauthorized() {
        User authorizedUser = User.builder().id(1L).build();
        Task mockTask = Task.builder().id(1L).title("task_1").user(authorizedUser).build();

        when(taskRepositoryMock.findById(1L)).thenReturn(Optional.of(mockTask));

        assertThrows(RuntimeException.class, () -> taskServiceMock.fetchTask(1L, 2L));
    }

    @Test
    public void updateTaskTest_Success() {
        TaskUpdateDTO dto = new TaskUpdateDTO();
        dto.setTitle("updated_task");
        dto.setDesc("updated_desc");

        User mockUser = User.builder().id(1L).build();
        Task mockTask = Task.builder()
                .id(1L)
                .title("old_task")
                .desc("old_desc")
                .user(mockUser)
                .build();

        when(taskRepositoryMock.findById(1L)).thenReturn(Optional.of(mockTask));

        taskServiceMock.updateTask(dto, 1L, 1L);

        assertEquals("updated_task", mockTask.getTitle());
        assertEquals("updated_desc", mockTask.getDesc());
    }

    @Test
    public void deleteTaskTest_Success() {
        User mockUser = User.builder().id(1L).build();
        Task mockTask = Task.builder().id(1L).user(mockUser).build();

        when(taskRepositoryMock.findById(1L)).thenReturn(Optional.of(mockTask));
        
        taskServiceMock.deleteTask(1L, 1L);
        
        verify(taskRepositoryMock).delete(mockTask);
    }
}
