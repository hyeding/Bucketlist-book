package TockTiMan.controller;

import TockTiMan.controller.message.MessageController;
import TockTiMan.dto.message.MessageCreateRequest;
import TockTiMan.entity.user.User;
import TockTiMan.repository.user.UserRepository;
import TockTiMan.service.message.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static TockTiMan.factory.UserFactory.createUserWithAdminRole;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MessageControllerTest {
    @InjectMocks
    MessageController messageController;

    @Mock
    MessageService messageService;
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    UserRepository userRepository;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();

    }


    @Test
    @DisplayName("쪽지 작성")
    public void createMessageTest() throws Exception {
        // given
        MessageCreateRequest req = new MessageCreateRequest("타이틀", "내용", "유저닉네임");

        // 테스트 코드 진행시 SecurityContext에 유저 정보 미리 담아두기
        User user = createUserWithAdminRole();
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(userRepository.findByUsername(authentication.getName())).willReturn(Optional.of(user));

        // when, then
        mockMvc.perform(
                        post("/api/messages")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))

                .andExpect(status().isCreated());

        verify(messageService).createMessage(user, req);
    }

    @Test
    @DisplayName("받은 쪽지함 확인")
    public void receiveMessagesTest() throws Exception {
        // given

        // 테스트 코드 진행시 SecurityContext에 유저 정보 미리 담아두기
        User user = createUserWithAdminRole();
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(userRepository.findByUsername(authentication.getName())).willReturn(Optional.of(user));

        // when, then
        mockMvc.perform(
                        get("/api/messages/receiver"))
                .andExpect(status().isOk());
        verify(messageService).receiveMessages(user);
    }

    @Test
    @DisplayName("받은 쪽지 개별 확인")
    public void receiveMessageTest() throws Exception {
        // given
        int id = 1;

        // 테스트 코드 진행시 SecurityContext에 유저 정보 미리 담아두기
        User user = createUserWithAdminRole();
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(userRepository.findByUsername(authentication.getName())).willReturn(Optional.of(user));

        // when, then
        mockMvc.perform(
                        get("/api/messages/receiver/{id}", id))
                .andExpect(status().isOk());
        verify(messageService).receiveMessage(id, user);
    }

    @Test
    @DisplayName("보낸 쪽지함 확인")
    public void sendMessagesTest() throws Exception {
        // given
        // 테스트 코드 진행시 SecurityContext에 유저 정보 미리 담아두기
        User user = createUserWithAdminRole();
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(userRepository.findByUsername(authentication.getName())).willReturn(Optional.of(user));

        // when, then
        mockMvc.perform(
                        get("/api/messages/sender"))
                .andExpect(status().isOk());
        verify(messageService).sendMessages(user);
    }

    @Test
    @DisplayName("보낸 쪽지 개별 확인")
    public void sendMessageTest() throws Exception {
        // given
        int id = 1;

        // 테스트 코드 진행시 SecurityContext에 유저 정보 미리 담아두기
        User user = createUserWithAdminRole();
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(userRepository.findByUsername(authentication.getName())).willReturn(Optional.of(user));

        // when, then
        mockMvc.perform(
                        get("/api/messages/sender/{id}", id))
                .andExpect(status().isOk());
        verify(messageService).sendMessage(id, user);
    }

    @Test
    @DisplayName("받은 쪽지 삭제")
    public void deleteReceiveMessageTest() throws Exception {
        // given
        int id = 1;

        // 테스트 코드 진행시 SecurityContext에 유저 정보 미리 담아두기
        User user = createUserWithAdminRole();
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(userRepository.findByUsername(authentication.getName())).willReturn(Optional.of(user));

        // when, then
        mockMvc.perform(
                        delete("/api/messages/receiver/{id}", id))
                .andExpect(status().isOk());
        verify(messageService).deleteMessageByReceiver(id, user);
    }

    @Test
    @DisplayName("보낸 쪽지 삭제")
    public void deleteSenderMessageTest() throws Exception {
        // given
        int id = 1;

        // 테스트 코드 진행시 SecurityContext에 유저 정보 미리 담아두기
        User user = createUserWithAdminRole();
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(userRepository.findByUsername(authentication.getName())).willReturn(Optional.of(user));

        // when, then
        mockMvc.perform(
                        delete("/api/messages/sender/{id}", id))
                .andExpect(status().isOk());
        verify(messageService).deleteMessageBySender(id, user);
    }

}
