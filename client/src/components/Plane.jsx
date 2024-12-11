import { useEffect, useRef, useState } from "react";

function Plane({limits, setLimits, response}) {
	
	const planeRef = useRef();
	const containerRef = useRef();
	const [height, setHeight] = useState(150);
	const [width, setWidth] = useState(150);

	const offsetRef = useRef({x:0, y:0});				// Used for calculating 
	const [offset, setOffset] = useState({x:0, y:0});	// Used for visual
	const [scale, setScale] = useState(1);

	const [isDragging, setIsDragging] = useState(false);
	const [startMousePos, setStartMousePos] = useState({x: 0, y:0});

	function handleMouseDown(e) {
		if (e.button === 0) {
			setIsDragging(true);
			setStartMousePos({x: e.clientX, y: e.clientY});
		}
	}
	
	function handleMouseMove(e) {
		if (isDragging) {
			let dx = 2 * (e.clientX - startMousePos.x) / planeRef.current.getBoundingClientRect().width;
			let dy = 2 * (e.clientY - startMousePos.y) / planeRef.current.getBoundingClientRect().height;

			offsetRef.current = {
				x:dx,
				y:dy
			};

			setOffset({
				x:dx,
				y:dy
			});
		}
	}

	function handleMouseUp(e) {
		if (isDragging) {
			setLimits(prev => ({
				xMin: prev.xMin + offsetRef.current.x,
				yMin: prev.yMin + offsetRef.current.y,
				xMax: prev.xMax + offsetRef.current.x,
				yMax: prev.yMax + offsetRef.current.y
			}));
			setIsDragging(false);
		}
	}

	function handleScroll(e) {
		if (!isDragging) {
			const avgX = (limits.xMin + limits.xMax) / 2;
			const avgY = (limits.yMin + limits.yMax) / 2;

			const distX = limits.xMax - avgX;
			const distY = limits.yMax - avgY;

			const scrollAmount = e.deltaY / 100;
			const scrollFactor = Math.pow(2, scrollAmount / 10);

			setLimits({
				xMin: avgX - distX * scrollFactor,
				yMin: avgY - distY * scrollFactor,
				xMax: avgX + distX * scrollFactor,
				yMax: avgY + distY * scrollFactor,
			})
		}
	}

	useEffect(() => {
		if (planeRef.current && containerRef.current) {
			const factor = (limits.yMax - limits.yMin) / (limits.xMax - limits.xMin);
			const size = Math.min(containerRef.current.getBoundingClientRect().width, containerRef.current.getBoundingClientRect().height);

			if (factor == 1) { //Both x and y have the same height
				setWidth(size);
				setHeight(size)
			} else if (factor > 1) { // X < Y
				setWidth(size/factor);
				setHeight(size);
			} else { // X > Y
				setWidth(size);
				setHeight(size * factor)
			}
		} 
	}, [planeRef, limits]);

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
	}, [isDragging]);

	useEffect(() => {
		offsetRef.current = {x: 0, y:0};
		setOffset({x: 0, y:0})
	}, [limits]);
	
	return (
		<div className="flex flex-col flex-1 w-full h-full" ref={containerRef}>
			<div className="grid lg:grid-cols-[auto_auto_auto] grid-cols-3 w-fit m-auto">
				<span>{`${Math.round(100 * (limits.xMin + offset.x)) / 100}+${Math.round(100 * (limits.yMax + offset.y)) / 100}i`}</span>
				<span className="invisible" style={{width: `${width}px`}}></span>
				<span className="text-right">{`${Math.round(100 * (limits.xMax + offset.x)) / 100}+${Math.round(100 * (limits.yMax + offset.y)) / 100}i`}</span>
			</div>
			<div
				ref={planeRef}
				className="border border-slate-300 select-none overflow-auto w-full m-auto"
				style={{
					width: `${width}px`,
					height: `${height}px`,
				}}
				onMouseDown={handleMouseDown}
				onWheel={handleScroll}
			>

			</div>
			<div className="grid lg:grid-cols-[auto_auto_auto] grid-cols-3 w-fit m-auto">
				<span>{`${Math.round(100 * (limits.xMin + offset.x)) / 100}+${Math.round(100 * (limits.yMin + offset.y)) / 100}i`}</span>
				<span className="invisible" style={{width: `${width}px`}}></span>
				<span className="text-right">{`${Math.round(100 * (limits.xMax + offset.x)) / 100}+${Math.round(100 * (limits.yMin + offset.y)) / 100}i`}</span>
			</div>
		</div>
	);
}

export default Plane;