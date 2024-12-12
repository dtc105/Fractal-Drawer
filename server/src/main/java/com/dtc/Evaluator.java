package com.dtc;

import org.apache.commons.math3.exception.NullArgumentException;
import org.nfunk.jep.JEP;
import org.nfunk.jep.type.Complex;

public class Evaluator {
    private String latex;
    JEP parser;

    public Evaluator(String latex) {
        this.latex = latex;
        this.parser = new JEP();
        this.parser.addComplex();
        this.parser.addVariable("z", new Complex(0,0));
        this.parser.addVariable("c", new Complex(0,0));
        this.parser.parseExpression(parseLatex(latex));
    }

    private String parseLatex(String latex) {
        return latex
            .replace("\\left(", "(")
            .replace("\\right)", ")")
            .replace("\\cdot", "*")
            .replace("\\^", "**")
            .replaceAll("\\\\frac\\{(.*?)\\}\\{(.*?)\\}", "($1)/($2)")
            .replaceAll("\\\\sqrt\\{(.*?)\\}", "sqrt($1)")
            .replace("\\sin", "sin")
            .replace("\\cos", "cos")
            .replace("\\tan", "tan")
            .replace("\\pi", "pi")
            .replace("\\", "");
    }

    public Complex evaluateComplexExpression(double x, double y, double x0, double y0) {
        try {
            // Parse and return result
            this.parser.setVarValue("z", new Complex(x,y));
            this.parser.setVarValue("c", new Complex(x0,y0));
            return this.parser.getComplexValue();
        } catch (Exception e) {
            // If an issue occurs print the error message and return 0
            System.err.println(e.getMessage());
            return new Complex(0,0);
        }
    }
}
