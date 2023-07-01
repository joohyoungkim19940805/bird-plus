package com.radcns.bird_plus.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.savedrequest.CookieServerRequestCache;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

import java.net.URI;
import java.security.KeyPair;

@Configuration
@EnableReactiveMethodSecurity
@Component
public class WebFluxSecurityConfig {
	
	@Value("${jjwt.password.secret}")
	private String secret;
	
	@Autowired
	private JwtVerifyHandler jwtVerifyHandler;
	/*
	@Bean
	public MapReactiveUserDetailsService userDetailsService() {
	    
	    UserDetails admin = User.withUsername("admin").password(passwordEncoder().encode("password")).roles("USER", "ADMIN").build();
	    UserDetails testUser = User.withUsername("test").password(passwordEncoder().encode("password")).roles("USER","GUEST").build();
	    
	    return new MapReactiveUserDetailsService(testUser, admin);
	}
	*///
	
    public ServerLogoutSuccessHandler logoutSuccessHandler(String uri) {
        RedirectServerLogoutSuccessHandler successHandler = new RedirectServerLogoutSuccessHandler();
        successHandler.setLogoutSuccessUrl(URI.create(uri));
        return successHandler;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, ReactiveAuthenticationManager authManager) {
    	
        return http
                .exceptionHandling()
                .authenticationEntryPoint((swe, e) -> 
                    Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED))
                )
                .accessDeniedHandler((swe, e) -> 
                    Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN))
                )
                .and()
                .csrf().disable()
                .logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler("/loginPage?status=logout")).and()
                .formLogin()
                .authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler("/"))
                .authenticationFailureHandler(new RedirectServerAuthenticationFailureHandler("/loginPage?status=error"))
                .loginPage("/login")
                .and()
                //.requestCache(c->c.requestCache(new CookieServerRequestCache()))
                .httpBasic().disable()
                //.authenticationManager(authenticationManager)
                //.securityContextRepository(securityContextRepository)
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .pathMatchers("/","/**.ico").permitAll()
                .pathMatchers("/login", "/loginPage", "/loginProc").permitAll()
                .pathMatchers("/home/**").authenticated()
                .pathMatchers("/api/**").authenticated()
                .anyExchange().authenticated()
                .and()
                .addFilterAt(bearerAuthenticationFilter(authManager), SecurityWebFiltersOrder.AUTHENTICATION)
                //.addFilterAt(cookieAuthenticationFilter(authManager), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
    AuthenticationWebFilter bearerAuthenticationFilter(ReactiveAuthenticationManager authManager) {
        
    	AuthenticationWebFilter bearerAuthenticationFilter = new AuthenticationWebFilter(authManager);
    	bearerAuthenticationFilter.setServerAuthenticationConverter(new ServerHttpBearerAuthenticationConverter(this.jwtVerifyHandler));
        bearerAuthenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/**"));
        
        return bearerAuthenticationFilter;
    }
}

