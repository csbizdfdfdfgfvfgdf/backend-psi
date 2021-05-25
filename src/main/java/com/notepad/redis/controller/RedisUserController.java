package com.notepad.redis.controller;

import com.notepad.redis.model.RedisUser;
import com.notepad.redis.repo.RedisUserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for managing {@link RedisUser}
 */
@RestController
@CrossOrigin
@RequestMapping("/redis-users")
public class RedisUserController {
	
	private final Logger log = LoggerFactory.getLogger(RedisUserController.class);

	@Autowired
	private RedisUserRepo redisUserRepo;
	
	
	/**
	 * {@code GET  /redis-users} : get all users(visitors) from redis DB.
	 * 
	 * @return the {@link Map} with status {@code 200 (OK)} and all users inside redisDB
	 * 
	 */
	@GetMapping
	public Map<String, RedisUser> getAll() {
		log.info("Rest request to find all users from Redis DB");
        return redisUserRepo.findAll();
    }
	
	/**
	 * {@code GET  /redis-users/{userName}} : get a user(visitor) from redis DB by userName.
	 * 
	 * @param userName : to get user with same userName.
	 * 
	 * @return the {@link RedisUser} with status {@code 200 (OK)} and a user inside redisDB
	 * 
	 */
	@GetMapping("/{userName}")
	public RedisUser getOne(@PathVariable("userName") String userName) {
		log.info("Rest request to find user from Redis DB with userName {} ", userName);
        return redisUserRepo.findByUserName(userName);
    }
	
	/**
	 * {@code POST  /redis-users} : create a user(visitor) inside redis DB.
	 * 
	 * @param redisUser : to create.
	 * 
	 * @return the {@link RedisUser} with status {@code 200 (OK)} and a created user inside redisDB
	 * 
	 */
	@PostMapping
    public RedisUser add(@RequestBody RedisUser redisUser) {
		log.info("Rest request to save user to Redis DB with user data {} ", redisUser);
		redisUserRepo.save(redisUser);
        return redisUserRepo.findByUserName(redisUser.getUserName());
    }

	/**
	 * {@code PUT  /redis-users} : update a user(visitor) inside redis DB.
	 * 
	 * @param redisUser : to update.
	 * 
	 * @return the {@link RedisUser} with status {@code 200 (OK)} and a updated user inside redisDB
	 * 
	 */
    @PutMapping
    public RedisUser update(@RequestBody RedisUser redisUser) {
    	log.info("Rest request to update user to Redis DB with user data {} ", redisUser);
		redisUserRepo.update(redisUser);
        return redisUserRepo.findByUserName(redisUser.getUserName());
    }

    /**
	 * {@code DELETE  /redis-users/{userName}} : delete a user(visitor) from redis DB by userName.
	 * 
	 * @param userName : to delete user with same userName.
	 * 
	 * @return the {@link Map} with status {@code 200 (OK)} and all users inside redisDB
	 * 
	 */
    @DeleteMapping("/{userName}")
    public Map<String, RedisUser> delete(@PathVariable("userName") String userName) {
    	log.info("Rest request to delete user from Redis DB with userName {} ", userName);
    	redisUserRepo.delete(userName);
        return getAll();
    }
	
}
