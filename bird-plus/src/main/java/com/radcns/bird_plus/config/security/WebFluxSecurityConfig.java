package com.radcns.bird_plus.config.security;

import org.springframework.beans.factory.annotation.Autowired;

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
import org.springframework.security.web.server.header.ReferrerPolicyServerHttpHeadersWriter;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter.Mode;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

import java.net.URI;

@Configuration
@EnableReactiveMethodSecurity
@Component
public class WebFluxSecurityConfig {
	
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
                .exceptionHandling(exceptionSpec -> exceptionSpec
                		.authenticationEntryPoint((swe, e) -> 
		                    Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED))
		                ).accessDeniedHandler((swe, e) -> 
		                    Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN))
		                )
                )

                .headers(headersSpec -> headersSpec
                		.contentSecurityPolicy(contentSecuritySpec->contentSecuritySpec
                				.policyDirectives("default-src 'self'; frame-src 'self' data:; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://storage.googleapis.com; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self' data:")
                		)
	                	.referrerPolicy(referrerSpec -> referrerSpec
	                			.policy(ReferrerPolicyServerHttpHeadersWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
	                	.permissionsPolicy(permissionsSpec -> permissionsSpec
	                			.policy("camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()")
	                	)
	                	.frameOptions(frameSprc -> frameSprc
	                			.mode(Mode.DENY)
	                	)
                )
                
                .csrf(csrfSpec -> csrfSpec
                		.disable()
                )
                
                .logout(logoutSpec -> logoutSpec
	            		.logoutUrl("/logout")
	            		.logoutSuccessHandler(logoutSuccessHandler("/loginPage?status=logout"))
                )
                
                .formLogin(formLoginSpec -> formLoginSpec
	            		.authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler("/"))
	                    .authenticationFailureHandler(new RedirectServerAuthenticationFailureHandler("/loginPage?status=error"))
	                    .loginPage("/login")
                )

                //.requestCache(c->c.requestCache(new CookieServerRequestCache()))
                .authenticationManager(authManager)
                .authorizeExchange(authSpec->
	                authSpec.pathMatchers(HttpMethod.OPTIONS).permitAll()
			                .pathMatchers("/files/**","/css/**","/js/**","/images/**","/**.ico", "/manifest.json").permitAll() // resources/static
			                .pathMatchers("/","/login", "/loginPage", "/loginProc").permitAll() // login
			                .pathMatchers("/v3/webjars/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll() // api doc
			                .pathMatchers("/home/**").authenticated()
			                .pathMatchers("/api/**").authenticated()
			                .anyExchange().authenticated()
                )
                
                .addFilterAt(bearerAuthenticationFilter(authManager), SecurityWebFiltersOrder.AUTHENTICATION)
                
                .build();
    }
    AuthenticationWebFilter bearerAuthenticationFilter(ReactiveAuthenticationManager authManager) {

    	AuthenticationWebFilter bearerAuthenticationFilter = new AuthenticationWebFilter(authManager);
    	bearerAuthenticationFilter.setServerAuthenticationConverter(new ServerHttpBearerAuthenticationConverter(this.jwtVerifyHandler));
        bearerAuthenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/**"));
        
        return bearerAuthenticationFilter;
    }
}

