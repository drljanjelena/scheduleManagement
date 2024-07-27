import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class Test {

    public static void main(String[] args) {
        testAddRoom(new ScheduleManager());
        testDeleteRoom(new ScheduleManager());
        testSetAndGetRoomProperty(new ScheduleManager());
        testAddTerm(new ScheduleManager());
        testDeleteTerm(new ScheduleManager());
        testMoveTerm(new ScheduleManager());

        testFilterRoomsByProperties(new ScheduleManager());
        testFilterTermsByProperties(new ScheduleManager());
        testFilterTermsByComparison(new ScheduleManager());
        testGetFreeTerms(new ScheduleManager());
        testGetFreeTerms2(new ScheduleManager());

        String pathToResources = "C:\\Users\\jovan\\Desktop\\komponente\\Domaci1 - Implementacija1\\Domaci1 - Implementacija1\\src\\main\\resources\\";
        testLoadRoomsFromFile(new ScheduleManager(), pathToResources + "sampleRooms.json");
        testSaveRoomsToFile(new ScheduleManager(), pathToResources + "testRoomsOutput.json");
        testLoadScheduleFromFile(new ScheduleManager(), pathToResources + "sampleSchedule.json");
        testSaveScheduleToFile(new ScheduleManager(), pathToResources + "testScheduleOutput.json");
    }

    private static void testAddRoom(ScheduleManager manager) {
        System.out.println("-------------------------------------");
        System.out.println("Test addRoom:");

        String roomId = "Room1";
        boolean testPassed = false;

        try {
            manager.addRoom(roomId);
            try {
                manager.addRoom(roomId);
                System.out.println("Test addRoom failed: Expected IllegalArgumentException for duplicate room ID");
            } catch (IllegalArgumentException e) {
                testPassed = true;
            }
        } catch (Exception e) {
            System.out.println("Test addRoom failed: Unexpected exception occurred");
        }

        System.out.println("Test addRoom succeeded: " + testPassed);
        System.out.println("-------------------------------------");
    }

    private static void testDeleteRoom(ScheduleManager manager) {
        System.out.println("-------------------------------------");
        System.out.println("Test deleteRoom:");

        String roomId = "Room2";
        manager.addRoom(roomId);
        boolean testPassed = false;

        try {
            manager.deleteRoom(roomId);
            try {
                manager.getRoom(roomId);
                System.out.println("Test deleteRoom failed: Expected IllegalArgumentException for deleted room");
            } catch (IllegalArgumentException e) {
                testPassed = true;
            }
        } catch (Exception e) {
            System.out.println("Test deleteRoom failed: Unexpected exception occurred");
        }

        System.out.println("Test deleteRoom succeeded: " + testPassed);
        System.out.println("-------------------------------------");
    }

    private static void testSetAndGetRoomProperty(ScheduleManager manager) {
        System.out.println("-------------------------------------");
        System.out.println("Test setAndGetRoomProperty:");

        String roomId = "Room3";
        manager.addRoom(roomId);
        String propertyKey = "capacity";
        int propertyValue = 50;
        boolean testPassed = false;

        try {
            manager.setRoomProperty(roomId, propertyKey, propertyValue);
            Object retrievedValue = manager.getRoomProperty(roomId, propertyKey);

            if (retrievedValue.equals(propertyValue)) {
                testPassed = true;
            } else {
                System.out.println("Test setAndGetRoomProperty failed: Retrieved value does not match set value");
            }
        } catch (Exception e) {
            System.out.println("Test setAndGetRoomProperty failed: Unexpected exception occurred");
        }

        System.out.println("Test setAndGetRoomProperty succeeded: " + testPassed);
        System.out.println("-------------------------------------");
    }


    private static void testAddTerm(ScheduleManager manager) {
        System.out.println("-------------------------------------");
        System.out.println("Test addTerm:");

        String roomId = "Room4";
        manager.addRoom(roomId);
        Date startDate = new GregorianCalendar(2023, Calendar.JANUARY, 1).getTime();
        Date endDate = new GregorianCalendar(2023, Calendar.JANUARY, 10).getTime();
        boolean testPassed = false;

        try {
            manager.addTerm(roomId, startDate, endDate);
            List<Map<String, Object>> terms = manager.getTerms().get(roomId);

            if (terms != null && !terms.isEmpty() && terms.get(0).get("startDate").equals(startDate) && terms.get(0).get("endDate").equals(endDate)) {
                testPassed = true;
            } else {
                System.out.println("Test addTerm failed: Term not added correctly");
            }
        } catch (Exception e) {
            System.out.println("Test addTerm failed: Unexpected exception occurred");
        }

        System.out.println("Test addTerm succeeded: " + testPassed);
        System.out.println("-------------------------------------");
    }

    private static void testDeleteTerm(ScheduleManager manager) {
        System.out.println("-------------------------------------");
        System.out.println("Test deleteTerm:");

        String roomId = "Room5";
        manager.addRoom(roomId);
        Date startDate = new GregorianCalendar(2023, Calendar.FEBRUARY, 1).getTime();
        Date endDate = new GregorianCalendar(2023, Calendar.FEBRUARY, 10).getTime();
        manager.addTerm(roomId, startDate, endDate);
        boolean testPassed = false;

        try {
            manager.deleteTerm(roomId, startDate, endDate);
            List<Map<String, Object>> terms = manager.getTerms().get(roomId);

            if (terms == null || terms.isEmpty() || !terms.get(0).get("startDate").equals(startDate)) {
                testPassed = true;
            } else {
                System.out.println("Test deleteTerm failed: Term not deleted correctly");
            }
        } catch (Exception e) {
            System.out.println("Test deleteTerm failed: Unexpected exception occurred");
        }

        System.out.println("Test deleteTerm succeeded: " + testPassed);
        System.out.println("-------------------------------------");
    }

    private static void testMoveTerm(ScheduleManager manager) {
        System.out.println("-------------------------------------");
        System.out.println("Test moveTerm:");

        String roomId = "Room6";
        manager.addRoom(roomId);
        Date originalStartDate = new GregorianCalendar(2023, Calendar.MARCH, 1).getTime();
        Date originalEndDate = new GregorianCalendar(2023, Calendar.MARCH, 10).getTime();
        Date newStartDate = new GregorianCalendar(2023, Calendar.MARCH, 11).getTime();
        Date newEndDate = new GregorianCalendar(2023, Calendar.MARCH, 20).getTime();
        manager.addTerm(roomId, originalStartDate, originalEndDate);
        boolean testPassed = false;

        try {
            manager.moveTerm(roomId, originalStartDate, originalEndDate, newStartDate, newEndDate);
            List<Map<String, Object>> terms = manager.getTerms().get(roomId);

            if (terms != null && !terms.isEmpty() && terms.get(0).get("startDate").equals(newStartDate) && terms.get(0).get("endDate").equals(newEndDate)) {
                testPassed = true;
            } else {
                System.out.println("Test moveTerm failed: Term not moved correctly");
            }
        } catch (Exception e) {
            System.out.println("Test moveTerm failed: Unexpected exception occurred");
        }

        System.out.println("Test moveTerm succeeded: " + testPassed);
        System.out.println("-------------------------------------");
    }

    private static void testLoadRoomsFromFile(ScheduleManager manager, String filePath) {
        System.out.println("-------------------------------------");
        System.out.println("Test loadRoomsFromFile:");

        boolean loadResult = manager.loadRoomsFromFile(filePath);
        boolean testPassed = loadResult && verifyLoadedRooms(manager, filePath);

        System.out.println("Test loadRoomsFromFile succeeded: " + testPassed);
        System.out.println("-------------------------------------");
    }

    private static boolean verifyLoadedRooms(ScheduleManager manager, String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Map<String, Object>>>() {}.getType();
            Map<String, Map<String, Object>> expectedRooms = gson.fromJson(reader, type);

            return expectedRooms.equals(manager.getRooms());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void testSaveRoomsToFile(ScheduleManager manager, String filePath) {
        System.out.println("-------------------------------------");
        System.out.println("Test saveRoomsToFile:");

        boolean saveResult = manager.saveRoomsToFile(filePath);
        boolean testPassed = saveResult && verifySavedRooms(manager, filePath);

        System.out.println("Test saveRoomsToFile succeeded: " + testPassed);
        System.out.println("-------------------------------------");
    }

    private static boolean verifySavedRooms(ScheduleManager manager, String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Map<String, Object>>>() {}.getType();
            Map<String, Map<String, Object>> savedRooms = gson.fromJson(reader, type);

            return savedRooms.equals(manager.getRooms());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void testLoadScheduleFromFile(ScheduleManager manager, String filePath) {
        System.out.println("-------------------------------------");
        System.out.println("Test loadScheduleFromFile:");

        boolean testPassed = manager.loadScheduleFromFile(filePath, new GregorianCalendar(2023, Calendar.JANUARY, 1).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 31).getTime(), new ArrayList<>());
        verifyLoadedSchedule(manager, filePath);

        System.out.println("Test loadScheduleFromFile succeeded: " + testPassed);
        System.out.println("-------------------------------------");
    }

    private static void verifyLoadedSchedule(ScheduleManager manager, String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, List<Map<String, Object>>>>() {}.getType();
            Map<String, List<Map<String, Object>>> expectedSchedule = gson.fromJson(reader, type);

            System.out.println("Expected schedule:");
            System.out.println(expectedSchedule);
            System.out.println("Actual schedule:");
            System.out.println(manager.getTerms());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void testSaveScheduleToFile(ScheduleManager manager, String filePath) {
        System.out.println("-------------------------------------");
        System.out.println("Test saveScheduleToFile:");

        boolean saveResult = manager.saveScheduleToFile(filePath);
        boolean testPassed = saveResult && verifySavedSchedule(manager, filePath);

        System.out.println("Test saveScheduleToFile succeeded: " + testPassed);
        System.out.println("-------------------------------------");
    }

    private static boolean verifySavedSchedule(ScheduleManager manager, String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, List<Map<String, Object>>>>() {}.getType();
            Map<String, List<Map<String, Object>>> savedSchedule = gson.fromJson(reader, type);

            return savedSchedule.equals(manager.getTerms());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void testFilterRoomsByProperties(ScheduleManager manager) {
        System.out.println("-------------------------------------");
        System.out.println("Test filterRoomsByProperties:");

        manager.addRoom("RoomX");
        manager.setRoomProperty("RoomX", "capacity", 40);
        manager.setRoomProperty("RoomX", "hasProjector", true);

        manager.addRoom("RoomY");
        manager.setRoomProperty("RoomY", "capacity", 50);
        manager.setRoomProperty("RoomY", "hasProjector", false);

        Map<String, Object> criteria = new HashMap<>();
        criteria.put("capacity", 40);
        criteria.put("hasProjector", true);

        List<String> filteredRooms = manager.filterRoomsByProperties(criteria);
        boolean testPassed = filteredRooms.contains("RoomX") && !filteredRooms.contains("RoomY");

        System.out.println("Test filterRoomsByProperties succeeded: " + testPassed);
        System.out.println("-------------------------------------");
    }

    private static void testFilterTermsByProperties(ScheduleManager manager) {
        System.out.println("-------------------------------------");
        System.out.println("Test filterTermsByProperties:");

        String roomId = "RoomZ";
        manager.addRoom(roomId);
        Date startDate = new GregorianCalendar(2023, Calendar.JANUARY, 1).getTime();
        Date endDate = new GregorianCalendar(2023, Calendar.JANUARY, 10).getTime();
        manager.addTerm(roomId, startDate, endDate);

        Map<String, Object> criteria = new HashMap<>();
        criteria.put("startDate", startDate);
        criteria.put("endDate", endDate);

        List<Map<String, Object>> filteredTerms = manager.filterTermsByProperties(criteria);
        boolean testPassed = !filteredTerms.isEmpty() && filteredTerms.get(0).get("startDate").equals(startDate) && filteredTerms.get(0).get("endDate").equals(endDate);

        System.out.println("Test filterTermsByProperties succeeded: " + testPassed);
        System.out.println("-------------------------------------");
    }

    private static void testFilterTermsByComparison(ScheduleManager manager) {
        System.out.println("-------------------------------------");
        System.out.println("Test filterTermsByComparison:");

        String roomId = "RoomA1";
        manager.addRoom(roomId);
        Date startDate = new GregorianCalendar(2023, Calendar.FEBRUARY, 1).getTime();
        Date endDate = new GregorianCalendar(2023, Calendar.FEBRUARY, 15).getTime();
        manager.addTerm(roomId, startDate, endDate);

        List<Map<String, Object>> filteredTerms = manager.filterTermsByComparison("startDate", new GregorianCalendar(2023, Calendar.JANUARY, 31).getTime(), 1);
        boolean testPassed = !filteredTerms.isEmpty() && filteredTerms.get(0).get("startDate").equals(startDate);

        System.out.println("Test filterTermsByComparison succeeded: " + testPassed);
        System.out.println("-------------------------------------");
    }

    private static void testGetFreeTerms(ScheduleManager manager) {
        System.out.println("-------------------------------------");
        System.out.println("Test getFreeTerms:");

        String roomId = "RoomFree";
        manager.addRoom(roomId);

        Date term1Start = new GregorianCalendar(2023, Calendar.MARCH, 1).getTime();
        Date term1End = new GregorianCalendar(2023, Calendar.MARCH, 5).getTime();
        manager.addTerm(roomId, term1Start, term1End);

        Date term2Start = new GregorianCalendar(2023, Calendar.MARCH, 10).getTime();
        Date term2End = new GregorianCalendar(2023, Calendar.MARCH, 15).getTime();
        manager.addTerm(roomId, term2Start, term2End);

        Date queryStart = new GregorianCalendar(2023, Calendar.MARCH, 1).getTime();
        Date queryEnd = new GregorianCalendar(2023, Calendar.MARCH, 20).getTime();

        Date expectedFreeTermStart = new GregorianCalendar(2023, Calendar.MARCH, 5).getTime();
        Date expectedFreeTermEnd = new GregorianCalendar(2023, Calendar.MARCH, 10).getTime();

        List<Map<String, Object>> freeTerms = manager.getFreeTerms(roomId, queryStart, queryEnd);

        System.out.println("Existing Terms: " + manager.getTerms().get(roomId));
        System.out.println("Free Terms Found: " + freeTerms);

        boolean testPassed = freeTerms.stream().anyMatch(term -> term.get("startDate").equals(expectedFreeTermStart) && term.get("endDate").equals(expectedFreeTermEnd));

        System.out.println("Test getFreeTerms succeeded: " + testPassed);
        System.out.println("-------------------------------------");
    }

    private static void testGetFreeTerms2(ScheduleManager manager) {
        System.out.println("-------------------------------------");
        System.out.println("Test getFreeTerms2:");

        String roomId = "RoomFree2";
        manager.addRoom(roomId);

        Date term1Start = new GregorianCalendar(2023, Calendar.MARCH, 1).getTime();
        Date term1End = new GregorianCalendar(2023, Calendar.MARCH, 5).getTime();
        manager.addTerm(roomId, term1Start, term1End);

        Date term2Start = new GregorianCalendar(2023, Calendar.MARCH, 10).getTime();
        Date term2End = new GregorianCalendar(2023, Calendar.MARCH, 15).getTime();
        manager.addTerm(roomId, term2Start, term2End);

        Date queryStart = new GregorianCalendar(2023, Calendar.MARCH, 1).getTime();
        Date queryEnd = new GregorianCalendar(2023, Calendar.MARCH, 20).getTime();

        Date expectedFreeTermStart = new GregorianCalendar(2023, Calendar.MARCH, 5).getTime();
        Date expectedFreeTermEnd = new GregorianCalendar(2023, Calendar.MARCH, 10).getTime();

        List<Map<String, Object>> freeTerms = manager.getFreeTerms2(Collections.emptyMap(), queryStart, queryEnd);

        System.out.println("Existing Terms: " + manager.getTerms().get(roomId));
        System.out.println("Free Terms Found: " + freeTerms);

        boolean testPassed = freeTerms.stream().anyMatch(term -> term.get("startDate").equals(expectedFreeTermStart) && term.get("endDate").equals(expectedFreeTermEnd));

        System.out.println("Test getFreeTerms2 succeeded: " + testPassed);
        System.out.println("-------------------------------------");
    }


}
