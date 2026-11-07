package com.ullas.University.management;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = {
		"app.seed.enabled=true",
		"app.seed.admin-password=Admin-test-password-123",
		"app.seed.teacher-password=Teacher-test-password-123",
		"app.seed.student-password=Student-test-password-123"
})
@AutoConfigureMockMvc
class UniversityManagementApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void loginPageRenders() throws Exception {
		mockMvc.perform(get("/login"))
				.andExpect(status().isOk())
				.andExpect(view().name("login"));
	}

	@Test
	void stylesheetIsServedFromConfiguredLocation() throws Exception {
		mockMvc.perform(get("/css/style.css"))
				.andExpect(status().isOk());
	}

	@Test
	void dashboardRequiresAuthentication() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login"));
	}

	@Test
	void adminCanCreateAccountThroughForm() throws Exception {
		mockMvc.perform(post("/admins/store")
				.with(user("admin").roles("ADMIN"))
				.with(csrf())
				.param("name", "Test Admin")
				.param("office", "Test Office")
				.param("username", "testadmin")
				.param("email", "testadmin@example.test")
				.param("password", "New-admin-test-password-123"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admins"));
	}

	@Test
	void adminApiRequiresAuthentication() throws Exception {
		mockMvc.perform(get("/api/admins").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
	}

	@Test
	void nonAdminCannotAccessAdminApi() throws Exception {
		mockMvc.perform(get("/api/admins")
				.with(user("teacher").roles("TEACHER"))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}

	@Test
	void adminCanCreateAccountThroughApiWithoutReturningPassword() throws Exception {
		mockMvc.perform(post("/api/admins")
				.with(user("admin").roles("ADMIN"))
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						  "name": "API Test Admin",
						  "office": "API Office",
						  "username": "apiadmin",
						  "email": "apiadmin@example.test",
						  "password": "New-api-admin-password-123"
						}
						"""))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", matchesPattern("/api/admins/[0-9]+")))
				.andExpect(jsonPath("$.username").value("apiadmin"))
				.andExpect(jsonPath("$.password").doesNotExist());
	}

}
