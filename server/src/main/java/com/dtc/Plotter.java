package com.dtc;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.time.LocalDateTime;

public class Plotter {
    private int width, height;
    private double xMin, yMin, xMax, yMax;
    public BufferedImage canvas;

    private long startTime;
    private long checkpointTime;
    private int checkpointCount;

    public Plotter(int width, int height, double xMin, double yMin, double xMax, double yMax) {
        this.width = width;
        this.height = height;
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
        this.canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public long startTimer() {
        this.startTime = System.currentTimeMillis();
        this.checkpointTime = this.startTime;
        this.checkpointCount = 1;

        System.out.println("Started timer.");

        return this.startTime;
    }

    public long checkpoint() {
        long elapsedTime = System.currentTimeMillis() - this.checkpointTime;
        float elapsedSeconds = (float) elapsedTime / 1000;

        System.out.println("Checkpoint " + checkpointCount + " completed in " + elapsedSeconds + " seconds.");

        this.checkpointTime = System.currentTimeMillis();
        this.checkpointCount++;

        return elapsedTime;
    }

    public long stopTimer() {
        long elapsedTime = System.currentTimeMillis() - this.startTime;
        float elapsedSeconds = (float) elapsedTime / 1000;

        System.out.println("Completed in " + elapsedSeconds + " seconds.");

        this.startTime = System.currentTimeMillis();
        this.checkpointCount = 1;

        return elapsedTime;
    }

    public void export() {
        String imagesDirectoryPath = "images";
        Path imagesDirectory = Paths.get(imagesDirectoryPath);

        String textFilePath = "images/info.txt";
        Path textFile = Paths.get(textFilePath);

        try {
            Files.createDirectories(imagesDirectory);
        } catch (IOException err) {
            System.err.println(err);
        }

        try {
            ImageIO.write(this.canvas, "png", new File(imagesDirectoryPath + "/Fractal " + getTimeString() + ".png"));

            String info = "Fractal " + getTimeString() + ".png : " + xMin + " + " + yMin + "i -> " + xMax + " + " + yMax + "i";
            Files.write(textFile, info.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException err) {
            System.err.println(err);
        }
    }

    public BufferedImage Mandelbrot(int maxIterations, boolean hasColor) {
        // For each pixel (i,j)
        for (int j = 0; j < this.height; j++) {
            for (int i = 0; i < this.width; i++) {

                // Set x and y to be proportional to the displayed section
                double x = this.xMin + i * (this.xMax - this.xMin) / this.width;
                double y = this.yMin + j * (this.yMax - this.yMin) / this.height;

                // Iterate over the mandelbrot sequence
                int iterations = getMandelbrotIterations(x, y, maxIterations);
                int color = getColor(iterations, maxIterations, hasColor);
                drawPixel(i, this.height - j - 1, color);
            }
        }

        return this.canvas;
    }

    private int getMandelbrotIterations(double x, double y, int maxIterations) {
        double x0 = x;
        double y0 = y;

        for (int k = 1; k <= maxIterations; k++) {

            // If |z| > 2 then draw pixel based on how long it took and go to next
            if (x * x + y * y >= 4) {
                return k;
            }

            // If |z| <= 2 then calculate next value
            double xNext = x * x - y * y + x0;
            double yNext = 2 * x * y + y0;

            x = xNext;
            y = yNext;
        }

        // If |z| <= 2 after maxIterations iterations then draw the pixel as black
        return 0;
    }

    private int getColor(int iterations, int maxIterations, boolean hasColor) {
        // If pixel is in the set
        if (iterations == 0) {
            // Black
            return Color.HSBtoRGB(0, 0, 0);
        }

        // If pixel not in set and there is no color
        if (!hasColor) {
            // White
            return Color.HSBtoRGB(0, 0, 1);
        }

        // If pixel not in set and there is color
        return Color.HSBtoRGB(270 * iterations / (float) maxIterations, 1, 1);
    }

    private void drawPixel(int x, int y, int color) {
        this.canvas.setRGB(x, y, color);
    }

    private String getTimeString() {
        LocalDateTime time = LocalDateTime.now();
        return time.getYear() + "-" +  time.getMonthValue() + "-" + time.getDayOfMonth() + " " + time.getHour() + "-" + time.getMinute() + "-" + time.getSecond();
    }
}
