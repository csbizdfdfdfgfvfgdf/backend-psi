package com.notepad.serviceImpl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	 * Get all the menus ordered by orderId.
	 *
	 * @return the list of entities.
	 */
	@Override
	public List<MenuDTO> findAllByLoggedInUser(Principal principal) {
		log.info("Request to get all Menus(Folders) by logged in user");
		List<MenuDTO> menuDTOs = new ArrayList<>();

		User user = userRepository.findByUserName(principal.getName());

		if (user != null) {
			List<Menu> menus = menuRepository.findAllByUserOrderByOrderId(user);

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
	 * @param menuId is parentId
	 * @return the list of entities.
	 */
	@Override
	public List<MenuDTO> findAllMenusByParentMenuAndUser(Long menuId, Principal principal) {

		List<MenuDTO> menuDTOs = new ArrayList<>();

		User user = userRepository.findByUserName(principal.getName());

		if (user != null) {
			Optional<Menu> parentMenu = menuRepository.findById(menuId);
			if (parentMenu != null) {
				List<Menu> menus = menuRepository.findAllByParentMenuAndUserOrderByOrderId(parentMenu.get(), user);
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
	 * to save a new menu
	 *
	 * @param menuDTO : the menuDTO to create.
	 * @return the persisted entity.
	 * 
	 */
	@Override
	public MenuDTO save(MenuDTO menuDTO, Principal principal) {
		log.info("Request to create/ update menu : {} ", menuDTO);

		// to create menu, rather is to update
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

			Menu menu = menuMapper.toEntity(menuDTO);

			// if parentMenuId(pid) is null then define it as a root menu
			if (menuDTO.getpId() != null) {
				Optional<Menu> menuOp = menuRepository.findById(menuDTO.getpId());
				menu.setParentMenu(menuOp.isPresent() ? menuOp.get() : null);
			} else {
				menu.setParentMenu(null);
			}

			// check if orderId presents?

			if (menuDTO.getOrderId() != null) { // check if orderId presents in requestBody
				// if yes, then do relevant changes

				// 1) get Menus by the pId of menu to be stored --> sortedByOrderId

				List<Menu> menus = new ArrayList<>();

				Long pId = menuDTO.getpId();

				if (pId == null) {

					// get all menus with pId == null (root menu)
					menus = menuRepository.findAllByParentMenuOrderByOrderId(null);

				} else {

					// get all menus with pId
					Optional<Menu> parentMenu = menuRepository.findById(pId);
					if (parentMenu != null) {
						menus = menuRepository.findAllByParentMenuOrderByOrderId(parentMenu.get());
					}
				}

				if (menus.isEmpty()) {
					// 1.1) if Menus do not exist, then set the first orderId as per requestBody
					menu.setOrderId(menuDTO.getOrderId());
				} else {

					for (Menu existingMenu : menus) {
						if (existingMenu.getOrderId() < menuDTO.getOrderId()) {
							// 1.2) if Menus (sortedByOrderId) exist, then iterate until menus'
							// existingMenuOrderId < newMenuOrderId
							continue;
						} else {
							// 1.2.1) then make orderId+1 for remaining Menus
							existingMenu.setOrderId(existingMenu.getOrderId() + 1);
						}
					}

					// 1.2.2) setNewMenuOrderId as per requestBoday
					menu.setOrderId(menuDTO.getOrderId());
				}

			} else { // if orderId does not present in requestBody

				// if not then to assign last orderId --> follow the steps below!

				// 1) get Menus by the pId of menu to be stored

				List<Menu> menus = new ArrayList<>();

				Long pId = menuDTO.getpId();

				if (pId == null) {

					// get all menus with pId == null (root menu)
					menus = menuRepository.findAllByParentMenuOrderByOrderId(null);

				} else {

					// get all menus with pId
					Optional<Menu> parentMenu = menuRepository.findById(pId);
					if (parentMenu != null) {
						menus = menuRepository.findAllByParentMenuOrderByOrderId(parentMenu.get());
					}
				}

				if (menus.isEmpty()) {
					// 1.1) if Menus do not exist, then set the first orderId as 0
					menu.setOrderId(0);
				} else {
					// 1.2) if exists, then get menu with maxOrderId
					Menu lastIndexedMenu = menus.get(menus.size() - 1);

					// 1.2.1) set maxOrderId+1 to the orderId of menu to be stored
					menu.setOrderId(lastIndexedMenu.getOrderId() + 1);
				}

			}

			Menu menuCreated = menuRepository.save(menu);
			return menuMapper.toDTO(menuCreated);

		} else { // to update the Menu

			Optional<Menu> menu = menuRepository.findById(menuDTO.getMenuId());
			menu.ifPresent(existingMenu -> {
				if (menuDTO.getMenuName() != null) {
					existingMenu.setMenuName(menuDTO.getMenuName());
				}
				if (menuDTO.getOrderId() != null) { // check if orderId presents in requestBody
					// if yes, then do relevant changes

					// 1) get Menus by the pId of menu to be stored --> sortedByOrderId

					List<Menu> menus = new ArrayList<>();

					Long pId = menuDTO.getpId();

					if (pId == null) {

						// get all menus with pId == null (root menu)
						menus = menuRepository.findAllByParentMenuOrderByOrderId(null);

					} else {

						// get all menus with pId
						Optional<Menu> parentMenu = menuRepository.findById(pId);
						if (parentMenu != null) {
							menus = menuRepository.findAllByParentMenuOrderByOrderId(parentMenu.get());
						}
					}

					if (menus.isEmpty()) {
						// 1.1) if Menus do not exist, then set the first orderId as per requestBody
						existingMenu.setOrderId(menuDTO.getOrderId());
					} else {

						for (Menu existingMenu1 : menus) {
							if (existingMenu.getMenuId().equals(existingMenu1.getMenuId())
									&& existingMenu.getMenuId() == existingMenu1.getMenuId()) {
								// skip for menu itself
								continue;
							}
							if (existingMenu1.getOrderId() < menuDTO.getOrderId()) {
								// 1.2) if Menus (sortedByOrderId) exist, then iterate until menus'
								// existingMenuOrderId < newMenuOrderId
								continue;
							} else {
								// 1.2.1) then make orderId+1 for remaining Menus
								existingMenu1.setOrderId(existingMenu1.getOrderId() + 1);
							}
						}

						// 1.2.2) setNewMenuOrderId as per requestBoday
						existingMenu.setOrderId(menuDTO.getOrderId());
					}

				}
				if (menuDTO.getUserType() != null) {
					existingMenu.setUserType(menuDTO.getUserType());
				}
				
				// TODO : update ordering while updating pId! --> need to MANAGE ORDERING to new pId(to new parent folder)
				
				
				if (menuDTO.getpId() != null) {
					Optional<Menu> menuOp = menuRepository.findById(menuDTO.getpId());
					existingMenu.setParentMenu(menuOp.isPresent() ? menuOp.get() : null);
					
					// 1) get Menus by the pId of menu to be stored

					List<Menu> menus = new ArrayList<>();

					Long pId = menuDTO.getpId();

					if (pId == null) {

						// get all menus with pId == null (root menu)
						menus = menuRepository.findAllByParentMenuOrderByOrderId(null);

					} else {

						// get all menus with pId
						Optional<Menu> parentMenu = menuRepository.findById(pId);
						if (parentMenu != null) {
							menus = menuRepository.findAllByParentMenuOrderByOrderId(parentMenu.get());
						}
					}

					if (menus.isEmpty()) {
						// 1.1) if Menus do not exist, then set the first orderId as 0
						existingMenu.setOrderId(0);
					} else {
						// 1.2) if exists, then get menu with maxOrderId
						Menu lastIndexedMenu = menus.get(menus.size() - 1);

						// 1.2.1) set maxOrderId+1 to the orderId of menu to be stored
						existingMenu.setOrderId(lastIndexedMenu.getOrderId() + 1);
					}
					
				}
			});

			Menu menuUpdated = menuRepository.save(menu.get());
			return menuMapper.toDTO(menuUpdated);
		}

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

	// /**
//     * Delete the menu by user.
//     *
//     * @param principal the logged in user.
//     */
//	@Override
//	public void deleteByUser(Principal principal) {
//		log.debug("Request to delete menu with logged in user");
//		
//		User user = userRepository.findByUserName(principal.getName());
//
//		List<Menu> menus = menuRepository.findAllByUser(user);
//		if (!menus.isEmpty()) {
//			menus.forEach(menu -> {
//				
//				// 1. delete all items inside the menu
//				List<Item> itemsByMenu = itemRepository.findAllByMenu(menu);
//				log.info("Deleting all the items inside menu with menuId : {}", menu.getMenuId());
//				// delete these items
//				itemsByMenu.forEach(item -> {
//					itemRepository.deleteById(item.getItemId());
//				});
//
//				// 2. delete all menus inside the menu
//				List<Menu> menusByParentMenu = menuRepository.findAllByParentMenu(menu);
//				log.info("Deleting all the menus inside menu with menuId : {}", menu.getMenuId());
//				// delete these menus + items
//				menusByParentMenu.forEach(menuByParentMenu -> {
//					this.delete(menuByParentMenu.getMenuId(), principal);
//				});
//				// 3. then delete menu
//				menuRepository.deleteByUser(user);
//				
//			});
//		}
//
//	}

}
