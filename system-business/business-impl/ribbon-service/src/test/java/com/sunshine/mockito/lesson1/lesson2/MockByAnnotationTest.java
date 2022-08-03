/*
package com.sunshine.mockito.lesson1.lesson2;

import com.sunshine.mockito.lesson1.Account;
import com.sunshine.mockito.lesson1.AccountDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

*/
/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/7/17 上午 10:54
 **//*

public class MockByAnnotationTest {
    @Mock(answer = Answers.RETURNS_SMART_NULLS)
    private AccountDao accountDao;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMock(){
        Account account = accountDao.findAccount("x", "x");
        System.out.println(account);
    }
}
*/
