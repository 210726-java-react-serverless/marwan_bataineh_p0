package com.revature.p0.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 */
public class ConsoleReaderUtil {

    private static ConsoleReaderUtil consoleReaderUtil = null;
    private BufferedReader consoleReader = null;

    private ConsoleReaderUtil() {
        consoleReader = new BufferedReader(new InputStreamReader(System.in));
    }

    public static ConsoleReaderUtil getInstance() {
        if (consoleReaderUtil == null) { consoleReaderUtil = new ConsoleReaderUtil(); }
        return consoleReaderUtil;
    }

    public BufferedReader getConsoleReader() { return consoleReader; }

    /**
     * The getLine method provides the BufferedReader 'readLine' functionality while abstracting the exception handling.
     * @return - user input (or null if there was an IOException).
     */
    public String getLine() {
        try {
            return consoleReader.readLine();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void processInput() {

    }

}
