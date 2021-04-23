package com.notepad.jwt.controller;

import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.notepad.entity.enumeration.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.notepad.dto.UserDTO;
import com.notepad.error.BadRequestAlertException;
import com.notepad.jwt.config.JwtTokenUtil;
import com.notepad.jwt.model.JwtRequest;
import com.notepad.jwt.model.JwtResponse;
import com.notepad.redis.model.RedisUser;
import com.notepad.redis.repo.RedisUserRepo;
import com.notepad.service.UserService;
import com.notepad.serviceImpl.UserServiceImpl;

/**
* The JwtAuthenticationController declares the REST APIs for authentications
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
@RestController
@CrossOrigin
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserServiceImpl userServiceImpl;
	
	@Autowired
	private RedisUserRepo redisUserRepo;

	/**
     * {@code POST  /authenticate} : authenticate user and generate token
     *
     * @param JwtRequest : request with username and password
     * @return the {@link ResponseEntity} with status {@code 200} and with body token.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

		// authenticate user by username and password
		authenticate(authenticationRequest.getUserName(), authenticationRequest.getPassword());

		// get user by username
		final UserDetails userDetails = userServiceImpl.loadUserByUsername(authenticationRequest.getUserName());

		// generate token
		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token));
	}
	
	/**
     * {@code POST  /authenticate-with-uuid} : create authentication token for visitor
     *
     * @param RedisUser : user with username and password
     * @return the {@link ResponseEntity} with status {@code 200} and with body token.
     * @throws BadRequestAlertEception if the Location URI syntax is incorrect.
     */
	@RequestMapping(value = "/authenticate-with-uuid", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationTokenForVisitor(@RequestBody RedisUser redisUser) throws Exception {

		// check to redisDB for authentication!

		RedisUser redisUser2 = redisUserRepo.findByUserName(redisUser.getUserName());

		if (redisUser2.getUuid().equals(redisUser.getUuid())) {

			authenticate(redisUser.getUserName(), redisUser.getPassword());

			final UserDetails userDetails = userServiceImpl.loadUserByUsername(redisUser.getUserName());

			final String token = jwtTokenUtil.generateToken(userDetails);

			return ResponseEntity.ok(new JwtResponse(token));
		} else {
			throw new BadRequestAlertException("Bad credentials or UUID! ", null, null);
		}

	}

	/**
     * {@code POST  /register} : change visitor user to registered user
     *
     * @param UserDTO : userifo
     * @return the {@link ResponseEntity} with status {@code 200} and with user in body.
     * @throws Exeption if user failed to save.
     */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody UserDTO userDTO) throws Exception {
		userDTO.setUserType(UserType.REGISTERED);
		return ResponseEntity.ok(userService.save(userDTO));
	}

	/**
     * {@code GET  /logout} : logout user
     *
     * @return the logout successful message
     */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		// return "redirect:/"; // to redirect to particular page
		return "Logout successful!";
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
