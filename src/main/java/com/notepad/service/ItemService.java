package com.notepad.service;

import com.notepad.config.UserPrincipal;
import com.notepad.dto.ItemDTO;

import java.util.List;

/**
* The ItemService interface defines the operations to be performed related to notes
*

*/
public interface ItemService {

	List<ItemDTO> save(List<ItemDTO> itemDTO, UserPrincipal principal);

	void delete(Long itemId,UserPrincipal userPrincipal);

	List<ItemDTO> findAllByMenu(Long menuId);

	List<ItemDTO> findAllByUserId(UserPrincipal principal);

}
