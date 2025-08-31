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
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.utils.RequestParams.X_USER_ID;

@WebMvcTest(BookingController.class)
@SpringJUnitWebConfig
public class BookingControllerTests {

    private final String url = "/bookings";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookingClient bookingClient;

    @BeforeTestClass
    public void setUp() {
        when(bookingClient.bookItem(any(), any()))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());

        when(bookingClient.approveBooking(any(), any(), any()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).build());
    }

    @Test
    public void checkCreateBooking_IsValid() throws Exception {
        BookingRequestDto booking = BookingRequestDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        mockMvc.perform(post(url)
                        .header(X_USER_ID, 1L)
                        .content(objectMapper.writeValueAsString(booking))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void checkCreateBooking_ItemIdIsNull_IsNotValid() throws Exception {
        BookingRequestDto booking = BookingRequestDto.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        mockMvc.perform(post(url)
                        .header(X_USER_ID, 1L)
                        .content(objectMapper.writeValueAsString(booking))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkCreateBooking_StartInPast_IsNotValid() throws Exception {
        BookingRequestDto booking = BookingRequestDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        mockMvc.perform(post(url)
                        .header(X_USER_ID, 1L)
                        .content(objectMapper.writeValueAsString(booking))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkCreateBooking_EndInPast_IsNotValid() throws Exception {
        BookingRequestDto booking = BookingRequestDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().minusDays(2))
                .build();

        mockMvc.perform(post(url)
                        .header(X_USER_ID, 1L)
                        .content(objectMapper.writeValueAsString(booking))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkCreateBooking_ItemIdIsNotPositive_IsNotValid() throws Exception {
        BookingRequestDto booking = BookingRequestDto.builder()
                .itemId(-1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().minusDays(2))
                .build();

        mockMvc.perform(post(url)
                        .header(X_USER_ID, 1L)
                        .content(objectMapper.writeValueAsString(booking))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void checkCreateBooking_UserIdIsNotPositive_IsNotValid() throws Exception {
        BookingRequestDto booking = BookingRequestDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().minusDays(2))
                .build();

        mockMvc.perform(post(url)
                        .header(X_USER_ID, -1L)
                        .content(objectMapper.writeValueAsString(booking))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkApproveBooking_IsValid() throws Exception {
        mockMvc.perform(patch(url + "/1?approved=true")
                        .header(X_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void checkApproveBooking_BookingIdIsNotPositive_IsNotValid() throws Exception {
        mockMvc.perform(patch(url + "/-1?approved=true")
                        .header(X_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkApproveBooking_UserIdIsNotPositive_IsNotValid() throws Exception {
        mockMvc.perform(patch(url + "/1?approved=true")
                        .header(X_USER_ID, -1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkApproveBooking_ApprovedIsNotValid_IsNotValid() throws Exception {
        mockMvc.perform(patch(url + "/1?approved=123")
                        .header(X_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void checkGetBooking_IsValid() throws Exception {
        mockMvc.perform(get(url + "/1")
                        .header(X_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void checkGetBooking_BookingIdIsNotPositive_IsValid() throws Exception {
        mockMvc.perform(get(url + "/-1")
                        .header(X_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkGetBooking_UserIdIsNotPositive_IsValid() throws Exception {
        mockMvc.perform(get(url + "/1")
                        .header(X_USER_ID, -1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkGetByOwner_IsValid() throws Exception {
        mockMvc.perform(get(url + "/owner?state=ALL")
                        .header(X_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void checkGetByOwner_UserIdIsNotPositive_IsValid() throws Exception {
        mockMvc.perform(get(url + "/owner?state=ALL")
                        .header(X_USER_ID, -1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkGetByOwner_StateIsNotValid_IsValid() throws Exception {
        mockMvc.perform(get(url + "/owner?state=123")
                        .header(X_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void checkGetByBooker_IsValid() throws Exception {
        mockMvc.perform(get(url + "?state=ALL")
                        .header(X_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void checkGetByBooker_UserIdIsNotPositive_IsValid() throws Exception {
        mockMvc.perform(get(url + "?state=ALL")
                        .header(X_USER_ID, -1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkGetByBooker_StateIsNotValid_IsValid() throws Exception {
        mockMvc.perform(get(url + "?state=123")
                        .header(X_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
