//package study.cb.security.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//@Order(0)
//public class SecurityConfigSample {
//    @Bean
//    public SecurityFilterChain sampleFilterChain1(HttpSecurity http) throws Exception {
//        http
//                .antMatcher("/admin/^^")
//                .authorizeRequests()
//                .anyRequest().authenticated();
//
//        http.httpBasic();
//
//        return http.build();
//    }
//}
//
//@Configuration
////@EnableWebSecurity
//@Order(1)
//class SecurityConfigSample2 {
//    @Bean
//    public SecurityFilterChain sampleFilterChain2(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .anyRequest().permitAll()
//                .and()
//                .formLogin();
//
//        return http.build();
//    }
//}