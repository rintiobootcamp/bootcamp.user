
package com.bootcamp.jwt;

import com.bootcamp.security.authentication.PagUserDetailsService;
import org.mockito.Mockito;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

@ComponentScan({"com.asklytics.security"})
@ImportResource({"classpath:/spring-security-test.xml"})
@EnableWebSecurity
@Configuration
public class SpringConfig {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return Mockito.mock(PagUserDetailsService.class);
    }
}
