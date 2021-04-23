package com.notepad.redis.repo;

import java.util.Map;

import com.notepad.redis.model.RedisUser;

/**
* The RedisUserRepo to define operations related to redis user
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
public interface RedisUserRepo {

	Map<String, RedisUser> findAll();

	void save(RedisUser redisUser);

	RedisUser findByUserName(String userName);

	void update(RedisUser redisUser);

	void delete(String userName);

}
