package com.notepad.mapper;

import com.notepad.dto.ItemDTO;
import com.notepad.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
* The ItemMapper that uses mapstruct library to convert
* itemDTO to itemEntity and vice versa
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
@Mapper(componentModel = "spring", uses = {UserMapper.class, MenuMapper.class})
public interface ItemMapper {

	// mapping the fields to convert item entity to itemDTO
	
	@Mapping(source = "user.userId", target = "uId")
	@Mapping(source = "user.userName", target = "userName")
	@Mapping(source = "menu.menuId", target = "pId")
	ItemDTO toDTO(Item item);	
	

	// mapping the fields to convert itemDTO to item entity

	@Mapping(source = "uId", target = "user.userId")
	@Mapping(source = "userName", target = "user.userName")
	@Mapping(source = "pId", target = "menu.menuId")
	Item toEntity(ItemDTO itemDTO);
	
}
