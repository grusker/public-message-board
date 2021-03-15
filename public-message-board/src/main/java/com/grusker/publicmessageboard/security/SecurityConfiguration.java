package com.grusker.publicmessageboard.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
       httpSecurity.csrf().disable()
               .authorizeRequests()
               .anyRequest().authenticated()
               .and()
               .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder managerBuilder) throws Exception {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        managerBuilder.inMemoryAuthentication()
                .withUser("Admin").password(encoder.encode("admin")).roles("ADMIN")
                .and()
                .withUser("Han").password(encoder.encode("solo")).roles("USER")
                .and()
                .withUser("Anakin").password(encoder.encode("skywalker")).roles("USER")
                .and()
                .withUser("Padme").password(encoder.encode("amidala")).roles("USER");
    }

}
