/*
package com.sunshine.mockito.lesson1.lesson2;

import com.sunshine.mockito.lesson1.Account;
import com.sunshine.mockito.lesson1.AccountDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;

*/
/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/7/17 上午 10:36
 **//*

@RunWith(MockitoJUnitRunner.class)
public class MockitoRunnerTest {

    @Test
    public void testMock(){
        AccountDao accountDao = mock(AccountDao.class);
        Account account = accountDao.findAccount("uhzhang", "23");
        System.out.println(account);
    }

    @Test
    public void testMock2(){
        AccountDao accountDao = mock(AccountDao.class, Mockito.RETURNS_SMART_NULLS);
        Account account = accountDao.findAccount("uhzhang", "23");
        System.out.println(account);
    }
}
*/
