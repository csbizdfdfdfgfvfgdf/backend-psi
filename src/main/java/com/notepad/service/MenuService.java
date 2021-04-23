package com.notepad.service;

import java.security.Principal;
import java.util.List;

import com.notepad.dto.MenuDTO;

/**
* The MenuService interface defines the operations to be performed related to folders
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
public interface MenuService {

	List<MenuDTO> save(List<MenuDTO> menuDTO, Principal principal);

	void delete(Long menuId, Principal principal);

	List<MenuDTO> findAllByLoggedInUser(Principal principal);

	List<MenuDTO> findAllMenusByParentMenuAndUser(Long menuId, Principal principal);

}
