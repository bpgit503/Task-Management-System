package en_bpdev_Task_Management_System.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import en_bpdev_Task_Management_System.entity.Task;
import en_bpdev_Task_Management_System.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    TaskService taskService;


    @Test
    void testDeleteBeer() throws Exception {
        Task mockTask = Task.builder()
                .id(1l)
                .title("Complete course")
                .description("Finish spring course")
                .dueDate(LocalDate.of(2025, 3, 8))
                .completed(false)
                .build();

        mockMvc.perform(delete("/api/tasks/" + mockTask.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockTask)))
                .andExpect(status().isNoContent());
        ArgumentCaptor<Long> longArgumentCaptor = ArgumentCaptor.forClass(Long.class);


        verify(taskService).deleteTask(longArgumentCaptor.capture());

        assertThat(mockTask.getId()).isEqualTo(longArgumentCaptor.getValue());


    }

    @Test
    void testUpdateTask() throws Exception {
        Task mockTask = Task.builder()
                .id(1l)
                .title("Complete course")
                .description("Finish spring course")
                .dueDate(LocalDate.of(2025, 3, 8))
                .completed(false)
                .build();

        mockMvc.perform(put("/api/tasks/" + mockTask.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockTask)))
                .andExpect(status().isNoContent());

        verify(taskService).updateTask(any(Long.class), any(Task.class));

    }

    @Test
    void testCreateNewTask() throws Exception {
        Task mockTask = Task.builder()
                .title("Complete course")
                .description("Finish spring course")
                .dueDate(LocalDate.of(2025, 3, 8))
                .completed(false)
                .createAt(LocalDate.of(2025, 4, 8))
                .build();

        given(taskService.createTask(any(Task.class))).willReturn(mockTask);

        mockMvc.perform(post("/api/tasks")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockTask)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }


    @Test
    void testGetTaskById() throws Exception {
        Task mockTask = Task.builder()
                .id(1l)
                .title("Complete course")
                .description("Finish spring course")
                .dueDate(LocalDate.of(2025, 3, 8))
                .completed(false)
                .build();

        given(taskService.getTaskById(mockTask.getId())).willReturn(Optional.of(mockTask));

        mockMvc.perform(get("/api/tasks/" + mockTask.getId()).
                        accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Complete course")));
    }

    @Test
    void testGetListTasks() throws Exception {

        List<Task> mockTasks = Arrays.asList(
                Task.builder()
                        .id(1L)
                        .title("Complete course")
                        .description("Finish spring course")
                        .dueDate(LocalDate.of(2025, 3, 8))
                        .completed(false)
                        .build(),
                Task.builder()
                        .id(2L)
                        .title("Practice testing")
                        .description("Write more unit tests")
                        .dueDate(LocalDate.of(2025, 3, 10))
                        .completed(false)
                        .build()
        );

        given(taskService.getAllTasks()).willReturn(mockTasks);

        mockMvc.perform(get("/api/tasks").
                        accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)));

    }


}
