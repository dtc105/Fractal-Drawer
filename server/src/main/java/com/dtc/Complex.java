public class Complex {
    private double real;
    private double imag;

    public Complex(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }

    public double Real() {
        return this.real;
    }

    public double Imag() {
        return this.imag;
    }

    public Complex add(Complex otherComplex) {
        double newReal = this.real + otherComplex.real;
        double newImag = this.imag + otherComplex.imag;
        return new Complex(newReal, newImag);
    }

    public Complex multiply(Complex otherComplex) {
        double newReal = this.real * otherComplex.real - this.imag * otherComplex.imag;
        double newImag = this.real * otherComplex.imag + this.imag * otherComplex.real;
        return new Complex(newReal, newImag);
    }

    public double magnitude() {
        return Math.sqrt(this.real * this.real + this.imag * this.imag);
    }

    public double sqMag() {
        return this.real * this.real + this.imag * this.imag;
    }

    public double arg() {
        return Math.atan2(this.imag, this.real);
    }

    public Complex conjugate() {
        return new Complex(this.real, -1 * this.imag);
    }

    public Complex pow(int n) {
        double newReal = this.real;
        double newImag = this.imag;

        for (int i = 0; i < n; i++) {
            double tempReal = newReal * newReal - newImag * newImag;
            double tempImag = 2 * newReal * newImag;

            newReal = tempReal;
            newImag = tempImag;
        }

        return new Complex(newReal, newImag);
    }

    public Complex pow(double p) {
        double mag = this.magnitude();
        double arg = this.arg();
        double newReal = mag * Math.cos(p * arg);
        double newImag = mag * Math.sin(p * arg);

        return new Complex(newReal, newImag);
    }

    @Override
    public String toString() {
        if (this.imag < 0) {
            return this.real + " - " + (-1 * this.imag) + "i";
        }
        return this.real + " + " + (-1 * this.imag) + "i";
    }
}
