
function Header() {
    
    
    return (
        <header className="text-xl p-4 border-b border-slate-400">
            <nav className="flex gap-8 justify-start items-end">
                <a href="/" className="text-2xl">Fractal Generator</a>
                <a href="/">Home</a>
                <a href="/about">About</a>
            </nav>
        </header>
    );
}

export default Header;