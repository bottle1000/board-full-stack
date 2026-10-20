package com.boardfullstack.global.exception;

import com.boardfullstack.domain.user.controller.AdminController;
import com.boardfullstack.domain.user.service.AdminService;
import com.boardfullstack.domain.post.dto.request.PostCreateRequest;
import com.boardfullstack.global.common.response.PagedResponse;
import jakarta.validation.Valid;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitConfig(AccessDeniedRegressionTest.Config.class)
@WebAppConfiguration
class AccessDeniedRegressionTest {
    @Configuration @EnableWebMvc @EnableMethodSecurity
    @org.springframework.data.web.config.EnableSpringDataWebSupport
    static class Config {
        @Bean AdminService adminService() { return mock(AdminService.class); }
        @Bean AdminController adminController(AdminService s) { return new AdminController(s); }
        @Bean GlobalExceptionHandler handler() { return new GlobalExceptionHandler(); }
        @Bean ProbeController probe() { return new ProbeController(); }
    }
    @RestController
    static class ProbeController {
        @GetMapping("/probe/internal") Object internal() { throw new IllegalStateException("private implementation detail"); }
        @GetMapping("/probe/custom") Object custom() { throw new CustomException(ErrorCode.POST_FORBIDDEN); }
        @PostMapping("/probe/validate") Object validate(@Valid @RequestBody PostCreateRequest body) { return body; }
    }
    @Autowired WebApplicationContext context;
    @Autowired AdminService service;
    MockMvc mvc;
    @BeforeEach void setup() { mvc=MockMvcBuilders.webAppContextSetup(context).build(); reset(service); }
    @AfterEach void cleanup() { SecurityContextHolder.clearContext(); }
    void role(String role) { SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken("qa-user", null, AuthorityUtils.createAuthorityList(role))); }
    @Test void userIsDeniedWith403AndServiceNotCalled() throws Exception {
        role("ROLE_USER");
        mvc.perform(get("/api/admin/users")).andExpect(status().isForbidden())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.error.code").value("AUTH_FORBIDDEN"));
        verifyNoInteractions(service);
    }
    @Test void administratorStillGets200() throws Exception {
        role("ROLE_ADMIN");
        when(service.getUsers(any())).thenReturn(new PagedResponse<>(List.of(),
            new PagedResponse.PageInfo(0,20,0,0,false,false)));
        mvc.perform(get("/api/admin/users")).andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
        verify(service).getUsers(any());
    }
    @Test void realInternalErrorStillGets500WithoutExceptionDetails() throws Exception {
        mvc.perform(get("/probe/internal")).andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.error.code").value("COMMON_INTERNAL_SERVER_ERROR"))
            .andExpect(jsonPath("$.error.message").value("서버 내부 오류가 발생했습니다."));
    }
    @Test void existingPostPermissionErrorIsPreserved() throws Exception {
        mvc.perform(get("/probe/custom")).andExpect(status().isForbidden())
            .andExpect(jsonPath("$.error.code").value("POST_FORBIDDEN"));
    }
    @Test void validationStillGets400() throws Exception {
        mvc.perform(post("/probe/validate").contentType("application/json")
            .content("{\"title\":\"\",\"content\":\"valid\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error.code").value("COMMON_INVALID_REQUEST"));
    }
}
