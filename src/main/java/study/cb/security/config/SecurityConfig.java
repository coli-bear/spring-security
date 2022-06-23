package study.cb.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private UserDetailsService userDetailService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        return http
//                .authorizeRequests()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .and()
//                .build();

        http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/loginPage")                   // 사용자 정의 로그인 페이
                .defaultSuccessUrl("/")                 // 로그인 성공 후 이동 페이지
                .failureUrl("/login")       // 로그인 실패 후 이동 페이지
                .usernameParameter("username")              // 아이디 파라미터 명 설정
                .passwordParameter("password")              // 패스워드 파라미터명 설정
                .loginProcessingUrl("/login_proc")               // 로그인 Form Action URL
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        System.out.println("authentication " + authentication.getName());
                        response.sendRedirect("/");
                    }
                })      // 로그인 성공 후 헨들러
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                        System.out.println("excepted " + exception.getMessage());
                        response.sendRedirect("/login");
                    }
                }) // 로그인 실패 후 핸들러
                .permitAll();

        http.logout()                   // 로그아웃 처리
                .logoutUrl("/logout")   // 로그아웃 처리 URL
                .logoutSuccessUrl("/login") // 로그아웃 성공 후 이동페이지
                .deleteCookies("JSESSIONID", "remember-me") // 쿠키 삭제
                .addLogoutHandler(new LogoutHandler() {
                    @Override
                    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
                        // 원하는 작업을 별도로 구현시 사용
                        HttpSession session = request.getSession();
                        session.invalidate();
                    }
                }) // 로그아웃 핸들러
//                .logoutSuccessHandler(new LogoutSuccessHandler() {
//                    @Override
//                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//                        response.sendRedirect("/login");
//                    }
//                }); // 로그아웃 성공 후 핸들러
                .logoutSuccessHandler((HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
                    // lambda 이용
                });

//        http.rememberMe()
//                .rememberMeParameter("remember") // 기본 파라미터 명은 remember-me (check box의 parameter)
//                .tokenValiditySeconds(3600) // default 14
//                .alwaysRemember(true)  // remember me 기능이 활성화 되지 않아도 항상 실행
//                .userDetailsService(userDetailService); // remember me 설정시 반드시 필요한 설정, 시스템에 있는 사용자 정보 가져오기 위한
        ;

        http.anonymous()    // 기본적으로 활성화 되어있음
                .authenticationFilter(new AnonymousAuthenticationFilter(
                                "key",
                                "anonymousUser",
                                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")
                        )
                )
                .authenticationProvider(new AuthenticationProvider() {
                    @Override
                    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                        return null;
                    }

                    @Override
                    public boolean supports(Class<?> authentication) {
                        return false;
                    }
                })
                .disable(); // 사용하지 않는 경우


        return http.build();
    }

}
