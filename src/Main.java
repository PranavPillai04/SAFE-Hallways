import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Main {

    public static void main(String[] args) {
        try {
            //initialize rooms and students
            Date startTime = Calendar.getInstance().getTime();
            WalkingSimulation s = new WalkingSimulation("studentInfoCSV","outputFile","firstFloorRoomInfo","secondFloorRoomInfo", "thirdFloorRoomInfo");
            s.run();

            s.startNextPeriod();
            s.run();
            s.startNextPeriod();
            s.run();
            s.startNextPeriod();
            s.run();

            s.printResultsToOutputFile();


//            Tester methods:
//            testPath(s);
//            testStudentDayPath(s);
//            testSpecificRoomStage1(s);
//            miscTester();

            Date endTime = Calendar.getInstance().getTime();
            System.out.println("Start time: " + startTime + "");
            System.out.println("End time: " + endTime + "");
            //debug print out time it took for program to run
            System.out.println("Time elapsed: " + 0.001 * (endTime.getTime() - startTime.getTime()) + " s");



        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void testPath(WalkingSimulation s) throws NoSuchRoomException{
        s.getTotalPath("r5", "r2");
    }

    public static void testStudentDayPath(WalkingSimulation s) {
        s.testerGetStudentDayPath();
    }

    public static void testSpecificRoomStage1(WalkingSimulation s) throws NoSuchRoomException, NoMorePeriodsException{
//        s.startNextPeriod();
//        s.startNextPeriod();
//        s.startNextPeriod();
        s.optimizeSpecificRoomStage1("312");
    }

    public static void miscTester() {
        System.out.println();
        System.out.println();
        int nReleases = 3;
        int shift = (int) Math.floor(Math.random() * nReleases); //0 or 1 or 2
        System.out.println("shift: " + shift);

        ArrayList<String> groups = new ArrayList<>();
        groups.add("a");
        groups.add("b");
        groups.add("c");

        ArrayList<String> tempSet = new ArrayList<>();

        for (int i = 0; i < shift; i++) {
            tempSet.add(groups.remove(0));
        }
        groups.addAll(tempSet);

        System.out.println(groups);
    }

}

