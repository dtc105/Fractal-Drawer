import { useState } from "react";
import { addStyles, EditableMathField } from "react-mathquill";
import { sendToServer } from "../lib/api.js";
import Plane from "../components/Plane";

function Home() {
    
    addStyles();

    const [expression, setExpression] = useState("");
    const [planeLimits, setPlaneLimits] = useState({
		xMin: -1,
		yMin: -1,
		xMax: 1,
		yMax: 1
	});
    const [response, setResponse] = useState(null);

    async function handleSubmit(e) {
        e.preventDefault();
        console.log(expression);
        const res = await sendToServer(expression, planeLimits);
        console.log(res);
        setResponse(res);
    }
    
    return (
        <div className="w-full h-full flex flex-col gap-8 items-center">
            <form className="w-full flex gap-4 justify-center">
                <fieldset className="w-1/2 flex items-center gap-4">
                    <label htmlFor="latex" className="text-lg">Equation</label>
                    <EditableMathField
                        id="latex"
                        latex={expression}
                        onChange={(e) => setExpression(e.latex())}
                        style={{
                            width: "100%",
                            minHeight: "2rem",
                            
                        }}
                    />
                </fieldset>
                <button
                    onClick={handleSubmit}
                    className="bg-blue-300 rounded px-2 py-1"
                >
                    Submit
                </button>
            </form>
            
            <Plane limits={planeLimits} setLimits={setPlaneLimits} response={response} />

            <form 
                className="flex gap-2"
                onSubmit={(e) => e.preventDefault()}>
                <fieldset className="flex flex-col">
                    <div>
                        <label htmlFor="xMin">Min R: </label>
                        <input 
                            id="xMin" 
                            type="text" 
                            value={planeLimits.xMin}
                            onChange={(e) => {setPlaneLimits(prev => ({...prev, xMin: parseFloat(e.target.value || "0")}))}}
                            inputMode="numeric" 
                            pattern="^-?\d*\.?\d+$"
                            className="border rounded"
                        />
                    </div>

                    <div>
                        <label htmlFor="yMin">Min I: </label>
                        <input 
                            id="yMin" 
                            type="text" 
                            value={planeLimits.yMin}
                            onChange={(e) => {setPlaneLimits(prev => ({...prev, yMin: parseFloat(e.target.value || "0")}))}}
                            inputMode="numeric" 
                            pattern="^-?\d*\.?\d+$"
                            className="border rounded"
                        />
                    </div>
                </fieldset>
                

                <fieldset>
                    <div>
                        <label htmlFor="xMax">Max R: </label>
                        <input 
                            id="xMax" 
                            type="text" 
                            value={planeLimits.xMax}
                            onChange={(e) => {setPlaneLimits(prev => ({...prev, xMax: parseFloat(e.target.value || "0")}))}}
                            inputMode="numeric" 
                            pattern="^-?\d*\.?\d+$"
                            className="border rounded"
                        />
                    </div>

                    <div>
                        <label htmlFor="yMax">Max I: </label>
                        <input 
                            id="yMax" 
                            type="text" 
                            value={planeLimits.yMax}
                            onChange={(e) => {setPlaneLimits(prev => ({...prev, yMax: parseFloat(e.target.value || "0")}))}}
                            inputMode="numeric" 
                            pattern="^-?\d*\.?\d+$"
                            className="border rounded"
                        />
                    </div>
                </fieldset>
            </form>
        </div>
    );
}

export default Home;