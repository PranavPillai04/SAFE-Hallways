import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class Room {
    private String roomNumber;
    private HashSet<Room> adjacents;

    private HashSet<Student> currentStudents;
    private ArrayList<ArrayList<Student>> groupedStudents;

    private final int nDimensions = 2;
    private double[] coordinates;
    private int floor;

    private boolean isStairLanding;

    public Room(String roomNumber, boolean isStairLanding, int floor, double... coordinates){
        adjacents = new HashSet<>();
        currentStudents = new HashSet<>();
        this.roomNumber = roomNumber;
        this.coordinates = new double[nDimensions];
        this.floor = floor;
        this.isStairLanding = isStairLanding;

        for (int i = 0; i < coordinates.length; i++) {
            this.coordinates[i] = coordinates[i];
        }
    }
    public String getRoomNumber() { return roomNumber; }

    public Vector getVectorCoordinates() {
        return new Vector(coordinates);
    }

    public int getFloor() {
        return floor;
    }

    public void addAdjacent(Room room) { adjacents.add(room); }

    public void addAdjacentList(ArrayList<Room> rooms) {
        for (Room room : rooms) {
            addAdjacent(room);
        }
    }

    public HashSet<Room> getAdjacents() {
        return adjacents;
    }

    public void addCurrentStudent(Student student) { currentStudents.add(student);}

    public void removeCurrentStudent(Student student) { currentStudents.remove(student);}

    public HashSet<Student> getCurrentStudents() {
        return currentStudents;
    }

    public void setGroupedStudents(ArrayList<ArrayList<Student>> groupedStudents) {
        this.groupedStudents = groupedStudents;
    }

    public ArrayList<ArrayList<Student>> getGroupedStudents() {
        return groupedStudents;
    }

    public void setStairLanding(boolean stairLanding) {
        isStairLanding = stairLanding;
    }

    public boolean isStairLanding() {
        return isStairLanding;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof String) {
            return roomNumber.equals(obj);
        }
        if (obj instanceof Room) {
            return roomNumber.equals(((Room) obj).roomNumber);
        }
        return false;
    }
    @Override
    public String toString() { return roomNumber; }

    @Override
    public int hashCode() {
        return Objects.hash(roomNumber);
    }
}
