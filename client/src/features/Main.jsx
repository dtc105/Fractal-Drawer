import {BrowserRouter as Router, Routes, Route} from "react-router-dom";
import Home from "../pages/Home.jsx";
import About from "../pages/About.jsx";
import NotFound from "../pages/NotFound.jsx"

function Main() {
    return (
        <main className="m-4 h-3/4">
            <Router>
                <Routes>
                    <Route path="/" Component={Home} />
                    <Route path="/about" Component={About} />
                    <Route path="*" Component={NotFound}/>
                </Routes>
            </Router>
        </main>
    );
}

export default Main;