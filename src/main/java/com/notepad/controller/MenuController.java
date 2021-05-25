package com.notepad.controller;

import com.notepad.config.UserPrincipal;
import com.notepad.dto.MenuDTO;
import com.notepad.entity.User;
import com.notepad.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The MenuController declares the REST APIs for operations for Folders
 *
 * @author  Zohaib Ali
 * @version 1.0
 * @since   2021-04-22
 */
@RestController
@CrossOrigin
@Api(value="Folders", description="Operations pertaining to folders", tags = { "Menu" })
public class MenuController {

	private final Logger log = LoggerFactory.getLogger(MenuController.class);

	@Autowired
	private MenuService menuService;

	@ApiIgnore
	@GetMapping("/test")
	public String testMethod() {
		log.info("logging is working fine!");
		return "notepad app is working. 2";
	}

	/**
	 * {@code POST  /menus} : Create new menu/menus
	 *
	 * @param menuDTO : the menuDTO to create.
	 * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new menuDTO.
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@ApiOperation(value = "Adds a folder with or without pId (parent folder id) and orderId for sorting folder, "
			+ "copy functionality for duplicating folder")
	@PostMapping("/api/menu/addMenu")
	public ResponseEntity<List<MenuDTO>> saveMenu(@RequestBody List<MenuDTO> menuDTOs) {
		log.info("Rest request to save Menu: {}", menuDTOs);
		return ResponseEntity.ok().body(menuService.save(menuDTOs, getPrincipal()));
	}

	/**
	 * {@code PUT  /updateMenu} : Updates existing menu/menus.
	 *
	 * @param menuDTO the menuDTO to update.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated menuDTO,
	 * or with status {@code 400 (Bad Request)} if the menuDTO is not valid,
	 * @throws URISyntaxException if the Location URI syntax is incorrect.
	 */
	@ApiOperation(value = "Updates a folder name, its sorting by orderId, cut/paste functionality for sorting folder"
			+ " or changing parent folder")
	@PutMapping("/api/menu/updateMenu")
	public ResponseEntity<List<MenuDTO>> updateMenu(@RequestBody List<MenuDTO> menuDTOs) {
		log.info("Rest request to update Menu: {}", menuDTOs);

		for (MenuDTO menuDTO: menuDTOs) {
			if(menuDTO.getMenuId() == null) {
				throw new BadCredentialsException("menuId should not be null");
			}
		}
		return ResponseEntity.ok().body(menuService.save(menuDTOs, getPrincipal()));
	}

	/**
	 * {@code GET  /menus} : get all the menus by logged in user.
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of menus in body.
	 */
	@ApiOperation(value = "Returns all folders by logged in user")
	@GetMapping("/menus")
	public ResponseEntity<List<MenuDTO>> getAllMenusByUser() {
		log.info("Rest request to get all Menus(Folders)");
		List<MenuDTO> menuDTOs = menuService.findAllByLoggedInUser(getPrincipal());
		return ResponseEntity.ok().body(menuDTOs);
	}

	/**
	 * {@code GET  /menus/{menuId}} : get all the menus by parentId and logged in user.
	 *
	 * @param menuId : is a parentId
	 * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of menus in body.
	 */
	@ApiOperation(value = "Returns all folders by parent folder id (menuId) and logged in user")
	@GetMapping("/menus/{menuId}")
	public ResponseEntity<List<MenuDTO>> getAllMenusByParentMenuAndUser(@PathVariable Long menuId) {
		log.info("Rest request to get all Menus(Folders)");
		List<MenuDTO> menuDTOs = menuService.findAllMenusByParentMenuAndUser(menuId, getPrincipal());
		return ResponseEntity.ok().body(menuDTOs);
	}


	/**
	 * {@code DELETE  /delMenu/:menuId} : delete the "id" menu.
	 *
	 * @param menuId the id of the menu to delete.
	 * @param principal the logged in user.
	 * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
	 */
	@ApiOperation(value = "Deletes a menu by id for logged in user")
	@DeleteMapping("api/menu/delMenu/{menuId}")
	public ResponseEntity<Map<String, String>> deleteMenuByIdAndUser(@PathVariable Long menuId) {
		log.info("Rest request to delete Menu with id: {}", menuId);
		menuService.delete(menuId, getPrincipal());

		Map<String, String> map = new HashMap<>();
		map.put("message","Folder deleted successfully");

		return ResponseEntity.status(HttpStatus.OK).body(map);
	}

	/**
	 * {@code  /hello} : Method to greet the user
	 *
	 * @param User
	 * @return the {@link ResponseEntity} with status {@code 200 and a greeting message}.
	 */
	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public ResponseEntity<String> greeting(User user) throws Exception {
		return ResponseEntity.ok().body(" HI Menu(folder) deleted successfully!");
	}

	public UserPrincipal getPrincipal() {
		return (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
