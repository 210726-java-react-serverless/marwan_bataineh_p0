package com.revature.p0.pages.student;

import com.revature.p0.models.PageIDList;
import com.revature.p0.pages.Page;
import com.revature.p0.util.ConsoleReaderUtil;
import com.revature.p0.util.PageNavUtil;
import com.revature.p0.util.service.UserService;

public class StudentDashboard extends Page {

    private static StudentDashboard studentDashboardInstance = null;

    private StudentDashboard() { super(PageIDList.studentDashboardID); }

    public static StudentDashboard getInstance() {
        if(studentDashboardInstance == null) {
            studentDashboardInstance = new StudentDashboard();
        }
        return studentDashboardInstance;
    }

    @Override
    public void loadPage() {
        UserService userService = new UserService();
        PageNavUtil pageNavUtil = PageNavUtil.getInstance();
        ConsoleReaderUtil consoleReaderUtil = ConsoleReaderUtil.getInstance();

        System.out.println("Student Dashboard working...");
        pageNavUtil.mountPage(PageIDList.landPageID);

    }
}
