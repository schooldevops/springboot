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
