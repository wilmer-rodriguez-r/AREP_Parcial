package org.example.back;


import org.example.front.HttpConnection;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.*;

public class CalculatorServer extends Thread {

    @Override
    public void run() {
        try {
            main(new String[]{""});
        } catch (IOException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        System.out.println(quicksort(new double[]{2.4, 2.2, 2.3}));
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        communication(serverSocket);
        serverSocket.close();
    }

    public static void communication(ServerSocket serverSocket) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        while (true) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            StringBuilder rawData = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                rawData.append(inputLine).append("\n");
                if (!in.ready()) {
                    break;
                }
            }
            out.println(getResponse(rawData.toString()));
            out.close();
            in.close();
            clientSocket.close();
        }
    }

    public static String getResponse(String rawData) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String path = rawData.split("\n")[0].split(" ")[1];
        String command = path.split("\\?")[1];
        String response = solve(command);
        return getHeaders() + response;
    }

    public static String solve(String command) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        try {
            String[] opAndNum = command.split("=")[1].split("\\(");
            String op = opAndNum[0];
            String[] nums = opAndNum[1].replace(")", "").split(",");
            if (nums.length == 1) {
                Double numDouble = Double.parseDouble(nums[0]);
                return Math.class.getDeclaredMethod(op, double.class).invoke(null, numDouble).toString();
            } else if (nums.length == 2) {
                Double[] numsDouble = new Double[nums.length];
                for (int i = 0; i< nums.length; i++) {
                    numsDouble[i] = Double.parseDouble(nums[i]);
                }
                return Math.class.getDeclaredMethod(op, double.class, double.class).invoke(null, numsDouble[0], numsDouble[1]).toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "No se encontro esa función";
    }
    public static String getHeaders() {
        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: application/json\r\n"
                + "\r\n";
    }

    public static double[] quicksort(double[] nums) {
        quicksortAl(0, nums.length - 1, nums);
        return nums;
    }

    public static void quicksortAl(int init, int end, double[] nums) {
        if (init < end) {
            int p = partition(nums, init, end);
            quicksortAl(init, p-1, nums);
            quicksortAl(p+1, end, nums);
        }
    }

    public static int partition(double[] nums, int init, int end) {
        double pivot = nums[end];
        for (int i = nums.length - 1; i > -1; i--) {
            if (pivot < nums[i]) {
                nums[end] = nums[i];
                nums[i] = pivot;
                return i;
            }
        }
    }
}
