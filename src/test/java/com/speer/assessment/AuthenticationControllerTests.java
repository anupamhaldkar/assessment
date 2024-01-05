package com.speer.assessment;

import com.speer.assessment.entity.User;
import com.speer.assessment.service.UserService;
import com.speer.assessment.util.JwtUtil;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AuthenticationControllerTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @TestConfiguration
    public static class TestConfig {
        @Bean
        @Primary
        public UserDetails loginMock() {
            UserService userService1 = Mockito.mock(UserService.class);
           UserDetails user = new org.springframework.security.core.userdetails.User("test",
                    "test", new ArrayList<>());
            Mockito.when(userService1.loadUserByUsername("test")).thenReturn(new org.springframework.security.core.userdetails.User("test",
                    "test", new ArrayList<>()));
            return user;
        }

        @Bean
        @Primary
        public User signUpMock() {
            UserService userService1 = Mockito.mock(UserService.class);
            User user = new User();
            user.setUsername("test");
            Mockito.when(userService1.signUp(Mockito.any())).thenReturn(user);
            return user;
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"requests/input.json"})
    public void testLogin(String reqBody) throws Exception {
        InputStream in = new ClassPathResource(reqBody).getInputStream();
        String request = new String(in.readAllBytes(), StandardCharsets.UTF_8);


        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("token").exists())
                .andDo(print())
                .andReturn();

    }

    @ParameterizedTest
    @ValueSource(strings = {"requests/input.json"})
    public void testSignUp(String reqBody) throws Exception {
        InputStream in = new ClassPathResource(reqBody).getInputStream();
        String request = new String(in.readAllBytes(), StandardCharsets.UTF_8);


        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();


    }

}


