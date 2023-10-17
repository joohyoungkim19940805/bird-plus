package com.radcns.bird_plus.config.security;

import java.security.Principal;

import lombok.ToString;

@ToString
public class UserPrincipal implements Principal {
    private String id;
    private String name;

    public UserPrincipal(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getName() {
    	
        return name;
    }
    
}
