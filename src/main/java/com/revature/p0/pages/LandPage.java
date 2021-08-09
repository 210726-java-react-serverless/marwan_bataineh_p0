package com.revature.p0.pages;

import com.revature.p0.models.PageIDList;
import com.revature.p0.util.AppState;
import com.revature.p0.util.ConsoleReaderUtil;
import com.revature.p0.util.PageNavUtil;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * The LandPage class provides the first, or "landing", page for the program which provides basic options such as "login",
 * "register", etc. This is a single instance, singleton, class.
 */
public class LandPage extends Page{

    private static LandPage landPageInstance = null;

    private LandPage() { super(PageIDList.landPageID); }

    public static LandPage getInstance() {
        if(landPageInstance == null) {
            landPageInstance = new LandPage();
        }
        return landPageInstance;
    }

    @Override
    public void loadPage() {
        ConsoleReaderUtil consoleReaderUtil = ConsoleReaderUtil.getInstance();
        BufferedReader consoleReader = consoleReaderUtil.getConsoleReader();
        PageNavUtil pageNavUtil = PageNavUtil.getInstance();

        System.out.println("\nWelcome! Please select an option below.");
        System.out.println("1) login");
        System.out.println("2) register");
        System.out.println("3) exit");

        System.out.print("> ");

        int selection = consoleReaderUtil.getIntOption();


        switch(selection) {
            case 1:
                pageNavUtil.mountPage(PageIDList.loginPageID);
                break;
            case 2:
                pageNavUtil.mountPage(PageIDList.registerPageID);
                break;
            case 3:
                AppState.sendExitSignal();
                break;
            default:
                System.out.println("\nInput not a valid option!\n");
                return; // Reload page
        }

    }
}
