package com.notepad.mapper;

import org.mapstruct.Mapper;

import com.notepad.dto.UserDTO;
import com.notepad.entity.User;

/**
* The UserMapper that uses mapstruct library to convert
* userDTO to userEntity and vice versa
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
@Mapper(componentModel = "spring", uses = {})
public interface UserMapper {
	
	UserDTO toDTO(User user);

	User toEntity(UserDTO userDTO);
	
}
