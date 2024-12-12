import axios from "axios";

export async function sendToServer(latexExpression, limits, dimensions, detail, iterations) {
    try {
        const res = await axios.post("http://localhost:8080/evaluate", {
            latex: latexExpression, // string
            limits: limits,         // {xMin: double, yMin: double, xMax: double, yMax: double}
            dimensions: dimensions, // {width: int, height: int}
            detail: detail,         // double
            iterations: iterations  // int
        });
        return res;
    } catch (err) {
        console.error("Error communicating with backend: ", err);
    }
}