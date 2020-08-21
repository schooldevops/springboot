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
