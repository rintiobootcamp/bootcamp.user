
package com.bootcamp.jwt;


import com.bootcamp.entities.PagRole;
import com.bootcamp.entities.PagUser;
import com.bootcamp.entities.PagRole;
import com.bootcamp.entities.UserRole;
import com.bootcamp.security.constants.SecurityToken;
import com.bootcamp.security.filter.JwtAuthenticationRequest;
import com.bootcamp.security.service.AuthenticationUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//@SpringBootTest(classes = SpringConfig.class)
//@AutoConfigureMockMvc
//@AutoConfigureWebMvc
//@TestExecutionListeners(MockitoTestExecutionListener.class)
public class AuthenticationTest extends AbstractTestNGSpringContextTests implements SecurityToken{
//    private static final String KNOWN_USER_MAIL = "valerian@mail.com";
//    private static final String KNOWN_USER_PASSWORD = "pa$$w0rd";
//    private static final String IN_MEMORY_KNOWN_USER_MAIL = "tdarex@gmail.com";
//    private static final String IN_MEMORY_KNOWN_USER_PASSWORD = "tgildas@gmail.com";
//
//    @MockBean
//    private AuthenticationUserService authenticationUserService;
//
//    @Autowired
//    private MockMvc mockMvc;
//
//
//    @Test
//    public void successLoginTest() throws Exception{
//        Mockito.when(authenticationUserService.getUser(KNOWN_USER_MAIL)).thenReturn(createdPagUser("admin"));
//        Answer<Collection<? extends GrantedAuthority>> answers = (invocationOnMock) -> getRoles();
//
//        Mockito.when(authenticationUserService.getAuthorities(KNOWN_USER_MAIL)).thenAnswer(answers);
//        ObjectMapper objectMapper = new ObjectMapper();
//        JwtAuthenticationRequest loginInfo = new JwtAuthenticationRequest(KNOWN_USER_MAIL, KNOWN_USER_PASSWORD);
//
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/login/")
//                .content(objectMapper.writeValueAsString(loginInfo));
//
//        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.cookie().exists(TOKEN));
//    }
//
//    @Test
//    public void incorrectPassword() throws Exception{
//        Mockito.when(authenticationUserService.getUser(KNOWN_USER_MAIL)).thenReturn(createdPagUser("admin"));
//        Answer<Collection<? extends GrantedAuthority>> answers = (invocationOnMock) -> getRoles();
//
//        Mockito.when(authenticationUserService.getAuthorities(KNOWN_USER_MAIL)).thenAnswer(answers);
//        ObjectMapper objectMapper = new ObjectMapper();
//        JwtAuthenticationRequest loginInfo = new JwtAuthenticationRequest(KNOWN_USER_MAIL, "abcdef");
//
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/auth")
//                .content(objectMapper.writeValueAsString(loginInfo));
//
//        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().is4xxClientError());
//    }
//
//    @Test
//    public void inMemoryDbLoginAndAlResourceFilterTest() throws Exception {
//        //Mockito.when(jdbcUserDetailsManager.loadUserByUsername(IN_MEMORY_KNOWN_USER_MAIL)).thenReturn(user());
//        //in memory db login test
//        ObjectMapper objectMapper = new ObjectMapper();
//        JwtAuthenticationRequest loginInfo = new JwtAuthenticationRequest(IN_MEMORY_KNOWN_USER_MAIL, IN_MEMORY_KNOWN_USER_PASSWORD);
//
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/auth")
//                .content(objectMapper.writeValueAsString(loginInfo));
//
//        MvcResult result = mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk())
//                                .andExpect(MockMvcResultMatchers.cookie().exists(TOKEN)).andReturn();
//        Cookie c = result.getResponse().getCookie(TOKEN);
//        requestBuilder = MockMvcRequestBuilders.get("/manage/health").servletPath("/manage/health").cookie(c);
//
//        mockMvc.perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isNotFound());
//    }
//
//    private static User user(){
//        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("USER");
//        grantedAuthorities.add(simpleGrantedAuthority);
//        return new User(IN_MEMORY_KNOWN_USER_MAIL, IN_MEMORY_KNOWN_USER_PASSWORD, grantedAuthorities);
//    }
//
//    private static PagUser createdPagUser(String roleStr){
//        PagUser pagUser = new PagUser();
//        pagUser.setNom("TOSSA Gildas");
//        pagUser.setUsername("username");
//        pagUser.setPassword("password");
//        pagUser.setId(1);
//
//        PagRole role = new PagRole();
//        role.setName(roleStr);
//        role.setId(1);
//
//        UserRole userRole = new UserRole();
//        userRole.setPagRole(role);
//        userRole.setPagUser(pagUser);
//
//        return pagUser;
//    }
//
//    private static Collection<? extends GrantedAuthority> getRoles(){
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("admin");
//        authorities.add(simpleGrantedAuthority);
//        return authorities;
//    }
}
