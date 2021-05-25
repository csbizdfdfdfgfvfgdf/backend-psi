package com.notepad.repository;

import com.notepad.entity.Menu;
import com.notepad.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* The MenuRepository that extends JpaRepository
*  to define custom operations related to folder
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

	List<Menu> findAllByOrderByMenuName();

	void deleteByMenuId(Long menuId);

	List<Menu> findAllByParentMenu(Menu menu);

	List<Menu> findAllByUser(User user);

	void deleteByUser(User user);

//	@Query("SELECT menu from Menu menu WHERE menu.user.userId = :userId order by :orderId")
//	List<Menu> findAllByUserIdOrderByMenuName(Long userId);

	List<Menu> findAllByUserOrderByOrderId(User user);

	List<Menu> findAllByParentMenuOrderByOrderId(Menu menu);

	List<Menu> findAllByParentMenuAndUserOrderByOrderId(Menu menu, User user);

}
