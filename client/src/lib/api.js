import axios from "axios";

export async function sendToServer(latexExpression, limits) {
    try {
        const res = await axios.post("http://localhost:8080/evaluate", {
            latex: latexExpression,
            limits: limits
        });
        return res;
    } catch (err) {
        console.error("Error communicating with backend: ", err);
    }
}