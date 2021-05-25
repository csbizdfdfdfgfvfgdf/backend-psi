package com.notepad.config;

import com.notepad.redis.model.RedisUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
* The RedisConfig class sets the connection factory and
* redis template to send and receive message using redis.
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
@Configuration
public class RedisConfig {
	
	/**
     * Create a bean for connectionFactory for redis
     *
     */
	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		return new JedisConnectionFactory();
	}

	/**
     * Create a bean that links the connection factory 
     * with the redisTemplate
     *
     */
	@Bean
	RedisTemplate<String, RedisUser> redisTemplate() {
		RedisTemplate<String, RedisUser> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());
		return redisTemplate;
 	}


}
