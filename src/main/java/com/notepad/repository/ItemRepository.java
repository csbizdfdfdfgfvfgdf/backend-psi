package com.notepad.repository;

import java.util.List;

import com.notepad.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.notepad.entity.Item;
import com.notepad.entity.Menu;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

	List<Item> findAllByMenu(Menu menu);

//	List<Item> findAllByMenuOrderByOrderId(Menu menu);
	
//	List<Item> findAllByMenuOrderByOrderId(Menu menu);

	List<Item> findAllByMenuOrderByOrderId(Menu menu);

//	List<Item> findAllByUserOrderByOrderId(User user);

	List<Item> findAllByUserAndMenu(User user,Menu menu);

	//List<Item> findAllByMenuId(Long menuId);

}
