package com.revature.p0.pages;

import com.revature.p0.models.PageIDList;
import com.revature.p0.util.AppState;
import com.revature.p0.util.ConsoleReaderUtil;

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
        ConsoleReaderUtil consoleReaderUtil = ConsoleReaderUtil.getInstance();
        BufferedReader consoleReader = consoleReaderUtil.getConsoleReader();

        try {
            System.out.print("Enter Username: ");
            String username = consoleReader.readLine();
            System.out.print("Enter password: ");
            String password = consoleReader.readLine();

            System.out.println("Username: " + username +
                    "\nPassword: " + password);
        } catch (IOException e) {
            e.printStackTrace();
        }

        AppState.sendExitSignal();

    }

}
