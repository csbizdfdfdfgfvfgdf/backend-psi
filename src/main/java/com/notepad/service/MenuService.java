package com.notepad.service;

import java.security.Principal;
import java.util.List;

import com.notepad.dto.MenuDTO;

public interface MenuService {

//	List<MenuDTO> findAll();

	List<MenuDTO> save(List<MenuDTO> menuDTO, Principal principal);

	void delete(Long menuId, Principal principal);

	List<MenuDTO> findAllByLoggedInUser(Principal principal);

	List<MenuDTO> findAllMenusByParentMenuAndUser(Long menuId, Principal principal);

//	void deleteByUser(Principal principal);

}
