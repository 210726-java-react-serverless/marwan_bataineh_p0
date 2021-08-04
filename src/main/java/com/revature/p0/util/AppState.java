package com.revature.p0.util;

import com.revature.p0.pages.LandPage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The AppState class provides a Singleton instance utility that controls the application state.
 */
public class AppState {

    private static AppState appStateInstance = null; // Stores the single instance (singleton) of AppState
    private boolean appRunning;
    private final String landPageID; // Landing page on launch... Always loaded in application
    private final PageDriver pageDriver;
    private final BufferedReader consoleReader;

    /**
     * The Default Constructor for the AppState class instantiates utilities available to the rest of the application.
     * pageDriver (PageDriver): facilitates page loading/switching
     * consoleReader (BufferedReader <<< System.in): provides ability to process user input
     */
    private AppState() {
        pageDriver = new PageDriver();
        consoleReader = new BufferedReader(new InputStreamReader(System.in));
        landPageID = LandPage.getInstance().getPageID();
    }

    /**
     * The startup method begins, and manages, the core application lifecycle.
     */
    public void startup() {
        appRunning = true;
        pageDriver.launchPage(landPageID);
    }

    /**
     * The stop method cleans up resources following the end of the core application lifecycle.
     */
    public void stop() {
        try {
            consoleReader.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The getInstance method provides the singleton instance of the AppState class either by creating a new instance
     * if one does not already exist, or by providing the existing instance of the AppState class.
     * @return - The singleton instance of the AppState class.
     */
    public static AppState getInstance() {
        if(appStateInstance == null) {
            appStateInstance = new AppState();
        }
        return appStateInstance;
    }

}
