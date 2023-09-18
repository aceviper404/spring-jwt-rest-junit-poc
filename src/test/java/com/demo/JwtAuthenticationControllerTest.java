package com.demo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import com.demo.config.JwtTokenUtil;
import com.demo.controller.JwtAuthenticationController;
import com.demo.model.JwtRequest;
import com.demo.model.JwtResponse;
import com.demo.service.JwtUserDetailsService;

public class JwtAuthenticationControllerTest {

	private JwtAuthenticationController authenticationController;
	private AuthenticationManager authenticationManager;
	private JwtTokenUtil jwtTokenUtil;
	private JwtUserDetailsService userDetailsService;

	@BeforeEach
	public void setup() {
		authenticationManager = mock(AuthenticationManager.class);
		jwtTokenUtil = mock(JwtTokenUtil.class);
		userDetailsService = mock(JwtUserDetailsService.class);

		authenticationController = new JwtAuthenticationController(authenticationManager, jwtTokenUtil, userDetailsService);
	}

	@Test
	public void testCreateAuthenticationToken() throws Exception {
	    Authentication authentication = mock(Authentication.class);
	    when(authenticationManager.authenticate(any())).thenReturn(authentication);

	    UserDetails userDetails = mock(UserDetails.class);
	    when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);

	    when(jwtTokenUtil.generateToken(userDetails)).thenReturn("mockedToken");

	    JwtRequest authenticationRequest = new JwtRequest();
	    authenticationRequest.setUsername("testUser");
	    authenticationRequest.setPassword("testPassword");

	    ResponseEntity<?> response = authenticationController.createAuthenticationToken(authenticationRequest);

	    assert (response.getStatusCodeValue() == HttpStatus.OK.value());
	    assert (response.getBody() instanceof JwtResponse);
	    assert (((JwtResponse) response.getBody()).getToken().equals("mockedToken"));
	}
}
