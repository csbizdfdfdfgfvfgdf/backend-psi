package com.notepad.serviceImpl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.notepad.dto.ItemDTO;
import com.notepad.entity.Item;
import com.notepad.entity.Menu;
import com.notepad.entity.User;
import com.notepad.error.BadRequestAlertException;
import com.notepad.mapper.ItemMapper;
import com.notepad.repository.ItemRepository;
import com.notepad.repository.MenuRepository;
import com.notepad.repository.UserRepository;
import com.notepad.service.ItemService;

/**
* The ItemServiceImpl implements ItemService that
* Get, Save, Update or Deletes Notes(Items)
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
@Service
public class ItemServiceImpl implements ItemService {

	private final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private ItemMapper itemMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MenuRepository menuRepository;

	/**
	 * Method to save or update new item/items
	 *
	 * @param List<ItemDTO> : list of items to create or update.
	 * @return the list of saved or updated items.
	 * 
	 */
	@Override
	public List<ItemDTO> save(List<ItemDTO> itemDTOs, Principal principal) {
		log.info("Request to create item : {} ", itemDTOs);

		// newItems to return saved items
		List<ItemDTO> newItems = new ArrayList<ItemDTO>();
		
		// itemToUpdateList to update list of items
		List<Item> itemToUpdateList = new ArrayList<Item>();
		
		for (ItemDTO itemDTO: itemDTOs) {
			
			// to create item
			
			if (itemDTO.getItemId() == null) {
				
				// get logged in user
				User user = userRepository.findByUserName(principal.getName());
				
				if (user != null) {
					// set user to item
					itemDTO.setuId(user.getUserId());
					itemDTO.setUserName(user.getUserName());
				}

				// if note content is empty or parent menu id is not given then throw error
				if (itemDTO.getpId() == null && itemDTO.getContent() == null) {
					throw new BadRequestAlertException("Content and parentId must not be null", "Item", null);
				}
				
				// convert ItemDTO to ItemEntity to save in repo
				Item newItem = itemMapper.toEntity(itemDTO);
				
				// If no menu item is given then set null
				if(newItem.getMenu().getMenuId()==null){
					newItem.setMenu(null);
				}
				
				itemToUpdateList.add(newItem);
				
			} else {
				// to update item
				
				// get item to update by item id
				Optional<Item> itemToUpdate = itemRepository.findById(itemDTO.getItemId());
				
				// if item found then update its content and parent id
				itemToUpdate.ifPresent(existingItem -> {
					
					// if there is content then update it
					if (itemDTO.getContent() != null) {
						existingItem.setContent(itemDTO.getContent());
					}
					
					// to update parent menu id
					//null checks and pid check as it can be null for root item
					if (null != itemDTO.getpId()) {
						
						//menu id is not equal to pid or menu id is null
						if((null != existingItem.getMenu() && (Objects.isNull(existingItem.getMenu().getMenuId()) || existingItem.getMenu().getMenuId() != itemDTO.getpId())) 
								|| Objects.isNull(existingItem.getMenu())) {
						
							// get Menu by menuDTO.getpId()
							Optional<Menu> menuOp = menuRepository.findById(itemDTO.getpId());
							if (menuOp.isPresent()) {
								// set this menu to existing item
								existingItem.setMenu(menuOp.get());
							}
						}
						
				
					}
					else {
						// If item is linked to no menu then set null
						existingItem.setMenu(null);
					}
					
					// update order id of item for sorting
					existingItem.setOrderId(itemDTO.getOrderId());
				});
				
				// add udpated item to list of items
				itemToUpdateList.add(itemToUpdate.get());
			}
		}

		// save or update list of all items
		List<Item> updatedItems = itemRepository.saveAll(itemToUpdateList);
		
		// convert saved items to itemDTOs
		for(Item updatedItem: updatedItems) {
			newItems.add(itemMapper.toDTO(updatedItem));
		}
		
		return newItems;
	}

	/**
	 * Delete the item by id.
	 *
	 * @param itemId the id of the entity.
	 */
	@Override
	public void delete(Long itemId) {
		log.info("Request to delete item with id : {} ", itemId);
		
		// delete item by item id
		itemRepository.deleteById(itemId);
	}

	/**
	 * Get all the items by menu.
	 *
	 * @param menuId id of menu to which item(s) belong(s)!
	 * @return the list of items.
	 */
	@Override
	public List<ItemDTO> findAllByMenu(Long menuId) {
		log.info("Request to find all items with menuId : {} ", menuId);
		
		// itemDTOs list to return all items of a menu
		List<ItemDTO> itemDTOs = new ArrayList<>();
		
		// get menu by menu id
		menuRepository.findById(menuId).ifPresent(menu -> {
			
			// get all items of a menu ordered by order id
			List<Item> items = itemRepository.findAllByMenuOrderByOrderId(menu);
			if (!items.isEmpty()) {
				
				// convert found items entities to ItemDTOs
				items.forEach(item -> {
					itemDTOs.add(itemMapper.toDTO(item));
				});
			}
		});

		return itemDTOs;
	}
	
	/**
	 * Get all the items by user.
	 *
	 * @return the list of items.
	 */
	@Override
	public List<ItemDTO> findAllByUserId(Principal principal) {
		log.info("Request to find all items with userId : {} ");
		
		// itemDTOs list to retuen all items of a user
		List<ItemDTO> itemDTOs = new ArrayList<>();
		
		// get logged in user by name
		User user = userRepository.findByUserName(principal.getName());
		if (user != null) {
			
			// get user by id
			userRepository.findById(user.getUserId()).ifPresent(res -> {
				
				// get all items of a user
				List<Item> items = itemRepository.findAllByUserAndMenu(res,null);
				
				// if found the convert all items entities to ItemDTOs
				if (!items.isEmpty()) {
					items.forEach(item -> {
						itemDTOs.add(itemMapper.toDTO(item));
					});
				}
			});
		}

		return itemDTOs;
	}
}
