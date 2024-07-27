import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class main {
    public static void main(String[] args) throws ParseException {
        ScheduleManager2 scheduleManager2 = new ScheduleManager2();

        String date1 = "01.10.2023.";
        String date2 = "31.12.2024.";
        String date3 = "01.01.2028.";
        String date4 = "02.12.2023.";
        String date5 = "04.01.2024.";
        String date6 = "20.10.2027.";
        String date7 = "20.01.2024.";
        String date8 = "20.10.2030.";

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy.");
        Date datum1 = simpleDateFormat.parse(date1);
        Date datum2 = simpleDateFormat.parse(date2);
        Date datum3 = simpleDateFormat.parse(date3);
        Date datum4 = simpleDateFormat.parse(date4);
        Date datum5 = simpleDateFormat.parse(date5);
        Date datum6 = simpleDateFormat.parse(date6);
        Date datum7 = simpleDateFormat.parse(date7);
        Date datum8 = simpleDateFormat.parse(date8);


        List<Date> lista = new ArrayList();
        lista.add(datum3);

        boolean loaded = scheduleManager2.loadScheduleFromFile("C:\\Users\\jovan\\Desktop\\komponente\\Domaci1 - Implementacija2\\Domaci1-Implementacija2\\src\\main\\resources\\raspored.csv",
                datum1,datum2,lista);
        System.out.println(loaded);
        scheduleManager2.printOutTerm();

        boolean rooms = scheduleManager2.loadRoomsFromFile("C:\\Users\\jovan\\Desktop\\komponente\\Domaci1 - Implementacija2\\Domaci1-Implementacija2\\src\\main\\resources\\rooms1.csv");
        System.out.println(rooms);
       // scheduleManager2.deleteRoom("ucionica4");
        scheduleManager2.printOutRooms();

        List<Map<String, Object>> filteredTerms;
        Map<String,Object> criteria = new HashMap<>();
       // criteria.put("Nastavnik", "Redzic Nikola Jovan");
        criteria.put("room","Raf07 (u)");
        filteredTerms = scheduleManager2.filterTermsByProperties(criteria);
        System.out.println("_______________________________________________________________________________________");
       // scheduleManager2.ispisi(filteredTerms);
        boolean b = scheduleManager2.isRoomOccupiedDuringTerm2(
                "Raf07 (u)",datum4,datum5,"?ET","13:15-15:00");
        System.out.println("Zauzet termin " + b);
        boolean a = scheduleManager2.isRoomOccupiedDuringTerm2(
                "Kolarac1",datum6,datum8,"?ET","09:15-10:00");
        System.out.println("AAAAAAAAAAAAAAAAAAAAAA" + a);

        scheduleManager2.addTerm1("Raf07 (u)",datum6,datum8,"?ET","13:15-15:00");
        scheduleManager2.setTermProperty1("Raf07 (u)",datum6,datum8,"?ET","13:15-15:00","Nastavnik", "Jelena Drljan");

        System.out.println(scheduleManager2.getTerms());

       // scheduleManager2.deleteTerm1("Raf07 (u)",datum3,datum6,"?ET","13:15-15:00");

      //  System.out.println(scheduleManager2.getTerms());

     //   scheduleManager2.moveTerm1("Raf07 (u)",datum3,datum6,datum7,datum8,"?ET","PON","13:15-15:00","16:15-18:00");

       // boolean j = scheduleManager2.isRoomOccupiedDuringTerm2(
      //               "Raf07 (u)",datum7,datum8,"PON","16:15-18:00");
      //  System.out.println("Zauzet termin " + j);

        Date startDate, endDate;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy.", Locale.ENGLISH);
        try {
            startDate = dateFormat.parse("01.10.2023.");
            endDate = dateFormat.parse("20.01.2024.");

            List<Map<String, Object>> freeTerms = scheduleManager2.getFreeTerms2(criteria, startDate, endDate);
           // System.out.println("Free terms : " + freeTerms);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolean e = scheduleManager2.saveScheduleToFile("C:\\Users\\jovan\\Desktop\\komponente\\Domaci1 - Implementacija2\\Domaci1-Implementacija2\\src\\main\\resources\\test3.csv");
        System.out.println(e);

       // System.out.println(scheduleManager2.getTerms());

        scheduleManager2.moveTerm1("Raf04 (u)",datum1,datum7,datum6,datum8,
                "UTO","PET","11:15-13","10:15-12:00");
    }

}

