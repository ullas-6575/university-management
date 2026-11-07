package com.ullas.University.management;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
        "app.seed.enabled=true",
        "app.seed.admin-password=Admin-test-password-123",
        "app.seed.teacher-password=Teacher-test-password-123",
        "app.seed.student-password=Student-test-password-123"
})
@AutoConfigureMockMvc
class RoleAccessIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void adminCanAccessEveryResourceGroup() throws Exception {
        mockMvc.perform(get("/api/admins").with(user("admin").roles("ADMIN"))).andExpect(status().isOk());
        mockMvc.perform(get("/api/teachers").with(user("admin").roles("ADMIN"))).andExpect(status().isOk());
        mockMvc.perform(get("/api/students").with(user("admin").roles("ADMIN"))).andExpect(status().isOk());
    }

    @Test
    void teacherCanAccessOnlyTeachers() throws Exception {
        mockMvc.perform(get("/api/teachers").with(user("teacher").roles("TEACHER"))).andExpect(status().isOk());
        mockMvc.perform(get("/api/admins").with(user("teacher").roles("TEACHER"))).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/students").with(user("teacher").roles("TEACHER"))).andExpect(status().isForbidden());
    }

    @Test
    void studentCanAccessOnlyStudents() throws Exception {
        mockMvc.perform(get("/api/students").with(user("student").roles("STUDENT"))).andExpect(status().isOk());
        mockMvc.perform(get("/api/admins").with(user("student").roles("STUDENT"))).andExpect(status().isForbidden());
        mockMvc.perform(get("/api/teachers").with(user("student").roles("STUDENT"))).andExpect(status().isForbidden());
    }
}
