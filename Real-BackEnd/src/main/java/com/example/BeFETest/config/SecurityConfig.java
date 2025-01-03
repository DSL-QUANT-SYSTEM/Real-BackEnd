package com.example.BeFETest.config;


import com.example.BeFETest.Error.CustomAccessDeniedHandler;
import com.example.BeFETest.Error.CustomAuthenticationEntryPoint;
import com.example.BeFETest.JWT.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          CustomAuthenticationEntryPoint authenticationEntryPoint,
                          CustomAccessDeniedHandler accessDeniedHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/error", "/favicon.ico");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(withDefaults())  // Enable CORS
                //.cors(cors -> cors.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .logout(logout -> logout.disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/strategy/**").permitAll()
                                .requestMatchers("/fortest").permitAll()
                                //.requestMatchers("/home/**").authenticated()
                                .requestMatchers("/logout").permitAll()
                                .requestMatchers("/api/user-info").permitAll()  //후에 check
                                .requestMatchers("/login/**").permitAll()
                                .requestMatchers("/refresh-token").permitAll()
                                .requestMatchers("/result/**").permitAll()
                                .requestMatchers("/multi_result/**").permitAll()
                                .requestMatchers("/user/info").permitAll()
                                .requestMatchers("/userinfo").permitAll()
                                .requestMatchers("/mypage/user").permitAll()
                                .requestMatchers("/mypage").permitAll()
                                .requestMatchers("/home/kospi").permitAll()
                                .requestMatchers("/home/kosdaq").permitAll()
                                .requestMatchers("/home/kospi200").permitAll()
                                .requestMatchers("/home/top20").permitAll()
                                .requestMatchers("/home/coinByFluctuating").permitAll()
                                .requestMatchers("/home/coinByClosingPrice").permitAll()
                                .requestMatchers("/home/coinByTradingVolume").permitAll()
                                .requestMatchers("/home/backtesting_gd").permitAll()
                                .requestMatchers("/home/backtesting_bb").permitAll()
                                .requestMatchers("/home/backtesting_ind").permitAll()
                                .requestMatchers("/home/backtesting_env").permitAll()
                                .requestMatchers("/home/backtesting_w").permitAll()
                                .requestMatchers("/home/backtesting_multi").permitAll()
                                .requestMatchers("/backtest/**").permitAll()
                                .requestMatchers("/backtesting_mine_/**").permitAll()
                                .requestMatchers("/home/coin/*").permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(authenticationEntryPoint)
                                .accessDeniedHandler(accessDeniedHandler)
                );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

    /*
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
//                        .allowedOrigins("http://localhost:5173")
                        .allowedOrigins("http://43.200.199.72:5173")
                        .allowedOrigins("https://dslquant.site")
                        .allowedOrigins("https://www.dslquant.site")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}

     */

/*
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/error", "/favicon.ico");
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(formLogin -> formLogin.disable())
                .logout(logout -> logout.disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                //.requestMatchers("/login/**").permitAll()
                                //.requestMatchers("/").permitAll()
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                //.anyRequest().authenticated()
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/logout").authenticated()
                                .requestMatchers("/api/user-info").authenticated()
                                .requestMatchers("/login/**").permitAll()
                                .requestMatchers("/refresh-token").permitAll()
                                .anyRequest().authenticated()
                                //.anyRequest().permitAll()
                );
                //.exceptionHandling(exceptionHandling ->
                //        exceptionHandling.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                //);

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

    /*

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
        return http.build();
    }
    */
/*
}
*/




