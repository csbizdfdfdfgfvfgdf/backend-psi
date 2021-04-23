package com.notepad.controller;

import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.notepad.dto.ResponseDto;
import com.notepad.dto.TokenAndPasswordDTO;
import com.notepad.dto.UserDTO;
import com.notepad.entity.User;
import com.notepad.entity.enumeration.UserType;
import com.notepad.error.BadRequestAlertException;
import com.notepad.repository.UserRepository;
import com.notepad.service.MailService;
import com.notepad.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
* The UserController declares the REST APIs for operations for Users
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
@RestController
@CrossOrigin
@Api(value="Users", description="Operations pertaining to users")
@ApiImplicitParams({})
public class UserController {

	private final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MailService mailService;

	/**
	 * {@code GET  /auth/me} : get user by token.
	 * 
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the
	 *         logged in userDTO.
	 */
	@ApiOperation(value = "Authenticate by returning user by token")
	@GetMapping("/auth/me")
	public ResponseEntity<UserDTO> getLoggedInUser(Principal principal) {
		log.info("Rest request to get information of logged in user by token!");
		String userName = principal.getName();
		UserDTO userDTO = userService.findByUserName(userName);
		return ResponseEntity.ok().body(userDTO);
	}

	/**
	 * {@code GET  /auth/makeUuid} : Creates UUID and a new visitor user.
	 * 
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the UUID.
	 */
	@ApiOperation(value = "Creates a user by new generating and returning UUID that is further used in header "
			+ "with all calls to authenticate user")
	@ApiImplicitParams(@ApiImplicitParam(name = "uuid"))
	@GetMapping("auth/makeUuid")
	public ResponseEntity<ResponseDto> getUUID() {
		log.info("Rest request to generate random UUID.");
		String uuid = UUID.randomUUID().toString();
		UserDTO userDTORes = new UserDTO();
		UserDTO userDTO = new UserDTO();
		userDTO.setUserName(uuid);
		userDTO.setPassword(uuid);
		userDTO.setConfirmPassword(uuid);
		userDTO.setUserType(UserType.VISITOR);
		userDTO.setEmail(uuid+"@gmail.com");
		userDTORes = userService.save(userDTO);
		return ResponseEntity.ok().body(
				ResponseDto.builder()
				.status("success")
				.data(uuid).build());
	}

	/**
	 * {@code GET  /users} : get all the users.
	 * 
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
	 *         of users in body.
	 */
	@GetMapping("/users")
	@ApiOperation(value = "Returns list of all users")
	public ResponseEntity<List<UserDTO>> getAllUsers() {
		log.info("Rest request to get all users");
		List<UserDTO> userDTOs = userService.findAll();
		return ResponseEntity.ok().body(userDTOs);
	}

	/**
	 * {@code POST /auth/retrievePwd} : send a password reset link to email id
	 * provided in param.
	 *
	 * @param email : the email id to send a password create link.
	 * @return the {@link ResponseEntity} with status {@code 200} and url generated
	 *         & sent to user.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@ApiOperation(value = "Sends a reset password link to the provided user email id")
	@PostMapping("/auth/retrievePwd")
	public ResponseEntity<ResponseDto> retrivePasswordSendMail(@RequestBody UserDTO userDTO, HttpServletRequest request) {
		log.info("Rest request to send mail on {} ", userDTO.getEmail());
		// check if user with email present or not?
		Optional<User> user = userRepository.findOneByEmail(userDTO.getEmail().toLowerCase());
		
		if (!user.isPresent()) {
			// TODO : make a custom exception for UserNotFound!
			throw new BadRequestAlertException("user not found with email : "+userDTO.getEmail(), null, null);
		} else {

			// generate & save token before sending a mail
			String token = UUID.randomUUID().toString();
			userService.saveTokenForUser(token, user.get());

			// generate URL for mail
			StringBuffer url = request.getRequestURL();
			String uri = request.getRequestURI();
			String ctx = request.getContextPath();
			String base = url.substring(0, url.length() - uri.length() + ctx.length()) + "/";

			// password reset link
			String passwordResetURL = base + "setNewPassword?id=" + user.get().getUserId() + "&token=" + token;
			System.out.println("passwordResetURL: " + passwordResetURL);

			// send reset password mail
			mailService.sendPasswordResetMail(user.get(), passwordResetURL);

		}

		return ResponseEntity.ok().body(
				ResponseDto.builder()
				.status("success")
				.data("mail sent succesfully to " + user.get().getEmail()).build()
				);
	}
	
	/**
	 * {@code POST /auth/resetPwd} : validate token and reset the password 
	 *
	 * @param TokenAndPasswordDTO : the token from URL in mail & new Password
	 * @return the {@link ResponseEntity} with status {@code 200} and return the updated user entity.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@ApiOperation(value = "Validates the token sent with reset password link and changes the password")
	@PostMapping("/auth/resetPwd")
	public ResponseEntity<ResponseDto> validateTokenAndResetPassword(@RequestBody TokenAndPasswordDTO tokenAndPasswordDTO) {
		log.info("Rest request to validate token and reset password");
		userService.resetPassword(tokenAndPasswordDTO);
		return ResponseEntity.ok().body(
				ResponseDto.builder()
				.status("success")
				.data("").build()
				);
	}
	
}
