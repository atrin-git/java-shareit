package ru.practicum.shareit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.ItemRequestClient;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestReqDto;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.utils.RequestParams.X_USER_ID;

@WebMvcTest(ItemRequestController.class)
@SpringJUnitWebConfig
public class RequestControllerTests {

    private final String url = "/requests";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemRequestClient requestClient;

    @BeforeTestClass
    public void setUp() {
        when(requestClient.createRequest(any(), any()))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());

        when(requestClient.getAllRequests(any()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).build());

        when(requestClient.getAllForUser(any()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).build());

        when(requestClient.getRequest(any(), any()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).build());
    }

    @Test
    public void checkCreateRequests_IsValid() throws Exception {
        ItemRequestReqDto request = ItemRequestReqDto.builder()
                .description("desc")
                .build();

        mockMvc.perform(post(url)
                        .header(X_USER_ID, 1L)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void checkCreateRequests_UserIdIsNotPositive_IsNotValid() throws Exception {
        ItemRequestReqDto request = ItemRequestReqDto.builder()
                .description("desc")
                .build();

        mockMvc.perform(post(url)
                        .header(X_USER_ID, -1L)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkCreateRequests_DescriptionIsNull_IsNotValid() throws Exception {
        ItemRequestReqDto request = ItemRequestReqDto.builder()
                .description(null)
                .build();

        mockMvc.perform(post(url)
                        .header(X_USER_ID, 1L)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkCreateRequests_DescriptionIsBlank_IsNotValid() throws Exception {
        ItemRequestReqDto request = ItemRequestReqDto.builder()
                .description("")
                .build();

        mockMvc.perform(post(url)
                        .header(X_USER_ID, 1L)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkGetAllRequests_IsValid() throws Exception {
        mockMvc.perform(get(url + "/all")
                        .header(X_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void checkGetAllRequests_UserIdIsNotPositive_IsNotValid() throws Exception {
        mockMvc.perform(get(url + "/all")
                        .header(X_USER_ID, -1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));

    }

    @Test
    public void checkGetAllForUser_IsValid() throws Exception {
        mockMvc.perform(get(url)
                        .header(X_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void checkGetAllForUser_UserIdIsNotPositive_IsNotValid() throws Exception {
        mockMvc.perform(get(url)
                        .header(X_USER_ID, -1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkGetRequest_IsValid() throws Exception {
        mockMvc.perform(get(url + "/1")
                        .header(X_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void checkGetRequest_RequestIdIsNotPositive_IsNotValid() throws Exception {
        mockMvc.perform(get(url + "/-1")
                        .header(X_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkGetRequest_UserIdIsNotPositive_IsNotValid() throws Exception {
        mockMvc.perform(get(url + "/1")
                        .header(X_USER_ID, -1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }
}
