package com.project_vector_backend;

import com.project_vector_backend.configurations.CorsConfiguration;
import com.project_vector_backend.configurations.CustomUserDetailsService;
import com.project_vector_backend.configurations.JwtAuthenticationEntryPoint;
import com.project_vector_backend.configurations.JwtAuthenticationFilter;
import com.project_vector_backend.configurations.SecurityConfig;
import com.project_vector_backend.controllers.AuthUserController;
import com.project_vector_backend.models.AuthUser;
import com.project_vector_backend.repositories.AuthUserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(AuthUserController.class)
@AutoConfigureJsonTesters
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = { SecurityConfig.class, CustomUserDetailsService.class,
		JwtAuthenticationEntryPoint.class, JwtAuthenticationFilter.class, CorsConfiguration.class })
class ProjectVectorBackendApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JacksonTester<AuthUser> jsonAuthUser;

	@MockBean
	CustomUserDetailsService customUserDetailsService;

	@MockBean
	JwtAuthenticationFilter jwtAuthenticationFilter;

	@MockBean
	private AuthUserRepository authUserRepository;

	@Test
	void signIn() throws Exception {

		AuthUser user = new AuthUser();
		user.setUsername("admin");
		user.setPassword("admin");
		authUserRepository.save(user);

		MockHttpServletResponse response = mockMvc
				.perform(
						post("/user/signin/").contentType(MediaType.APPLICATION_JSON).content(jsonAuthUser.write(user).getJson()))
				.andReturn().getResponse();

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	}

	@Test
	void authUsers() throws Exception {
		AuthUser authUser = new AuthUser();
		authUser.setUsername("admin");
		authUser.setPassword("admin");
		authUserRepository.save(authUser);

		MockHttpServletResponse response = mockMvc.perform(get("/user").contentType("application/json")).andReturn()
				.getResponse();

		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
	}

}
