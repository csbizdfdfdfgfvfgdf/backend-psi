package com.notepad.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.notepad.config.UserPrincipal;
import com.notepad.controller.request.ForgetPasswordRequest;
import com.notepad.controller.response.JwtResponse;
import com.notepad.dto.ResponseDto;
import com.notepad.controller.request.TokenAndPasswordDTO;
import com.notepad.dto.UserDTO;
import com.notepad.controller.request.VerifyEmailTokenDTO;
import com.notepad.entity.Token;
import com.notepad.entity.User;
import com.notepad.entity.enumeration.UserType;
import com.notepad.error.BadRequestAlertException;
import com.notepad.jwt.config.JwtTokenUtil;
import com.notepad.repository.UserRepository;
import com.notepad.service.MailService;
import com.notepad.service.RefreshTokenService;
import com.notepad.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.UUID;

/**
 * The UserController declares the REST APIs for operations for Users
 *
 * @author  Zohaib Ali
 * @version 1.0
 * @since   2021-04-22
 */
@RestController
@CrossOrigin
@Api(value="Users", description="Operations pertaining to users", tags = { "User" })
@ApiImplicitParams({})
public class UserController {

	private final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MailService mailService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private RefreshTokenService refreshTokenService;

	/**
	 * {@code GET  /auth/me} : get user by token.
	 *
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
	 *         logged in userDTO.
	 */
	@ApiOperation(value = "Authenticate by returning user by token")
	@GetMapping("/auth/me")
	public ResponseEntity<UserDTO> getLoggedInUser() {
		log.info("Rest request to get information of logged in user by token!");
		UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userName = userPrincipal.getDbUserName();
		UserDTO userDTO = userService.findByUserName(userName);
		return ResponseEntity.ok().body(userDTO);
	}

	/**
	 * {@code GET  /auth/makeUuid} : Creates UUID and a new visitor user.
	 *
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the UUID.
	 */
	@ApiOperation(value = "Creates a visitor user and returning UUID and JWT token and Refresh token.")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", required = false, paramType = "header", dataType = "String", allowEmptyValue = true, readOnly = true)})
	@GetMapping("auth/makeUuid")
	public ResponseEntity<JwtResponse> getUUID() {
		log.info("Rest request to generate random UUID.");
		String uuid = UUID.randomUUID().toString();
		UserDTO userDTORes = new UserDTO();
		UserDTO userDTO = new UserDTO();
		userDTO.setUserName(uuid);
		userDTO.setPassword(uuid);
		userDTO.setConfirmPassword(uuid);
		userDTO.setUserType(UserType.VISITOR);
		userDTO.setEmail(uuid+"@gmail.com");
		userDTO.setEmailVerified(Boolean.TRUE);
		userDTORes = userService.save(userDTO);
		// generate token
		final String token = jwtTokenUtil.generateToken(userDTO.getEmail());
		// generate refresh-token
		String refreshToken = refreshTokenService.createRefreshToken(userDTO.getEmail()).getToken();
		return ResponseEntity.ok(new JwtResponse(token,refreshToken,uuid));
	}

	/**
	 * {@code GET  /users} : get all the users.
	 *
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of users in body.
	 */
/*	@GetMapping("/users")
	@ApiOperation(value = "Returns list of all users")
	public ResponseEntity<List<UserDTO>> getAllUsers() {
		log.info("Rest request to get all users");
		List<UserDTO> userDTOs = userService.findAll();
		return ResponseEntity.ok().body(userDTOs);
	}*/

	/**
	 * {@code POST /auth/retrievePwd} : send a password reset link to email id
	 * provided in param.
	 *
	 *
	 * @return the {@link ResponseEntity} with status {@code 200} and url generated
	 *         & sent to user.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@ApiOperation(value = "Sends a reset password link to the provided user email id")
	@PostMapping("/auth/forget-password")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", required = false, paramType = "header", dataType = "String", allowEmptyValue = true, readOnly = true)})
	public ResponseEntity<ResponseDto> retrivePasswordSendMail(@Valid @RequestBody ForgetPasswordRequest userDTO, HttpServletRequest request) {
		log.info("Rest request to send mail on {} ", userDTO.getEmail());
		// check if user with email present or not?
		Optional<User> user = userRepository.findOneByEmail(userDTO.getEmail().toLowerCase());

		if (!user.isPresent()) {
			// TODO : make a custom exception for UserNotFound!
			throw new BadRequestAlertException("user not found with email : "+userDTO.getEmail(), null, null);
		} else {

			// generate & save token before sending a mail
			String passwordResetURL = generatePasswordTokenWithURL(request, user);

			// send reset password mail
			mailService.sendPasswordResetMail(user.get(), passwordResetURL);
		}

		return ResponseEntity.ok().body(
				ResponseDto.builder()
						.status("success")
						.data("mail sent succesfully to " + user.get().getEmail()).build()
		);
	}

	private String generatePasswordTokenWithURL(HttpServletRequest request, Optional<User> user) {
		String token = UUID.randomUUID().toString();
		userService.saveTokenForUser(token, user.get(), Token.getResetPasswordExpiration());

		// generate URL for mail
		StringBuffer url = request.getRequestURL();
		String uri = request.getRequestURI();
		String ctx = request.getContextPath();
		String base = url.substring(0, url.length() - uri.length() + ctx.length()) + "/";

		// password reset link
		String passwordResetURL = base + "forget-password?email=" + user.get().getEmail() + "&token=" + token;
		return passwordResetURL;
	}

	/**
	 * {@code POST /auth/resetPwd} : validate token and reset the password
	 *
	 * @param tokenAndPasswordDTO : the token from URL in mail & new Password
	 * @return the {@link ResponseEntity} with status {@code 200} and return the updated user entity.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@ApiOperation(value = "Validates the token and changes the password")
	@PostMapping("/auth/change-password")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", required = false, paramType = "header", dataType = "String", allowEmptyValue = true, readOnly = true)})
	public ResponseEntity<ResponseDto> validateTokenAndResetPassword(@Valid @RequestBody TokenAndPasswordDTO tokenAndPasswordDTO) {
		log.info("Rest request to validate token and change password");
		userService.resetPassword(tokenAndPasswordDTO);
		return ResponseEntity.ok().body(
				ResponseDto.builder()
						.status("success")
						.data("").build()
		);
	}


	/**
	 * verify email token if valid then market email is verified
	 * @param tokenAndPasswordDTO
	 * @return
	 */
	@ApiOperation(value = "Validates the token and verify email")
	@PostMapping("/auth/verify-email")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", required = false, paramType = "header", dataType = "String", allowEmptyValue = true, readOnly = true)})
	public ResponseEntity<ResponseDto> validateTokenAndVerifyEmail(@Valid @RequestBody VerifyEmailTokenDTO tokenAndPasswordDTO) {
		log.info("Rest request to validate token and verify email");
		userService.verifyEmailToken(tokenAndPasswordDTO);
		return ResponseEntity.ok().body(
				ResponseDto.builder()
						.status("success")
						.data("").build()
		);
	}


	/**
	 * {@code POST  /signup} : Creates user.
	 *
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the UUID.
	 */
	/*@ApiOperation(value = "Creates a user")
	@PostMapping("/sign-up")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", required = false, paramType = "header", dataType = "String", allowEmptyValue = true, readOnly = true)})
	public ResponseEntity<ResponseDto> createUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
		log.info("Rest request to create user.");
		createUserRequest.setEmail(createUserRequest.getEmail().toLowerCase());
		userService.createUser(createUserRequest);
		mailService.registerEmail(userRepository.findOneByEmail(createUserRequest.getEmail().toLowerCase()).get());
		return ResponseEntity.ok().body(ResponseDto.builder()
						.status("success").build());
	}*/

	@ApiIgnore
	@ApiOperation(value = "Test email is coming or not after passing email id")
	@PostMapping("/test-email/{email}")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "Authorization", required = false, paramType = "header", dataType = "String", allowEmptyValue = true, readOnly = true)})
	public ResponseEntity<ResponseDto> testEmail(@PathVariable String email) {
		User user = new User();
		user.setEmail(email);
		mailService.sendPasswordResetMail(user, "");
		return ResponseEntity.ok().body(
				ResponseDto.builder()
						.status("success")
						.data("").build()
		);
	}
}
