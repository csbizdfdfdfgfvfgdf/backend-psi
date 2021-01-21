package com.notepad.mapper;

import org.mapstruct.Mapper;

import com.notepad.dto.UserDTO;
import com.notepad.entity.User;

@Mapper(componentModel = "spring", uses = {})
public interface UserMapper {
	
	UserDTO toDTO(User user);

	User toEntity(UserDTO userDTO);
	
}
