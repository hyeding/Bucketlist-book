package com.mybucketlistbook.config;

import com.mybucketlistbook.jwt.JwtAccessDeniedHandler;
import com.mybucketlistbook.jwt.JwtAuthenticationEntryPoint;
import com.mybucketlistbook.jwt.JwtSecurityConfig;
import com.mybucketlistbook.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.List;

@EnableWebSecurity
@RequiredArgsConstructor
    public class SecurityConfig extends WebSecurityConfigurerAdapter {
        private final TokenProvider tokenProvider;
        private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
        private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(
                        "/h2-console/**",
                        "/favicon.ico",
                        "/error",
                        "/swagger-resources/**",
                        "/swagger-ui/**",
                        "/v2/api-docs",
                        "/webjars/**"
                );
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // token 을 사용하는 방식이므로 csrf 를 disable 함
                .csrf().disable()

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler);

        // CORS
        httpSecurity.cors().configurationSource(request -> {
            var cors = new CorsConfiguration();
            cors.setAllowedOrigins(List.of("http://localhost:3000"));
            cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
            cors.setAllowedHeaders(List.of("*"));
            return cors;
        });
        httpSecurity
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll();

        httpSecurity
                // exception handling 할 때 우리가 만든 클래스를 추가
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // 시큐리티는 기본적으로 세션을 사용
                // 세션을 사용하지 않기 때문에 STATELESS 로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)


                // 로그인, 회원가입 API 는 토큰이 없는 상태에서 요청이 들어오므로 permitAll 설정함
                .and()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/api/auth/login").permitAll()
                .antMatchers("/api/signup/**").permitAll()
                .antMatchers("/api/user/check_nickname**").permitAll()
                .antMatchers("/api/mail").permitAll()

                .anyRequest().authenticated()

                .and()
                .cors()

                .and()
                .apply(new JwtSecurityConfig(tokenProvider));
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("https://i5a608.p.ssafy.io:3000");
        configuration.addAllowedOrigin("https://i5a608.p.ssafy.io");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
