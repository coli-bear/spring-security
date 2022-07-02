//package study.cb.security.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.authentication.logout.LogoutHandler;
//import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
//import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
//import org.springframework.security.web.savedrequest.RequestCache;
//import org.springframework.security.web.savedrequest.SavedRequest;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.io.IOException;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//    private UserDetailsService userDetailService;
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////        return http
////                .authorizeRequests()
////                .anyRequest().authenticated()
////                .and()
////                .formLogin()
////                .and()
////                .build();
//
//        http
//                .authorizeRequests()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/loginPage")                   // 사용자 정의 로그인 페이
//                .defaultSuccessUrl("/")                 // 로그인 성공 후 이동 페이지
//                .failureUrl("/login")       // 로그인 실패 후 이동 페이지
//                .usernameParameter("username")              // 아이디 파라미터 명 설정
//                .passwordParameter("password")              // 패스워드 파라미터명 설정
//                .loginProcessingUrl("/login_proc")               // 로그인 Form Action URL
//                .successHandler(new AuthenticationSuccessHandler() {
//                    @Override
//                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//                        System.out.println("authentication " + authentication.getName());
//                        response.sendRedirect("/");
//                    }
//                })      // 로그인 성공 후 헨들러
//                .failureHandler(new AuthenticationFailureHandler() {
//                    @Override
//                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//                        System.out.println("excepted " + exception.getMessage());
//                        response.sendRedirect("/login");
//                    }
//                }) // 로그인 실패 후 핸들러
//                .permitAll();
//
//        http.logout()                   // 로그아웃 처리
//                .logoutUrl("/logout")   // 로그아웃 처리 URL
//                .logoutSuccessUrl("/login") // 로그아웃 성공 후 이동페이지
//                .deleteCookies("JSESSIONID", "remember-me") // 쿠키 삭제
//                .addLogoutHandler(new LogoutHandler() {
//                    @Override
//                    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//                        // 원하는 작업을 별도로 구현시 사용
//                        HttpSession session = request.getSession();
//                        session.invalidate();
//                    }
//                }) // 로그아웃 핸들러
////                .logoutSuccessHandler(new LogoutSuccessHandler() {
////                    @Override
////                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
////                        response.sendRedirect("/login");
////                    }
////                }); // 로그아웃 성공 후 핸들러
//                .logoutSuccessHandler((HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
//                    // lambda 이용
//                });
//
////        http.rememberMe()
////                .rememberMeParameter("remember") // 기본 파라미터 명은 remember-me (check box의 parameter)
////                .tokenValiditySeconds(3600) // default 14
////                .alwaysRemember(true)  // remember me 기능이 활성화 되지 않아도 항상 실행
////                .userDetailsService(userDetailService); // remember me 설정시 반드시 필요한 설정, 시스템에 있는 사용자 정보 가져오기 위한
//        ;
//
//        http.anonymous()    // 기본적으로 활성화 되어있음
//                .authenticationFilter(new AnonymousAuthenticationFilter(
//                                "key",
//                                "anonymousUser",
//                                AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS")
//                        )
//                )
//                .authenticationProvider(new AuthenticationProvider() {
//                    @Override
//                    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//                        return null;
//                    }
//
//                    @Override
//                    public boolean supports(Class<?> authentication) {
//                        return false;
//                    }
//                })
//                .disable(); // 사용하지 않는 경우
//
//        http.sessionManagement() // 세션 관리 기능 동작
//                .maximumSessions(1) // 최대 허용 가능 세션 수 , -1 : 무제한 로그인
//                .maxSessionsPreventsLogin(true) // 동시 로그인 차단, false: 기존 세션 만료(default)
//                .expiredUrl("/expired") // 세션이 만료된 경우 이동 할 페이지
//        // 아래 옵션은 인강에서는 나오는데 spring 2.7에서는 안나옴...
////                .invalidSessionUrl("/invalid") // 세션이 유효하지 않을 때 이동 할 페이지
//        ;
//
//        // 세션 고정 보호
//        http.sessionManagement()
//                .sessionFixation().changeSessionId()// 기본값,
//        // none, migrateSession, newSession
//
//        // migrateSession : 이전 세션에 설정한 속성값을 그대로 사용
//        // newSession : 세션을 새로 발급하지만 이전에 설정한 속헝값을 사용 못함
//        // none : 세션ID 가 바뀌지 않아 세션 고정 공격에 당하게 됨
//        ;
//
//        http.authorizeRequests()
//                .antMatchers("/login").permitAll()
//                .antMatchers("/users").hasRole("USER")
//                .antMatchers("/admin/pay").hasRole("ADMIN")
//                .antMatchers("/admin/**").hasAnyRole("ADMIN", "SYS");
//
//        http.formLogin()
//                .successHandler(new AuthenticationSuccessHandler() {
//                    @Override
//                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//                        // 사용자가 원래 가고자 했던 정보
//                        RequestCache requestCache = new HttpSessionRequestCache();
//                        // 사용자가 가고자했던 요청 정보 저장
//                        SavedRequest savedRequest = requestCache.getRequest(request, response);
//
//                        // 원래 가고자 했던 요청 URL
//                        String redirectUrl = savedRequest.getRedirectUrl();
//                        response.sendRedirect(redirectUrl);
//                    }
//                })
//        ;
//
//        http.exceptionHandling() // 예외처리 기능이 작동
//                // 인증 처리 실패 구현
//                .authenticationEntryPoint(new AuthenticationEntryPoint() {
//                    @Override
//                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
//
//                    }
//                })
//                // 인가 실패 처리 구현
//                .accessDeniedHandler(new AccessDeniedHandler() {
//                    @Override
//                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
//
//                    }
//                });
//
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationManagerBuilder auth) throws Exception {
//
//        auth
//                // 메모리에 사용자 인증을 생성
//                // 사용자 생성 개수는 재한이 없음
//                // {noop} 패스워드 암호화시 특정 패스워드 암호화 방식을 적어줘야 한다, 패스워드 암호화가 이뤄지지 않음
//                .inMemoryAuthentication().withUser("user").password("{noop}1111").roles("USER");
//        auth.inMemoryAuthentication().withUser("sys").password("{noop}1111").roles("SYS");
//        auth.inMemoryAuthentication().withUser("admin").password("{noop}1111").roles("ADMIN");
//
//        return auth.build();
//    }
//}
