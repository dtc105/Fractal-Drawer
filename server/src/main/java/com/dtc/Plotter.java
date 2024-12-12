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

    public void draw(int maxIterations, double threshold, boolean hasColor, double detail) {
        for (int j = 0; j < height; j += 1/detail) {  // Iterate over every row
            for (int i = 0; i < width; i += 1/detail) {   // Iterate over every cell in a row
                int iterations = getIterations(i, j, maxIterations, threshold);
                int color = getColor(iterations, maxIterations, hasColor);
                drawPixel(i, j, color);
            }
        }
    }

    public void drawOptimized(int maxIterations, double threshold, boolean hasColor) {
        drawOptimizedRecur(0, 0, width - 1, height - 1, maxIterations, threshold, hasColor);
    }

    private void drawOptimizedRecur(int xMin, int yMin, int xMax, int yMax, int maxIterations, double threshold, boolean hasColor) {
        boolean res = checkBoundary(xMin, yMin, xMax, yMax, maxIterations, threshold, hasColor);

        if (xMax <= xMin || yMax <= yMin) {
            return;
        }

        if (res) {
            fillBoundary(xMin, yMin, xMax, yMax);
            return;
        }

        int xMid = (xMax + xMin) / 2;
        int yMid = (yMax + yMin) / 2;

        if (xMin < xMid) {
            if (yMin < yMid) {
                drawOptimizedRecur(xMin, yMin, xMid, yMid, maxIterations, threshold, hasColor);                 // Bottom left
            }
            if (yMid < yMax) {
                drawOptimizedRecur(xMin, yMid + 1, xMid, yMax, maxIterations, threshold, hasColor);                 // Top left
            }
        }
        if (xMid < xMax) {
            if (yMin < yMid) {
                drawOptimizedRecur(xMid + 1, yMin, xMax, yMid,  maxIterations, threshold, hasColor);   // Bottom right
            }
            if (yMid < yMax) {
                drawOptimizedRecur(xMid + 1, yMid + 1, xMax, yMax, maxIterations, threshold, hasColor);             // Top right
            }
        }
    }

    private boolean checkBoundary(int xMin, int yMin, int xMax, int yMax, int maxIterations, double threshold, boolean hasColor) {
        int checkWidth = xMax - xMin;
        int checkHeight = yMax - yMin;

        boolean result = true;

        // Check top side
        for (int i = 0; i < checkWidth - 1; i++) {
            int iterations = getIterations(xMin + i, yMin, maxIterations, threshold);
            int color = getColor(iterations, maxIterations, hasColor);
            drawPixel(xMin + i, yMin, color);

            if (iterations != 0) {
                result = false;
            }
        }

        // Check right side
        for (int i = 0; i < checkHeight - 1; i++) {
            int iterations = getIterations(xMax, yMin + i, maxIterations, threshold);
            int color = getColor(iterations, maxIterations, hasColor);
            drawPixel(xMax, yMin + i, color);

            if (iterations != 0) {
                result = false;
            }
        }

        // Check bottom side
        for (int i = 0; i < checkWidth - 1; i++) {
            int iterations = getIterations(xMin + i, yMax, maxIterations, threshold);
            int color = getColor(iterations, maxIterations, hasColor);
            drawPixel(xMin + i, yMax, color);

            if (iterations != 0) {
                result = false;
            }
        }

        // Check left side
        for (int i = 0; i < checkHeight - 1; i++) {
            int iterations = getIterations(xMin, yMin + i, maxIterations, threshold);
            int color = getColor(iterations, maxIterations, hasColor);
            drawPixel(xMin, yMin + i, color);

            if (iterations != 0) {
                result = false;
            }
        }

        return result;

    }

    private void fillBoundary(int xMin, int yMin, int xMax, int yMax) {
        int fillWidth = xMax - xMin;
        int fillHeight = yMax - yMin;
        int black = getColor(0, 1, false);

        for (int j = 1; j < fillHeight - 1; j++) {
            for (int i = 1; i < fillWidth - 1; i++) {
                drawPixel(xMin + i, yMin + j, black);
            }
        }
    }

    private int getIterations(int i, int j, int maxIterations, double threshold) {
        double x = this.xMin + i * (this.xMax - this.xMin) / this.width;
        double y = this.yMax - j * (this.yMax - this.yMin) / this.height;

        double x0 = x;
        double y0 = y;

        for (int iter = 1; iter < maxIterations; iter++) {  // Repeat until escape threshold is broken

            if (x * x + y * y > threshold * threshold) {
                return iter;
            }    
            
            Complex next = evaluator.evaluateComplexExpression(x, y, x0, y0);
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
        try {
            this.canvas.setRGB(x, y, color);
        } catch (Exception e) {
            System.err.println("Attempted to draw out of bounds: (" + x + ", " + y + ") in range (0,0) -> (" + this.width + ", " + this.height + ")");
        }
    }

    private String getTimeString() {
        LocalDateTime time = LocalDateTime.now();
        return time.getYear() + "-" +  time.getMonthValue() + "-" + time.getDayOfMonth() + " " + time.getHour() + "-" + time.getMinute() + "-" + time.getSecond();
    }
}
