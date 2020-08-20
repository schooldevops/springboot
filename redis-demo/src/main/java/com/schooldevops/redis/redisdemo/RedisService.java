package com.schooldevops.redis.redisdemo;

import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class RedisService {

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOperations;

    @Resource(name = "redisTemplate")
    private SetOperations<String, String> setOperations;

    @Resource(name = "redisTemplate")
    private ZSetOperations<String, String> zSetOperations;

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> hashOperations;

    @Resource(name = "redisTemplate")
    private ListOperations<String, String> listOperations;

    public String saveString(String key, String value) {
        valueOperations.set(key, value);
        return key;
    }

    public String getStringByKey(String key) {
        return valueOperations.get(key);
    }

    public String saveSet(String key, String value) {
        setOperations.add("key:" + key, value);
        return key;
    }

    public Set<String> getSetItems(String key) {
        Set<String> members = setOperations.members("key:" + key);
        return members;
    }

    public String saveHash(String hashKey, String hashValue) {
        hashOperations.put("hash:key", hashKey, hashValue);
        return hashKey;
    }

    public Map<String, String> getHash() {
        return hashOperations.entries("hash:key");
    }

    public String saveZSet(String key, String value, Integer score) {
        zSetOperations.add("zsetKey:" + key, value, score);
        return key;
    }

    public Set<String> getZSet(String key) {
        return zSetOperations.rangeByScore("zsetKey:" + key, 0, 5);
    }


}
