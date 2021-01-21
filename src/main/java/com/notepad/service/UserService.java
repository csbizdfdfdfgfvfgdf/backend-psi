package com.notepad.service;

import java.util.List;

import com.notepad.dto.TokenAndPasswordDTO;
import com.notepad.dto.UserDTO;
import com.notepad.entity.User;

public interface UserService {

	UserDTO save(UserDTO userDTO);

	List<UserDTO> findAll();

	UserDTO findByUserName(String userName);

	void saveTokenForUser(String token, User user);

	void resetPassword(TokenAndPasswordDTO tokenAndPasswordDTO);

	
}
