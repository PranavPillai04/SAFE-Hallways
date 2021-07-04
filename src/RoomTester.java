import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class RoomTester {
    private final static int studentCSV_NVAR = 4; // 4 rooms

    public static void main(String[] args) {
        HashSet<String> roomList = new HashSet<>();
        Collections.addAll(roomList, firstFloorRooms);
        Collections.addAll(roomList, secondFloorRooms);
        Collections.addAll(roomList, thirdFloorRooms);

        //turn file into stream
        try {
            List<String> fileStream = Files.readAllLines(Paths.get("studentInfoCSV"));
            //remove headings line
            fileStream.remove(0);
            //add students and their rooms
            fileStream.stream().forEach((String line) -> {
                Scanner lineReader = new Scanner(line);
                lineReader.useDelimiter(",");
                String[] values = new String[studentCSV_NVAR];
                //move past ID
                lineReader.next();
                //move past name
                lineReader.next();
                //check each room
                for (int i = 0; i < values.length; i++) {
                    //added .trim() if CSV file has spaces
                    //check if roomList contains this room
                    String val = lineReader.next().trim();
                    boolean fileRoomIsContained = false;
                    for (String srm : roomList) {
                        if (srm.equalsIgnoreCase(val)) {
                            fileRoomIsContained = true;
                        }
                    }
                    if (!fileRoomIsContained) {
                        System.out.println("Room " + val + " is not in the dataset");
                    }
                }
                lineReader.close();
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }


    private static String[] firstFloorRooms = {
            "11",
            "12",
            "171",
            "173",
            "175",
            "152",
            "150",
            "146",
            "142",
            "140",
            "138",
            "166",
            "164",
            "169",
            "167",
            "165",
            "160",
            "158",
            "161",
            "137",
            "135",
            "129",
            "127",
            "133",
            "CAFE",
            "187",
            "189",
            "191",
            "190",
            "193",
            "195",
            "197",
            "GYM",
            "POOL"
    };

    private static String[] secondFloorRooms = {
            "280",
            "281",
            "279",
            "277",
            "275",
            "273",
            "271",
            "274",
            "272",
            "270",
            "251",
            "249",
            "247",
            "250",
            "MEDIA",
            "256",
            "253",
            "254",
            "252",
            "283",
            "285",
            "287",
            "288",
            "289",
            "291",
            "290",
            "293",
            "295",
            "222",
            "217",
            "220",
            "218",
            "216",
            "210",
            "208",
            "206",
            "205",
            "203",
            "204",
            "202",
            "201",
            "200",
            "228",
            "AUDITORIUM",
            "230",
            "232",
            "235",
            "236",
            "234"
    };

    private static String[] thirdFloorRooms = {
            "300",
            "301",
            "303",
            "304",
            "305",
            "306",
            "308",
            "310",
            "312",
            "314",
            "316",
            "321",
            "322",
            "323",
            "324",
            "325",
            "326",
            "327",
            "328",
            "340",
            "342",
            "343",
            "344",
            "345",
            "350",
            "351",
            "352",
            "355",
            "360",
            "371",
            "373",
            "375",
            "377",
            "379",
            "381",
            "383",
            "385",
            "387",
            "388",
            "389",
            "390",
            "391",
            "393",
            "395",
            "397"
    };
}
