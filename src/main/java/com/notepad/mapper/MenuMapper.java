package com.notepad.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.notepad.dto.MenuDTO;
import com.notepad.entity.Menu;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface MenuMapper {
	
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
	
	@Mapping(source = "uId", target = "user.userId")
	@Mapping(source = "userName", target = "user.userName")
	@Mapping(source = "pId", target = "parentMenu.menuId")
//	@Mapping(target = "parentMenu.menuId", ignore = true)
//	@Mapping(target = "parentMenu", ignore = true)
	Menu toEntity(MenuDTO menuDTO);

}
