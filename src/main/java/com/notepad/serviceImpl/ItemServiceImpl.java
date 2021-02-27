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
	 * to save a new item
	 *
	 * @param itemDTO : the itemDTO to create.
	 * @return the persisted entity.
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
				User user = userRepository.findByUserName(principal.getName());
				if (user != null) {
					// set user to item
					itemDTO.setuId(user.getUserId());
					itemDTO.setUserName(user.getUserName());
				}

				if (itemDTO.getpId() == null && itemDTO.getContent() == null) {
					throw new BadRequestAlertException("Content and parentId must not be null", "Item", null);
				}
				
				Item newItem = itemMapper.toEntity(itemDTO);
				if(newItem.getMenu().getMenuId()==null){
					newItem.setMenu(null);
				}
				
				itemToUpdateList.add(newItem);
				
//				Item item = itemRepository.save(newItem);
//				newItems.add(itemMapper.toDTO(item));
				
			} else {
				// to update item
				
				Optional<Item> itemToUpdate = itemRepository.findById(itemDTO.getItemId());
				
				itemToUpdate.ifPresent(existingItem -> {
					if (itemDTO.getContent() != null) {
						existingItem.setContent(itemDTO.getContent());
					}
					
					// to update parent menu id
					//null checks and pid check as it can be null for root item
					if (null != itemDTO.getpId()) {
						
						//menu id is not equal to pid or menu id is null
						if((null != existingItem.getMenu() && (Objects.isNull(existingItem.getMenu().getMenuId()) || existingItem.getMenu().getMenuId() != itemDTO.getpId())) 
								|| Objects.isNull(existingItem.getMenu())) {
						
							// get MenuBy menuDTO.getpId()
							
							Optional<Menu> menuOp = menuRepository.findById(itemDTO.getpId());
							if (menuOp.isPresent()) {
								// set this menu to existing item
								existingItem.setMenu(menuOp.get());
							}
						}
						
				
					}
					else {
						existingItem.setMenu(null);
					}
					
					existingItem.setOrderId(itemDTO.getOrderId());
				});
				
				itemToUpdateList.add(itemToUpdate.get());
			}
		}

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
		itemRepository.deleteById(itemId);
	}

	/**
	 * Get all the items by menu.
	 *
	 * @param menuId id of menu to which item(s) belong(s)!
	 * @return the list of entities.
	 */
	@Override
	public List<ItemDTO> findAllByMenu(Long menuId) {
		log.info("Request to find all items with menuId : {} ", menuId);
		List<ItemDTO> itemDTOs = new ArrayList<>();
		menuRepository.findById(menuId).ifPresent(menu -> {
			List<Item> items = itemRepository.findAllByMenuOrderByOrderId(menu);
			if (!items.isEmpty()) {
				items.forEach(item -> {
					itemDTOs.add(itemMapper.toDTO(item));
				});
			}
		});

		return itemDTOs;
	}
	@Override
	public List<ItemDTO> findAllByUserId(Principal principal) {
		log.info("Request to find all items with userId : {} ");
		List<ItemDTO> itemDTOs = new ArrayList<>();
		User user = userRepository.findByUserName(principal.getName());
		if (user != null) {
			userRepository.findById(user.getUserId()).ifPresent(res -> {
				List<Item> items = itemRepository.findAllByUserAndMenu(res,null);
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
