package com.demo.model;

import com.demo.dao.DAOUser;

public class UserWithToken {
	
	private DAOUser user;
	private String jwtToken;
	
	public UserWithToken() {}
	
	
	public UserWithToken(DAOUser user, String jwtToken) {
		super();
		this.user = user;
		this.jwtToken = jwtToken;
	}


	public DAOUser getUser() {
		return user;
	}
	public void setUser(DAOUser user) {
		this.user = user;
	}
	public String getJwtToken() {
		return jwtToken;
	}
	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}

	
}
