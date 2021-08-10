package com.revature.p0.pages;

import com.revature.p0.models.PageIDList;
import com.revature.p0.models.User;
import com.revature.p0.util.AppState;
import com.revature.p0.util.ConsoleReaderUtil;
import com.revature.p0.util.PageNavUtil;
import com.revature.p0.util.services.UserService;

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

        System.out.print("\n[Register Page]" +
                "\n1) continue" +
                "\n2) back" +
                "\n3) exit" +
                "\n> ");

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
        int permissions = 0;

        // TODO: I want these to check for validity on each input field, and to loop them if needed (w/ options to continue or not)
        System.out.println("Fill out your information below.");
        System.out.print("Are you a faculty member?\n" +
                "1) yes\n" +
                "2) no\n" +
                "> ");
        int isFaculty = consoleReaderUtil.getIntOption();
        switch(isFaculty) {
            case 1:
                permissions = 1;
                break;
            case 2:
                break;
            default:
                System.out.println("\nInvalid option!");
                return;
        }

        do {
            System.out.print("First name: ");
            firstName = consoleReaderUtil.getLine();
            if (!userService.isNameValid(firstName)) {
                System.out.println("\nThat is not a valid name.");
                System.out.print("Try again?\n" +
                        "1) re-enter\n" +
                        "2) go back\n" +
                        "> ");
                int choice = consoleReaderUtil.getIntOption();
                switch(choice) {
                    case 1:
                        break;
                    case 2:
                        pageNavUtil.goBack();
                        return;
                    default:
                        System.out.println("\nInvalid option!");
                        break;
                }
            }
        } while(!userService.isNameValid(firstName));

        System.out.print("Last name: ");
        lastName = consoleReaderUtil.getLine();
        if(!userService.isNameValid(lastName)) {
            System.out.println("\nThat is not a valid name.");
            return;
        }

        System.out.print("Email: ");
        email = consoleReaderUtil.getLine();
        if(!userService.isEmailValid(email)) {
            System.out.println("\nThat is not a valid email.");
            return;
        }

        System.out.print("Username: ");
        username = consoleReaderUtil.getLine();
        if(!userService.isUsernameValid(username)) {
            System.out.println("\nThat is not a valid username.");
            return;
        }

        System.out.print("Password: ");
        password = consoleReaderUtil.getLine();
        if(!userService.isPasswordValid(password)) {
            System.out.println("\nThat is not a valid password.");
            return;
        }

        User newUser = new User(permissions, firstName, lastName, email, username, password);
        if(userService.register(newUser) == null) return;

        System.out.println("\nRegistration Successful!");

        pageNavUtil.mountPage(PageIDList.landPageID);

    }

}
