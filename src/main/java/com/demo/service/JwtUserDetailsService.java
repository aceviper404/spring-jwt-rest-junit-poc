package com.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.config.JwtTokenUtil;
import com.demo.dao.DAOUser;
import com.demo.dto.UserDTO;
import com.demo.model.UserWithToken;
import com.demo.repository.UserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	@javax.transaction.Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		DAOUser user = userRepository.findByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		} else {
			return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
		}
	}

	public DAOUser save(UserDTO user) {
		DAOUser newUser = new DAOUser(user.getUsername(), passwordEncoder.encode(user.getPassword()));
		return userRepository.save(newUser);
	}

	public List<String> getAllUsernames() {
		List<String> usernames = new ArrayList<String>();
		userRepository.findAll().forEach(user -> usernames.add(user.getUsername()));
		return usernames;
	}

	public String getUserById(Long id) {
		Optional<DAOUser> foud = userRepository.findById(id);
		if (foud.isPresent())
			return foud.get().getUsername();
		else
			return "username not found";
	}

	public UserWithToken putUserById(long id, UserDTO user) throws Exception {
		DAOUser daouser = userRepository.findById(id)
				.orElseThrow(() -> new Exception("Employee doesnt exist with id: " + id));

		daouser.setUsername(user.getUsername());
		daouser.setPassword(passwordEncoder.encode(user.getPassword()));

		DAOUser updateduser = userRepository.save(daouser);
		UserDetails userdetails = new User(updateduser.getUsername(), updateduser.getPassword(), new ArrayList<>());
		String newtoken = jwtTokenUtil.generateToken(userdetails);
		
		return new UserWithToken(updateduser, newtoken);
	}

	@Transactional
	public void deleteUserById(Long id) throws Exception {
		userRepository.findById(id)
				.orElseThrow(() -> new Exception("Employee doesnt exist with id: " + id));

		userRepository.deleteById(id);
	}

}