package TockTiMan.controller;

import TockTiMan.controller.report.ReportController;
import TockTiMan.dto.report.BoardReportRequest;
import TockTiMan.dto.report.UserReportRequest;
import TockTiMan.entity.user.User;
import TockTiMan.repository.user.UserRepository;
import TockTiMan.service.report.ReportService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ReportControllerTest {
    @InjectMocks
    ReportController reportController;

    @Mock
    ReportService reportService;

    @Mock
    UserRepository userRepository;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void beforeEach() {

        mockMvc = MockMvcBuilders.standaloneSetup(reportController).build();
    }

    @Test
    @DisplayName("유저 신고 하기")
    public void reportUserTest() throws Exception {
        // given
        UserReportRequest req = new UserReportRequest(1, "내용");

        User user = createUserWithAdminRole();
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(userRepository.findByUsername(authentication.getName())).willReturn(Optional.of(user));


        // when, then
        mockMvc.perform(
                        post("/api/reports/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        verify(reportService).reportUser(user, req);
    }

    @Test
    @DisplayName("게시판 신고 하기")
    public void reportBoardTest() throws Exception {
        // given
        BoardReportRequest req = new BoardReportRequest(1, "내용");

        User user = createUserWithAdminRole();
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getId(), "", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(userRepository.findByUsername(authentication.getName())).willReturn(Optional.of(user));


        // when, then
        mockMvc.perform(
                        post("/api/reports/boards")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        verify(reportService).reportBoard(user, req);
    }
}
