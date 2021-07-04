import java.util.ArrayList;
import java.util.Objects;

public class Student {
    private String ID;
    private String name;
    private ArrayList<Room> rooms;
    private ArrayList<ArrayList<Room>> dayPath;
    private ArrayList<ArrayList<Pair<Double, Vector>>> timeDayPathMap;

    private int currentClass;
    private int nextClass;

    private Vector vectorCoordinates;
    private int currentFloor;
    private double SPEED;

    public Student(String ID, String name, double SPEED) {
        rooms = new ArrayList<>();
        this.ID = ID;
        this.name = name;
        this.SPEED = SPEED;

        vectorCoordinates = new Vector();
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vector getVectorCoordinates() {
        return vectorCoordinates;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public double getSPEED() {
        return SPEED;
    }

    /**
     * Creates an association between the time and the velocity vector for the student
     * Very efficient way to model walking students for Stage 2 optimization
     * @return
     */
    public ArrayList<ArrayList<Pair<Double,Vector>>> createTimeDayPathMap() {
        timeDayPathMap = new ArrayList<>();
        for (ArrayList<Room> path : dayPath) {
            ArrayList<Pair<Double, Vector>> timePathMap = new ArrayList<>();
            double tempTime = 0;
            for (int i = 1; i < path.size(); i++) {
                //calculate displacement
                Vector displacement = VectorCalc.calculateDisplacement(path.get(i - 1), path.get(i));
                //if the displacement is 0, rooms are on top of each other
                if (displacement.equals(new Vector())) {
                    timePathMap.add(new Pair<>(tempTime, displacement));
                    continue;
                }
                tempTime += displacement.magnitude() * (1.0 / SPEED); //add time that it takes to reach next room
                Vector velocity = displacement.direction().scale(SPEED);
                //timePathMap shows that student is moving with this directional velocity until it reaches time tempTime
                timePathMap.add(new Pair<>(tempTime, velocity));
            }
            timeDayPathMap.add(timePathMap);
        }
        return timeDayPathMap;
    }

    //not using this, deprecated
    @Deprecated
    public Vector getVelocityAtTime(int periodEnded, double time) {
//        double tempTime = 0;
//        ArrayList<Room> path = dayPath.get(periodEnded);
//        for (int i = 1; i < path.size(); i++) { //convert daypath into a time-path map so we don't have to find the time student reaches each room
//            Vector currentVelocity = VectorCalc.calculateDisplacement(path.get(i - 1), path.get(i)).scale(1.0 / SPEED);
//            tempTime += currentVelocity.magnitude(); //add time that it takes to reach next room
//            if (time < tempTime) {
//                return currentVelocity;
//            }
//        }
//        return new Vector();

        for (int i = 0; i <timeDayPathMap.get(periodEnded-1).size(); i++) {
            Pair<Double, Vector> pair = timeDayPathMap.get(periodEnded-1).get(i);
            if (time < pair.getKey()) {
                return pair.getValue();
            }
        }
        return new Vector();
    }

    public void moveByDisplacement(Vector displacement) {
        vectorCoordinates = vectorCoordinates.plus(displacement);
    }

    public void moveByTime(int periodEnded, double time, double dt) {
        for (int i = 0; i <timeDayPathMap.get(periodEnded-1).size(); i++) {
            Pair<Double, Vector> pair = timeDayPathMap.get(periodEnded-1).get(i);
            if (time + dt < pair.getKey()) {
                moveByDisplacement(pair.getValue().scale(dt));
                return;
            } else if (time < pair.getKey()) {
                setCoordinatesToRoom(dayPath.get(periodEnded-1).get(i+1));
            }
        }
//        setCoordinatesToRoom(dayPath.get(periodEnded-1).get(dayPath.get(periodEnded-1).size()-1));
//        for (Pair<Double, Vector> pair : timeDayPathMap.get(periodEnded)) {
//            if (time < pair.getKey()) {
//                moveByDisplacement(pair.getValue().scale(dt));
//                return;
//            }
//        }
    }

    public void setCoordinatesToRoom(Room r) {
        vectorCoordinates = r.getVectorCoordinates();
        currentFloor = r.getFloor();
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }
    public int getCurrentClass() {
        return currentClass;
    }

    public int getNextClass() {
        return nextClass;
    }

    public Room getCurrentClassRoom() {
        if (currentClass >= rooms.size()) {
            return null;
        }
        return rooms.get(currentClass);
    }

    public Room getNextClassRoom() {
        if (nextClass >= rooms.size()) {
            return null;
        }
        return rooms.get(nextClass);
    }

    public void setCurrentClass(int currentClass) {
        this.currentClass = currentClass;
    }

    public void setNextClass(int nextClass) {
        this.nextClass = nextClass;
    }
    public ArrayList<ArrayList<Room>> getDayPath() {
        return dayPath;
    }

    public void setDayPath(ArrayList<ArrayList<Room>> dayPath) {
        this.dayPath = dayPath;
        createTimeDayPathMap();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(ID, student.ID);
    }

    public void startSchool() {
        currentClass = 0;
        assigntoCurrentRoom();
        nextClass = 1;
    }

    public void startNextPeriod(boolean initial) throws NoMorePeriodsException{
        if (currentClass == rooms.size() - 1) { //if current period is last period
            throw new NoMorePeriodsException("There are no more periods");
        }
        if (!initial) {
            removeFromCurrentRoom();
        }
        currentClass++;
        nextClass++;
        if (!initial) {
            assigntoCurrentRoom();
        }
    }

    public void removeFromCurrentRoom() {
        getCurrentClassRoom().removeCurrentStudent(this);
    }

    public void assigntoCurrentRoom() {
        getCurrentClassRoom().addCurrentStudent(this);
    }

    private boolean fixedBloc;

    public boolean isFixedBloc() {
        return fixedBloc;
    }

    public void setFixedBloc(boolean fixedBloc) {
        this.fixedBloc = fixedBloc;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }
}
