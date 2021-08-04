package com.revature.p0.util;

import com.revature.p0.pages.Page;

import java.util.ArrayDeque;
import java.util.HashSet;

/**
 * The PageDriver class provides methods for storing and transitioning between Pages.
 */
public class PageDriver {

    private ArrayDeque<Page> pageHistoryDeque = new ArrayDeque<>(10); // Hold page navigation history (up to 10)
    private HashSet<Page> pageBufferedSet = new HashSet<>(); // Store previously loaded pages here; do not load duplicates

    public void launchPage(String pageID) {

        switch(pageID) {
            case "LandPage":
                break;
            case "LoginPage":
                break;
            case "RegisterPage":
                break;
            default:
                System.out.println("Selection not recognized");
                return;
        }

    }

}