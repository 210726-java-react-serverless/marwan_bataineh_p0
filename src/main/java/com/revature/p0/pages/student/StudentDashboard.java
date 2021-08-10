package com.revature.p0.pages.student;

import com.revature.p0.models.PageIDList;
import com.revature.p0.pages.Page;
import com.revature.p0.util.AppState;
import com.revature.p0.util.ConsoleReaderUtil;
import com.revature.p0.util.PageNavUtil;
import com.revature.p0.util.UserState;
import com.revature.p0.util.services.UserService;

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

        System.out.print("\nHello, " + UserState.getInstance().getUser().getFirstName() + "! What would you like to do?" +
                "\n1) search courses / register" +
                "\n2) view my courses / drop" +
                "\n3) logout" +
                "\n4) exit" +
                "\n> ");

        int selection = consoleReaderUtil.getIntOption();

        switch(selection) {
            case 1:
                System.out.println("\nSearch / Register");
                return;
                //break;
            case 2:
                System.out.println("\nView / Drop");
                //System.out.println(userService.getAllCoursesFromList(UserState.getInstance().getUser().getCourses()));
                return;
                //break;
            case 3:
                UserState.getInstance().logout();
                pageNavUtil.portalHome();
                return;
            case 4:
                AppState.sendExitSignal();
                return;
            default:
                System.out.println("\nUnrecognized Input!");
        }

        System.out.println("\nStudent Dashboard working...");
        pageNavUtil.mountPage(PageIDList.landPageID);
        UserState.getInstance().logout();
    }
}
