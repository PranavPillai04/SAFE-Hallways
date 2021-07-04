import java.util.Arrays;
import java.util.Objects;

public class Vector {
    private final int ndimensions = 2;
    private double[] data;

    public Vector() {
        this.data = new double[ndimensions];
    }

    public Vector(double[] data) {
        // defensive copy
        this.data = new double[ndimensions];
        for (int i = 0; i < ndimensions; i++)
            this.data[i] = data[i];
    }

    public int getNdimensions() {
        return ndimensions;
    }

    public double[] getData() {
        return data;
    }

    public double getX() {
        return data[0];
    }

    public double getY() {
        return data[1];
    }

    public double dot(Vector other) {
        double sum = 0.0;
        for (int i = 0; i < ndimensions; i++)
            sum = sum + (this.data[i] * other.data[i]);
        return sum;
    }

    public double magnitude() {
        return Math.sqrt(this.dot(this));
    }

    public Vector scale(double factor) {
        Vector c = new Vector();
        for (int i = 0; i < ndimensions; i++)
            c.getData() [i] = factor * data[i];
        return c;
    }

    public Vector direction() throws ArithmeticException{
        if (this.magnitude() == 0.0)
            throw new ArithmeticException("zero-vector has no direction");
        return this.scale(1.0 / this.magnitude());
    }

    public Vector plus(Vector that) {
        Vector c = new Vector();
        for (int i = 0; i < ndimensions; i++)
            c.data[i] = this.data[i] + that.data[i];
        return c;
    }

    public Vector minus(Vector that) {
        Vector c = new Vector();
        for (int i = 0; i < ndimensions; i++)
            c.data[i] = this.data[i] - that.data[i];
        return c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
//        System.out.println("imageX: " + (getX() == vector.getX()));
//        System.out.println("imageY: " + (getY() == vector.getY()));
        return getX() == vector.getX() && getY() == vector.getY();
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(ndimensions);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    @Override
    public String toString() {
        return "Vector{" +
                "data=" + Arrays.toString(data) +
                '}';
    }
}
