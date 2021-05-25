package com.notepad.serviceImpl;

import com.notepad.dto.ItemDTO;
import com.notepad.dto.UserDTO;
import com.notepad.entity.enumeration.UserType;
import com.notepad.service.ItemWebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ItemWebSocketServiceImpl implements ItemWebSocketService {

	private final Logger log = LoggerFactory.getLogger(ItemWebSocketServiceImpl.class);

	@Autowired private SimpMessagingTemplate messagingTemplate;

	/**
     * broadcast to all user , it is dummy data to check
	 */
	@Override
	public void sendData(){
		UserDTO userDTO = new UserDTO();
		userDTO.setUserName("hello_world");
		userDTO.setUserType(UserType.REGISTERED);
		messagingTemplate.convertAndSend("/topic/messages",userDTO);
	}

	/**
     * send to specific user
	 * @param itemDto
     * @param type
     * @param userName
	 */
	@Override
	public void sendData(ItemDTO itemDto, String type,String userName) {
		itemDto.setType(type);
		messagingTemplate.convertAndSendToUser(userName,"/queue/items",itemDto);
	}
}