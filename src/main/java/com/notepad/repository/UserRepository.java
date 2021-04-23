package com.notepad.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notepad.entity.User;

/**
* The UserRepository that extends JpaRepository
*  to define custom operations related to user
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByUserName(String username);
	
	Optional<User> findOneByUserName(String userName);
	
	Optional<User> findOneByEmail(String email);
}
