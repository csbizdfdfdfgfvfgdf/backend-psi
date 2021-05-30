package com.notepad.service;

import com.notepad.controller.request.CreateUserRequest;
import com.notepad.controller.request.TokenAndPasswordDTO;
import com.notepad.dto.UserDTO;
import com.notepad.controller.request.VerifyEmailTokenDTO;
import com.notepad.entity.User;

import java.util.List;

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

	void saveTokenForUser(String token, User user, long tokenExpire);

	void resetPassword(TokenAndPasswordDTO tokenAndPasswordDTO);

	void createUser(CreateUserRequest createUserRequest);

	User findByEmail(String email);

	void verifyEmailToken(VerifyEmailTokenDTO verifyEmailTokenDTO);

}
