
package com.bootcamp.security.filter;

import com.bootcamp.security.authentication.PagUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.sql.DataSource;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(2)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${app.security.loginUrl:/api/login/**}") //default /api/login/**
    private String loginUrl;

    @Value("${app.security.cors.origins:*}") //default *
    private String origins;

    @Value("${app.security.cors.headers:*}") //default *
    private String headers;

    @Value("${app.security.cors.methods:*}") //default *
    private String methods;

    //@Value("${app.security.cors.allowedCredentials:true}") //default true
    private Boolean allowedCredentials;

    @Value("${app.security.cors.exposedHeaders:Access-Control-Allow-Origin,Access-Control-Allow-Credentials}") //default Access-Control-Allow-Origin,Access-Control-Allow-Credentials
    private String exposedHeaders;

    @Value("${app.api.version:1.0}") //default 1.0
    private String apiVersion;

    @Autowired
    private PagUserDetailsService userDetailsService;

//    @Autowired
//    private DataSource dataSource;
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication().dataSource(dataSource);
//    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(passwordEncoder());

        //this configuration is for checking resources for asklytics team
        //this is not a regular authentication process
        //it will look for user in users and authorities table in asklytics db
        //authenticationManagerBuilder.jdbcAuthentication().dataSource(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    public AlResourceAuthenticationFilter alResourceAuthenticationFilter() throws Exception{
        return new AlResourceAuthenticationFilter();
    }

    @Bean
    public JwtLoginFilter jwtLoginFilter() throws Exception {
        return new JwtLoginFilter(loginUrl, authenticationManager(), authenticationFailureHandler());
    }

    @Bean
    public AuthenticationEntryPoint jwtAuthenticationEntryPoint(){
        return new JwtAuthenticationEntryPoint();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return new AlAuthenticationFailureHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .addFilterBefore(corsFilter(), ChannelProcessingFilter.class)
            // we don't need CSRF because our token is invulnerable
            .csrf().disable()
            .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint()).and()

             // don't create session
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

            .authorizeRequests()
            //.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

            // allow anonymous resource requests
            .antMatchers(
                    "/",
                    "/**/product/pricing/**",
                    "/*.html",
                    "/v2/api-docs",           // swagger
                    "/webjars/**",            // swagger-ui webjars
                    "/swagger-resources/**",  // swagger-ui resources
                    "/configuration/**",
                    "/favicon.ico",
                    "/**/*.html",
                    "/**/*.css",
                    "/**/*.js"
            ).permitAll()
            .antMatchers(HttpMethod.POST, loginUrl).permitAll()
            .antMatchers(HttpMethod.GET, "/logout/**").permitAll()
                .antMatchers("**/admin/**").hasAnyAuthority("ADMIN_TECHNIQUE", "ADMIN_CONTENU")
                .antMatchers("**/stat/**").hasAnyAuthority("ADMIN_TECHNIQUE", "ADMIN_CONTENU", "PARTENAIRE")
                .anyRequest().permitAll()
            //.anyRequest().authenticated()

            .and()
                // We filter the api/login/resource requests sepera
            .addFilterBefore(jwtLoginFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(alResourceAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        http.headers().cacheControl();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(allowedCredentials);

        if ("*".equals(origins)){
            config.addAllowedOrigin(origins);
        } else {
            String[] originTokenizer = origins.split(",");
            for (String origin : originTokenizer) {
                config.addAllowedOrigin(origin);
            }
        }

        if ("*".equals(headers)){
            config.addAllowedHeader(headers);
        } else {
            String[] headerTokenizer = headers.split(",");
            for (String header : headerTokenizer) {
                config.addAllowedHeader(header);
            }
        }

        if ("*".equals(methods)){
            config.addAllowedMethod(methods);
        } else {
            String[] methodsTokenizer = methods.split(",");
            for (String method : methodsTokenizer) {
                config.addAllowedMethod(method);
            }
        }

        String[] exposedHeaderTokenizer = exposedHeaders.split(",");
        for (String exposedHeader : exposedHeaderTokenizer){
            config.addExposedHeader(exposedHeader);
        }
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(  "/**/resetPassword/**", "/**/logout/**", "/**/forgotPassword/**", "/**/customer/**", "/**/product/productEditionLimit/**");
    }
}
