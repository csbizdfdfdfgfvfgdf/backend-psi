package com.notepad.repository;

import com.notepad.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
* The TokenRepository that extends JpaRepository
*  to define custom operations related to token
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
@Repository
public interface TokenRepository extends JpaRepository<Token, Long>{

	Token findByTokenAndStatus(String token, boolean status);
}
