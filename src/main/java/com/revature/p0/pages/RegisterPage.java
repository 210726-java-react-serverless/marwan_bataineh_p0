package com.revature.p0.pages;

import com.revature.p0.models.PageIDList;
import com.revature.p0.models.User;
import com.revature.p0.util.ConsoleReaderUtil;
import com.revature.p0.util.PageNavUtil;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * The RegisterPage class provides a service to register a new user to the database.
 */
public class RegisterPage extends Page{

    private static RegisterPage registerPageInstance = null;

    private RegisterPage() { super(PageIDList.registerPageID); }

    public static RegisterPage getInstance() {
        if(registerPageInstance == null) {
            registerPageInstance = new RegisterPage();
        }
        return registerPageInstance;
    }

    @Override
    public void loadPage() {
        ConsoleReaderUtil consoleReaderUtil = ConsoleReaderUtil.getInstance();
        BufferedReader consoleReader = consoleReaderUtil.getConsoleReader();
        PageNavUtil pageNavUtil = PageNavUtil.getInstance();


        /* Get registration information from user TODO: implement business logic */
        String firstName, lastName, email, username, password;

        System.out.println("Fill out your information below.");
        System.out.print("First name: ");
        firstName = consoleReaderUtil.getLine();

        System.out.print("Last name: ");
        lastName = consoleReaderUtil.getLine();

        System.out.print("Email: ");
        email = consoleReaderUtil.getLine();

        System.out.print("Username: ");
        username = consoleReaderUtil.getLine();

        System.out.print("Password: ");
        password = consoleReaderUtil.getLine();

        User newUser = new User(firstName, lastName, email, username, password);
        System.out.println(newUser.toString() + "\n");

        pageNavUtil.mountPage(PageIDList.landPageID);

    }

}
