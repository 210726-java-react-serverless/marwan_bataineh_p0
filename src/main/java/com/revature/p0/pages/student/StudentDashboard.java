package com.revature.p0.pages.student;

import com.revature.p0.models.Course;
import com.revature.p0.models.CourseHeader;
import com.revature.p0.models.PageIDList;
import com.revature.p0.pages.Page;
import com.revature.p0.util.AppState;
import com.revature.p0.util.ConsoleReaderUtil;
import com.revature.p0.util.PageNavUtil;
import com.revature.p0.util.UserState;
import com.revature.p0.util.services.UserService;

import java.util.List;

public class StudentDashboard extends Page {

    private static StudentDashboard studentDashboardInstance = null;

    private StudentDashboard() { super(PageIDList.studentDashboardID); }

    public static StudentDashboard getInstance() {
        if(studentDashboardInstance == null) {
            studentDashboardInstance = new StudentDashboard();
        }
        return studentDashboardInstance;
    }

    private UserService userService;
    private PageNavUtil pageNavUtil;
    private ConsoleReaderUtil consoleReaderUtil;

    @Override
    public void loadPage() {
        userService = new UserService();
        pageNavUtil = PageNavUtil.getInstance();
        consoleReaderUtil = ConsoleReaderUtil.getInstance();

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
                searchAndRegister();
                return;
                //break;
            case 2:
                System.out.println("\nView / Drop");
                viewAndDrop();
                //System.out.println(userService.getAllCoursesFromList(UserState.getInstance().getUser().getCourses()));
                return;
                //break;
            case 3:
                userService.logout();
                return;
            case 4:
                AppState.sendExitSignal();
                return;
            default:
                System.out.println("\nUnrecognized Input!");
        }
    }

    private void searchAndRegister() {
        System.out.print("\n\n\n\n" +
                "What would you like to do?\n" +
                "1) view all courses\n" +
                "2) search for courses\n" +
                "3) register for a course\n" +
                "4) logout\n" +
                "5) exit\n" +
                "> ");

        int selection = consoleReaderUtil.getIntOption();

        System.out.println();

        switch(selection) {
            case 1: // View all courses
                List<Course> courseList = userService.getAllCourses();
                for(int i = 0; i < courseList.size(); i++) {
                    System.out.println("[" + i + "]: " + courseList.get(i) + "\n");
                }
                /* User can register from list of courses */
                System.out.print("Would you like to register for a course?\n" +
                        "1) register\n" +
                        "2) go back\n" +
                        "> ");

                int newSelection = consoleReaderUtil.getIntOption();
                System.out.println();

                if(newSelection == 1) {
                    System.out.print("Select a course to register [number]: ");
                    int registerCourse = consoleReaderUtil.getIntOption();
                    if(registerCourse < 0 || registerCourse >= courseList.size()) {
                        System.out.println("\nThat input is not valid/within range.");
                        return;
                    } else if(courseList.get(registerCourse).getSpace() <= 0) {
                        System.out.println("\nThat course is full!");
                        return;
                    }
                    if(userService.updateAddUserCourse(UserState.getInstance().getUser().getUsername(), courseList.get(registerCourse))) {
                        System.out.println("\nCourse registered successfully!");
                    } else {
                        System.out.println("\nFailed to register for course.");
                    }
                    // Just let option 2 fall through here.
                }
                return;
            case 2: // Search Courses
                System.out.print("Search for a course?\n" +
                        "1) search\n" +
                        "2) go back\n" +
                        "> ");

                int newSelection2 = consoleReaderUtil.getIntOption();
                if(newSelection2 == 1) {

                    System.out.print("Enter field (i.e. 'cs'): ");
                    String field = consoleReaderUtil.getLine();
                    if(field == null) {
                        System.out.println("\nInvalid input entered.");
                        return;
                    }
                    if(!userService.isFieldValid(field)) {
                        System.out.println("\nEntered field not recognised!");
                        return;
                    }

                    List<Course> courseListByField = userService.getCoursesByField(field);
                    for(int i = 0; i < courseListByField.size(); i++) {
                        System.out.println("[" + i + "]: " + courseListByField.get(i) + "\n");
                    }

                    /* User can register from list of courses */
                    System.out.print("Would you like to register for a course?\n" +
                            "1) register\n" +
                            "2) go back\n" +
                            "> ");

                    int selectionRegister = consoleReaderUtil.getIntOption();
                    if(selectionRegister == 1) {
                        System.out.print("Select a course to register [number]: ");
                        int registerCourse = consoleReaderUtil.getIntOption();
                        if(registerCourse < 0 || registerCourse >= courseListByField.size()) {
                            System.out.println("\nThat input is not valid/within range.");
                            return;
                        } else if(courseListByField.get(registerCourse).getSpace() <= 0) {
                            System.out.println("\nThat course is full!");
                            return;
                        }
                        if(userService.updateAddUserCourse(UserState.getInstance().getUser().getUsername(), courseListByField.get(registerCourse))) {
                            System.out.println("\nCourse registered successfully!");
                        } else {
                            System.out.println("\nFailed to register for course.");
                        }
                    } else if(selectionRegister == 2) {
                        return;
                    } else {
                        System.out.println("\nInput not recognized.");
                    }
                } else if(newSelection2 == 2) {
                    return;
                } else {
                    System.out.println("\nInput not recognized.");
                    return;
                }
                return;
            case 3: // Register for course (specific)
                register();
                return;
            case 4:
                UserState.getInstance().logout();
                pageNavUtil.portalHome();
                return;
            case 5:
                AppState.sendExitSignal();
                return;
            default:
                System.out.println("\nInput not recognized.");
                return;
        }
    }

    private void register() {
        System.out.print("\n\n\n\n" +
                "Register for a course (fields cannot be blank)\n\n");

        String courseId = null;
        while(courseId == null || courseId.trim().equals("")) {
            System.out.print("courseId: ");
            courseId = consoleReaderUtil.getLine();
            if (courseId != null && !courseId.trim().equals("")) break;
            System.out.println("Invalid input!");
        }

        int section = 0;
        while(section < 1) {
            System.out.print("section: ");
            section = consoleReaderUtil.getIntOption();
            if(section >= 1) break;
            System.out.println("Invalid input!");
        }

        CourseHeader courseHeader = new CourseHeader(courseId, section);

        int selection = -1;
        while(selection < 1 || selection > 2) {
            System.out.println(courseHeader);
            System.out.print("\nConfirm register course\n" +
                    "1) confirm\n" +
                    "2) cancel\n" +
                    "> ");
            selection = consoleReaderUtil.getIntOption();
            if(selection < 1 || selection > 2) System.out.println("\nInvalid input.");
        }

        if(selection == 1) {
            userService.addCourseToStudent(UserState.getInstance().getUser().getUsername(), courseHeader);
        } // Fall through
        return;
    }

    private void viewAndDrop() {
        System.out.print("\n\n\n\n" +
                "My Courses\n\n");

        List<Course> courseList = userService.getAllCoursesFromList(UserState.getInstance().getUser().getCourses());
        for(int i = 0; i < courseList.size(); i++) {
            System.out.println("[" + i + "]: " + courseList.get(i) + "\n");
        }

        System.out.print("Drop a course?\n" +
                "1) drop\n" +
                "2) back\n" +
                "3) logout\n" +
                "4) exit\n" +
                "> ");

        int selection = consoleReaderUtil.getIntOption();

        System.out.println();

        switch(selection) {
            case 1:
                System.out.print("Select a course to drop [number]: ");
                int dropCourse = consoleReaderUtil.getIntOption();
                if(dropCourse < 0 || dropCourse >= courseList.size()) {
                    System.out.println("\nThat input is not valid/within range.");
                    return;
                }
                //TODO userService drop course courseList.get(dropCourse)
                userService.updateDeleteUserCourse(UserState.getInstance().getUser().getUsername(), courseList.get(selection));
            case 2:
                return;
            case 3:
                UserState.getInstance().logout();
                pageNavUtil.portalHome();
                return;
            case 4:
                AppState.sendExitSignal();
                return;
        }

    }
}
