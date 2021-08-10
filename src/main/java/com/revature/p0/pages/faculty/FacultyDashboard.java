package com.revature.p0.pages.faculty;

import com.revature.p0.models.PageIDList;
import com.revature.p0.pages.Page;
import com.revature.p0.util.ConsoleReaderUtil;
import com.revature.p0.util.PageNavUtil;
import com.revature.p0.util.UserState;
import com.revature.p0.util.service.UserService;

public class FacultyDashboard extends Page {

    private static FacultyDashboard instance = null;

    private FacultyDashboard() {
        super(PageIDList.facultyDashboardID);
    }

    public static FacultyDashboard getInstance() {
        if(instance == null) instance = new FacultyDashboard();
        return instance;
    }

    @Override
    public void loadPage() {
        UserService userService = new UserService();
        PageNavUtil pageNavUtil = PageNavUtil.getInstance();
        ConsoleReaderUtil consoleReaderUtil = ConsoleReaderUtil.getInstance();

        System.out.println("\nFaculty Dashboard working...");

        pageNavUtil.mountPage(PageIDList.landPageID);
        UserState.getInstance().logout();
    }
}
