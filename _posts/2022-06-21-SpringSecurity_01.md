# Spring Security #1 - Spring Security 기본 API 및 Filter 이해 (1)
## 환경
- `java 11`
- `spring 2.7`
- `spring security 2.7`
- `spring data jpa 2.7`

## Spring Security 기본 설정
### Sample code
```java
@GetMapping("/")
class SecurityController {
    public String index() {
        return "hello";
    }
}
```
- 브라우저 접속 결과
![img](../assets/images/spring_security/01/no_security_request.png)

### Spring Security dependency
```groovy
dependencies {
    ...
    
    implementation 'org.springframework.boot:spring-boot-starter-security'
    
    ...
}
```
- 적용 후 접속 결과
![img](../assets/images/spring_security/01/apply_security_request.png)
- Username : user
- password : random string 
![img](../assets/images/spring_security/01/apply_security_request_password.png)
- spring boot 시작시 위 사진과 같은 랜덤한 패스워드 생성
#### 의존성 추가시 일어나는 일들
- 서버가 기동되면 스프링 시큐리티의 초기화 작업 및 보안 설정이 이루어짐
- 별도의 설정이나 구현을 하지 않아도 기본적인 웹 보안 기능이 현재 시스템에 연동되어 작동
  - 모든 요청은 인증이 되어야 자원에 접근이 가능
  - 인증 방식은 `폼 로그인` 방식과 `httpBasic` 로그인 방식 제공
  - 기본 로그인 페이지 제공
  - 기본 계정 한 개 제공
- 문제점으로 아래와 같다
  - 계정 추가, 권한 추가, DB 연동 등
  - 기본적인 보안 기능 외에 시스템에서 필요로 하는 더 세부적이고 추가적인 보안기능이 필

## 사용자 정의 보안 기능 구현
### WebSecurityConfigurerAdapter
- 스프링 시큐리티의 웹 보안 기능 초기화 및 설정 
- 해당 클레스가 `HttpSecurity`를 생성
  - HttpSecurity : 세부적인 보안 기능을 설정할 수 있는 API 제공(인증, 인가)

#### CustomSecurityConfig 구현
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            // 인가 정책
            .authorizeRequests()
            .anyRequest().authenticated().and()
            // 인증 정책
            .formLogin().and()
            .build();
    }
}
```

- 위 방식은 `spring 2.7` 이상에서만 적용(아래는 이전)
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // 인가 정책
            .authorizeRequests()
            .anyRequest().authenticated()
            .and()
            // 인증 정책
            .formLogin();
    } 
    
}
```
#### Custom Spring Security user and password
- 아래 설정을 통해 기본 사용자 설정을 변경할 수 있음
```yaml
spring:
  security:
    user:
      name: user
      password: 1111
```