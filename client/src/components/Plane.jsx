import { useEffect, useRef, useState } from "react";

function Plane() {
	
	const planeRef = useRef();

	const [scale, setScale] = useState(1);
	const [limits, setLimits] = useState({
		xMin: -1,
		yMin: -1,
		xMax: 1,
		yMax: 1
	});

	const [isDragging, setIsDragging] = useState(false);
	const [prevMousePos, setPrevMousePos] = useState({x: 0, y:0});

	function handleMouseDown(e) {
		if (e.button === 0) {
			setIsDragging(true);
			setPrevMousePos({x: e.clientX, y: e.clientY});
		}
	}
	
	function handleMouseMove(e) {
		if (isDragging) {
			let dx = 2 * (prevMousePos.x - e.clientX) / planeRef.current.getBoundingClientRect().width;
			let dy = 2 * (prevMousePos.y - e.clientY) / planeRef.current.getBoundingClientRect().height;

			setLimits(prev => ({
				xMin: prev.xMin + dx,
				yMin: prev.yMin + dy,
				xMax: prev.xMax + dx,
				yMax: prev.yMax + dy,
			}));

			setPrevMousePos({x: e.clientX, y: e.clientY});
		}
	}

	function handleMouseUp(e) {
		console.log("x", prevMousePos.x - e.clientX, "y", prevMousePos.y - e.clientY);
		setIsDragging(false);
	}

	useEffect(() => {
		if (isDragging) {
			document.addEventListener("mousemove", handleMouseMove);
			document.addEventListener("mouseup", handleMouseUp);
		} else {
			document.removeEventListener("mousemove", handleMouseMove);
			document.removeEventListener("mouseup", handleMouseUp);
		}

		return (() => {
			document.removeEventListener("mousemove", handleMouseMove);
			document.removeEventListener("mouseup", handleMouseUp);
		});
	}, [isDragging])
	
	return (
		<div>
			<div className="flex justify-between">
				<span>{`${Math.round(100 * limits.xMin) / 100}+${Math.round(100 * limits.yMax) / 100}i`}</span>
				<span>{`${Math.round(100 * limits.xMax) / 100}+${Math.round(100 * limits.yMax) / 100}i`}</span>
			</div>
			<div
				ref={planeRef}
				className="w-96 h-96 border border-slate-300 select-none"
				onMouseDown={handleMouseDown}
			>
				
			</div>
			<div className="flex justify-between">
			<span>{`${Math.round(100 * limits.xMin) / 100}+${Math.round(100 * limits.yMin) / 100}i`}</span>
			<span>{`${Math.round(100 * limits.xMax) / 100}+${Math.round(100 * limits.yMin) / 100}i`}</span>
			</div>
		</div>
	);
}

export default Plane;