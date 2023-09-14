package org.example;

import java.lang.reflect.*;
import org.example.back.CalculatorServer;
import org.example.front.HttpConnection;
import org.example.front.HttpServer;

import java.lang.reflect.InvocationTargetException;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CalculatorServer calculatorServer = new CalculatorServer();
        HttpServer httpServer = new HttpServer();
        calculatorServer.start();
        httpServer.start();
    }
}