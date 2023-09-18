package com.demo.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.demo.dao.DAOUser;

@Repository
public interface UserRepository extends CrudRepository<DAOUser, Integer>{
	
	DAOUser findByUsername(String username);
	Optional<DAOUser> findById(Long id);
	void deleteById(Long id);
}