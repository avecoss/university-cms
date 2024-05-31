package dev.alexcoss.universitycms.config;

import dev.alexcoss.universitycms.service.auth.PersonDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PersonDetailsService personDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize ->
                authorize
                    //.requestMatchers("/api/users", "/api/change_role").hasRole("ADMIN")
                    .requestMatchers("/webjars/**", "/css/**", "/icons/**", "/img/**").permitAll()
                    .requestMatchers("/login", "/error", "/registration", "/home").permitAll()
                    //.requestMatchers("/api/auth/registration").permitAll()
                    .anyRequest().authenticated())
                    //.anyRequest().hasAnyRole("USER", "ADMIN"))
            //.sessionManagement(sessionManagement ->
            //    sessionManagement
            //        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
            .formLogin(login ->
                login
                    .loginPage("/login")
                    .defaultSuccessUrl("/home", true)
                    .failureUrl("/login?error"));

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(personDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }

    //@Bean
    //public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    //    return configuration.getAuthenticationManager();
    //}
}
