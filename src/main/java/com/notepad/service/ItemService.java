package com.notepad.service;

import java.security.Principal;
import java.util.List;

import com.notepad.dto.ItemDTO;

/**
* The ItemService interface defines the operations to be performed related to notes
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
public interface ItemService {

	List<ItemDTO> save(List<ItemDTO> itemDTO, Principal principal);

	void delete(Long itemId);

	List<ItemDTO> findAllByMenu(Long menuId);

	List<ItemDTO> findAllByUserId(Principal principal);

}
