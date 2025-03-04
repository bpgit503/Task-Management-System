package en_bpdev_Task_Management_System.controller;

import en_bpdev_Task_Management_System.entity.Task;
import en_bpdev_Task_Management_System.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    TaskService taskService;

    @Test
    void getTaskById() throws Exception {
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
