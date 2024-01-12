package ua.thecompany.eloguru.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ua.thecompany.eloguru.model.EnumeratedRole;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/accounts/signup", "/accounts/login", "/", "/accounts/check",
                                "/login", "/sign-up", "/courses", "swagger-ui/**", "/actuator", "/actuator/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/accounts/logout").authenticated()
                        .requestMatchers(HttpMethod.POST, "/courses/*/enroll", "/courses/*/disenroll", "/feedbacks/*", "/feedbacks").hasAuthority(EnumeratedRole.STUDENT.toString())
                        .requestMatchers(HttpMethod.POST, "/courses/create", "/courses/*/topics/create").hasAuthority(EnumeratedRole.TEACHER.toString())
                        .requestMatchers(HttpMethod.GET, "/feedbacks/*", "/courses", "/courses/*", "/courses/*/topics/*", "/courses/*/topics").permitAll()
                        .requestMatchers(HttpMethod.GET, "/accounts/*", "/accounts/list").hasAuthority(EnumeratedRole.ADMIN.toString())
                        .requestMatchers(HttpMethod.PUT, "/feedbacks/*").hasAuthority(EnumeratedRole.STUDENT.toString())
                        .requestMatchers(HttpMethod.PUT, "/courses/*", "/courses/*/topics/*").hasAnyAuthority(EnumeratedRole.ADMIN.toString(), EnumeratedRole.TEACHER.toString())
                        .requestMatchers(HttpMethod.DELETE, "/courses/force_delete").hasAnyAuthority(EnumeratedRole.ADMIN.toString())
                        .requestMatchers(HttpMethod.DELETE, "/courses/*", "/courses/*/topics/*").hasAnyAuthority(EnumeratedRole.ADMIN.toString(), EnumeratedRole.TEACHER.toString())
                        .requestMatchers(HttpMethod.DELETE, "/accounts/*").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/feedbacks/*").hasAnyAuthority(EnumeratedRole.ADMIN.toString(), EnumeratedRole.STUDENT.toString())
                        .requestMatchers("/swagger-ui/**", "/api-docs/*").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
    }
}
