package TockTiMan.controller;

import TockTiMan.controller.comment.CommentController;
import TockTiMan.dto.comment.CommentCreateRequest;
import TockTiMan.dto.comment.CommentReadCondition;
import TockTiMan.entity.user.User;
import TockTiMan.repository.user.UserRepository;
import TockTiMan.service.comment.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static TockTiMan.factory.UserFactory.createUserWithAdminRole;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {
    @InjectMocks
    CommentController commentController;

    @Mock
    UserRepository userRepository;

    @Mock
    CommentService commentService;
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
    }

    @Test
    @DisplayName("댓글 작성")
    public void createCommentTest() throws Exception {
        // given
        CommentCreateRequest req = new CommentCreateRequest("content", 1);

        User user = createUserWithAdminRole();
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(userRepository.findByUsername(authentication.getName())).willReturn(Optional.of(user));

        //when then
        mockMvc.perform(
                        post("/api/comments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        verify(commentService).create(req, user);
    }


    @Test
    @DisplayName("댓글 조회")
    public void findAllTest() throws Exception {
        // given
        CommentReadCondition commentReadCondition = new CommentReadCondition(1);

        // when, then
        mockMvc.perform(
                        get("/api/comments")
                                .param("boardId", String.valueOf(commentReadCondition.getBoardId()))
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        verify(commentService).findAll(commentReadCondition);
    }

    @Test
    @DisplayName("댓글 삭제")
    public void deleteTest() throws Exception {
        // given
        int id = 1;

        User user = createUserWithAdminRole();
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(userRepository.findByUsername(authentication.getName())).willReturn(Optional.of(user));

        // when, then
        mockMvc.perform(
                        delete("/api/comments/{id}", id))
                .andExpect(status().isOk());
        verify(commentService).delete(id, user);
    }
}
