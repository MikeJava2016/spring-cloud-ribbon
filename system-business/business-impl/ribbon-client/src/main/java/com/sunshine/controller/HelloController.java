package com.sunshine.controller;

import com.sunshine.annotation.SunShine;
import com.sunshine.common.util.Result;
import com.sunshine.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestMapping("/hello")
@RestController
public class HelloController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * http://localhost:8080/hello/hello
     * @return
     */
    @GetMapping("/hello")
    @SunShine(supported = false)
    public String hello() {
        logger.info("HelloController hello");
        return "hello";
    }

    // url:http://localhost:8085/user/1
    @GetMapping("/getOne")
    public Result<User> getOne(@RequestParam("id") Long id) {
        logger.info(" id = {}", id);
        return Result.success(new User(id));
    }

    @PostMapping("/post")
    public Result<User> post(@RequestBody User user) {
        user.setId(2L);
        logger.info(" user = {}", user);
        return Result.success(user);
    }

    @PutMapping("/put")
    public Result<User> put(@RequestBody User user) {
        logger.info("put user = {}", user);
        return Result.success(user);
    }

    @DeleteMapping("/{id}")
    public Result<User> delete(@PathVariable("id") Long id) {
        logger.info("delete id = {}", id);
        return Result.success(new User(id));
    }


}
