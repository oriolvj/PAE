package com.pae.pae.config;

import com.pae.pae.middelware.JWTFilter;
import com.pae.pae.models.Rols;
import com.pae.pae.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JWTFilter filter;
    private final AuthService uds;
    private final AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    public SecurityConfig(JWTFilter filter, @Lazy AuthService uds, AuthenticationConfiguration authenticationConfiguration) {
        this.filter = filter;
        this.uds = uds;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(authorize -> authorize
                        // Aqui es poden afegir els endpoints que es volen protegir amb els rols adequats
                        .requestMatchers(HttpMethod.POST, "/usuaris/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuaris").permitAll()
                        .anyRequest().authenticated()
                )
                .userDetailsService(uds)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((req, res, exc) -> res.sendError(401, "Unauthorized"))
                        .accessDeniedHandler((req, res, exc) -> res.sendError(403, "Forbidden"))
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        System.out.println("HTTP" + http);
        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

   @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
