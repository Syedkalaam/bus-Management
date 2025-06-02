package com.bus_reservation_system.config;

import com.bus_reservation_system.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/users/register", "/saveUser", "/css/**", "/js/**").permitAll()

                        .requestMatchers("/users/register",
                                "/users/details/**",
                                "/users/list",
                                "/users/update/**",
                                "/trips/list",
                                "/bookings/new",
                                "/bookings/delete/**"
                        ).hasAnyRole("USER","ADMIN")

                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/users/**", "/buses/**", "/trips/**", "/bookings/**").hasAnyRole("ADMIN")

                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login").permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout","GET"))
                        .logoutSuccessUrl("/login?logout")
                        .permitAll())

                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
