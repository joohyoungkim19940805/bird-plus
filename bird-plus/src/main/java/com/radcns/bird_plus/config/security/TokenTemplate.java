package com.radcns.bird_plus.config.security;

import java.util.List;

public interface TokenTemplate {

	String getIssuer();
	
	String getSubject();
	
	String getName();
	
	List<Role> getRoles();
}
