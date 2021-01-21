package com.notepad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notepad.entity.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long>{

	//Token findByToken(String token);

	Token findByTokenAndStatus(String token, boolean status);

}
