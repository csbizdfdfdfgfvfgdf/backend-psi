package com.notepad.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.notepad.dto.ItemDTO;
import com.notepad.entity.Item;

@Mapper(componentModel = "spring", uses = {UserMapper.class, MenuMapper.class})
public interface ItemMapper {

	@Mapping(source = "user.userId", target = "uId")
	@Mapping(source = "user.userName", target = "userName")
	@Mapping(source = "menu.menuId", target = "pId")
	ItemDTO toDTO(Item item);	

	@Mapping(source = "uId", target = "user.userId")
	@Mapping(source = "userName", target = "user.userName")
	@Mapping(source = "pId", target = "menu.menuId")
	Item toEntity(ItemDTO itemDTO);
	
}
