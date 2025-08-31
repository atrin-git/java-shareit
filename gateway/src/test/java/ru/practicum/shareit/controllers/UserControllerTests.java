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
import ru.practicum.shareit.user.UserClient;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.dto.UserUpdateRequestDto;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@SpringJUnitWebConfig
public class UserControllerTests {

    private final String url = "/users";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserClient userClient;

    @BeforeTestClass
    public void setUp() {
        when(userClient.createUser(any()))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());

        when(userClient.editUser(any(), any()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).build());

        when(userClient.getUser(any()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).build());

        doNothing().when(userClient).deleteUser(any());
    }

    @Test
    public void checkCreateUserIsValid() throws Exception {
        UserRequestDto user = UserRequestDto.builder()
                .name("testName")
                .email("test@email.ru")
                .build();

        mockMvc.perform(post(url)
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void checkCreateUserNameIsNullIsNotValid() throws Exception {
        UserRequestDto user = UserRequestDto.builder()
                .name(null)
                .email("test@email.ru")
                .build();

        mockMvc.perform(post(url)
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkCreateUserNameIsBlankIsNotValid() throws Exception {
        UserRequestDto user = UserRequestDto.builder()
                .name("")
                .email("test@email.ru")
                .build();

        mockMvc.perform(post(url)
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkCreateUserEmailIsNullIsNotValid() throws Exception {
        UserRequestDto user = UserRequestDto.builder()
                .name("testName")
                .email(null)
                .build();

        mockMvc.perform(post(url)
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkCreateUserEmailIsBlankIsNotValid() throws Exception {
        UserRequestDto user = UserRequestDto.builder()
                .name("testName")
                .email("")
                .build();

        mockMvc.perform(post(url)
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkCreateUserEmailIsNotValidPatternIsNotValid() throws Exception {
        UserRequestDto user = UserRequestDto.builder()
                .name("testName")
                .email("test")
                .build();

        mockMvc.perform(post(url)
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkGetUserIsSuccess() throws Exception {
        mockMvc.perform(get(url + "/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void checkGetUserIdIsStringIsNotValid() throws Exception {
        mockMvc.perform(get(url + "/text"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void checkGetUserIdIsNotPositiveIsNotValid() throws Exception {
        mockMvc.perform(get(url + "/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkEditUserIsValid() throws Exception {
        UserUpdateRequestDto user = UserUpdateRequestDto.builder()
                .name("testName")
                .email("test@email.com")
                .build();

        mockMvc.perform(patch(url + "/1")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void checkEditUserEmailNotValidPatternIsNotValid() throws Exception {
        UserUpdateRequestDto user = UserUpdateRequestDto.builder()
                .email("test")
                .build();

        mockMvc.perform(patch(url + "/1")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkEditUserIdIsNotPositiveIsNotValid() throws Exception {
        UserUpdateRequestDto user = UserUpdateRequestDto.builder().build();

        mockMvc.perform(patch(url + "/-1")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkDeleteUserIsValid() throws Exception {
        mockMvc.perform(delete(url + "/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void checkDeleteUserIsIsNotPositiveIsValid() throws Exception {
        mockMvc.perform(delete(url + "/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }
}
