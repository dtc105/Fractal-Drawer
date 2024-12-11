import { useState } from "react";
import { addStyles, EditableMathField } from "react-mathquill";
import Plane from "../components/Plane";

function Home() {
    
    addStyles();

    const [expression, setExpression] = useState("");

    function handleSubmit() {
        console.log(expression);
    }
    
    return (
        <div className="">
            <form action="" className="w-full flex gap-4">
                <fieldset className="w-1/2">
                    <EditableMathField
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
            <Plane />
        </div>
    );
}

export default Home;