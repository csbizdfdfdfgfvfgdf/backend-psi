package com.notepad.repository;

import java.util.List;

import com.notepad.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notepad.entity.Item;
import com.notepad.entity.Menu;

/**
* The ItemRepository that extends JpaRepository
*  to define custom operations note
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

	List<Item> findAllByMenu(Menu menu);
	
	List<Item> findAllByMenuOrderByOrderId(Menu menu);
	
	List<Item> findAllByUserAndMenu(User user,Menu menu);
}
