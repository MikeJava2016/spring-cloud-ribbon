package com.sunshine.controller.qualifier;

import com.sunshine.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public List<User> getUsers() {
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
