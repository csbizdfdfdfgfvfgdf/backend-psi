package com.notepad.service;

import java.util.List;

import com.notepad.dto.TokenAndPasswordDTO;
import com.notepad.dto.UserDTO;
import com.notepad.entity.User;

/**
* The UserService interface defines the operations to be performed related to users
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
public interface UserService {

	UserDTO save(UserDTO userDTO);

	List<UserDTO> findAll();

	UserDTO findByUserName(String userName);

	void saveTokenForUser(String token, User user);

	void resetPassword(TokenAndPasswordDTO tokenAndPasswordDTO);

}
