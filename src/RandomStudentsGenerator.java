import java.lang.reflect.Array;
import java.util.*;

public class RandomStudentsGenerator {
    public static void main(String[] args) {
        randomSchedules(1,1300);
//        randomThirdFloorOnlySchedules(1,150);
    }

    public static void randomSchedules(int nStart, int nEnd) {

        ArrayList<String> allRooms = new ArrayList<>();

        Collections.addAll(allRooms, firstFloorRooms);
        Collections.addAll(allRooms, secondFloorRooms);
        Collections.addAll(allRooms, thirdFloorRooms);

//        System.out.println(allRooms);

        ArrayList<String> namesFirstLI = formatFirstLast();

        Collections.shuffle(allRooms);

                System.out.println("#STUDENT_ID,FIRST_NAME LAST_INITIAL,ROOM_1,ROOM_2,ROOM_3,ROOM_4");
        for (int i = nStart; i <= nEnd; i++) {
            System.out.print("000" + i);
            System.out.print("," + namesFirstLI.get((int)(Math.random()*namesFirstLI.size())));
            for (int period = 0; period < 4; period++) {
                System.out.print("," + allRooms.get((int) (Math.random() * allRooms.size() * 0.6 )));
            }
            System.out.println();
        }
    }

    public static void randomFirstFloorOnlySchedules(int nStart, int nEnd) {


        /*
        Arrays.sort(firstFloorRooms);
        for (String r : firstFloorRooms) {
            System.out.println(r);
        }
         */


        for (int i = nStart; i <= nEnd; i++) {
            System.out.println("000" + i);
            System.out.println(firstFloorRooms[(int) (Math.random() * firstFloorRooms.length)]);
            System.out.println(firstFloorRooms[(int) (Math.random() * firstFloorRooms.length)]);
            System.out.println(firstFloorRooms[(int) (Math.random() * firstFloorRooms.length)]);
            System.out.println(firstFloorRooms[(int) (Math.random() * firstFloorRooms.length)]);
        }


    }

    public static void randomSecondFloorOnlySchedules(int nStart, int nEnd) {

        /*
        Arrays.sort(secondFloorRooms);
        for (String r : secondFloorRooms) {
            System.out.println(r);
        }
         */


        for (int i = nStart; i <= nEnd; i++) {
            System.out.println("000" + i);
            System.out.println(secondFloorRooms[(int) (Math.random() * secondFloorRooms.length)]);
            System.out.println(secondFloorRooms[(int) (Math.random() * secondFloorRooms.length)]);
            System.out.println(secondFloorRooms[(int) (Math.random() * secondFloorRooms.length)]);
            System.out.println(secondFloorRooms[(int) (Math.random() * secondFloorRooms.length)]);
        }


    }

    public static void randomThirdFloorOnlySchedules(int nStart, int nEnd) {


        //prints out rooms in order
        /*
        Arrays.sort(thirdFloorRooms);
        for (int r : thirdFloorRooms) {
            System.out.println(r);
        }
         */


        for (int i = nStart; i <= nEnd; i++) {
            System.out.println("000" + i);
            System.out.println(thirdFloorRooms[(int) (Math.random() * thirdFloorRooms.length)]);
            System.out.println(thirdFloorRooms[(int) (Math.random() * thirdFloorRooms.length)]);
            System.out.println(thirdFloorRooms[(int) (Math.random() * thirdFloorRooms.length)]);
            System.out.println(thirdFloorRooms[(int) (Math.random() * thirdFloorRooms.length)]);
        }


    }

    public static void randomAttendance(int nStudents) {
        for (int i = 1; i <= nStudents; i++) {
            System.out.println("000" + i);
            System.out.println(new Random().nextBoolean());
        }
    }

    public static ArrayList<String> formatFirstLast() {
        ArrayList<String> nameList = new ArrayList<>();

        while (names.contains("\n")) {
            int indexFirstNewLine = names.indexOf("\n");
            String substring = names.substring(0, indexFirstNewLine);
            nameList.add(substring);
//            System.out.println("Namelist is " + nameList);
            names = names.substring(indexFirstNewLine + 1);
//            System.out.println("Names is " + names);
        }

        nameList.replaceAll((String n) -> n.substring(0, n.indexOf(" ") + 2).concat("."));

        /*
        for (String name : nameList) {
            System.out.println(name);
            System.out.println();
        }
         */

        return nameList;

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

    private static String names = "Isla-Mae Slater\n" +
            "Eisa Gilmore\n" +
            "Trevor Sweet\n" +
            "Rochelle Murray\n" +
            "Rumaysa Hewitt\n" +
            "Francis Ho\n" +
            "Kier Herring\n" +
            "Fatma Peterson\n" +
            "Lindsey Lucero\n" +
            "Kiyan Salter\n" +
            "Samirah Erickson\n" +
            "Arham Prince\n" +
            "Killian Clayton\n" +
            "Daisy-May Robin\n" +
            "Ferne Delgado\n" +
            "Meg Ortega\n" +
            "Beatrix Cotton\n" +
            "Kailan Montgomery\n" +
            "Melanie Devine\n" +
            "Abraham Mack\n" +
            "Ellena Woodard\n" +
            "Farhana Bartlett\n" +
            "Fynley Mcconnell\n" +
            "Caleb Seymour\n" +
            "Kaiser Chan\n" +
            "Amiee Sykes\n" +
            "Amal Kennedy\n" +
            "Leela Vinson\n" +
            "Dottie Booker\n" +
            "Isabelle Esquivel\n" +
            "Daphne Connelly\n" +
            "Kole Rigby\n" +
            "Elana Robles\n" +
            "Casey Le\n" +
            "Ceara Washington\n" +
            "Aamir Mcgrath\n" +
            "Lidia Shah\n" +
            "Teo Lamb\n" +
            "Aj Goddard\n" +
            "Warwick Hail\n" +
            "Ella-Louise Forbes\n" +
            "Ismaeel Villalobos\n" +
            "Lia Armstrong\n" +
            "Zahid Snyder\n" +
            "Teri Parsons\n" +
            "Eshan Larson\n" +
            "Connah Pugh\n" +
            "Kairon Hartley\n" +
            "Ocean Greaves\n" +
            "Fabio Kerr\n" +
            "Maureen Akhtar\n" +
            "Amayah Paul\n" +
            "Elliott Christian\n" +
            "Maaria Gonzalez\n" +
            "Harlee Herbert\n" +
            "Nojus Cameron\n" +
            "Kelsi Cherry\n" +
            "Jose Cook\n" +
            "Leroy Costa\n" +
            "Samiha Oliver\n" +
            "Eleanor Rice\n" +
            "Edison Wilkerson\n" +
            "Damon Pittman\n" +
            "Balraj Burke\n" +
            "Tasha Irvine\n" +
            "Nur Worthington\n" +
            "Shakira Ferguson\n" +
            "Laylah Hills\n" +
            "Maddie Samuels\n" +
            "Xavier Luna\n" +
            "Steve Harrington\n" +
            "Ptolemy Bautista\n" +
            "Cairon Joyner\n" +
            "Nayan Hamer\n" +
            "Sila Ali\n" +
            "Sullivan Michael\n" +
            "Skylah Dickens\n" +
            "Joni Randall\n" +
            "Aniqa Mccall\n" +
            "Octavia Holmes\n" +
            "Sadia Russell\n" +
            "Huey Medrano\n" +
            "Willis Vaughn\n" +
            "Nabil Fisher\n" +
            "Osian Allison\n" +
            "Vickie Doherty\n" +
            "Giovanni Mcbride\n" +
            "Mira Graves\n" +
            "Camden Gaines\n" +
            "Hibah Fowler\n" +
            "Adam Eastwood\n" +
            "Roseanna Carty\n" +
            "Mylo Camacho\n" +
            "Hisham Harwood\n" +
            "Kavita Holloway\n" +
            "Sonny Warren\n" +
            "Marcelina Mayer\n" +
            "Tara Corona\n" +
            "Yash Xiong\n" +
            "Amanah Farrow\n" +
            "Ida Guerra\n" +
            "Jad Bray\n" +
            "Lynsey Esquivel\n" +
            "Kieran Humphreys\n" +
            "Kofi Howe\n" +
            "Isobella Erickson\n" +
            "Jacob Crouch\n" +
            "Oliwier Winters\n" +
            "Elyas Croft\n" +
            "Mikael Daniels\n" +
            "Jaxx Britt\n" +
            "Carter Hudson\n" +
            "Eileen Mahoney\n" +
            "Cormac Moses\n" +
            "Miya Colon\n" +
            "Nathaniel Hamer\n" +
            "Jamel Knapp\n" +
            "Hywel Kennedy\n" +
            "Kristopher Hendrix\n" +
            "Princess Valencia\n" +
            "Jensen Timms\n" +
            "Mimi Burris\n" +
            "Tayah Dorsey\n" +
            "Cherie Wooten\n" +
            "Lily-May Lambert\n" +
            "Kevin Tomlinson\n" +
            "Rosanna Hunt\n" +
            "Denzel Dunne\n" +
            "Habib Daugherty\n" +
            "Zane Martin\n" +
            "Tolga Moreno\n" +
            "Jordana Mackenzie\n" +
            "Sheena Salas\n" +
            "Rimsha Whitley\n" +
            "Aaron Peck\n" +
            "Mahad Lu\n" +
            "Aleksandra Maynard\n" +
            "Emil Wardle\n" +
            "Denis Barrow\n" +
            "Kamile Bull\n" +
            "August Morley\n" +
            "Jade Callahan\n" +
            "Callie Sutherland\n" +
            "Richard Keenan\n" +
            "Jay Mora\n" +
            "Alessia Whitfield\n" +
            "Alana Terrell\n" +
            "Rachel Weaver\n" +
            "Marsha Harrell\n" +
            "Neel Wickens\n" +
            "Jevon Blevins\n" +
            "Rhonda Herbert\n" +
            "Samson Irwin\n" +
            "Inara Gordon\n" +
            "Zakariyah Franks\n" +
            "Arman Busby\n" +
            "Amir Hart\n" +
            "Marcie Gentry\n" +
            "Clyde Sinclair\n" +
            "Irfan Macdonald\n" +
            "Anam Lynn\n" +
            "Umaiza Samuels\n" +
            "Saoirse Hahn\n" +
            "Lexi-Mai Clarkson\n" +
            "Serenity Wade\n" +
            "Habibah Pike\n" +
            "Corrina Wood\n" +
            "Shea Carlson\n" +
            "Connar Rush\n" +
            "Finnian Mccarty\n" +
            "Misha Chase\n" +
            "Cristian Cabrera\n" +
            "Maxine Snider\n" +
            "Archie Pritchard\n" +
            "Sofie Welsh\n" +
            "Isma Kelly\n" +
            "Meredith Dickerson\n" +
            "Noor Short\n" +
            "Ahmet Cresswell\n" +
            "Roseanne Weston\n" +
            "Shauna Mackie\n" +
            "Moses Arellano\n" +
            "Amit Bishop\n" +
            "Karolina Graham\n" +
            "Beau Tait\n" +
            "Yaseen Cohen\n" +
            "Billy-Joe Sears\n" +
            "Sophie-Louise Garner\n" +
            "Tamanna Zavala\n" +
            "Dionne Munro\n" +
            "Malaikah Barber\n" +
            "Kerrie Sampson\n" +
            "Andreas Pearce\n" +
            "Neve Day\n" +
            "Lily Cairns\n" +
            "Nichola Church\n" +
            "Anabel Costa\n" +
            "Ziva Preece\n" +
            "Bryce Pickett\n" +
            "Danniella Baker\n";
}
