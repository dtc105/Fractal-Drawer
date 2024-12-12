package com.dtc;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.time.LocalDateTime;
import org.nfunk.jep.type.Complex;

public class Plotter {
    private int width, height;
    private double xMin, yMin, xMax, yMax;
    private BufferedImage canvas;
    private Evaluator evaluator;

    private long startTime;
    private long checkpointTime;
    private int checkpointCount;

    public Plotter(String expression, int width, int height, double xMin, double yMin, double xMax, double yMax) {
        this.width = width;
        this.height = height;
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
        this.canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.evaluator = new Evaluator(expression);
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

    public byte[] export() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(canvas, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return new byte[0];
        }
    }

    public void draw(int maxIterations, double threshold, boolean hasColor) {
        for (int j = 0; j < height; j++) {  // Iterate over every row
            for (int i = 0; i < width; i++) {   // Iterate over every cell in a row
                int iterations = getIterations(i, j, maxIterations, threshold);
                int color = getColor(iterations, maxIterations, hasColor);
                drawPixel(i, j, color);
            }
        }
    }

    private int getIterations(int i, int j, int maxIterations, double threshold) {
        double x = this.xMin + i * (this.xMax - this.xMin) / this.width;
        double y = this.yMax - j * (this.yMax - this.yMin) / this.height;

        for (int iter = 1; iter < maxIterations; iter++) {  // Repeat until escape threshold is broken

            if (x * x + y * y > threshold * threshold) {
                return iter;
            }    
            
            Complex next = evaluator.evaluateComplexExpression(x, y);
            x = next.re();
            y = next.im();
        }

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
