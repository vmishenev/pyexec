package com.company;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
        System.out.println("Input  Python executable:");
        try {
            runExec(System.out, System.in);
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void runExec(PrintStream out, InputStream input) throws IOException, ExecutionException, InterruptedException {
        Scanner scanner = new Scanner(input);
        String path = scanner.nextLine();
        // https://docs.python.org/3/library/timeit.html#timeit-command-line-interface
        ProcessBuilder procBuilder = new ProcessBuilder(path, "-m", "timeit", "-r", "10");
        Process process = procBuilder.start();

        InputStreamReader isrStdout = new InputStreamReader( process.getInputStream() );
        BufferedReader brStdout = new BufferedReader(isrStdout);

        long startTime = System.currentTimeMillis();
        List<String> outputProcess = new LinkedList<>();

        ExecutorService pool = Executors.newCachedThreadPool();
        Runnable reader = () -> {
            String line = null;
            while (true) {
                try {
                    if (!((line = brStdout.readLine()) != null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                outputProcess.add(line);
            }
        };
        pool.submit(reader);

        Runnable waiter = () -> {
            while (true)
                try {
                    if (process.waitFor(1, TimeUnit.SECONDS)) {
                        out.print(String.join("\n", outputProcess));
                        out.println("Process exited with: " + process.exitValue());
                        break;
                    } else {
                        out.println("Current time: "+  (System.currentTimeMillis() - startTime)/1000 );
                    }
                } catch (InterruptedException ex) {
                }
        };
        pool.submit(waiter).get();
        pool.shutdown();
    }
}
