# SpringBoot Redis 연동하기. 

Redis 는 다양한 자료구조를 제공하는 대표적인 NoSQL 입니다. 캐시, Queue 등 다양한 활용 용도로 인해서 프로젝트에 활발히 사용하고 있습니다. \
이번 아티클을 통해서 Redis 설정과 간단한 오퍼레이션을 알아보고자 합니다. 

## Project 생성하기. 

https://start.spring.io 에 접속하여 프로젝트를 생성합니다. 

- Web
- Redis
- Lombok 

의존성을 추가합니다. 우리는 이번 프로젝트에서는 gradle 을 이용할 것입니다. 

### Gradle 설정 확인하기. 

build.gradle

```
... 중략 
dependencies {
	compile 'org.springframework.data:spring-data-redis:2.2.6.RELEASE'
	compile group: 'redis.clients', name: 'jedis', version: '3.3.0'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	compileOnly 'org.projectlombok:lombok'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation('org.springframework.boot:spring-boot-starter-test') {
		exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'
	}
}
... 중략 
```

위와 같이 
- spring-data-redis : redis 연동을 위한 spring data 를 사용할 수 있습니다. redisTemplate 등 다양한 모듈을 사용할 수 있습니다. 
- jedis: 대표적인 redis 모듈입니다. spring 과 연동하여 redis 커넥션등을 수행할 수 있습니다. 

## Redis Config 설정하기. 

이제 Redis 에 연동하기 위해서 RedisConfig 파일을 생성하고 다음과 같이 작해 줍니다. 

```
package com.schooldevops.redis.redisdemo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private int port;

    @Bean
    public JedisClientConfiguration jedisClientConfiguration() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(50);
        jedisPoolConfig.setMinIdle(50);
        jedisPoolConfig.setTestWhileIdle(true);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(50000);

        JedisClientConfiguration clientConfiguration = JedisClientConfiguration.builder()
                .usePooling()
                .poolConfig(jedisPoolConfig)
                .build();
        return clientConfiguration;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(config, jedisClientConfiguration());
        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());

        template.setDefaultSerializer(new StringRedisSerializer());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());
        return template;
    }
}
```

설정 파일을 하나하나 알아보겠습니다. 

### Pool 설정

다음과 같이 Spring Cloud 에서 커넥션 풀을 작성했습니다.

JedisClientConfiguration을 통해서 커넥션 풀을 설정할 수 있습니다. 

```
@Bean
public JedisClientConfiguration jedisClientConfiguration() {
    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    jedisPoolConfig.setMaxIdle(50);
    jedisPoolConfig.setMinIdle(50);
    jedisPoolConfig.setTestWhileIdle(true);
    jedisPoolConfig.setMinEvictableIdleTimeMillis(50000);

    JedisClientConfiguration clientConfiguration = JedisClientConfiguration.builder()
            .usePooling()
            .poolConfig(jedisPoolConfig)
            .build();
    return clientConfiguration;
}
```

위 설정은 최대 커넥션 중 idle 상태로 둘 수 있는 개수를 50개 설정했습니다. 
그리고 최소 idle 상태로 둘 수 있는 커넥션 개수도 50개로 잡았습니다. 
idle 상태인경우 커넥션이 유효한지 검사하도록 하고 있습니다. 
그리고 50초 idle 상태로 되어 있는 커넥션들은 정리하도록 설정했습니다. 

상세 정보는 아래 내역을 확인할 수 있습니다 .

- maxActive: 
    - 얼마나 많은 jedis 인스턴스를 pool 에 생성할 수 있는지 지정한다. pool.getResource() 를 통해서 확인가능하다. 
    - 만약 -1로 설정되면 제한이 없다는 의미이다.
    - 만약 maxActive 까지 인스턴스가 차면, 풀은 모두 소진된다. 

- maxIdle: 
    - 얼마나 많은 jedis 인스턴스가 pool 에서 idle 상태로 유지할 수 있는지 설정한다. 

- maxWait: 
    - jedis 인스턴스가 풀을 다 채우고 이미 사용중에 있을때 얼마나 기다릴지 최대 시간을 지정한다. 

- tesOnBorrow: 
    - jedis 인스턴스를 얻어올때 해당 커넥션이 validate 한지 검사한다. validate 한 인스턴스를 반환하면 정상으로 사용할 수 있다. 

- tesOnReturn: 
    - 풀에 반환할때 validate 한지 검사한다. 

- tesWhileIdle: 
    - 만약 true 로 설정이 된다면 idle 한 객체를 스캔하면서 validate 한지 검사한다. 그리고 만약 validate 가 실패한다면, 객체는 풀에서 제거된다. 
    - 이것은 오직 TimeBetweenEvictionRunsMillis 가 0 이상인경우에 의미가 있다. 

- timeBetweenEvictionRunsMillis:
    - idle 객체가 scan 을 수행할 때 evict 시킬 시간을 나타낸다. idle 하게 이 시간동안 존재했다면 이후에 scan 할때 제거된다. 

- numTestsPerEvictionRun: 
    - 매번 evictor 를 수행할 때 idle 객체를 스캔할 때 객체의 개수를 나타낸다. 

- minEvictableIdleTimeMillis: 
    - 객체가 idle 상태로 된 최소 시간을 가리키며, 이는 idle evictor 에 의해서 스캔되는 대상이 된다. 이 아이템은 오직 Time Between Eviction Run Millis 가 0 이상인경우에 동작한다. 

### Redis Connection Factory 설정 

커넥션 팩토리는 Redis 에 커넥션을 맺어 인스턴스를 생성해주는 역할을 합니다. 

```
@Bean
public JedisConnectionFactory jedisConnectionFactory() {
    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
    JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(config, jedisClientConfiguration());
    return jedisConnectionFactory;
}
```

### Redis Template 설정하기. 

우리 예제는 redisTemplate 을 활용하여 오퍼레이션을 수행합니다. 

redisTemplate 을 이용하여 key, value 등을 위한 serialization 을 설정할 수 있습니다. 

```
@Bean
public RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(jedisConnectionFactory());

    template.setDefaultSerializer(new StringRedisSerializer());
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new StringRedisSerializer());
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(new StringRedisSerializer());
    return template;
}
```

RedisTemplate 의 키는 String 으로 설정하고, 값은 Object 로 설정하였습니다. 

그리고 기본 Serializer, KeySerializer, ValueSerializer, HashKeySerializer, HashValueSerializer 모두 StringRedisSerializer 로 설정했습니다. 

이렇게 설정해주면, 저장되는 모든 키와 값은 스트링으로 저장하고, 조회하게 됩니다. 

## Service 작성하기. 

이제 본격적으로 오퍼레이션을 수행해 보겠습니다. 

```
package com.schooldevops.redis.redisdemo;

import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class RedisService {

    //  키/값 오퍼레이션으 수행하기 위한 오퍼레이션을 설정합니다. RedisTemplate 로 부터 가져온다. 
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOperations;

    //  Set 오퍼레이션으로 수행하기 위한 오퍼레이션을 설정합니다. RedisTemplate 로 부터 가져온다. 
    @Resource(name = "redisTemplate")
    private SetOperations<String, String> setOperations;

    //  ZSet 오퍼레이션으로 수행하기 위한 오퍼레이션을 설정합니다. RedisTemplate 로 부터 가져온다.
    //  ZSet 은 Set과 동일하지만, Score 를 저장할 수 있습니다. 
    @Resource(name = "redisTemplate")
    private ZSetOperations<String, String> zSetOperations;

    //  Hash 오퍼레이션으로 수행하기 위한 오퍼레이션을 설정합니다. RedisTemplate 로 부터 가져온다.
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOperations;

    //  List 오퍼레이션을 수행할 수 있습니다. 
    @Resource(name = "redisTemplate")
    private ListOperations<String, String> listOperations;

    //  key:value 오퍼레이션을 수행합니다.
    public String saveString(String key, String value) {
        valueOperations.set(key, value);
        return key;
    }

    public String getStringByKey(String key) {
        return valueOperations.get(key);
    }

    //  Set 오퍼레이션을 수행한다.
    public String saveSet(String key, String value) {
        setOperations.add("key:" + key, value);
        return key;
    }

    public Set<String> getSetItems(String key) {
        Set<String> members = setOperations.members("key:" + key);
        return members;
    }

    //  Hash 오퍼레이션을 수행한다.
    public String saveHash(String hashKey, String hashValue) {
        hashOperations.put("hash:key", hashKey, hashValue);
        return hashKey;
    }

    public Map<String, String> getHash() {
        return hashOperations.entries("hash:key");
    }

    //  ZSet 옾레이션을 수행한다.
    public String saveZSet(String key, String value, Integer score) {
        zSetOperations.add("zsetKey:" + key, value, score);
        return key;
    }

    public Set<String> getZSet(String key) {
        return zSetOperations.rangeByScore("zsetKey:" + key, 0, 5);
    }

    //  List 오퍼레이션을 수행한다.
    public String saveList(String key, String value) {
        listOperations.rightPush("list:" + key, value);
        return key;
    }

    public List<String> getList(String key) {
        return listOperations.range(key, 0, 5);
    }

}

```

- ValueOperations<String, String>: key/value 오퍼레이션을 수행할 수 있도록 합니다. 
- SetOperations<String, String>: key 를 잡고, 해당 키에 대해서 Set 으로 중복없이 아이템을 추가할 수 있습니다. 
- ZSetOperations<String, String>: ZSet 은 Set 과 동일하지만, score 를 각 아이템에 달 수 있으며, 이 스코어를 통해서 조회를 할 수 있습니다. 
- HashOperations<String, String, String>: 해시는 해시를 위한 키와, key:value 의 집합으로 저장할 수 있게 합니다. 
- ListOperations<String, String>: 키에 대해서 리스트로 데이터를 저장합니다. 

위와 같이 서비스를 하나 만들어 보았습니다. 

## 컨트롤러 작성하고 테스트하기. 

```
package com.schooldevops.redis.redisdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class RedisController {

    @Autowired
    RedisService redisService;

    @PostMapping("/string/{key}")
    public String saveString(@PathVariable("key") String key, @RequestParam String value) {
        return redisService.saveString(key, value);
    }

    @GetMapping("/string/{key}")
    public String getString(@PathVariable("key") String key) {
        return redisService.getStringByKey(key);
    }

    @PostMapping("/set/{key}")
    public String saveSet(@PathVariable("key") String key, @RequestParam String value) {
        return redisService.saveSet(key, value);
    }

    @GetMapping("/set/{key}")
    public Set<String> getFromSet(@PathVariable("key") String key) {
        return redisService.getSetItems(key);
    }

    @PostMapping("/hash/{key}")
    public String saveHash(@PathVariable("key") String key, @RequestParam String value) {
        return redisService.saveHash(key, value);
    }

    @GetMapping("/hash")
    public Map<String, String> getFromSet() {
        return redisService.getHash();
    }

    @PostMapping("/zset/{key}")
    public String saveZSet(@PathVariable("key") String key, @RequestParam String value, @RequestParam Integer score) {
        return redisService.saveZSet(key, value, score);
    }

    @GetMapping("/zset/{key}")
    public Set<String> getFromZSet(@PathVariable("key") String key) {
        return redisService.getZSet(key);
    }

    @PostMapping("/list/{key}")
    public String saveList(@PathVariable("key") String key, @RequestParam String value) {
        return redisService.saveList(key, value);
    }

    @GetMapping("/list/{key}")
    public List<String> getList(@PathVariable("key") String key) {
        return redisService.getList(key);
    }
}

```

위 결과와 같이 Redis 오퍼레이션을 수행해보았습니다. 

## properties 확인하기

프로퍼티는 redis 에 접속 호스트와 포트만 지정했습니다. 

```
redis:
  host: localhost
  port: 6377
```

## Redis 실행하기. 

우리는 Redis 는 Docker 로 실행할 것입니다. 

```
docker run --name my-redis -p 6377:6379 -v /Users/baekido/docker/redis/data:/data -d redis
```

와 같이 my-redis 이름으로 컨테이너를 실행하고, 접근하는 포트를 6377 로 접근할 수 있습니다. 


## Wrapup

지금까지 SpringData Redis 연동을 해 보았습니다. 다양한 데이터를 저장하고, 조회하는 정도의 작업을 수행해 보았습니다. 

실무에서는 이것보다 더 복잡한 작업을 수행할 것이지만, 상세한 사용법은 https://github.com/xetorthio/jedis 에서 기본 사용법을 확인해보면 도움이 됩니다. 

https://github.com/xetorthio/jedis/wiki 에서는 좀더 다양한 활용법을 확인할 수 있습니다. 
