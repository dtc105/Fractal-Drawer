package com.dtc;

import org.nfunk.jep.JEP;
import org.nfunk.jep.type.Complex;

public class Evaluator {
    private String latex;

    public Evaluator(String latex) {
        this.latex = latex;
    }

    public Complex evaluateComplexExpression(double x, double y) {
        // Rewrite z as (x+iy) with x and y filled in
        String expression = this.latex.replace("z", String.format("(%f + %f * i)", x,y));

        // Create a complex valued parser
        JEP parser = new JEP();
        parser.addComplex();

        try {
            // Parse and return result
            parser.parseExpression(expression);
            Complex res = parser.getComplexValue();
            return res;
        } catch (Exception e) {
            // If an issue occurs print the error message and return 0
            System.err.println(e.getMessage());
            return new Complex(0,0);
        }
    }
}
