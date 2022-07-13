---
title: "Spring Security #3 - Ajax 인증 (1)"

excerpt: "Spring Security"

toc: true

toc_sticky: true

categories:

- Spring Security

tags:

- java
- Spring Boot
- Spring Security

author: coli-bear

---

# Spring Security #3 - Ajax 인증 (1)

## 환경

- `java 11`
- `spring 2.7`
- `spring security 2.7`
- `spring data jpa 2.7`

## Ajax 인증

### 흐름 및 개요

![img](../assets/images/spring_security/03/ajax_authentication.png)

- Ajax 용 `Filter`, `Token`, `Provider`등을 구현 하지만 실제 동작 로직은 `Form Login`방식과 비슷

## AjaxAuthenticationFilter

- `AbstractAuthenticationProcessingFilter` 상속
- 필터의 작동 조건
  - `AntPathRequestMatcher("/api/login")`로 요청 정보와 매칭하고 요청 방식이 Ajax 이면 필터 작동
- `AjaxAuthenticationToken`을 생성하여 `AuthenticationManager`에게 전달하여 인증 처리
- `Filter` 추가

```java
http.addFilterBefore(AjaxAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class);
```

- spring 2.7에서는 도무지 모르겠음...
- 일단 아래 코드로...

### AjaxAuthenticationToken 생성

```java

public class AjaxAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private final Object principal;

    private Object credentials;

    public AjaxAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    public AjaxAuthenticationToken(Object principal, Object credentials,
                                   Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true); // must use super, as we override
    }

    public static UsernamePasswordAuthenticationToken unauthenticated(Object principal, Object credentials) {
        return new UsernamePasswordAuthenticationToken(principal, credentials);
    }

    public static UsernamePasswordAuthenticationToken authenticated(Object principal, Object credentials,
                                                                    Collection<? extends GrantedAuthority> authorities) {
        return new UsernamePasswordAuthenticationToken(principal, credentials, authorities);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated,
            "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}
```

### SecurityConfig 설정

```java
public class SecurityConfig {
    ...

    public AjaxLoginProcessingFilter ajaxLoginProcessingFilter() {
        AjaxLoginProcessingFilter ajaxLoginProcessingFilter = new AjaxLoginProcessingFilter();
        return ajaxLoginProcessingFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(ajaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
        
        ...
    }
    
    ...
```

### ajax.http

```http request
### Send POST request with body as parameters
POST http://localhost:8080/api/login
Content-Type: application/json
X-Requested-With: XMLHttpRequest

{
  "username": "user",
  "password": "1111"
}

###


```

## AjaxAuthenticationProvider

- Ajax 인증 처리를 위한 SecurityConfig 별도 구현

### AjaxSecurityConfig

- Ajax 인증 부분을 `AjaxSecurityConfig`로 이동

```java

@Configuration
@Order(0) // 동시 설정때문에 발생하는 문제를 방지하기 위해
public class AjaxSecurityConfig {

    public AjaxLoginProcessingFilter ajaxLoginProcessingFilter() {
        AjaxLoginProcessingFilter ajaxLoginProcessingFilter = new AjaxLoginProcessingFilter();
        return ajaxLoginProcessingFilter;
    }

    @Bean
    public AuthenticationProvider ajaxAuthenticationProvider() {
        return new AjaxAuthenticationProvider();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(ajaxAuthenticationProvider());

        AjaxLoginProcessingFilter ajaxLoginProcessingFilter = ajaxLoginProcessingFilter();
        ajaxLoginProcessingFilter.setAuthenticationManager(authenticationManagerBuilder.build());

        http.addFilterBefore(ajaxLoginProcessingFilter, UsernamePasswordAuthenticationFilter.class);

        http
            .antMatcher("/api/**") // api 요청에 한에서만 동작
            .authorizeRequests()
            .anyRequest().authenticated();

        return http.build();
    }
}
```

### AjaxAuthenticationProvider

```java
public class AjaxAuthenticationProvider implements AuthenticationProvider {
    // ajax를 이용한다고 해도 form 인증방식과 동일하게 동작
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // 검증을 위한 구현
    // 입력한 ID, Password 정보가 있음
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        AccountContext accountContext = (AccountContext) userDetailsService.loadUserByUsername(username);

        FormWebAuthenticationDetails details = (FormWebAuthenticationDetails) authentication.getDetails();
        String secretKey = details.getSecretKey();

        if (secretKey == null || !"secret".equals(secretKey)) {
            throw new InsufficientAuthenticationException("InsufficientAuthenticationException");
        }

        if (passwordEncoder.matches(password, accountContext.getAccount().getPassword())) {
            // 인증 Token만 ajax를 사용
            return new AjaxAuthenticationToken(accountContext.getAccount(), null, accountContext.getAuthorities());
        }

        throw new BadCredentialsException("BadCredentialException");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 인증 Token만 ajax를 사용
        return AjaxAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

```

## AjaxAuthenticationSuccessHandler

## AjaxAuthenticationFailureHandler

