package com.notepad.service;

import com.notepad.dto.ItemDTO;

public interface ItemWebSocketService {
    void sendData();

    void sendData(ItemDTO itemDto,String type,String userName);
}
