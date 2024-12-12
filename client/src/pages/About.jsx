
function About() {
    
    
    
    return (
        <article className="w-full flex flex-col items-center gap-4">
            <h2 className="m-auto text-xl">Welcome to my fractal generator</h2>
            <section className="w-fit">
                The way this works is you enter in a complex valued equation with variables x, y, z, and c, and the program will iterate the function <code>z<sub>n+1</sub> = f(z<sub>n</sub>)</code>.<br/>
                z is your complex number valued at x + iy,<br/>
                x is the real part of z and y is the imaginary part of z. <br/>
                c is the complex number evaluated where you started at, unlike z it's value remains the same.<br/>
                Try typing <code>z<sup>2</sup>+c</code> to see the <a target="_blank" href="https://en.wikipedia.org/wiki/Mandelbrot_set" className="underline text-blue-800">Mandelbrot set</a>
            </section>
            <section  className="w-fit">
                Code can be found here: <a target="_blank" href="https://github.com/dtc105/Fractal-Drawer" className="underline text-blue-800">https://github.com/dtc105/Fractal-Drawer</a><br/>
                I can be found here: <a target="_blank" href="https://www.linkedin.com/in/dcooper105" className="underline text-blue-800">https://www.linkedin.com/in/dcooper105</a><br/>
            </section>

            <p>Enjoy!</p>
        </article>
    );
}

export default About;