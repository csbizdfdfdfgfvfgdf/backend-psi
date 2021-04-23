package com.notepad.serviceImpl;

import static com.notepad.util.Utility.isValidPassword;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.notepad.dto.TokenAndPasswordDTO;
import com.notepad.dto.UserDTO;
import com.notepad.entity.Token;
import com.notepad.entity.User;
import com.notepad.error.BadRequestAlertException;
import com.notepad.error.EmailAlreadyUsedException;
import com.notepad.error.InvalidPasswordException;
import com.notepad.error.UsernameAlreadyUsedException;
import com.notepad.mapper.UserMapper;
import com.notepad.repository.TokenRepository;
import com.notepad.repository.UserRepository;
import com.notepad.service.UserService;

/**
* The UserServiceImpl implements UserService and UserDetailsService that
* Get, Save, Update or Deletes Folders(Menus)
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
@Service
public class UserServiceImpl implements UserService, UserDetailsService {
	
	private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private TokenRepository tokenRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
     * Get user by the username.
     * 
     * method to be used to generate JWT.
     *
     * @param username the userName of entity
     * @return the entity with userName.
     */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("Request to load user by username : {} ", username);
		
		// get user by user name
		User user = userRepository.findByUserName(username);
		if (user == null) {
			throw new BadRequestAlertException("User not found with username: " + username, null, null);
		}
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				new ArrayList<>());
	}

	/**
     * to save a new user
     *
     * @param userDTO : the userDTO to create.
     * @return the persisted entity.
     * 
     */
	@Override
	public UserDTO save(UserDTO userDTO) {
		log.info("Request to register user : {} ",userDTO);
		
		// validate user password before saving
		if(!isValidPassword(userDTO.getPassword())) {
			throw new InvalidPasswordException();
		}
		
		userDTO.setEmail(userDTO.getEmail().toLowerCase());
		userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		
		if (userDTO.getUserId() == null) {
			log.info("Checking if username is already exists with username : {} ", userDTO.getUserName());
			
			// get user by name and throw exception if already found
			userRepository.findOneByUserName(userDTO.getUserName()).ifPresent(existingUser -> {
				throw new UsernameAlreadyUsedException();
			});
			
			log.info("Checking if email is already exists with email : {} ", userDTO.getEmail());
			
			// get user by email and throw exception if already found
			userRepository.findOneByEmail(userDTO.getEmail()).ifPresent(existingUser -> {
				throw new EmailAlreadyUsedException();
			});
			
			log.info("Storing user to DB : {} ", userDTO);
		}
		
		// convert userDTO to userEntity to store user in DB
		User userToSave = userMapper.toEntity(userDTO);
		userToSave.setUserType(userDTO.getUserType());
		
		// save user to DB
		User user = userRepository.save(userToSave);
		
		// convert and return userEntity to userDTO
		return userMapper.toDTO(user);
	}

	/**
     * Get all the users.
     *
     * @return the list of all users.
     */
	@Override
	public List<UserDTO> findAll() {
		log.info("Request to get all Users");
		
		// get all users
		List<User> users = userRepository.findAll();
		
		// userDTOs list to return all users
		List<UserDTO> userDTOs = new ArrayList<UserDTO>();
		
		// convert all user entities to userDTOs
		users.forEach(user -> {
			userDTOs.add(userMapper.toDTO(user));
		});
		return userDTOs;
	}

	/**
     * Get user with the username.
     *
     * @param userName the userName of entity
     * @return the entity with userName.
     */
	@Override
	public UserDTO findByUserName(String userName) {
		log.info("Request to find user by username: {}",userName);
		
		// get user by username
		Optional<User> user = userRepository.findOneByUserName(userName);
		if (user.isPresent()) {
			
			// convert and return userEntiry to UserDTO
			return userMapper.toDTO(user.get());
		} else {
			
			// throw exception if user is not found by name
			throw new UsernameNotFoundException("User not found with username: " + userName);
		}
	}

	/**
     * to save a password reset token
     *
     * @param token : token to be stored.
     * @param user : the user to link with token.
     */
	@Override
	public void saveTokenForUser(String tokenString, User user) {
		log.info("Request to save token: {}",tokenString);
		
		// Token to link with user
		Token token = new Token();
		token.setToken(tokenString);
		token.setUser(user);
		token.setStatus(true);
		
		// set token expiry time
		Date date = new Date();
		long timeNow = date.getTime();
		long expiryTime = timeNow + Token.getResetPasswordExpiration();
		token.setExpiryDate(expiryTime);
		
		// save token that is linked to user
		tokenRepository.save(token);
		
	}

	/**
     * to validate token and reset the password for a user
     *
     * @param tokenAndPasswordDTO : dto with token to be validated and password to be reset.
     * @param user : the user to link with token.
     */
	@Override
	public void resetPassword(TokenAndPasswordDTO tokenAndPasswordDTO) {
		log.info("Request to validate token and save the reset password for a user");
		
		// throw exception if password doesn't matches the criteria
		if(!isValidPassword(tokenAndPasswordDTO.getPassword())) {
			throw new InvalidPasswordException();
		}
		
		// get active token from DB by provided token
		Token token = tokenRepository.findByTokenAndStatus(tokenAndPasswordDTO.getToken(), true);
		
		// if token is valid and not exprired and email matches to the user that is assigned the token
		if(this.validateToken(tokenAndPasswordDTO, token) && 
				token.getUser().getEmail().equalsIgnoreCase(tokenAndPasswordDTO.getEmail())) {
			
			// get user by email
			userRepository.findOneByEmail(tokenAndPasswordDTO.getEmail().toLowerCase()).ifPresent(user -> {
				
				// convert userEntity to userDTO
				UserDTO userDTO = userMapper.toDTO(user);
				
				// set updated password to userDTO
				userDTO.setPassword(tokenAndPasswordDTO.getPassword());
				this.save(userDTO);
				
				// deactivate the token once password is changed
				token.setStatus(false);
				tokenRepository.save(token);
			});
		}
	}

	/**
     * validate and check the token expiry
     * @param tokenAndPasswordDTO : dto with token to be validated.
     * @param token : the token from DB
     * 
     * @return true if token is valid and not expired, false otherwise
     */
	private boolean validateToken(TokenAndPasswordDTO tokenAndPasswordDTO, Token token) {
		log.info("Checking if token is not null & not expired!");
		if (!tokenAndPasswordDTO.getToken().equals(null)) {
			if (token != null) {
				Date date = new Date();
				long timeNow = date.getTime();
				
				// if token expiry date is pending then return true
				if (timeNow<=token.getExpiryDate()) {
					return true;
				} else {
					log.info("token is expired!");
					return false;
				}
			} else {
				log.info("no token entity found!");
				return false;
			}
		} else {
			log.info("no token found in tokenAndPasswordDTO");
			return false;
		}
		
	}
	
}
