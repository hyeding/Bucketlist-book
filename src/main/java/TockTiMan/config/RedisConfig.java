package TockTiMan.config;

import TockTiMan.domain.Member;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableCaching
public class RedisConfig {
    @Bean
    RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory("hostname", 6379);
    }

    @Bean
    RedisTemplate<String, Member> redisTemplate(){
        RedisTemplate<String, Member> redisTemplate =   new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        return redisTemplate;
    }
}