package com.sunshine.mockito.lesson1;

import javax.servlet.http.HttpServletRequest;

/**
 * @version v1
 * @Description TODO
 * @Author huzhanglin
 * @Date 2022/7/17 上午 10:00
 **/
public class AccountLoginController {
    private final AccountDao accountDao;

    public AccountLoginController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public String login(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            Account account = accountDao.findAccount(username, password);
            if (null == account) {
                return "/login";
            } else {
                return "/index";
            }
        } catch (Exception e) {
            return "/505";
        }

    }
}
