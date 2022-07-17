package com.sunshine.lesson2;

import com.sunshine.mockito.lesson1.Account;
import com.sunshine.mockito.lesson1.AccountDao;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.mock;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/7/17 上午 10:58
 **/

public class MockByRuleTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void testMock(){
        AccountDao accountDao = mock(AccountDao.class);
        Account account = accountDao.findAccount("x", "x");
        System.out.println(
                account
        );
    }


    @Mock
    private AccountDao accountDao;

    @Test
    public void testMock2(){

        Account account = accountDao.findAccount("x", "x");
        System.out.println(
                account
        );
    }
}
