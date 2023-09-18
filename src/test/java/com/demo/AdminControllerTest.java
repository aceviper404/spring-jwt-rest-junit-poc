package com.demo;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.demo.config.JwtTokenUtil;
import com.demo.controller.AdminController;
import com.demo.dao.DAOUser;
import com.demo.dto.UserDTO;
import com.demo.model.UserWithToken;
import com.demo.service.JwtUserDetailsService;

public class AdminControllerTest {

	private AdminController adminController;
	private JwtUserDetailsService userDetailsService;
	private JwtTokenUtil jwtUtil;

	@BeforeEach
	public void setup() {
		userDetailsService = mock(JwtUserDetailsService.class);
		jwtUtil = mock(JwtTokenUtil.class);
		adminController = new AdminController(userDetailsService);
	}

	@Test
	public void testSaveUser() throws Exception {
		UserDTO user = new UserDTO();
		user.setUsername("testUser");
		user.setPassword("testPassword");

		DAOUser daoUser = new DAOUser();
		when(userDetailsService.save(user)).thenReturn(daoUser);

		ResponseEntity<?> response = adminController.saveUser(user);

		assert (response.getStatusCodeValue() == HttpStatus.OK.value());
		assert (response.getBody() instanceof DAOUser);
	}

	@Test
	public void testGetAllUsernames() {
		List<String> usernames = new ArrayList<>();
		usernames.add("user1");
		usernames.add("user2");

		when(userDetailsService.getAllUsernames()).thenReturn(usernames);

		ResponseEntity<List<String>> response = adminController.getAllUsernames();

		assert (response.getStatusCodeValue() == HttpStatus.OK.value());
		assert (response.getBody().equals(usernames));
	}

	@Test
	public void testGetUsernameById() {
		when(userDetailsService.getUserById(1L)).thenReturn("testUser");

		ResponseEntity<String> response = adminController.getUsernameById("1");

		assert (response.getStatusCodeValue() == HttpStatus.OK.value());
		assert (response.getBody().equals("testUser"));
	}

	@Test
	public void testPutUsernameById() throws Exception {
	    UserDTO user = new UserDTO();
	    user.setUsername("newUser");
	    user.setPassword("newPassword");

	    UserWithToken daoUser = new UserWithToken();
	    daoUser.setUser(new DAOUser(user.getUsername(), user.getPassword()));
	    
	    // Convert DAOUser to UserDetails
	    UserDetails userDetails = new User(daoUser.getUser().getUsername(), daoUser.getUser().getPassword(), new ArrayList<>());

	    // Mock the generateToken method of jwtUtil
	    when(jwtUtil.generateToken(userDetails)).thenReturn("mockToken");

	    // Generate a token with UserDetails
	    daoUser.setJwtToken(jwtUtil.generateToken(userDetails));

	    when(userDetailsService.putUserById(1L, user)).thenReturn(daoUser);

	    ResponseEntity<UserWithToken> response = adminController.putUsernameById(user, "1");

	    assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
	    assertTrue(response.getBody() instanceof UserWithToken);
	    assertEquals(user.getUsername(), response.getBody().getUser().getUsername());
	}




	@Test
	public void testDeleteUsernameById() throws Exception {
		ResponseEntity<String> response = adminController.deleteUsernameById("1");

		assert (response.getStatusCodeValue() == HttpStatus.OK.value());
		assert (response.getBody().equals("User deleted successfully!"));
	}
}
