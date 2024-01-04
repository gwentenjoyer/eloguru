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
                        .requestMatchers("/account/signup", "/account/login", "/", "/account/check",
                                "/login", "/sign-up", "/courses", "swagger-ui/**", "/actuator", "/actuator/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/account/logout").authenticated()
                        .requestMatchers(HttpMethod.POST, "/feedback").hasAuthority(EnumeratedRole.STUDENT.toString())
                        .requestMatchers(HttpMethod.POST, "/course/*/enroll", "/course/*/disenroll", "/feedback/**").hasAuthority(EnumeratedRole.STUDENT.toString())
                        .requestMatchers(HttpMethod.POST, "/course/create", "/course/*/topic/create").hasAuthority(EnumeratedRole.TEACHER.toString())
                        .requestMatchers(HttpMethod.GET, "/feedback/*", "/course", "/course/*", "/course/*/topic/*", "/course/*/topic").permitAll()
                        .requestMatchers(HttpMethod.GET, "/account/*", "/account/list").hasAuthority(EnumeratedRole.ADMIN.toString())
                        .requestMatchers(HttpMethod.PUT, "/feedback/*").hasAuthority(EnumeratedRole.STUDENT.toString())
                        .requestMatchers(HttpMethod.PUT, "/course/*", "/course/*/topic/*").hasAnyAuthority(EnumeratedRole.ADMIN.toString(), EnumeratedRole.TEACHER.toString())
                        .requestMatchers(HttpMethod.DELETE, "/course/force_delete").hasAnyAuthority(EnumeratedRole.ADMIN.toString())
                        .requestMatchers(HttpMethod.DELETE, "/course/*", "/course/*/topic/*").hasAnyAuthority(EnumeratedRole.ADMIN.toString(), EnumeratedRole.TEACHER.toString())
                        .requestMatchers(HttpMethod.DELETE, "/account/*").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/feedback/*").hasAnyAuthority(EnumeratedRole.ADMIN.toString(), EnumeratedRole.STUDENT.toString())
                        .requestMatchers("/swagger-ui/**", "/api-docs/*").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//                .logout((logout) -> {logout.logoutUrl("/account/logout");
//                    logout.addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.CACHE)));
//                    logout.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());})
                    .build();
    }
}
