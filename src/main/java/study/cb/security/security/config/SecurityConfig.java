package study.cb.security.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import study.cb.security.security.provider.CustomAuthenticationProvider;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // CustomAuthenticationProvider에서 제공
//    private final UserDetailsService userDetailsService;

    private final AuthenticationDetailsSource authenticationDetailsSource;
    private final AuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final AuthenticationFailureHandler customAuthenticationFailureHandler;
    private final AccessDeniedHandler customAccessDeniedHandler;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .antMatchers("/h2-console/**");
    }

    @Bean
    public AuthenticationProvider authenticationProvider () {
        return new CustomAuthenticationProvider();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

//        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
//        authenticationManagerBuilder.userDetailsService(userDetailsService);
//        http
//                .authenticationManager(authenticationManagerBuilder.build());

        http
                .authorizeRequests()
                .antMatchers("/", "/users", "/user/login/**", "/login").permitAll()
                .antMatchers("/mypage").hasRole("USER")
                .antMatchers("/messages").hasRole("MANAGER")
                .antMatchers("/config").hasRole("ADMIN")
                .anyRequest().authenticated();

        http
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login_proc")
                .defaultSuccessUrl("/")
                .authenticationDetailsSource(authenticationDetailsSource)
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler)
                .permitAll();

        http.exceptionHandling()
                        .accessDeniedHandler(customAccessDeniedHandler);

        http.authenticationProvider(authenticationProvider());


        return http.build();
    }


//    // In-Memory Authentication User add
//    @Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//
//        String password = passwordEncoder().encode("1111");
//
//        UserDetails user1 = User.builder().password(password).username("username").roles("USER").build();
//        UserDetails user2 = User.builder().password(password).username("manager").roles("MANAGER", "USER").build();
//        UserDetails user3 = User.builder().password(password).username("admin").roles("ADMIN", "MANAGER", "USER").build();
//        return new InMemoryUserDetailsManager(user1, user2, user3);
//    }
}
