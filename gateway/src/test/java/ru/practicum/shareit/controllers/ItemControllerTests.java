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
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.comment.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.utils.RequestParams.X_USER_ID;

@WebMvcTest(ItemController.class)
@SpringJUnitWebConfig
public class ItemControllerTests {

    private final String url = "/items";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemClient itemClient;

    @BeforeTestClass
    public void setUp() {
        when(itemClient.createItem(any(), any()))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());

        when(itemClient.editItem(any(), any(), any()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).build());

        when(itemClient.getAllItems(any()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).build());

        when(itemClient.getItem(any(), any()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).build());

        when(itemClient.searchItem(any(), any()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).build());

        when(itemClient.addComment(any(), any(), any()))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());

        when(itemClient.getComments(any(), any()))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).build());

        doNothing().when(itemClient).deleteItem(any(), any());
    }

    @Test
    public void checkCreateItemIsValid() throws Exception {
        ItemRequestDto item = ItemRequestDto.builder()
                .name("testName")
                .description("desc")
                .available(true)
                .requestId(1L)
                .build();

        mockMvc.perform(post(url)
                        .header(X_USER_ID, 1L)
                        .content(objectMapper.writeValueAsString(item))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void checkCreateItemNameIsNullIsNotValid() throws Exception {
        ItemRequestDto item = ItemRequestDto.builder()
                .name(null)
                .description("desc")
                .available(true)
                .requestId(1L)
                .build();

        mockMvc.perform(post(url)
                        .header(X_USER_ID, 1L)
                        .content(objectMapper.writeValueAsString(item))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkCreateItemDescriptionIsNullIsNotValid() throws Exception {
        ItemRequestDto item = ItemRequestDto.builder()
                .name("name")
                .description(null)
                .available(true)
                .requestId(1L)
                .build();

        mockMvc.perform(post(url)
                        .header(X_USER_ID, 1L)
                        .content(objectMapper.writeValueAsString(item))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkCreateItemAvailableIsNullIsNotValid() throws Exception {
        ItemRequestDto item = ItemRequestDto.builder()
                .name("name")
                .description("desc")
                .available(null)
                .requestId(1L)
                .build();

        mockMvc.perform(post(url)
                        .header(X_USER_ID, 1L)
                        .content(objectMapper.writeValueAsString(item))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkCreateItemRequestIdIsNotPositiveIsNotValid() throws Exception {
        ItemRequestDto item = ItemRequestDto.builder()
                .name("name")
                .description("desc")
                .available(true)
                .requestId(-1L)
                .build();

        mockMvc.perform(post(url)
                        .header(X_USER_ID, 1L)
                        .content(objectMapper.writeValueAsString(item))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkCreateItemUserIdIsNotPositiveIsNotValid() throws Exception {
        ItemRequestDto item = ItemRequestDto.builder()
                .name("name")
                .description("desc")
                .available(true)
                .requestId(1L)
                .build();

        mockMvc.perform(post(url)
                        .header(X_USER_ID, -1L)
                        .content(objectMapper.writeValueAsString(item))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkEditItemIsValid() throws Exception {
        ItemRequestDto item = ItemRequestDto.builder()
                .name("name")
                .description("desc")
                .available(true)
                .requestId(1L)
                .build();

        mockMvc.perform(patch(url + "/1")
                        .header(X_USER_ID, 1L)
                        .content(objectMapper.writeValueAsString(item))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void checkEditItemIdIsNotPositiveIsNotValid() throws Exception {
        ItemRequestDto item = ItemRequestDto.builder()
                .name("name")
                .description("desc")
                .available(true)
                .requestId(1L)
                .build();

        mockMvc.perform(patch(url + "/-1")
                        .header(X_USER_ID, 1L)
                        .content(objectMapper.writeValueAsString(item))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkEditItemUserIdIsNotPositiveIsNotValid() throws Exception {
        ItemRequestDto item = ItemRequestDto.builder()
                .name("name")
                .description("desc")
                .available(true)
                .requestId(1L)
                .build();

        mockMvc.perform(patch(url + "/1")
                        .header(X_USER_ID, -1L)
                        .content(objectMapper.writeValueAsString(item))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkGetItemIsValid() throws Exception {
        mockMvc.perform(get(url + "/1")
                        .header(X_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void checkGetItemIdIsNotPositiveIsNotValid() throws Exception {
        mockMvc.perform(get(url + "/-1")
                        .header(X_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkGetItemUserIdIsNotPositiveIsNotValid() throws Exception {
        mockMvc.perform(get(url + "/1")
                        .header(X_USER_ID, -1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkGetAllItemsIsValid() throws Exception {
        mockMvc.perform(get(url)
                        .header(X_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void checkGetAllItemsUserIdIsNotPositiveIsNotValid() throws Exception {
        mockMvc.perform(get(url)
                        .header(X_USER_ID, -1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkDeleteItemIsValid() throws Exception {
        mockMvc.perform(delete(url + "/1")
                        .header(X_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void checkDeleteItemIdIsNotPositiveIsNotValid() throws Exception {
        mockMvc.perform(delete(url + "/-1")
                        .header(X_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkDeleteItemUserIdIsNotPositiveIsNotValid() throws Exception {
        mockMvc.perform(delete(url + "/1")
                        .header(X_USER_ID, -1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkSearchIsValid() throws Exception {
        mockMvc.perform(get(url + "/search?text=text")
                        .header(X_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void checkSearchTextIsNullIsNotValid() throws Exception {
        mockMvc.perform(get(url + "/search")
                        .header(X_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void checkSearchTextIsWrongNameParamIsNotValid() throws Exception {
        mockMvc.perform(get(url + "/search?text1=text")
                        .header(X_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void checkSearchUserIdIsNotPositiveIsNotValid() throws Exception {
        mockMvc.perform(get(url + "/search?text=text")
                        .header(X_USER_ID, -1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkAddComment_IsValid() throws Exception {
        CommentRequestDto comment = CommentRequestDto.builder()
                .text("text")
                .build();

        mockMvc.perform(post(url + "/1/comment")
                        .header(X_USER_ID, 1L)
                        .content(objectMapper.writeValueAsString(comment))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void checkAddComment_TextIsNull_IsNotValid() throws Exception {
        CommentRequestDto comment = CommentRequestDto.builder()
                .text(null)
                .build();

        mockMvc.perform(post(url + "/1/comment")
                        .header(X_USER_ID, 1L)
                        .content(objectMapper.writeValueAsString(comment))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkAddComment_TextIsBlank_IsNotValid() throws Exception {
        CommentRequestDto comment = CommentRequestDto.builder()
                .text("")
                .build();

        mockMvc.perform(post(url + "/1/comment")
                        .header(X_USER_ID, 1L)
                        .content(objectMapper.writeValueAsString(comment))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkAddComment_NoComment_IsNotValid() throws Exception {
        mockMvc.perform(post(url + "/1/comment")
                        .header(X_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void checkAddComment_UserIdIsNotPositive_IsNotValid() throws Exception {
        CommentRequestDto comment = CommentRequestDto.builder()
                .text("text")
                .build();

        mockMvc.perform(post(url + "/1/comment")
                        .header(X_USER_ID, -1L)
                        .content(objectMapper.writeValueAsString(comment))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkAddComment_ItemIdIsNotPositive_IsNotValid() throws Exception {
        CommentRequestDto comment = CommentRequestDto.builder()
                .text("text")
                .build();

        mockMvc.perform(post(url + "/-1/comment")
                        .header(X_USER_ID, 1L)
                        .content(objectMapper.writeValueAsString(comment))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkGetComments_IsValid() throws Exception {
        mockMvc.perform(get(url + "/1/comment")
                        .header(X_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void checkGetComments_ItemIdIsNotPositive_IsNotValid() throws Exception {
        mockMvc.perform(get(url + "/-1/comment")
                        .header(X_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }

    @Test
    public void checkGetComments_UserIdIsNotPositive_IsNotValid() throws Exception {
        mockMvc.perform(get(url + "/1/comment")
                        .header(X_USER_ID, -1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Валидация не пройдена")));
    }
}
