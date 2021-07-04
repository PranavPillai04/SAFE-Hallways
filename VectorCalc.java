import java.util.ArrayList;

public class VectorCalc {

    public static Vector calculateDisplacement(Room start, Room target) {
        //if both are staircases and on top of each other, displacement is 0
        if (start.isStairLanding() && target.isStairLanding() && target.equals(new Room(start.getRoomNumber().substring(0,2).concat(target.getFloor()+""), true,0,0,0 ))) {
            //set displacement equal to nothing
            return new Vector();
        }
        return target.getVectorCoordinates().minus(start.getVectorCoordinates());
    }
    public static Vector calculateDirection(Room start, Room target) {
        try {
            return calculateDisplacement(start, target).direction();
        } catch (ArithmeticException e) {
            double[] noData = {0,0};
            return new Vector(noData); //temporary patch for if start and target are the same
        }
    }

    /**
     * Selects the best adjacent for the room
     * @param path - it won't pick an adjacent room that the student has already been to
     * @param start
     * @param target
     * @return
     */
    public static Room chooseAdjacent(ArrayList<Room> path, Room start, Room target) {
        Room bestAdjacent = new Room("fillerRoom", false,0, 0); //filler
        boolean first = true;
        if (start.getAdjacents().contains(target)) {
            bestAdjacent = target;
            return bestAdjacent;
        }
        for (Room adjacent : start.getAdjacents()) {
            if (first) {
                first = false;
                bestAdjacent = adjacent;
            }
            if (path.contains(adjacent)) {
                continue;
            }
            if (    calculateDirection(start, adjacent).direction().dot(calculateDirection(start, target))
                    >
                    calculateDirection(start, bestAdjacent).direction().dot(calculateDirection(start, target)))
            {
                bestAdjacent = adjacent;
            }
        }
        return bestAdjacent;
    }

    //uses a randomizing function that is proportional to mean, which is used in order to get a randomized reasonable speed for student
    public static double expdist(double mean) {
        return 2*(-0.5 + Math.random()) * (mean / 3.0) + mean;
//        return (-mean * Math.log(1 - Math.random()));
//        double imageX = 2.5 + Math.random();
//        return (1.0 / (sD * Math.sqrt(2 * Math.PI))) * Math.pow(Math.E, -1.0 * Math.pow(imageX - mean, 2) / (2* Math.pow(sD, 2)));
    }

}
