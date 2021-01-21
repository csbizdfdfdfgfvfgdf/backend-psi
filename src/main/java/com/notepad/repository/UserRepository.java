package com.notepad.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notepad.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByUserName(String username);

	Optional<User> findOneByUserName(String userName);

	Optional<User> findOneByEmail(String email);

	//User findByEmail(String email);

}
