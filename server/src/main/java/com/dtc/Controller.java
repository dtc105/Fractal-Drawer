package com.dtc;

import java.util.Map;

// import org.scilab.forge.jlatexmath.TeXFormula;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/evaluate")
@CrossOrigin(origins = "http://localhost:5173")
public class Controller {

    @PostMapping
    public byte[] evaluateLatex(@RequestBody Request req) {
        try {
            String latex = req.getLatex();
            Map<String, Double> limits = req.getLimits();

            Plotter plotter = new Plotter(latex, 100, 100, limits.get("xMin"), limits.get("yMin"), limits.get("xMax"), limits.get("yMax"));

            plotter.draw(100, 2, true);

            byte[] res = plotter.export();

            return res;
            // return "Received: " + formula;
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return new byte[0];
        }
    }
}