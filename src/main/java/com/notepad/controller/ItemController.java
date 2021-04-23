package com.notepad.controller;

import java.net.URISyntaxException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.notepad.dto.ItemDTO;
import com.notepad.entity.Item;
import com.notepad.service.ItemService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
* The ItemController declares the REST APIs for operations for Notes
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
@Api(value="Notes", description="Operations pertaining to notes")
@RestController
//@RequestMapping("/api")
@CrossOrigin
public class ItemController {

	private final Logger log = LoggerFactory.getLogger(ItemController.class);

	@Autowired
	private ItemService itemService;
	
	/**
     * {@code POST  /api/menu/addItem} : Create a new item/items
     *
     * @param itemDTO : the itemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemDTO.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
	@ApiOperation(value = "Adds a note under a folder with pId and orderId for sorting note, copy functionality for "
			+ "duplicating note")
	@PostMapping("/api/menu/addItem")
	public ResponseEntity<List<ItemDTO>> saveItem(@RequestBody List<ItemDTO> itemDTO, Principal principal) {
		log.info("Rest request to save Item: {}", itemDTO);
		return ResponseEntity.ok().body(itemService.save(itemDTO, principal));
	}
	
	/**
     * {@code PUT  /api/menu/updateItem} : Updates existing item/items.
     *
     * @param itemDTO the itemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemDTO,
     * or with status {@code 400 (Bad Request)} if the itemDTO is not valid,
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
	@ApiOperation(value = "Updates a note content, its sorting by orderId, cut/paste functionality for sorting or changing"
			+ "parent folder")
	@PutMapping("/api/menu/updateItem")
	public ResponseEntity<List<ItemDTO>> updateItem(@RequestBody List<ItemDTO> itemDTOs, Principal principal) {
		log.info("Rest request to update Item: {}", itemDTOs);
		for (ItemDTO itemDTO: itemDTOs) {
			if(itemDTO.getItemId() == null) {
				throw new BadCredentialsException("itemId should not be null");
			}
		}
		return ResponseEntity.ok().body(itemService.save(itemDTOs, principal));
	}
	
	/**
     * {@code GET  /api/menu/menuItem/{menuId}} : get all the items by menuId.
     * @param menuId to get all the items inside this "menuId" menu
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notes in body.
     */
	@ApiOperation(value = "Returns all notes within a folder by menuId")
	@GetMapping("/api/menu/menuItem/{menuId}")
	public ResponseEntity<List<ItemDTO>> getAllItemsByMenu(@PathVariable Long menuId) {
		log.info("Rest request to get all Items from menu with menuId : {}", menuId);
		List<ItemDTO> itemDTOs = itemService.findAllByMenu(menuId);
		return ResponseEntity.ok().body(itemDTOs);
	}
	
	/**
     * {@code GET  /api/menu/menuItemByUser} : get all the items by user id.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notes in body.
     */
	@ApiOperation(value = "Returns all notes by user id")
	@GetMapping("/api/menu/menuItemByUser")
	public ResponseEntity<List<ItemDTO>> getAllItemsByUserId(Principal principal) {
		log.info("Rest request to get all Items from menu with userId : {}");
		List<ItemDTO> itemDTOs = itemService.findAllByUserId(principal);
		return ResponseEntity.ok().body(itemDTOs);
	}
	/**
     * {@code DELETE  /api/menu/delItem/:itemId} : delete the "id" item.
     *
     * @param itemId the id of the item to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
	@ApiOperation(value = "Deletes a note by itemId")
	@DeleteMapping("/api/menu/delItem/{itemId}")
	public ResponseEntity<Map<String, String>> deleteItem(@PathVariable Long itemId) {
		log.info("Rest request to delete Item with id: {}", itemId);
		itemService.delete(itemId);
		
		Map<String, String> map = new HashMap<>();
	    map.put("message","Note deleted successfully");
		
	    return ResponseEntity.status(HttpStatus.OK).body(map);
	}
	
}
