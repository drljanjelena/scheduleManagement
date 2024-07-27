import javax.swing.text.html.HTMLDocument;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main{
    public static void main(String[] args) {
        try{
            Class.forName("ScheduleManager2");
        } catch (Exception e){
            e.printStackTrace();
        }
        AbstractScheduleManager specification = Specification.getAbstractScheduleManager();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        Scanner sc = new Scanner(System.in);
        String command = "";

        System.out.println("If you want to see all the commands, do -h or help");
        while (!command.equals("exit")){
            command = sc.nextLine();
            if(command.equals("-h") || command.equalsIgnoreCase("help")){
                System.out.println("All values you enter for a command, should be separated with space if its not said diffrently");
                System.out.println("Please enter all dates in the following format: dd.MM.yyyy.");
                System.out.println("Commands you can use: ");
                System.out.println("1.ROOM RELATED COMMANDS");
                System.out.println("LoadRooms path");
                System.out.println("    path to file where your file with rooms is");
                System.out.println("SaveRoomsToFile path");
                System.out.println("    path to file where you want to save rooms");
                System.out.println("AddRoom roomName");
                System.out.println("    roomName, a name you want added room to have");
                System.out.println("AddRooms roomName1 roomName2 roomName3...");
                System.out.println("    roomNames, a list of roomNames you want to add");
                System.out.println("GetRoom roomName");
                System.out.println("    roomName, a name of a room you want to get data from");
                System.out.println("DeleteRoom roomName");
                System.out.println("    roomName, a name of a room you want to delete");
                System.out.println("SetRoomProperty roomName propertyName propertyValue");
                System.out.println("    roomName, a name of a room you want to add property to");
                System.out.println("    propertyName, a name of a property you want to add");
                System.out.println("    propertyValue, a value of a property you want to add");
                System.out.println("GetRoomProperty roomName propertyName");
                System.out.println("    roomName, a name of a room you want to get propertyValue of");
                System.out.println("    propertyName, a name of a property you want to get");
                System.out.println("HasRoomProperty roomId propertyName");
                System.out.println("    roomName, a name of a room you want to check");
                System.out.println("    propertyName, a name of a property you want to check");
                System.out.println("FilterRoomsByProperties propertyName1 propertyValue1 propertyName2 propertyValue2...");
                System.out.println("    propertyName, a name of a property you want to filter by");
                System.out.println("    propertyValue, a value of a property you want to add to filtered rooms");
                System.out.println("1.TERMS RELATED COMMANDS");
                System.out.println("LoadScheduleFromFile filePath startDate endDate excludedDay1 excludedDay2 excludedDay3...");
                System.out.println("    path to file where your file with terms is");
                System.out.println("    startDate terms that start after startDate");
                System.out.println("    endDate terms that end before endDate");
                System.out.println("    excludedDay1 one(or more) days, that need to be excluded from term schedule");
                System.out.println("SaveScheduleToFile path");
                System.out.println("    path to file where you want to save terms(Schedule)");
                System.out.println("SaveSpecificScheduleToFile filePath criteriaName1 criteriaValue1 criteriaName2 criteriaValue2...");
                System.out.println("    path to file where you want to save terms(Schedule)");
                System.out.println("    criteriaName, a name of a property you want to filter by");
                System.out.println("    criteriaValue, a value of a property you want to add to filtered terms");
                System.out.println("AddTerm roomName startDate endDate day time");
                System.out.println("    roomName, a name of a room you want your term to be in");
                System.out.println("    startDate, a date when your term starts");
                System.out.println("    endDate, a date when your term ends");
                System.out.println("    day,day of a term");
                System.out.println("AddTerms roomName (startDate1,endDate1,day1,time1) (startDate2,endDate2,day2,time2)");
                System.out.println("    roomName, a name of a room you want your term to be in");
                System.out.println("    startDate1, a date when your term starts");
                System.out.println("    endDate1, a date when your term ends");
                System.out.println("    day1,day of a term");
                System.out.println("    time1, time of a term(format hh:MM-hh:MM)");
                System.out.println("    put propertys of one term into brackets with coma in between");
                System.out.println("SetTermProperty roomName startDate endDate day time propertyName propertyValue");
                System.out.println("    roomName, a name of a room of term you want to add property in");
                System.out.println("    startDate, a date when your term starts");
                System.out.println("    endDate, a date when your term ends");
                System.out.println("    day,day of a term");
                System.out.println("    time, time of a term(format hh:MM-hh:MM)");
                System.out.println("    propertyName, a name of a property you want to add");
                System.out.println("    propertyValue, a value of a property you want to add");
                System.out.println("DeleteTerm roomName startDate endDate day time");
                System.out.println("    roomName, a name of a term you want to delete");
                System.out.println("    startDate, a date when term for deleteing starts");
                System.out.println("    endDate, a date when term for deleteing ends");
                System.out.println("    day,day of a term for deleteing");
                System.out.println("    time, time of a term(format hh:MM-hh:MM) for deleteing");
                System.out.println("MoveTerm roomId originalStartDate originalEndDate " +
                        "                           newStartDate newEndDate " +
                        "                           originalDay newDay " +
                                                    "originalTime newTime");
                System.out.println("    roomName, a name of a term you want to move");
                System.out.println("    originalStartDate, a date when original term starts");
                System.out.println("    originalEndDate, a date when original term ends");
                System.out.println("    newStartDate, a date when moved term starts");
                System.out.println("    newEndDate, a date when moved term ends");
                System.out.println("    originalDay,day of a original term for moving");
                System.out.println("    newDay,new day of a term for moving");
                System.out.println("    originalTime,original time of a term(format hh:MM-hh:MM) for moving");
                System.out.println("    newTime,new time of a term(format hh:MM-hh:MM) for moving");
                System.out.println("IsRoomOccupied roomName startDate endDate day time");
                System.out.println("    roomName, a name of a term you want to check availability for");
                System.out.println("    startDate, a start date of term you want to check availability for");
                System.out.println("    endDate, a end date of term you want to check availability for");
                System.out.println("    day,day of a term you want to check availability");
                System.out.println("    time, time of a term(format hh:MM-hh:MM) you want to check availability");
                System.out.println("FilterTerms propertyName1 propertyValue1 propertyName2 propertyValue2...");
                System.out.println("    propertyName, a name of a property you want to filter by");
                System.out.println("    propertyValue, a value of a property you want to add to filtered terms");
                System.out.println("exit, leave the program");

            }else if (command.startsWith("LoadRooms")){
                String[] str = command.split(" ");
                boolean loading = specification.loadRoomsFromFile(str[1]);
                if(loading){
                    System.out.println("Rooms are succceffuly loaded!");
                }
            }else if(command.startsWith("SaveRoomsToFile")){
                String[] str = command.split(" ");
                boolean write = specification.saveRoomsToFile(str[1]);
                if (write){
                    System.out.println("Rooms are saved on path you wrote.");
                }
            }else if(command.startsWith("AddRoom") && !command.startsWith("AddRooms")){
                String[] str = command.split(" ");
                specification.addRoom(str[1]);
                System.out.println("Room with name :" + str[1] + " is succesffuly added");
            }else if (command.startsWith("AddRooms")){
                String[] str = command.split(" ");
                List<String> rooms = new ArrayList<>();
                for(int i=1; i< str.length; i++){
                    rooms.add(str[i]);
                }
                specification.addRooms(rooms);
                System.out.println("Rooms are successfully added");
            }else if(command.startsWith("GetRoom")){
                String[] str = command.split(" ");
                Map<String,Object> room = specification.getRoom(str[1]);
                System.out.println(room);
            }else if (command.startsWith("DeleteRoom")){
                String[] str = command.split(" ");
                specification.deleteRoom(str[1]);
                System.out.println("Room with name : " + str[1] + "is deleted");
            }else if(command.startsWith("SetRoomProperty")){
                String[] str = command.split(" ");
                specification.setRoomProperty(str[1],str[2],str[3]);
                System.out.println("Room with name :" + str[1] + " had property set");
            }else if(command.startsWith("GetRoomProperty")){
                String[] str = command.split(" ");
                Object o = specification.getRoomProperty(str[1],str[2]);
                System.out.println(o);
            }else if (command.startsWith("HasRoomProperty")){
                String[] str = command.split(" ");
                boolean b = specification.hasRoomProperty(str[1],str[2]);
                System.out.println(b);
            }else if(command.startsWith("FilterRoomsByProperties")){
                String[] str = command.split(" ");
                Map<String, Object> criteria = new HashMap<>();
                for (int i = 1; i < str.length; i += 2) {
                    if (i + 1 < str.length) {
                        criteria.put(str[i], str[i + 1]);
                    }
                }
                List<String> filteredRooms = specification.filterRoomsByProperties(criteria);
                System.out.println(filteredRooms);
            }else if(command.startsWith("LoadScheduleFromFile")){
                String[] str = command.split(" ");
                String path = str[1];
                Date startDate = null;
                Date endDate = null;
                List<Date> excluded = new ArrayList<>();
                try {
                    startDate = dateFormat.parse(str[2]);
                    endDate = dateFormat.parse(str[3]);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                for(int i=4; i<str.length; i++){
                    try {
                        excluded.add(dateFormat.parse(str[i]));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                boolean load = specification.loadScheduleFromFile(path,startDate,endDate,excluded);
                System.out.println(load);
            }else if(command.startsWith("SaveScheduleToFile")){
                String[] str = command.split(" ");
                boolean saved = specification.saveScheduleToFile(str[1]);
                System.out.println(saved);
            }else if(command.startsWith("SaveSpecificScheduleToFile")){
                String[] str = command.split(" ");
                String path = str[1];
                Map<String, Object> criteria = new HashMap<>();
                for (int i = 1; i < str.length; i += 2) {
                    if (i + 1 < str.length) {
                        criteria.put(str[i], str[i + 1]);
                    }
                }
                boolean save = specification.saveSpecificScheduleToFile(path,criteria);
                System.out.println(save);
            }else if(command.startsWith("DeleteTerm")){
                String[] str = command.split(" ");
                try {
                    specification.deleteTerm1(str[1],dateFormat.parse(str[2]),dateFormat.parse(str[3]),str[4],str[5]);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }else if(command.startsWith("AddTerm") && !command.startsWith("AddTerms")){
                String[] str = command.split(" ");
            try {
                specification.addTerm1(str[1],dateFormat.parse(str[2]),dateFormat.parse(str[3]),str[4],str[5]);
                System.out.println("Term is added!");
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            }else if(command.startsWith("AddTerms")){
                String[] str = command.split(" ");
                List<Map<String, Object>> termini = new ArrayList<>();
                for (int i=2; i<str.length; i++) {
                    System.out.println(str[i]);
                    str[i] = str[i].replaceAll("\\(|\\)", "");
                    String[] a = str[i].split(",");
                    for(int j=0; j<a.length; j++){
                        System.out.println(a[j]);
                    }

                    Map<String, Object> mapa = new HashMap<>();
                    try {
                        mapa.put("startDate", dateFormat.parse(a[0]));
                        mapa.put("endDate", dateFormat.parse(a[1]));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    mapa.put("Dan", a[2]);
                    mapa.put("Termin", a[3]);

                    termini.add(mapa);
                }
                specification.addTerms(str[1],termini);
                System.out.println("Terms are added!");
            }else if(command.startsWith("FilterTerms")){
                String[] str = command.split(" ");
                Map<String, Object> criteria = new HashMap<>();
                for (int i = 1; i < str.length; i += 2) {
                    if (i + 1 < str.length) {
                        if (str[i+1].contains("-")){
                            str[i+1] = str[i+1].replaceAll("-", " ");
                        }
                        criteria.put(str[i], str[i + 1]);
                    }
                }
                System.out.println(criteria);
                List<Map<String,Object>> filteredRooms = specification.filterTermsByProperties(criteria);
                System.out.println(filteredRooms);
            }else if(command.startsWith("IsRoomOccupied")){
                String[] str = command.split(" ");
                boolean b = false;
                try {
                    if(str.length == 6) {
                        b = specification.isRoomOccupiedDuringTerm2(str[1], dateFormat.parse(str[2]), dateFormat.parse(str[3]), str[4], str[5]);
                    }else if(str.length == 7){
                        b = specification.isRoomOccupiedDuringTerm2(str[1] + " " + str[2], dateFormat.parse(str[3]), dateFormat.parse(str[4]), str[5], str[6]);
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(b);
            }else if(command.startsWith("MoveTerm")){
                String[] str = command.split(" ");
                try {
                    if(str.length == 10) {
                        specification.moveTerm1(str[1],
                                dateFormat.parse(str[2]), dateFormat.parse(str[3]),
                                dateFormat.parse(str[4]), dateFormat.parse(str[5]),
                                str[6], str[7], str[8], str[9]);
                    }else if(str.length == 11){
                        specification.moveTerm1(str[1] + " " + str[2],
                                dateFormat.parse(str[3]), dateFormat.parse(str[4]),
                                dateFormat.parse(str[5]), dateFormat.parse(str[6]),
                                str[7], str[8], str[9], str[10]);
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }else if(command.startsWith("SetTermProperty")){
                String[] str = command.split(" ");
                for (int i = 1; i < str.length; i += 2) {
                       str[i].contains("-");
                        str[i] = str[i].replaceAll("-", " ");
                }
                try {
                    specification.setTermProperty1(str[1],dateFormat.parse(str[2]),dateFormat.parse(str[3]),
                                                    str[4],str[5],str[6],str[7]);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

            }else if(command.startsWith("Printout terms")){
                specification.printOutTerm();
            }else if(command.startsWith("Printout rooms")){
                specification.printOutRooms();
            }else if(command.startsWith("SaveSpecific")){
                String[] str = command.split(" ");
                Map<String, Object> criteria = new HashMap<>();
                for (int i = 2; i < str.length; i += 2) {
                    if (i + 1 < str.length) {
                        if(str[i+1].contains("-")){
                            str[i+1] = str[i+1].replaceAll("-", " ");
                        }
                        criteria.put(str[i], str[i + 1]);
                    }
                }
                specification.saveSpecificScheduleToFile(str[1],criteria);
                System.out.println("Terms are saved on path you wrote.");
            }
        }
    }
}

/** <dependency>
 <groupId>org.example</groupId>
 <artifactId>Domaci1-Implementacija1</artifactId>
 <version>1.0.0</version>
 </dependency>
 */