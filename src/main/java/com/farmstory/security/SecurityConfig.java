package com.farmstory.security;

import com.farmstory.oauth2.MyOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor // 생성자 어노테이션
@Configuration
public class SecurityConfig {

    private final MyOauth2UserService myOauth2UserService;

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(googleClientRegistration());
    }

    private ClientRegistration googleClientRegistration() {
        return ClientRegistration.withRegistrationId("google")
                .clientId("949146169145-oqminlempngrsv0rhl14ba257skse4or.apps.googleusercontent.com")
                .clientSecret("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")
                .scope("profile", "email")
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .tokenUri("https://accounts.google.com/o/oauth2/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName("sub")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .clientName("Google")
                .build();
    }

    private ClientRegistration naverClientRegistration() {
        return ClientRegistration.withRegistrationId("naver")
                .clientId("x6Go0vQ85nI18r7OlwAj")
                .clientSecret("e7E1wsFO9k")
                .scope("profile", "email")
                .authorizationUri("https://nid.naver.com/oauth2.0/authorize")
                .tokenUri("https://uid.naver.com/oauth2.2/token")
                .userInfoUri("https://openapi.naver.com/v1/nid/me")
                .userNameAttributeName("sub")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .clientName("Naver")
                .build();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        // 로그인 설정
        http.formLogin(login -> login
                .loginPage("/user/login")
                .defaultSuccessUrl("/")
                .failureUrl("/user/login?success=100")
                .usernameParameter("uid")
                .passwordParameter("pass"));

        // 로그아웃 설정
        http.logout(logout -> logout
                .invalidateHttpSession(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                .logoutSuccessUrl("/"));
                // .logoutSuccessUrl("/user/login?success=101"));

        // OAuth2 설정
        http.oauth2Login(login -> login.loginPage("/user/login")
                .userInfoEndpoint(endpoint -> endpoint.userService(myOauth2UserService)));
        // oauth2 등록을 위한 서비스라고 보면 됨

        // 인가 설정
//        http.authorizeHttpRequests(authorize -> authorize
//                .requestMatchers("/").permitAll()
//                .requestMatchers("/article/**").permitAll()
//                .requestMatchers("/category/**").permitAll()
//                .requestMatchers("/article/write").authenticated()
//                .requestMatchers("/article/delete/**").authenticated()
//                .requestMatchers("/user/**").permitAll()
//                .anyRequest().permitAll());

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/").permitAll()
                .requestMatchers("/user/**").permitAll()
                .requestMatchers("/introduction/**").permitAll()
                .requestMatchers("/userinfo/**").authenticated()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/crop/**").permitAll()
                .requestMatchers("/community/**").permitAll()
                .requestMatchers("/crop/CropWrite").authenticated()
                .anyRequest().permitAll());

        // 기타 보안 설정
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();

//           .authorizeHttpRequests((requests) -> requests
//                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()  // 정적 리소스 허용
//                .requestMatchers("/", "/index", "/**").permitAll()  // 인증 없이 접근 허용할 경로
//                .anyRequest().permitAll() // 그 외의 요청은 인증 필요
//        )

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //평문을 암호화시킬때 암호문 만들때 도와주는 encoder
        return new BCryptPasswordEncoder();
    }

}