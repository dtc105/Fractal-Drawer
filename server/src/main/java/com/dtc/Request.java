package com.dtc;

import java.util.Map;

public class Request {
    private String latex;
    private Map<String, Double> limits;


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
}
