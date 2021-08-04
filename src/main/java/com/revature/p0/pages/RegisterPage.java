package com.revature.p0.pages;

import com.revature.p0.models.PageIDList;

/**
 * The RegisterPage class provides a service to register a new user to the database.
 */
public class RegisterPage extends Page{

    private static RegisterPage registerPageInstance = null;

    private RegisterPage() { pageID = PageIDList.registerPageID; }

    public static RegisterPage getInstance() {
        if(registerPageInstance == null) {
            registerPageInstance = new RegisterPage();
        }
        return registerPageInstance;
    }

    @Override
    public void loadPage() {



    }

}
