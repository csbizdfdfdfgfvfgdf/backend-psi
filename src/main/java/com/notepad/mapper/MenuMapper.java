package com.notepad.mapper;

import com.notepad.dto.MenuDTO;
import com.notepad.entity.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
* The MenuMapper that uses mapstruct library to convert
* menuDTO to menuEntity and vice versa
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface MenuMapper {
	
	// mapping the fields to convert menu entity to menuDTO
	@Mapping(source = "user.userId", target = "uId")
	@Mapping(source = "user.userName", target = "userName")
	@Mapping(source = "parentMenu.menuId", target = "pId")
	MenuDTO toDTO(Menu menu);

//	@BeforeMapping
//    default void beforeMapping(@MappingTarget Menu target, MenuDTO source) {
//        if (!source.getpId().equals(null)) {
//        	target.setMenuId(source.getpId());
//        	target.setParentMenu(findById(source.getMenuId()).get());
//        }
//    }
	
	// mapping the fields to convert menuDTO to menu entity
	
	@Mapping(source = "uId", target = "user.userId")
	@Mapping(source = "userName", target = "user.userName")
	@Mapping(source = "pId", target = "parentMenu.menuId")
//	@Mapping(target = "parentMenu.menuId", ignore = true)
//	@Mapping(target = "parentMenu", ignore = true)
	Menu toEntity(MenuDTO menuDTO);

}
