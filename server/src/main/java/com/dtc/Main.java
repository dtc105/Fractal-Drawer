package com.dtc;

public class Main {
    public static void main(String[] args) {
        Plotter plotter = new Plotter(25600, 14400, -0.8605 - 0.0024265, 0.237 - 0.0027298125, -0.8605 + 0.0024265, 0.237 + 0.0027298125);

        plotter.startTimer();

        plotter.Mandelbrot(100000, true);

        plotter.checkpoint();

        plotter.export();

        plotter.stopTimer();
    }
}