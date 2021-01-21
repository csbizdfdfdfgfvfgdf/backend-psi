package com.notepad.redis.repo;

import java.util.Map;

import com.notepad.redis.model.RedisUser;

public interface RedisUserRepo {

	Map<String, RedisUser> findAll();

	void save(RedisUser redisUser);

	RedisUser findByUserName(String userName);

	void update(RedisUser redisUser);

	void delete(String userName);

}
