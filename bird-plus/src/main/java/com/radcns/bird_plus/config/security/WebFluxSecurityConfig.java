package com.radcns.bird_plus.config.security;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.header.ReferrerPolicyServerHttpHeadersWriter;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter.Mode;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

import java.net.URI;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity(useAuthorizationManager=true)
@Component
public class WebFluxSecurityConfig {
	
	@Autowired
	private JwtVerifyHandler jwtVerifyHandler;
	
	@Autowired
    private SecurityContextRepository securityContextRepository;
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
                
                .httpBasic(httpBasicSpec -> httpBasicSpec
                		.authenticationEntryPoint((swe, e) -> 
                        Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED))
	                    )
                		.authenticationManager(authManager)
                )
                
                
                .headers(headersSpec -> headersSpec
                		//.contentSecurityPolicy(contentSecuritySpec->contentSecuritySpec
                				//.policyDirectives("default-src 'self'; frame-src 'self' data:; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://storage.googleapis.com; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self' data:")
                		//)
	                	.referrerPolicy(referrerSpec -> referrerSpec
	                			.policy(ReferrerPolicyServerHttpHeadersWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
	                	.permissionsPolicy(permissionsSpec -> permissionsSpec
	                			/**
	                			 * @see https://stackoverflow.com/questions/72135699/geolocation-api-granted-on-firefox-but-denied-on-chrome
	                			 */
	                			.policy("camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()")
	                	)
	                	.frameOptions(frameSprc -> frameSprc
	                			.mode(Mode.SAMEORIGIN)
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
	                		.pathMatchers("/api/**").authenticated()
			                .pathMatchers("/mobile/main/**").authenticated()
			                .pathMatchers("/files/**","/css/**","/js/**","/images/**","/**.ico", "/model/**", "/manifest.json").permitAll() // resources/static)
			                .pathMatchers("/*", "/account-verify/*").permitAll() // auth 검사 안 할 url path
			                .pathMatchers("/v3/webjars/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll() // api doc
			                .pathMatchers("/api/generate-presigned-url/test/").permitAll()
			                .anyExchange().authenticated()
                )
                
                .addFilterAt(bearerAuthenticationFilter(authManager), SecurityWebFiltersOrder.AUTHENTICATION)
                
                .securityContextRepository(securityContextRepository)
                .build();
    }
    AuthenticationWebFilter bearerAuthenticationFilter(ReactiveAuthenticationManager authManager) {

    	AuthenticationWebFilter bearerAuthenticationFilter = new AuthenticationWebFilter(authManager);
    	bearerAuthenticationFilter.setServerAuthenticationConverter(new ServerHttpBearerAuthenticationConverter(this.jwtVerifyHandler));
        bearerAuthenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/**"));
        bearerAuthenticationFilter.setSecurityContextRepository(securityContextRepository);
        return bearerAuthenticationFilter;
    }
}

