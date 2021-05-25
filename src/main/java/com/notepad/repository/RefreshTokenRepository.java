package com.notepad.repository;


import com.notepad.entity.RefreshToken;
import com.notepad.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
	RefreshToken findByToken(String token);

	@Modifying
	int deleteByUser(User user);
}
