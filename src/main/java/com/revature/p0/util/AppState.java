package com.revature.p0.util;

import com.revature.p0.pages.LandPage;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * The AppState class provides an instance utility that controls the application state.
 */
public class AppState {

    private static boolean appRunning;
    private final String landPageID; // Landing page on launch... Always loaded in application
    private final PageNavUtil pageNavUtil;
    private final ConsoleReaderUtil consoleReaderUtil;
    private final BufferedReader consoleReader;
    private final MongoClientFactory mongoClientFactory;

    /**
     * The Default Constructor for the AppState class instantiates utilities available to the rest of the application.
     * pageDriver (PageDriver): facilitates page loading/switching
     * consoleReader (BufferedReader <<< System.in): provides ability to process user input
     */
    public AppState() {
        pageNavUtil = PageNavUtil.getInstance();
        consoleReaderUtil = ConsoleReaderUtil.getInstance();
        consoleReader = consoleReaderUtil.getConsoleReader();
        landPageID = LandPage.getInstance().getPageID();
        mongoClientFactory = MongoClientFactory.getInstance();
    }

    /**
     * The startup method begins, and manages, the core application lifecycle.
     */
    public void startup() {
        appRunning = true;
        pageNavUtil.mountPage(landPageID);

        /* Application Lifecycle lives here... */
        while(appRunning) {
            pageNavUtil.loadPage();
        }

        /* Close/Cleanup Application */
        stop();
    }

    /**
     * The sendExitSignal method provides a static signaling method for the application, so that operations elsewhere
     * can signal to finish up the lifecycle of the application and close out.
     */
    public static void sendExitSignal() {
        appRunning = false;
    }

    /**
     * The stop method cleans up resources following the end of the core application lifecycle.
     */
    public void stop() {
        try {
            consoleReader.close();
            mongoClientFactory.cleanUp();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
