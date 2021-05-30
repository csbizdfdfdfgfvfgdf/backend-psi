package com.notepad.controller;

import com.notepad.controller.request.CreateUserRequest;
import com.notepad.controller.request.JwtRequest;
import com.notepad.controller.request.TokenRefreshRequest;
import com.notepad.controller.response.JwtResponse;
import com.notepad.dto.ResponseDto;
import com.notepad.entity.Token;
import com.notepad.entity.User;
import com.notepad.jwt.config.JwtTokenUtil;
import com.notepad.redis.repo.RedisUserRepo;
import com.notepad.service.MailService;
import com.notepad.service.RefreshTokenService;
import com.notepad.service.UserService;
import com.notepad.serviceImpl.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

/**
* The JwtAuthenticationController declares the REST APIs for authentications
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
@RestController
@CrossOrigin
@Api(value="User Authentication", description="", tags = { "User Management" })
@ApiImplicitParams({})
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

	@Autowired
	private RefreshTokenService refreshTokenService;

	@Autowired
	MailService mailService;

	/**
     * {@code POST  /authenticate} : authenticate user and generate token
     *
     * @param authenticationRequest : request with username and password
     * @return the {@link ResponseEntity} with status {@code 200} and with body token.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
	@ApiOperation(value = "Authenticate registered user")
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", required = false, paramType = "header", dataType = "String", allowEmptyValue = true, readOnly = true)})
	public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

		// authenticate user by username and password
		authenticate(authenticationRequest.getUserName(), authenticationRequest.getPassword());

		// get user by username
		final UserDetails userDetails = userServiceImpl.loadUserByUsername(authenticationRequest.getUserName());

		// generate token
		final String token = jwtTokenUtil.generateToken(userDetails);
		// generate refresh-token
		String refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername()).getToken();
		return ResponseEntity.ok(new JwtResponse(token,refreshToken));
	}

	/**
     * {@code POST  /authenticate-with-uuid} : create authentication token for visitor
     *
     * @param RedisUser : user with username and password
     * @return the {@link ResponseEntity} with status {@code 200} and with body token.
     * @throws BadRequestAlertEception if the Location URI syntax is incorrect.
     */
/*	@RequestMapping(value = "/authenticate-with-uuid", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationTokenForVisitor(@RequestBody RedisUser redisUser) throws Exception {

		// check to redisDB for authentication!

		RedisUser redisUser2 = redisUserRepo.findByUserName(redisUser.getUserName());

		if (redisUser2.getUuid().equals(redisUser.getUuid())) {

			authenticate(redisUser.getUserName(), redisUser.getPassword());

			final UserDetails userDetails = userServiceImpl.loadUserByUsername(redisUser.getUserName());

			final String token = jwtTokenUtil.generateToken(userDetails);

			return ResponseEntity.ok(new JwtResponse(token,null));
		} else {
			throw new BadRequestAlertException("Bad credentials or UUID! ", null, null);
		}

	}*/

	/**
     * {@code POST  /register} : change visitor user to registered user
     *
     * @param UserDTO : userifo
     * @return the {@link ResponseEntity} with status {@code 200} and with user in body.
     * @throws Exeption if user failed to save.
     */
/*	@ApiOperation(value = "User registration")
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody UserDTO userDTO) throws Exception {
		userDTO.setUserType(UserType.REGISTERED);
		return ResponseEntity.ok(userService.save(userDTO));
	}*/

	@ApiOperation(value = "Creates a user")
	@PostMapping("/sign-up")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", required = false, paramType = "header", dataType = "String", allowEmptyValue = true, readOnly = true)})
	public ResponseEntity<ResponseDto> createUser(@Valid @RequestBody CreateUserRequest createUserRequest, HttpServletRequest request) {
		createUserRequest.setEmail(createUserRequest.getEmail().toLowerCase());
		userService.createUser(createUserRequest);
		User user = userService.findByEmail(createUserRequest.getEmail());
		String registerURL = generateEmailTokenWithURL(request, Optional.of(user));

		// send reset password mail
		mailService.registerEmail(user, registerURL);

	//	mailService.registerEmail(userService.findByEmail(createUserRequest.getEmail()));
		return ResponseEntity.ok().body(ResponseDto.builder()
				.status("success").build());
	}


	/**
     * {@code GET  /logout} : logout user
     *
     * @return the logout successful message
     */
	@ApiOperation(value = "Logout user")
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

	@ApiOperation(value = "Valid refresh token are being used to fetch new (JWT token and refresh token)")
	@PostMapping("refresh-token")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", required = false, paramType = "header", dataType = "String", allowEmptyValue = true, readOnly = true)})
	public ResponseEntity<JwtResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
		return ResponseEntity.ok(refreshTokenService.validateAndGenerateRefreshToken(request));
	}

	private String generateEmailTokenWithURL(HttpServletRequest request, Optional<User> user) {
		String token = UUID.randomUUID().toString();
		userService.saveTokenForUser(token, user.get(), Token.getEmailPasswordExpiration());

		// generate URL for mail
		StringBuffer url = request.getRequestURL();
		String base = url.substring(0, url.length() - request.getRequestURI().length() + request.getContextPath().length()) + "/";

		// password reset link
		String emailVerifyURL = base + "verify-email?email=" + user.get().getEmail() + "&token=" + token;
		return emailVerifyURL;
	}
}
