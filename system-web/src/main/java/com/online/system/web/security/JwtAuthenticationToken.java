package com.online.system.web.security;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * 自定义令牌对象
 * @author guigl
 * @date
 */
public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = 1L;
	
	private String token;

    public JwtAuthenticationToken(Object principal, Object credentials){
        super(principal, credentials);
    }
    
    public JwtAuthenticationToken(Object principal, Object credentials, String token){
    	super(principal, credentials);
    	this.token = token;
    }

    public JwtAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String token) {
    	super(principal, credentials, authorities);
    	this.token = token;
    }
    
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
