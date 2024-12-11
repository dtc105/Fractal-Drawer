package com.dtc;

import org.scilab.forge.jlatexmath.TeXFormula;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/evaluate")
@CrossOrigin(origins = "http://localhost:5173")
public class Controller {

    @PostMapping
    public String evaluateLatex(@RequestBody LatexRequest req) {
        try {
            TeXFormula formula = new TeXFormula(req.getLatex());
            return "Received: " + formula;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}