import { useState } from "react";
import { addStyles, EditableMathField } from "react-mathquill";
import { sendToServer } from "../lib/api.js";
import Plane from "../components/Plane";

function Home() {
    
    addStyles();

    const [expression, setExpression] = useState("");
    const [dimensions, setDimensions] = useState({width: 0, height: 0});
    const [planeLimits, setPlaneLimits] = useState({
		xMin: -1,
		yMin: -1,
		xMax: 1,
		yMax: 1
	});
    const [detail, setDetail] = useState(1);
    const [iterations, setIterations] = useState(100);

    const [response, setResponse] = useState(null);

    async function handleSubmit(e) {
        e.preventDefault();
        const res = await sendToServer(expression, planeLimits, dimensions, detail, iterations);
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
            
            <Plane limits={planeLimits} setLimits={setPlaneLimits} dimensions={dimensions} setDimensions={setDimensions} response={response} />

            <form 
                className="flex flex-col justify-center items-center gap-2"
                onSubmit={(e) => e.preventDefault()}
            >    
                <div className="flex gap-2">
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
                </div>

                <fieldset className="flex justify-end items-center w-full">
                    <div className="flex gap-1 items-center">
                        <label htmlFor="detail">Detail: ({detail})</label>
                        <input 
                            type="range"
                            id="detail"
                            value={detail}
                            onChange={(e) => setDetail(e.target.value)} 
                            step={0.01}
                            min={0.01} 
                            max={1} 
                        />
                    </div>
                    <div className="flex gap-1 items-center">
                        <label htmlFor="iterations">Iterations: ({iterations})</label>
                        <input 
                            type="range"
                            id="iterations"
                            value={iterations}
                            onChange={(e) => setIterations(e.target.value)} 
                            step={1}
                            min={1} 
                            max={10000} 
                        />
                    </div>
                </fieldset>
            </form>
        </div>
    );
}

export default Home;