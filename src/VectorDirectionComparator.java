import java.util.Comparator;

/**
 * This comparator class calculates the angle from the +x axis for each vector and compares the directions of the vectors accordingly
 * It is used to sort the students by the angle they are leaving the room in
 */
public class VectorDirectionComparator implements Comparator<Vector> {
    @Override
    public int compare(Vector v1, Vector v2) {
        double angle1 = getAngleFromXAxis(v1);

        double angle2 = getAngleFromXAxis(v2);

        return Double.compare(angle1, angle2);
    }

    private double getAngleFromXAxis(Vector v){
        try {
            double x = v.direction().getX();
            double y = v.direction().getY();
            if (x == 0) {
                if (y > 0) {
                    return Math.PI / 2;
                }
                return -1 * Math.PI / 2;
            }
            if (y == 0) {
                if (x > 0) {
                    return 0;
                }
                return -Math.PI;
            }
            double inverseTanResult = Math.atan(y / x);
            switch (findQuadrant(v)) {
                case 1:
                    return inverseTanResult + 0;
                case 2:
                    return inverseTanResult + Math.PI;
                case 3:
                    return inverseTanResult + Math.PI;
                case 4:
                    return inverseTanResult + 2 * Math.PI;
            }
            return -1;
        } catch ( ArithmeticException e) {
            return 0;
        }
    }

    private int findQuadrant(Vector v) {
        if (v.getData()[0] > 0 && v.getData()[1] > 0) {
            return 1;
        } else if (v.getData()[0] < 0 && v.getData()[1] > 0) {
            return 2;
        } else if (v.getData()[0] < 0 && v.getData()[1] < 0) {
            return 3;
        } else if (v.getData()[0] > 0 && v.getData()[1] < 0) {
            return 4;
        }
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}
