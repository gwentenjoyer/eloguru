package ua.thecompany.eloguru.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import ua.thecompany.eloguru.model.EnumeratedRole;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.*;

import java.util.Arrays;


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
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                        .requestMatchers("/accounts/signup", "/accounts/login", "/", "/accounts/check", "/",
                                "/index.html", "/static/**", "/*.ico", "/*.json", "/*.png","/*.jpg",
                                "/static/coursesPhotos/**", "/coursesPhotos/**", "/accounts/activate", "/activate", "/about", "/login", "/sign-up",
                                "/courses", "/courses", "/course", "swagger-ui/**", "/actuator", "/actuator/**", "/accounts/signup").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/courses/{courseId}").hasAnyAuthority(EnumeratedRole.TEACHER.name().toLowerCase())
                        .requestMatchers(HttpMethod.GET, "/courses/**", "/courses/{courseId}", "/course/{courseId}",
                                "/accounts/teacher/{teacherId}/getName", "/courses/{courseId}/topics","/accounts/check").permitAll()
                        .requestMatchers("/profile", "/accounts/logout","/accounts/getUserInfo","/accounts","/accounts/refreshToken").authenticated()
                        .requestMatchers("/courses/create", "/admin/**", "/courses/{courseId}/topics/{topicId}/delete",
                                "/courses/{courseId}/topics/{topicId}", "/courses/{courseId}", "/courses/{courseId}/delete",
                                "/courses/create","/courses/{courseId}/topics/create","/createCourse", "/course/{courseId}").hasAnyAuthority(EnumeratedRole.TEACHER.name().toLowerCase())
                        .requestMatchers("/admin", "/admin/**", "/courses/{courseId}/delete").hasAnyAuthority(EnumeratedRole.ADMIN.name().toLowerCase())
                        .requestMatchers(HttpMethod.GET, "/courses/{courseId}/topics/{topicId}/completed",
                                "/courses/{courseId}/checkEnroll","/courses/{id}/getProgress").hasAnyAuthority(EnumeratedRole.STUDENT.name().toLowerCase())
                        .requestMatchers(HttpMethod.POST,"/courses/{courseId}/topics/{topicId}/save_topic_progress",
                                "/courses/{courseId}/enroll", "/courses/{courseId}/disenroll", "/course/{courseId}/disenroll",
                                "/courses/{courseId}/topics/{topicId}/remove_topic_progress",
                                "/feedbacks").hasAnyAuthority(EnumeratedRole.STUDENT.name().toLowerCase())
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form
                    .loginPage("/?login=true")
                    .permitAll()
                )
                .build();
    }

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**").allowedOrigins("*");
//            }
//        };
//    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://eloguru.saasjet.com"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
