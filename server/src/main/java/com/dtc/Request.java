package com.dtc;

import java.util.Map;

public class Request {
    private String latex;
    private Map<String, Double> limits;
    private Map<String, Integer> dimensions;
    private double detail;
    private int iterations;

    public String getLatex() {
        return this.latex;
    }

    public void setLatex(String latex) {
        this.latex = latex;
    }

    public Map<String, Double> getLimits() {
        return this.limits;
    }

    public void setLimits(Map<String, Double> limits) {
        this.limits = limits;
    }

    public Map<String, Integer> getDimensions() {
        return this.dimensions;
    }

    public void setDimensions(Map<String, Integer> dimensions) {
        this.dimensions = dimensions;
    }

    public double getDetail() {
        return this.detail;
    }

    public void setDetail(double detail) {
        this.detail = detail;
    }

    public int getIterations() {
        return this.iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }
}
