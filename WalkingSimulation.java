import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WalkingSimulation {
    private final static int nDimensions = 2; // 2D floor plan

    private File outputFile;

    // below is a Map <Room, Periods <Groups <Students> > >
    private HashMap<Room,ArrayList<ArrayList<ArrayList<Student>>>> dayDismissalGroupsRoomMap;

    public WalkingSimulation(String studentScheduleFilename, String outputFilename, String floor1_RoomsFilename, String floor2_RoomsFilename, String floor3_RoomsFilename) throws IOException {
        dayDismissalGroupsRoomMap = new HashMap<>();
        currentPeriod = 1;
        //initialize positions of allRooms
        allRooms = new ArrayList<>();
        //initialize positions of allStairs
        allStairs = new ArrayList<>();

        outputFile = new File(outputFilename);

        //Must initialize floors in order
        //floor 1
        System.out.println("floor 1 init starting...");
        initializeRoomList(floor1_RoomsFilename, 1);
        //floor 2
        System.out.println("floor 2 init starting...");
        initializeRoomList(floor2_RoomsFilename, 2);
        //floor 3
        System.out.println("floor 3 init starting...");
        initializeRoomList(floor3_RoomsFilename, 3);

        //students must be initialized after allRooms in order to assign schedules
        System.out.println("student schedule init starting...");
        initializeStudentListCSV(studentScheduleFilename);

        //get rooms debug
        /*
        for (Room r : allRooms.get(0)) {
            System.out.println("\"" + r.getRoomNumber() + "\",");
        }
         */

        //get students debug
        /*
        for (Student s : allStudents) {
            System.out.println(s.getID() + ", " + s.getName() + ", " + s.getRooms());
        }
         */

        System.out.println("init done");
    }

    ArrayList<ArrayList<Room>> allRooms;
    ArrayList<ArrayList<Room>> allStairs;

    private final static int roomNVAR = 1 + nDimensions + 1; //1 room name + 2 dim + 1 Array(? adjacents)
    public void initializeRoomList(String filename, int floor) throws IOException {
        //initialize allRooms and adjacents

        ArrayList<Room> floorRooms = new ArrayList<>();
        allRooms.add(floorRooms);

        ArrayList<Room> floorsStairLandings = new ArrayList<>();
        allStairs.add(floorsStairLandings);

        ArrayList<ArrayList<String>> myAdjacentLists = new ArrayList<>();
        //Scan in room info
        BufferedReader in = new BufferedReader(new FileReader(filename));
        String[] tempVarHolders = new String[roomNVAR];

        //find number of lines in the file
        List<String> fileStream = Files.readAllLines(Paths.get(filename));
        int noOfLines = fileStream.size();

        for (int i = 0; i < noOfLines; i++) {
            try {
                if (i % roomNVAR <= nDimensions) { //only for name + 2 dim
                    tempVarHolders[i % roomNVAR] = in.readLine();
                } else { // ? adjacents
                    ArrayList<String> adjacentRooms = new ArrayList<>();
                    Scanner linereader = new Scanner(in.readLine());
                    while (linereader.hasNext()) {
                        adjacentRooms.add(linereader.next());
                    }
                    linereader.close();
                    myAdjacentLists.add(adjacentRooms);
                }
            }
            catch (IOException e) {throw new IncorrectFormattingException("File " + filename + " is incorrectly formatted.");}
            if (i% roomNVAR == (roomNVAR -1)) {
                Room r = new Room(tempVarHolders[0], false, floor, Double.parseDouble(tempVarHolders[1]), Double.parseDouble(tempVarHolders[2]));
                floorRooms.add(r);
                dayDismissalGroupsRoomMap.put(r,new ArrayList<>());

                //if room is a stair landing, add to allStairs
                switch (r.getRoomNumber().substring(0,2)) {
                    case "SA": case "SB": case "SC": case "SD": case "SE": case "SF": case "SG":
                        r.setStairLanding(true);
                        floorsStairLandings.add(r);
//                        System.out.println("Added stair: " + s.getRoomNumber());
                }
            }
        }
        in.close();

        //add adjacent rooms to each Room object
        for (int i = 0; i < floorRooms.size(); i++) {
//            System.out.println("Room " + floorRooms.get(i).getRoomNumber() + " has adjacent allRooms of: " + floorRooms.get(i).getAdjacents());
            for (String adjString : myAdjacentLists.get(i)) {
//                System.out.println("The adjacent room object is " + getRoomFromName(adjString));
                floorRooms.get(i).addAdjacent(getRoomFromName(adjString));

            }
        }
    }

    ArrayList<Student> allStudents;
    private final static double AVERAGE_SPEED = 20; //average human walking speed
    private final static int studentCSV_NVAR = 1 + 1 + 4; //1 ID + 1 (First L.) + 4 rooms
    public void initializeStudentListCSV(String filename) throws IOException{
        allStudents = new ArrayList<>();

        //turn file into stream
        List<String> fileStream = Files.readAllLines(Paths.get(filename));

        //remove headings line
        fileStream.remove(0);

        //add students and their rooms
        fileStream.stream().forEach((String line)-> {
            Scanner lineReader = new Scanner(line);
            lineReader.useDelimiter(",");
            String[] values = new String[studentCSV_NVAR];
            for (int i = 0; i < values.length; i++) {
                //added .trim() if CSV file has spaces
                values[i] = lineReader.next().trim();
//                System.out.println(values[i]);
            }
            lineReader.close();

            //create student
            Student s = new Student(values[0], values[1], VectorCalc.expdist(AVERAGE_SPEED));
            for (int i = 2; i < values.length; i++) {
                try {
                    s.addRoom(getRoomFromName(values[i]));
                } catch (NoSuchRoomException e) {
                    System.out.println("There is no room with this room number : " + values[2] + ", for student " + values[0]);
                }
            }
            //make students leave either thru room 11 or room 12 (exits for the building)
            try {
                boolean exitMain = new Random().nextBoolean();
                Room exit = exitMain ? getRoomFromName("11") : getRoomFromName("12");
                s.addRoom(exit);
                allStudents.add(s);
            } catch (NoSuchRoomException e) {
                System.out.println("There is no room with room number 11 or 12");
            }

            //calculate dayPath for student and assign it to the student
            s.startSchool();
//            System.out.println(s.getID());
            s.setDayPath(calculateStudentDayPath(s));

            s.createTimeDayPathMap();

            //debug (remove s.createTimeDayPathMap() above because it is inside debug)
//            System.out.println("Student " + s.getID() + " has a day path of " + s.createTimeDayPathMap());
//            System.out.println("Student " + s.getID() + " goes to allRooms " + s.getDayPath());
//            System.out.println();

            //student position set at first classroom
            s.setCoordinatesToRoom(s.getCurrentClassRoom());
        });
    }

    private int currentPeriod; //hold int value for current period. 0, 1, or 2.

    public Room getRoomFromName(String name) throws NoSuchRoomException{
//        System.out.println("String is: " + name);
        for (int floor = 0; floor < allRooms.size(); floor++) {
            for (Room potentialRoom : allRooms.get(floor)) {
                if (potentialRoom.equals(name)) {
//                    System.out.println("Potential room object is: " + potentialRoom.getRoomNumber());
                    return potentialRoom;
                }
            }
        }
        throw new NoSuchRoomException("There is no room with this room number : " + name);
    }

    public ArrayList<Room> getTotalPath(String start, String target) throws NoSuchRoomException{
        return getTotalPath(getRoomFromName(start), getRoomFromName(target));
    }

    public ArrayList<Room> getTotalPath(Room start, Room target) {
        /*
        System.out.println("Overall measurements:");
        System.out.println(VectorCalc.calculateDisplacement(start, target));
        System.out.println(VectorCalc.calculateDirection(start, target));
         */

        ArrayList<Room> path = new ArrayList<>();
//        System.out.println("Path:");
        Room current = start;
        path.add(current);

        //if the student has to move between floors
        //they have to go to a staircase before they reach the target floor
        if (start.getFloor() != target.getFloor()) {

            //filler to put best stair
            Room bestStair = new Room("fillerStair",true,0,0,0);
            boolean first = true;

            //get closest stair landing on the starting floor
            for (Room s : allStairs.get(start.getFloor() - 1)) {
//                System.out.println(s.getRoomNumber());
                if (first) {
                    first = false;
                    bestStair = s;
                }
                if (    VectorCalc.calculateDirection(start, s).direction().dot(VectorCalc.calculateDirection(start, target))
                        >
                        VectorCalc.calculateDirection(start, bestStair).direction().dot(VectorCalc.calculateDirection(start, target)))
                {
                    bestStair = s;
                }
            }
//            System.out.println(bestStair.getRoomNumber());
            while (!current.equals(bestStair)) {
//            System.out.println("Current: " + current);
//            System.out.println("BestStair: " + bestStair);
                current = VectorCalc.chooseAdjacent(path, current, bestStair);
                path.add(current);
//            System.out.println("Path: " + path);
            }
            Room otherFloorLandingFiller = new Room(bestStair.getRoomNumber().substring(0,2).concat(target.getFloor() + ""), true, 0,0,0);

            int otherFloorLandingIndex = allStairs.get(target.getFloor()-1).indexOf(otherFloorLandingFiller);
            current = allStairs.get(target.getFloor()-1).get(otherFloorLandingIndex);
            path.add(current);
//            System.out.println("Moved thru stairs to: " + current.getRoomNumber());
        }

        // until target is reached, find the adjacent that is closest to overall displacement
        while (!current.equals(target)) {
//            System.out.println("Current: " + current);
//            System.out.println("Target: " + target);
            current = VectorCalc.chooseAdjacent(path, current, target);
            path.add(current);
//            System.out.println("Path: " + path);
        }
        return path;
    }
    public static final int nReleases = 3;

    private static final double frameRateMultiplier = 0; // usually 5, dt * multiplier is how long thread sleeps between animation updates

    private static final double BLOC_TOTAL_TIME = 3*60;
    private static final double BLOC_BUFFER_TIME = 0.75 * 60;

    // ideally repeat optimization a few times so that the students are fixed in their blocs
    private static final int STAGE_2_ITERATIONS = 10;

    public void optimize() {
        System.out.println("Period #" + (currentPeriod) + ":");
        System.out.println("***");
        System.out.println("Stage One");
        for (int floor = 0; floor < allRooms.size(); floor++) {
            for (Room r : allRooms.get(floor)) {
                r.setGroupedStudents(optimizeInitialStudentGroups(r));
            }
        }

        ArrayList<Set<Student>> studentsInHallwayBlocs = new ArrayList<>();
        for (int i = 0; i < nReleases; i++) {
            ConcurrentHashMap<Student, Integer> initialMap = new ConcurrentHashMap<>();
            Set<Student> studentsInHallways = initialMap.newKeySet();
            for (int floor = 0; floor < allRooms.size(); floor++) {
                for (Room r : allRooms.get(floor)) {
                    studentsInHallways.addAll(r.getGroupedStudents().get(i));
                }
            }
            //studentsinhallwayBlocs includes students in each bloc info, and then it is passed into optimizeParametrized
            //this means that optimizeParametrized can moveByDisplacement students between blocs with ease
            studentsInHallwayBlocs.add(studentsInHallways);
        }


        //repeat stage 2 optimization for n iterations
        for (int i = 0; i < STAGE_2_ITERATIONS; i++) {
            System.out.println("Stage Two #" + (i+1));
            optimizeParametrizedStudentGroups(studentsInHallwayBlocs);
        /*
        //print out groups for each room
        for (int i = 0; i < nReleases; i++) {
            System.out.println("Bloc " + (i+1) + ". Release at __");
            System.out.println("Students in bloc: " + studentsInHallwayBlocs.get(i).size());
            for (int floor = 0; floor < allRooms.size(); floor++) {
                for (Room r : allRooms.get(floor)) {
                    System.out.print(r.getRoomNumber() + ": ");
                    for (Student s : r.getGroupedStudents().get(i)) {
                        System.out.print(s.getID() + "   ");
                    }
                    System.out.println();
                }
            }
            System.out.println("-");
        }
         */
        }


        //save groups to dayDismissalGroupsRoomMap
        for (ArrayList<Room> floorRooms : allRooms) {
            for (Room r : floorRooms) {
                dayDismissalGroupsRoomMap.get(r).add(r.getGroupedStudents());
            }
        }

        //debug statistics
        System.out.println("Period " + (currentPeriod));
        System.out.println("Total students in school: " + allStudents.size());
        for (int i = 0; i < nReleases; i++) {
            System.out.println("Students in Bloc "+ (i+1) + ": " + studentsInHallwayBlocs.get(i).size());
        }


        /*
        //if displaying final product, run animation graphic
        if (DISPLAY_RESULT_GRAPHICS) {
            System.out.println("DISPAYING FINAL HALLWAYS ANIMATION...");
            displayFinalGraphicAnimation(studentsInHallwayBlocs);
        }
         */


        //set up for next period run, so that no students have been fixed
        for (Set<Student> studentList : studentsInHallwayBlocs) {
            for (Student student : studentList) {
                student.setFixedBloc(false);
            }
        }

        System.out.println(" - Optimization complete - ");
    }

    //Optimization Stage 1:
    public ArrayList<ArrayList<Student>> optimizeInitialStudentGroups(Room room) {

        //if the student is moving to a new classroom, add to studentsLeavingRoom
        ArrayList<Student> studentsLeavingRoom = new ArrayList<>();
        for (Student s : room.getCurrentStudents()) {
            if (!s.getNextClassRoom().equals(s.getCurrentClassRoom())) {
                studentsLeavingRoom.add(s);
            }
        }
        //calculates the angle for each student who is leaving, and then sort the list by angle
        studentsLeavingRoom.sort( (s1, s2) -> {
                VectorDirectionComparator v = new VectorDirectionComparator();
//            System.out.println(room.getRoomNumber());
//            System.out.println(s1.getID() + " " + s2.getID());
                return v.compare(VectorCalc.calculateDirection(s1.getCurrentClassRoom(), s1.getNextClassRoom()),
                        VectorCalc.calculateDirection(s2.getCurrentClassRoom(), s2.getNextClassRoom()));

        });

        ArrayList<ArrayList<Student>> groupsBeforeShuffle = new ArrayList<>();

        //groupsBeforeShuffle splits the sorted studentsLeavingRoom so that students near each other are put together in groupNearEachOther
        int ngroups = (int) Math.ceil(studentsLeavingRoom.size() / (double) nReleases);
        for (int i = 0; i < ngroups; i++) {
            ArrayList<Student> groupNearEachOther = new ArrayList<>();
            groupsBeforeShuffle.add(groupNearEachOther);
            for (int j = 0; j < nReleases && ((nReleases*i + j) < studentsLeavingRoom.size()); j++) {
                groupNearEachOther.add(studentsLeavingRoom.get(nReleases*i + j));
            }
        }

        ArrayList<ArrayList<Student>> groups = new ArrayList<>();

        //groups takes one from each element list in groupsBeforeShuffle, so that final groups are spread out with one from each angle section
        for (int i = 0; i < nReleases; i++) {
            ArrayList<Student> tempArrayList = new ArrayList<>();
            for (ArrayList<Student> a : groupsBeforeShuffle) {
                if (i < a.size()) {
                    tempArrayList.add(a.get(i));
                }
            }
            groups.add(tempArrayList);
        }

        Collections.shuffle(groups);

        /*
        //shift so that group with most people is randomized
        int shift = (int) Math.floor(Math.random() * nReleases); //0 or 1 or 2
        ArrayList<ArrayList<Student>> tempSet = new ArrayList<>();
        for (int i = 0; i < shift; i++) {
            tempSet.add(groups.remove(0));
        }
        groups.addAll(tempSet);
         */

        //return the Stage 1 groups
        return groups;
    }


    private static final double dt = 1; //usually 0.3, update student position every 0.2 second
    private static final int interactionDistance = 10;
    private static final int nStudents_forInteraction = 2;
    //number of students for interaction has to be higher for last period because pretty much everyone is leaving through rooms 11 or 12 to leave the school
    private static final int nStudents_forInteractionLastPeriod = 2;

    //Optimization Stage 2:
    // blocs are 0, 1, 2
    public synchronized ArrayList<Set<Student>> optimizeParametrizedStudentGroups(ArrayList<Set<Student>> studentsInHallwayBlocs) {
        int maxInteractions = (currentPeriod < allStudents.get(0).getRooms().size()-1) ? nStudents_forInteraction : nStudents_forInteractionLastPeriod;
//        System.out.println(maxInteractions);
        //make sure each student is at the correct spot
        for (Set<Student> studentList : studentsInHallwayBlocs) {
            for (Student student : studentList) {
                //set student coordinates to room they start in for the period
                student.setCoordinatesToRoom(student.getDayPath().get(currentPeriod-1).get(0));
            }
        }

        //iterate through each bloc of time
        for (int bloc = 0; bloc < studentsInHallwayBlocs.size(); bloc++) {
            //initialize variables for animation and movement of students
            Set<Student> studentsInHallwaysArrayList = studentsInHallwayBlocs.get(bloc);

            double blocTotalTime = BLOC_TOTAL_TIME;
            boolean firstReached = false;
            //iterate through each dt (1 second) in the total time for the bloc
            //if the bloc doesn't need the full time, it will reduce it and add the buffer
            for (double time = 0; time < blocTotalTime; time += dt) { //uses 5 because each bloc is 5 min long

                boolean haveAllReachedRooms = true;
                for (Student s : studentsInHallwaysArrayList) {

                    // CHANGED LOOP

//                for (int studentNum = 0; studentNum < studentsInHallwaysArrayList.size(); studentNum++) {
//                    Student s = studentsInHallwaysArrayList.get(studentNum);
                    // debug if student 0002 has reached target room
//                    if (s.getID().equals("0002")) {
//                        System.out.println("Current position: " + s.getVectorCoordinates().getX());
//                        System.out.println("Target room: " + s.getNextClassRoom().getVectorCoordinates().getX() );
//                        System.out.println(s.getID() + " the value of haveAllReachedRooms is " + haveAllReachedRooms);
//                    }
                    //is student still in hallways
                    //if so, all students haven't reached their allRooms yet
                    if (!s.getVectorCoordinates().equals(s.getNextClassRoom().getVectorCoordinates())) {
                        haveAllReachedRooms = false;
                    }

                    //collection needs to be an arraylist for the below debug function to work :(
//                    if (s == studentsInHallways.get(0)) {
//                        System.out.println(s.getID() + " has a position at " + s.getVectorCoordinates());
////                        System.out.println(s.getID() + " has a velocity of " + s.getVelocityAtTime(currentPeriod, time));
//                    }
                    int interactionCounter = 0;
                    for (Student o : studentsInHallwaysArrayList) {
                        if (s.equals(o)) {
                            continue;
                        }
                        //CHECK IF STUDENTS ON SAME FLOOR FOR INTERACTION TO HAPPEN
                        //if distance less than 40 and both haven't reached their next room
                        if (s.getCurrentFloor() == o.getCurrentFloor()
                                && s.getVectorCoordinates().minus(o.getVectorCoordinates()).magnitude() < interactionDistance
                                && !o.getVectorCoordinates().equals(o.getNextClassRoom().getVectorCoordinates())
                                && !s.getVectorCoordinates().equals(s.getNextClassRoom().getVectorCoordinates()) ) {
//                            System.out.println("Interaction");
                            interactionCounter++;
                        }
                    }
                    //if there are more than or equal to 2 students near and this student hasn't been moved already
                    //then switch student to a different bloc
                    if (interactionCounter >= maxInteractions && !s.isFixedBloc()) {
//                        System.out.println("Too many interactions");

                        //obtain replacement student from other bloc but same room
                        int otherBloc = (bloc+1)%nReleases;
                        Room r = s.getCurrentClassRoom();
                        for (int i = 0; i < r.getGroupedStudents().get(0).size(); i++) {

                            //we don't set replacement to fixed - we should leave it open because it isn't verified yet.
                            s.setFixedBloc(true);
                            s.setCoordinatesToRoom(s.getCurrentClassRoom());

                            if (i +1 <= r.getGroupedStudents().get(otherBloc).size()) {
                                Student possibleReplacement = r.getGroupedStudents().get(otherBloc).get(i);
                                if (possibleReplacement.isFixedBloc()) {
                                    continue;
                                }
                                // this will be confirmed replacement
                                Student replacement = possibleReplacement;

                                //switch students between blocs
                                studentsInHallwayBlocs.get(otherBloc).remove(replacement);
                                studentsInHallwayBlocs.get(bloc).add(replacement);

                                studentsInHallwayBlocs.get(bloc).remove(s);
                                studentsInHallwayBlocs.get(otherBloc).add(s);

                                r.getGroupedStudents().get(bloc).remove(s);
                                r.getGroupedStudents().get(otherBloc).set(i, s);

                                r.getGroupedStudents().get(bloc).add(replacement);

                                //get replacement up to correct time
                                for (double tempTime = 0; tempTime < time; tempTime += dt) {
                                    replacement.moveByTime(currentPeriod, tempTime, dt);
                                }
                                break;
                            }

                            // if bloc is not filled, just add student at the end
                            studentsInHallwayBlocs.get(bloc).remove(s);
                            studentsInHallwayBlocs.get(otherBloc).add(s);

                            r.getGroupedStudents().get(bloc).remove(s);
                            r.getGroupedStudents().get(otherBloc).add(s);
                        }

                        continue;
                    }
                    //move student
                    s.moveByTime(currentPeriod, time, dt);
                }

                //if everyone reached their allRooms already
                //then end period after a given buffer time
                if (haveAllReachedRooms && !firstReached) {
                    firstReached = true;
//                    System.out.println("Everyone has reached their allRooms. Buffer is ongoing");
                    //don't end period w buffer time if it would take longer than the given bloc time
                    if (time + BLOC_BUFFER_TIME < blocTotalTime) {
                        blocTotalTime = time + BLOC_BUFFER_TIME;
                    }
//                    System.out.println(blocTotalTime);
                }
            }
        }

        //studentsInHallwayBlocs is sorted and ready to go
        //groupedStudents for each room has been modified and optimized as needed
        return studentsInHallwayBlocs;
    }


    public synchronized void displayFinalGraphicAnimation(ArrayList<Set<Student>> studentsInHallwayBlocs) {
        List<Student> presentStudents = allStudents;
        ArrayList<Set<Student>> presentSIHB = new ArrayList<>();

        //make sure each student is at the correct spot
        for (Set<Student> studentList : studentsInHallwayBlocs) {
            Set<Student> presentS = new HashSet<>();
            presentSIHB.add(presentS);
            for (Student student : studentList) {
                if (presentStudents.contains(student)) {
                    presentS.add(student);
                }
                //set student coordinates to room they start in for the period
                student.setCoordinatesToRoom(student.getDayPath().get(currentPeriod-1).get(0));
            }
        }

        //iterate through each bloc of time
        for (int bloc = 0; bloc < presentSIHB.size(); bloc++) {

            //initialize variables for animation and movement of students
            Set<Student> studentsInHallwaysArrayList = presentSIHB.get(bloc);

            double blocTotalTime = BLOC_TOTAL_TIME;
            boolean firstReached = false;

            //iterate through each dt (1 second) in the total time for the bloc
            //if the bloc doesn't need the full time, it will reduce it and add the buffer
            for (double time = 0; time < blocTotalTime; time += dt) { //uses 5 because each bloc is 5 min long

                boolean haveAllReachedRooms = true;
                for (Student s : studentsInHallwaysArrayList) {
                    if (!presentStudents.contains(s)) {
                        continue;
                    }
                    //is student still in hallways
                    //if so, all students haven't reached their allRooms yet
                    if (!s.getVectorCoordinates().equals(s.getNextClassRoom().getVectorCoordinates())) {
                        haveAllReachedRooms = false;
                    }

                    for (Object oObj : studentsInHallwaysArrayList.stream().filter(o -> s.getCurrentFloor() == o.getCurrentFloor()).toArray()) {
                        Student o = (Student) oObj;
                        if (s.equals(o) || !allStudents.contains(o)) {
                            continue;
                        }
                        //CHECK IF STUDENTS ON SAME FLOOR FOR INTERACTION TO HAPPEN
                        //if distance less than 40 and both haven't reached their next room
                        if (s.getCurrentFloor() == o.getCurrentFloor()
                                && s.getVectorCoordinates().minus(o.getVectorCoordinates()).magnitude() < interactionDistance
                                && !o.getVectorCoordinates().equals(o.getNextClassRoom().getVectorCoordinates())
                                && !s.getVectorCoordinates().equals(s.getNextClassRoom().getVectorCoordinates()) ) {
//                            System.out.println("Interaction");
//                            totalInteractions++;
                        }
                    }

                    //if everyone reached their allRooms already
                    //then end period after a hardcoded buffer time
                    if (haveAllReachedRooms && !firstReached) {
                        firstReached = true;
                        if (time + BLOC_BUFFER_TIME < blocTotalTime) {
                            blocTotalTime = time + BLOC_BUFFER_TIME;
                        }
                    }

                    //move student
                    s.moveByTime(currentPeriod, time, dt);
                }
            }
        }
    }

    public ArrayList<ArrayList<Room>> calculateStudentDayPath(Student s) {
        //create array to hold day paths for each period
        ArrayList<ArrayList<Room>> dayPath = new ArrayList<>();

        //hold current and next period ints so that the student can be returned to it afterwards
        int current = s.getCurrentClass();
        int next = s.getNextClass();

        //until there are no more classrooms, keep finding the path to the next classroom
        //store the path in dayPath, each path as a separate element
        while (s.getNextClassRoom() != null) {
            dayPath.add(getTotalPath(s.getCurrentClassRoom(), s.getNextClassRoom()));
            try {
                s.startNextPeriod(true);
            }
            catch (NoMorePeriodsException e) {
                System.out.println(e.getMessage());
                break;
            }
        }
        //return student to original state
        s.setCurrentClass(current);
        s.setNextClass(next);
//        System.out.println(dayPath);
        //return dayPath, which holds path between allRooms for every period
        return dayPath;
    }

    public void startNextPeriod() throws NoMorePeriodsException{
        currentPeriod++;
        for (Student s : allStudents) {
            //start next period for each student, and actually move them
            s.startNextPeriod(false);
        }
    }

    public void run() throws NoMorePeriodsException{
        //run program as needed

//        /*
        //should just need optimize
        optimize();

//         */

        /*
        //debug allRooms
        for (Room r : allRooms) {
            System.out.println("\"" + r.getRoomNumber() + "\",");
//            System.out.println("Room " + r.getRoomNumber() + ". Current students: " + r.getCurrentStudents() + ". Adjacent allRooms: " + r.getAdjacents());
        }
         */

        /*
        //debug students
        for (Student s : allStudents) {
            System.out.println("Student " + s.getID() + ". Attendance " + s.isPresent() + " at room " + s.getCurrentClassRoom().getRoomNumber());
        }
         */

    }

    public void printResultsToOutputFile() {
        try {
            FileWriter writer = new FileWriter(outputFile);

            //write dismissal groups to outputFile
            for (ArrayList<Room> floorRooms : allRooms) {
                floorRooms.sort(Comparator.comparing(Room::getRoomNumber));

                //for every room
                for (Room r : floorRooms) {
                    //print room name
                    writer.write("Room " + r.getRoomNumber() + ":");
                    ArrayList<ArrayList<ArrayList<Student>>> info = dayDismissalGroupsRoomMap.get(r);
                    //read every period
                    for (int period = 0; period < info.size() ; period++) {
                        //print period number
                        writer.write("\n    Period " + (period+1) + ": ");
                        //read every group
                        for (int i = 0; i < nReleases; i++) {
                            writer.write("\n        ");
                            for (Student s : info.get(period).get(i)) {
                                writer.write(s.getName() + ", ");
                            }
                        }
                    }
                    writer.write("\n");
                }
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("There was an error writing to the file: " + outputFile.getName());
        }
    }


    //Tester methods:

    public void testerGetStudentDayPath() {
        Student s = allStudents.get(0);
        calculateStudentDayPath(s);
    }

    public void optimizeSpecificRoomStage1(String roomString) throws NoSuchRoomException{
        System.out.println("Optimized groups for room : " + roomString);
        for (ArrayList<Student> sList : optimizeInitialStudentGroups(getRoomFromName(roomString))) {
            for (Student s : sList) {
                System.out.println(s.getID() + " " + VectorCalc.calculateDirection(s.getCurrentClassRoom(), s.getNextClassRoom()));
            }
            System.out.println();
        }
    }

}
