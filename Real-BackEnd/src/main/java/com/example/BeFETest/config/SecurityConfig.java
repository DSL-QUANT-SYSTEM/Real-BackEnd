package com.example.BeFETest.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() { // security를 적용하지 않을 리소스
        return web -> web.ignoring()
                // error endpoint를 열어줘야 함, favicon.ico 추가!
                .requestMatchers("/error", "/favicon.ico");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // rest api 설정
                .csrf(csrf -> csrf.disable()) // csrf 비활성화 -> cookie를 사용하지 않으면 꺼도 된다. (cookie를 사용할 경우 httpOnly(XSS 방어), sameSite(CSRF 방어)로 방어해야 한다.)
                .cors(cors -> cors.disable()) // cors 비활성화 -> 프론트와 연결 시 따로 설정 필요
                .httpBasic(httpBasic -> httpBasic.disable()) // 기본 인증 로그인 비활성화
                .formLogin(formLogin -> formLogin.disable()) // 기본 login form 비활성화
                .logout(logout -> logout.disable()) // 기본 logout 비활성화
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())) // X-Frame-Options 비활성화
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                //.requestMatchers("/","/login/**").permitAll()
                                .anyRequest().permitAll()
                );
//                .sessionManagement(c ->
//                        c.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // 세션 사용하지 않음

//                // request 인증, 인가 설정
//                .authorizeHttpRequests(request ->
//                        request.requestMatchers(
//                                new AntPathRequestMatcher("/"),
//                                new AntPathRequestMatcher("/auth/success"),
//                                        ...
//                                ).permitAll()
//                .anyRequest().authenticated()
//                )

        //oauth2 설정
//                .oauth2Login(oauth -> // OAuth2 로그인 기능에 대한 여러 설정의 진입점
//                // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정을 담당
//                oauth.userInfoEndpoint(c -> c.userService(oAuth2UserService))
//                        // 로그인 성공 시 핸들러
//                        .successHandler(oAuth2SuccessHandler)
//        )

        // jwt 관련 설정
//                .addFilterBefore(tokenAuthenticationFilter,
//                        UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(new TokenExceptionFilter(), tokenAuthenticationFilter.getClass()) // 토큰 예외 핸들링

        // 인증 예외 핸들링
//                .exceptionHandling((exceptions) -> exceptions
//                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
//                        .accessDeniedHandler(new CustomAccessDeniedHandler()));

        return http.build();
    }
}
