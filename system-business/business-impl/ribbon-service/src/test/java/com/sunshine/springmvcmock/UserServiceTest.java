package com.sunshine.springmvcmock;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/7/18 下午 04:13
 **/

import com.sunshine.entity.User;
import com.sunshine.mapper.UserMapper;
import com.sunshine.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceTest.class);

    /**
     * 需要被測試的對象
     */
    @InjectMocks
    private UserServiceImpl userService;

    /**
     * 依赖的对象
     */
    @Mock
    private UserMapper userMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_selectById() {
        userService = spy(userService);
        when(userMapper.selectById(anyLong())).thenAnswer((Answer<User>) invocation -> {
            Object argument = invocation.getArgument(0);
            Long uid = (Long) argument;
            User user = new User();
            user.setId(uid);
            user.setUsername("username" + uid);
            user.setPassword("pwd" + uid);
            user.setToken("token:" + uid);
            return user;
        });
        doNothing().when(userService).realMethodThrow();
        User user = userService.selectById(3L);
        assertThat(user.getUsername(), equalTo("username3"));
        reset(userMapper);
    }


}
