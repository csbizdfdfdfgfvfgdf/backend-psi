package com.notepad.service;

import com.notepad.config.UserPrincipal;
import com.notepad.dto.MenuDTO;

import java.util.List;

/**
* The MenuService interface defines the operations to be performed related to folders
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
public interface MenuService {

	List<MenuDTO> save(List<MenuDTO> menuDTO, UserPrincipal principal);

	void delete(Long menuId, UserPrincipal principal);

	List<MenuDTO> findAllByLoggedInUser(UserPrincipal principal);

	List<MenuDTO> findAllMenusByParentMenuAndUser(Long menuId, UserPrincipal principal);

}
