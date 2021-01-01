package org.kebish.sample.reservation

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.provisioning.InMemoryUserDetailsManager

import org.springframework.security.core.userdetails.UserDetailsService


@Configuration
@EnableWebSecurity
class WebSecurityConfig {

    @Bean
    fun userDetailsService(): UserDetailsService {
        /* For sample application is this hardcoded list of users - good enough. */
        val users = listOf(
            User.withDefaultPasswordEncoder()
                .username("user")
                .password("pass")
                .roles("USER")
                .build(),
            User.withDefaultPasswordEncoder()
                .username("admin")
                .password("pass")
                .roles("ADMIN")
                .build(),
        )
        return InMemoryUserDetailsManager(users)
    }

}