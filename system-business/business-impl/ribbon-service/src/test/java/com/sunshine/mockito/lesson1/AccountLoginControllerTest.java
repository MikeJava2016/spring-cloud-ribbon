/*
package com.sunshine.mockito.lesson1;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class AccountLoginControllerTest {
    private AccountDao accountDao;
    private HttpServletRequest request;
    private AccountLoginController accountLoginController;

    @Before
    public void setup() {
        this.accountDao = Mockito.mock(AccountDao.class);
        this.request = Mockito.mock(HttpServletRequest.class);
        this.accountLoginController = new AccountLoginController(accountDao);
    }

    @Test
    public void testLoginSuccess(){
        Account account = new Account();
        when(request.getParameter("username")).thenReturn("alex");
        when(request.getParameter("password")).thenReturn("123456");
        when(accountDao.findAccount(anyString(),anyString())).thenReturn(account);
        String result = accountLoginController.login(request);
        assertThat(result,equalTo("/index"));
    }

    @Test
    public void testLoginFail(){
        Account account = new Account();
        when(request.getParameter("username")).thenReturn("alex");
        when(request.getParameter("password")).thenReturn("1234561");
        when(accountDao.findAccount(anyString(),anyString())).thenReturn(null);
        String result = accountLoginController.login(request);
        assertThat(result,equalTo("/login"));
    }

    @Test
    public void testLogin505(){
        Account account = new Account();
        when(request.getParameter("username")).thenReturn("alex");
        when(request.getParameter("password")).thenReturn("123456");
        when(accountDao.findAccount(anyString(),anyString())).thenThrow(UnsupportedOperationException.class);
        String result = accountLoginController.login(request);
        assertThat(result,equalTo("/505"));
    }
}*/
