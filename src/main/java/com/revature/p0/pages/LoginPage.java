package com.revature.p0.pages;

import com.revature.p0.dao.MongodUserDAO;
import com.revature.p0.models.PageIDList;
import com.revature.p0.util.AppState;
import com.revature.p0.util.ConsoleReaderUtil;
import com.revature.p0.util.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * The LoginPage class provides a single instance, singleton, user login service.
 */
public class LoginPage extends Page{

    private static LoginPage loginPageInstance = null;

    private LoginPage() { super(PageIDList.loginPageID); }

    public static LoginPage getInstance() {
        if(loginPageInstance == null) {
            loginPageInstance = new LoginPage();
        }
        return loginPageInstance;
    }

    @Override
    public void loadPage() {
        UserService userService = new UserService();
        ConsoleReaderUtil consoleReaderUtil = ConsoleReaderUtil.getInstance();

        System.out.print("Enter Username: ");
        String username = consoleReaderUtil.getLine();
        System.out.print("Enter password: ");
        String password = consoleReaderUtil.getLine();

        System.out.println("Username: " + username +
                "\nPassword: " + password);

        userService.login(username, password);

        AppState.sendExitSignal();

    }

}
