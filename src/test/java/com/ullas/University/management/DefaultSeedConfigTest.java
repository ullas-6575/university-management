package com.ullas.University.management;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = "app.seed.enabled=true")
@AutoConfigureMockMvc
class DefaultSeedConfigTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void defaultSeedCredentialsAllowApplicationToStart() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }
}
