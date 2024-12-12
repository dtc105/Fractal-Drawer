package com.dtc;

import java.util.Base64;
import java.util.Map;

// import org.scilab.forge.jlatexmath.TeXFormula;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/evaluate")
@CrossOrigin(origins = "http://localhost:5173")
public class Controller {

    @PostMapping
    public String evaluateLatex(@RequestBody Request req) {
        System.out.println("Request received: " + req);
        try {
            String latex = req.getLatex();
            Map<String, Double> limits = req.getLimits();
            Map<String, Integer> dimensions = req.getDimensions();
            double detail = req.getDetail(); //* Ignore for now
            int iterations = req.getIterations();

            Plotter plotter = new Plotter(latex, (int) (detail * dimensions.get("width")), (int)(detail * dimensions.get("height")), limits.get("xMin"), limits.get("yMin"), limits.get("xMax"), limits.get("yMax"));

            plotter.startTimer();

            plotter.draw(iterations, 2, true, detail);

            plotter.checkpoint();

            byte[] res = plotter.export();

            plotter.stopTimer();

            return Base64.getEncoder().encodeToString(res);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}