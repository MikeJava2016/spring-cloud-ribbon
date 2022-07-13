package com.sunshine.controller.qualifier;

import com.sunshine.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RequestMapping("/qualifier")
@RestController
public class QualifierController {

    /**
     * 所有带有Qualifier标签的User
     */
    @Autowired
    @Qualifier
    private List<User> userList;

    /**
     * http://localhost:8081/qualifier/users
     * @return
     */
    @RequestMapping("users")
    public List<User> getUsers(HttpServletRequest request, HttpServletResponse response) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);

        // 释放http连接，转为异步
        AsyncContext context = request.startAsync();
        // 4秒才超时了，超时也会中断当前请求直接返回
        context.setTimeout(4000L);

        // 异步处理，等待3秒后执行
        executorService.schedule(()->{
            PrintWriter writer = null;
            try {
                writer = context.getResponse().getWriter();
            } catch (IOException e) {
                e.printStackTrace();
            }
            writer.print("111111111111");
            // TODO 异步完成，返回客户端信息
            context.complete();

        },3, TimeUnit.SECONDS);

        return userList;
    }

    /**
     * 所有的User
     */
    @Autowired
    private List<User> userList2;

    @RequestMapping("users2")
    public List<User> getUsers2() {
        return userList2;
    }

}
