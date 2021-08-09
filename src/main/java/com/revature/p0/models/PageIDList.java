package com.revature.p0.models;

import java.util.ArrayList;

/**
 * The PageIDList class is a list container for the page IDs in the application.
 */
public class PageIDList {
    public static final String landPageID = "com.revature.p0.pages.LandPage";
    public static final String loginPageID = "com.revature.p0.pages.LoginPage";
    public static final String registerPageID = "com.revature.p0.pages.RegisterPage";
    public static final String studentDashboardID = "com.revature.p0.pages.student.StudentDashboard";

    public static ArrayList<String> pageIDList() {
        ArrayList<String> pageIDList = new ArrayList<String>();

        pageIDList.add(landPageID);
        pageIDList.add(loginPageID);
        pageIDList.add(registerPageID);
        pageIDList.add(studentDashboardID);

        return pageIDList;
    }
}
