package com.notepad.serviceImpl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.notepad.dto.ItemDTO;
import com.notepad.dto.MenuDTO;
import com.notepad.entity.Item;
import com.notepad.entity.Menu;
import com.notepad.entity.User;
import com.notepad.entity.enumeration.UserType;
import com.notepad.mapper.MenuMapper;
import com.notepad.repository.ItemRepository;
import com.notepad.repository.MenuRepository;
import com.notepad.repository.UserRepository;
import com.notepad.service.MenuService;

/**
* The MenuServiceImpl implements MenuService that
* Get, Save, Update or Deletes Folders(Menus)
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
@Service
//@Transactional
public class MenuServiceImpl implements MenuService {

	private final Logger log = LoggerFactory.getLogger(MenuServiceImpl.class);

	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private MenuMapper menuMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ItemRepository itemRepository;

	/**
	 * Get all the menus of logged in user ordered by orderId.
	 *
	 * @return the list of menus.
	 */
	@Override
	public List<MenuDTO> findAllByLoggedInUser(Principal principal) {
		log.info("Request to get all Menus(Folders) by logged in user");
		
		// menuDTOs list to return all menus
		List<MenuDTO> menuDTOs = new ArrayList<>();

		// get logged in user
		User user = userRepository.findByUserName(principal.getName());

		if (user != null) {
			
			// Get all menus of a user ordered by orderId
			List<Menu> menus = menuRepository.findAllByUserOrderByOrderId(user);

			// If menus are not empty then convert menu Entities to menuDTOs
			if (!menus.isEmpty()) {
				menus.forEach(menu -> {
					menuDTOs.add(menuMapper.toDTO(menu));
				});
			}
		}

		return menuDTOs;
	}

	/**
	 * Get all the child menus by parentMenuId, ordered by orderId.
	 *
	 * @param menuId is parent folder id
	 * @return the list of menus.
	 */
	@Override
	public List<MenuDTO> findAllMenusByParentMenuAndUser(Long menuId, Principal principal) {

		// menuDTOs to return all menus
		List<MenuDTO> menuDTOs = new ArrayList<>();

		// get logged in user
		User user = userRepository.findByUserName(principal.getName());

		if (user != null) {
			
			// Get parent menu by parent menuId
			Optional<Menu> parentMenu = menuRepository.findById(menuId);
			if (parentMenu != null) {
				
				// get all sub folders of a menu of a user ordered by order id
				List<Menu> menus = menuRepository.findAllByParentMenuAndUserOrderByOrderId(parentMenu.get(), user);
				
				// If menus are not empty then convert menu Entities to menuDTOs
				if (!menus.isEmpty()) {
					menus.forEach(menu -> {
						menuDTOs.add(menuMapper.toDTO(menu));
					});
				}
			}
		}

		return menuDTOs;
	}

	/**
	 * Method to save or update new menu/menus
	 *
	 * @param List<MenuDTO> : the list of menuDTO to create or update.
	 * @return the list of saved or updated menus.
	 * 
	 */
	@Override
	public List<MenuDTO> save(List<MenuDTO> menuDTOs, Principal principal) {
		log.info("Request to create/ update menu : {} ", menuDTOs);

		// newItems to return saved items
		List<MenuDTO> newMenus = new ArrayList<MenuDTO>();
		
		// itemToUpdateList to update list of items
		List<Menu> menuToUpdateList = new ArrayList<Menu>();
				
		for (MenuDTO menuDTO: menuDTOs) {
			
			// to create new menu
			if (menuDTO.getMenuId() == null) {
				User user = userRepository.findByUserName(principal.getName());
				if (user != null) {
					// set user to menu
					menuDTO.setuId(user.getUserId());
					menuDTO.setUserName(user.getUserName());
					// set userType
					// 1 -> registered user
					// 2 -> visitor
					if (user.getUserType().equals(UserType.REGISTERED)) {
						menuDTO.setUserType(1);
					}
					if (user.getUserType().equals(UserType.VISITOR)) {
						menuDTO.setUserType(2);
					}
				}

				// Convert DTO to Menu Entity
				Menu newMenu = menuMapper.toEntity(menuDTO);
				
				// if parentMenuId(pid) is null then define it as a root menu
				if (menuDTO.getpId() != null) {
					
					// Get parent menu by parent menu id
					Optional<Menu> menuOp = menuRepository.findById(menuDTO.getpId());
					
					// set parent menu if given
					newMenu.setParentMenu(menuOp.isPresent() ? menuOp.get() : null);
				} else {
					newMenu.setParentMenu(null);
				}
				
				menuToUpdateList.add(newMenu);
			} else {
				// to update the Menu

				Optional<Menu> menuToUpdate = menuRepository.findById(menuDTO.getMenuId());
				
				// If found menu by id then update its name, userType and parent.
				menuToUpdate.ifPresent(existingMenu -> {
					
					// Update menu name
					if (menuDTO.getMenuName() != null) {
						existingMenu.setMenuName(menuDTO.getMenuName());
					}
					
					// Update menu type
					if (menuDTO.getUserType() != null) {
						existingMenu.setUserType(menuDTO.getUserType());
					}
					
					// to update parent menu
					if (menuDTO.getpId() != null) {
						
						// get a parent menu by parent menuId
						Optional<Menu> menuOp = menuRepository.findById(menuDTO.getpId());
						
						// update parent menu
						existingMenu.setParentMenu(menuOp.isPresent() ? menuOp.get() : null);	
					}
					else {
						existingMenu.setParentMenu(null);
					}
					
					// set order id for sorting
					existingMenu.setOrderId(menuDTO.getOrderId());
				});
				
				menuToUpdateList.add(menuToUpdate.get());
			}
		}
		
		// Create or update all menus
		List<Menu> updatedMenus = menuRepository.saveAll(menuToUpdateList);
		
		// convert saved menus to menuDTOs
		for(Menu updatedMenu: updatedMenus) {
			newMenus.add(menuMapper.toDTO(updatedMenu));
		}
		
		return newMenus;
	}

	/**
	 * Delete the menu by id and logged in user.
	 *
	 * @param menuId the id of the entity.
	 */
	@Override
	public void delete(Long menuId, Principal principal) {
		log.debug("Request to delete menu with id : {}", menuId);

		Optional<Menu> menu = menuRepository.findById(menuId);

		// If found menu then delete its notes and subfolders
		menu.ifPresent(menu1 -> {

			// 1. delete all items inside the menu
			List<Item> itemsByMenu = itemRepository.findAllByMenu(menu1);
			log.info("Deleting all the items inside menu with menuId : {}", menu1.getMenuId());
			// delete these items
			itemsByMenu.forEach(item -> {
				itemRepository.deleteById(item.getItemId());
			});

			// 2. delete all menus inside the menu
			List<Menu> menusByParentMenu = menuRepository.findAllByParentMenu(menu1);
			log.info("Deleting all the menus inside menu with parentId : {}", menu1.getMenuId());
			// delete these menus + items
			menusByParentMenu.forEach(menuByParentMenu -> {
				this.delete(menuByParentMenu.getMenuId(), principal);
			});
			// 3. then delete menu
			menuRepository.deleteById(menuId);

		});
	}
}
