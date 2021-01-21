package com.notepad.redis.repoImpl;

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.notepad.entity.User;
import com.notepad.entity.enumeration.UserType;
import com.notepad.error.UsernameAlreadyUsedException;
import com.notepad.redis.model.RedisUser;
import com.notepad.redis.repo.RedisUserRepo;
import com.notepad.repository.UserRepository;

@Repository
public class RedisUserRepositoryImpl implements RedisUserRepo {
	
	private final Logger log = LoggerFactory.getLogger(RedisUserRepositoryImpl.class);

	@SuppressWarnings("unused")
	private RedisTemplate<String, RedisUser> redisTemplate;

	private HashOperations<String, String, RedisUser> hashOperations;
	
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public RedisUserRepositoryImpl(RedisTemplate<String, RedisUser> redisTemplate, UserRepository userRepository) {

		this.redisTemplate = redisTemplate;
		this.userRepository = userRepository;
		hashOperations = redisTemplate.opsForHash();
	}

	/**
     * Get all the users inside redis DB.
     *
     * @return the map of entities.
     */
	@Override
	public Map<String, RedisUser> findAll() {
		log.info("Request to find all user from Redis DB");
		return hashOperations.entries("USER");
	}

	/**
	 * to save a user(visitor) inside redis DB and MySQL-DB.
	 * 
	 * @param redisUser : to save.
	 * 
	 */
	@Override
	public void save(RedisUser redisUser) {
		log.info("Request to save user to Redis DB with user data {} ", redisUser);

		// 1. check if user exists in redis-DB

		RedisUser redisUser1 = this.findByUserName(redisUser.getUserName());
		if (redisUser1 == null) {

			// 2. if not in redis-DB then check if user exists in MySQL-DB
			User user = userRepository.findByUserName(redisUser.getUserName());

			if (user == null) {

				// 3. if not then save USER to MySQL-DB

				User visitor = new User();
				// set username and password same for the firsttime
				visitor.setUserName(redisUser.getUserName());
				visitor.setPassword(passwordEncoder.encode(redisUser.getUserName()));
				visitor.setUserType(UserType.VISITOR);

				userRepository.save(visitor);

				// 4. finally save it to redis-DB
				String uuid = UUID.randomUUID().toString();
				redisUser.setUuid(uuid);
				hashOperations.put("USER", redisUser.getUserName(), redisUser);

			} else {
				throw new UsernameAlreadyUsedException();
			}

		} else {
			throw new UsernameAlreadyUsedException();
		}

	}

	/**
     * find the user from redis-DB by username.
     *
     * @param username : of the entity.
     * @return {@link RedisUser} a user from redis-DB
     */
	@Override
	public RedisUser findByUserName(String username) {
		log.info("Request to find user from Redis DB with username {} ", username);
		return (RedisUser) hashOperations.get("USER", username);
	}

	/**
	 * to update a user(visitor) inside redis DB and MySQL-DB.
	 * 
	 * @param redisUser : to update.
	 * 
	 */
	@Override
	public void update(RedisUser redisUser) {
		log.info("Request to update user to Redis DB with user data {} ", redisUser);
		this.save(redisUser);
	}

	/**
     * Delete the user from redis-DB by username.
     *
     * @param username : of the entity.
     */
	@Override
	public void delete(String username) {
		log.info("Request to delete user from Redis DB with username {} ", username);
		hashOperations.delete("USER", username);
	}

}
