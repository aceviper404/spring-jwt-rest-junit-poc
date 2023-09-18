package com.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demo.dao.DAOUser;
import com.demo.dto.UserDTO;
import com.demo.model.UserWithToken;
import com.demo.service.JwtUserDetailsService;


@RestController
@CrossOrigin
@RequestMapping("/users")
public class AdminController {
	
	@Autowired
	private JwtUserDetailsService userDetailsService;
	
	public AdminController(JwtUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
	
	// register
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody UserDTO user) throws Exception {
		return ResponseEntity.ok(userDetailsService.save(user));
	}
	
	// getallusers
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<String>> getAllUsernames(){
		return ResponseEntity.ok(userDetailsService.getAllUsernames());
	}
	
	// getuserbyid
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<String> getUsernameById(@PathVariable String id){
		return ResponseEntity.ok(userDetailsService.getUserById(Long.parseLong(id)));
	}
	
	// putuser
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UserWithToken> putUsernameById(@RequestBody UserDTO user, @PathVariable String id) throws NumberFormatException, Exception{
		return ResponseEntity.ok(userDetailsService.putUserById(Long.parseLong(id), user));
	}
	
	// deleteuserbyid
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteUsernameById(@PathVariable String id) throws NumberFormatException, Exception{
		userDetailsService.deleteUserById(Long.parseLong(id));
		return ResponseEntity.ok("User deleted successfully!");
	}

}
