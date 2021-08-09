package com.revature.p0.pages;

import com.revature.p0.dao.MongodUserDAO;
import com.revature.p0.models.PageIDList;
import com.revature.p0.models.User;
import com.revature.p0.util.AppState;
import com.revature.p0.util.ConsoleReaderUtil;
import com.revature.p0.util.PageNavUtil;
import com.revature.p0.util.service.UserService;

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
        PageNavUtil pageNavUtil = PageNavUtil.getInstance();
        UserService userService = new UserService();

        System.out.print("\nWould you like to register a new user?" +
                "\n1) register" +
                "\n2) back" +
                "\n3) exit" +
                "\n > ");

        int selection = consoleReaderUtil.getIntOption();

        switch(selection) {
            case 1:
                break;
            case 2:
                pageNavUtil.goBack();
                return;
            case 3:
                AppState.sendExitSignal();
                return;
            default:
                System.out.println("Selection not valid!");
                return;
        }

        /* Get registration information from user TODO: implement business logic */
        String firstName, lastName, email, username, password;

        // TODO: I want these to check for validity on each input field, and to loop them if needed (w/ options to continue or not)
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
        userService.register(newUser);

        pageNavUtil.mountPage(PageIDList.landPageID);

    }

}
