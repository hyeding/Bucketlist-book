package TockTiMan.controller;

import TockTiMan.controller.admin.AdminController;
import TockTiMan.service.admin.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {
    @InjectMocks
    AdminController adminController;

    @Mock
    AdminService adminService;

    MockMvc mockMvc;
    ObjectMapper objectMapper;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();


    }

    @Test
    @DisplayName("정지된 유저 관리")
    public void manageReportedUserTest() throws Exception {
        // given

        // when, then
        mockMvc.perform(
                        get("/api/admin/manages/users"))
                .andExpect(status().isOk());
        verify(adminService).manageReportedUser();
    }

    @Test
    @DisplayName("정지된 게시글 관리")
    public void manageReportedBoardTest() throws Exception {
        // given

        // when, then
        mockMvc.perform(
                        get("/api/admin/manages/boards"))
                .andExpect(status().isOk());
        verify(adminService).manageReportedBoards();
    }

    @Test
    @DisplayName("신고된 유저 정지 해제")
    public void unlockUser() throws Exception {
        // given
        int id = 1;

        // when, then
        mockMvc.perform(
                        post("/api/admin/manages/users/{id}", id))
                .andExpect(status().isOk());
        verify(adminService).unlockUser(id);
    }

    @Test
    @DisplayName("신고된 게시글 정지 해제")
    public void unlockBoard() throws Exception {
        // given
        int id = 1;

        // when, then
        mockMvc.perform(
                        post("/api/admin/manages/boards/{id}", id))
                .andExpect(status().isOk());
        verify(adminService).unlockBoard(id);
    }

}
