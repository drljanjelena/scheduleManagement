import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ScheduleManager2 extends AbstractScheduleManager {
    private final Map<String, Map<String, Object>> rooms;
    private final Map<String, List<Map<String, Object>>> terms;
    private final Map<String, Object> defaultRoomValues;
    private final Map<String, Object> defaultTermValues;

    static {
        Specification.initialize(new ScheduleManager2());
    }

    public Map<String, List<Map<String, Object>>> getTerms() {
        return terms;
    }

    protected void initializeDefaultValues() {
    }

    public ScheduleManager2() {
        rooms = new HashMap<>();
        terms = new HashMap<>();
        defaultRoomValues = new HashMap<>();
        defaultTermValues = new HashMap<>();

        defaultRoomValues.put("capacity", 0);
        defaultRoomValues.put("computerCount", 0);
        defaultRoomValues.put("hasWhiteboard", false);
        defaultRoomValues.put("hasProjector", false);

        defaultTermValues.put("startDate", new Date(0));
        defaultTermValues.put("endDate", new Date(0));
    }

    public Map<String, Map<String, Object>> getRooms() {
        return rooms;
    }

    public void setRoomDefaultPropertyValue(String propertyKey, Object propertyValue) {
        defaultRoomValues.put(propertyKey, propertyValue);
    }

    public Object getRoomDefaultPropertyValue(String propertyKey) {
        return defaultRoomValues.get(propertyKey);
    }

    public void setTermDefaultPropertyValue(String propertyKey, Object propertyValue) {
        defaultTermValues.put(propertyKey, propertyValue);
    }

    public Object getTermDefaultPropertyValue(String propertyKey) {
        return defaultTermValues.get(propertyKey);
    }


    public void addRoom(String roomId) {
        if (rooms.containsKey(roomId)) {
            throw new IllegalArgumentException("Room with id " + roomId + " already exists.");
        }

        rooms.put(roomId, new HashMap<>());
        terms.put(roomId, new ArrayList<>());
    }

    public void addRooms(List<String> roomIds) {
        for (String roomId : roomIds) {
            addRoom(roomId);
        }
    }

    public Map<String, Object> getRoom(String roomId) {
        if (!rooms.containsKey(roomId)) {
            throw new IllegalArgumentException("Room with id " + roomId + " does not exist.");
        }
        return rooms.get(roomId);
    }

    public void deleteRoom(String roomId) {
        if (!rooms.containsKey(roomId)) {
            throw new IllegalArgumentException("Room with id " + roomId + " does not exist.");
        }

        rooms.remove(roomId);
        terms.remove(roomId);
    }

    public void setRoomProperty(String roomId, String propertyKey, Object propertyValue) {

        if (!rooms.containsKey(roomId)) {
            throw new IllegalArgumentException("Room with id " + roomId + " does not exist.");
        }

        if (!defaultRoomValues.containsKey(propertyKey)) {
            this.setRoomDefaultPropertyValue(propertyKey, propertyValue);
            System.out.println("Property " + propertyKey + " does not exist yet. The entered value + " + propertyValue + " has been set as default.");
        }

        rooms.get(roomId).put(propertyKey, propertyValue);
    }

    public Object getRoomProperty(String roomId, String propertyKey) {
        if (!rooms.containsKey(roomId)) {
            throw new IllegalArgumentException("Room with id " + roomId + " does not exist.");
        }

        if (!defaultRoomValues.containsKey(propertyKey)) {
            throw new IllegalArgumentException("Property " + propertyKey + " does not exist.");
        }

        return rooms.get(roomId).getOrDefault(propertyKey, defaultRoomValues.get(propertyKey));
    }

    public boolean hasRoomProperty(String roomId, String propertyKey) {
        if (!rooms.containsKey(roomId)) {
            throw new IllegalArgumentException("Room with id " + roomId + " does not exist.");
        }

        if (!defaultRoomValues.containsKey(propertyKey)) {
            throw new IllegalArgumentException("Property " + propertyKey + " does not exist.");
        }

        return rooms.get(roomId).containsKey(propertyKey) || defaultRoomValues.containsKey(propertyKey);
    }

    public List<String> filterRoomsByProperties(Map<String, Object> criteria) {
        List<String> filteredRooms = new ArrayList<>();
        for (String roomId : rooms.keySet()) {
            boolean roomMatchesCriteria = true;
            for (String propertyKey : criteria.keySet()) {
                if (!hasRoomProperty(roomId, propertyKey) || !getRoomProperty(roomId, propertyKey).equals(criteria.get(propertyKey))) {
                    roomMatchesCriteria = false;
                    break;
                }
            }
            if (roomMatchesCriteria) {
                filteredRooms.add(roomId);
            }
        }
        return filteredRooms;
    }

    public void addTerm(String roomId, Date startDate, Date endDate) {
        if (!rooms.containsKey(roomId)) {
            throw new IllegalArgumentException("Room with id " + roomId + " does not exist.");
        }

        if (this.isRoomOccupiedDuringTerm(roomId, startDate, endDate)) {
            throw new IllegalArgumentException("Room with id " + roomId + " is occupied during term.");
        }

        Map<String, Object> termMap = new HashMap<>();
        termMap.put("startDate", startDate);
        termMap.put("endDate", endDate);
        terms.get(roomId).add(termMap);
    }

    public void addTerm1(String roomId, Date startDate, Date endDate, String day,String time) {
        if (!rooms.containsKey(roomId)) {
            throw new IllegalArgumentException("Room with id " + roomId + " does not exist.");
        }

        if (this.isRoomOccupiedDuringTerm2(roomId, startDate, endDate,day,time)) {
            throw new IllegalArgumentException("Room with id " + roomId + " is occupied during time and day you wanted.");
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy.");
        Map<String, Object> termMap = new HashMap<>();
        termMap.put("startDate", simpleDateFormat.format(startDate));
        termMap.put("endDate", simpleDateFormat.format(endDate));
        termMap.put("Termin", time);
        termMap.put("Dan", day);

        terms.get(roomId).add(termMap);
    }

    public void addTerms(String roomId, List<Map<String, Object>> terms) {
        for (Map<String, Object> term : terms) {
            if (!term.containsKey("startDate") || !term.containsKey("endDate") ||
                    !term.containsKey("Dan") || !term.containsKey("Termin")) {
                throw new IllegalArgumentException("Term must contain startDate,endDate,day and termin.");
            }

            Date startDate = (Date) term.get("startDate");
            Date endDate = (Date) term.get("endDate");
            String dan = (String) term.get("Dan");
            String vreme = (String) term.get("Termin");

            this.addTerm1(roomId, startDate, endDate, dan, vreme);
        }
    }

    public void deleteTerm(String roomId, Date startDate, Date endDate) {
        if (!rooms.containsKey(roomId)) {
            throw new IllegalArgumentException("Room with id " + roomId + " does not exist.");
        }

        List<Map<String, Object>> termini = terms.get(roomId);
        Iterator<Map<String, Object>> iterator = termini.iterator();

        while (iterator.hasNext()) {
            Map<String, Object> term = iterator.next();
            int counter = 0;

            for (Map.Entry<String, Object> entry : term.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (key.equals("startDate")) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                    if (value.equals(dateFormat.format(startDate))) {
                        counter++;
                    }
                } else if (key.equals("endDate")) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                    if (value.equals(dateFormat.format(endDate))) {
                        counter++;
                    }
                } else if (key.equals("Termin")) {
                    String str = (String) value;
                    String[] times = str.split("-");
                    String startTime1 = times[0];
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    String startTime2 = timeFormat.format(startDate);
                    if (startTime1.equals(startTime2)) {
                        counter++;
                    }
                }
            }
            if (counter == 3) {
                iterator.remove();
                System.out.println("Termin je uspeno obrisan.");
            }
        }
    }

    public void deleteTerm1(String roomId, Date startDate, Date endDate,String day, String time) {
        if (!rooms.containsKey(roomId)) {
            throw new IllegalArgumentException("Room with id " + roomId + " does not exist.");
        }

        List<Map<String, Object>> termini = terms.get(roomId);
        Iterator<Map<String, Object>> iterator = termini.iterator();

        while (iterator.hasNext()) {
            Map<String, Object> term = iterator.next();
            int counter = 0;

            for (Map.Entry<String, Object> entry : term.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (key.equals("startDate")) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy.");
                    if (value.equals(dateFormat.format(startDate))) {
                        counter++;
                    }
                } else if (key.equals("endDate")) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy.");
                    if (value.equals(dateFormat.format(endDate))) {
                        counter++;
                    }
                } else if (key.equals("Termin")) {
                    String str = (String) value;
                    String[] times = str.split(":");
                    String startTime1 = times[0];
                    String[] times2 = time.split(":");
                    String startTime2 = times2[0];
                    if (startTime1.equals(startTime2)) {
                        counter++;
                    }
                }else if (key.equals("Dan")) {
                    if (value.equals(day)) {
                        counter++;
                    }
                }
            }
            if (counter == 4) {
                iterator.remove();
                System.out.println("Termin je uspesno obrisan.");
            }
        }
    }

    public void moveTerm1(String roomId, Date originalStartDate, Date originalEndDate,
                                         Date newStartDate, Date newEndDate,
                                          String originalDay, String newDay,
                                          String originalTime, String newTime) {
        if (!rooms.containsKey(roomId)) {
            throw new IllegalArgumentException("Room with id " + roomId + " does not exist.");
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy.");

        String[] s = originalTime.split(":");
        Date formatedoriginalTime = null;
        try {
            formatedoriginalTime = timeFormat.parse(originalTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        boolean termExists = false;
        Map<String, Object> existingTerm = null;
        for (Map<String, Object> term : terms.get(roomId)) {
            String termin = (String) term.get("Termin");
            String[] str = termin.split(":");
            Date formattedTermin = null;
            try {
                formattedTermin = timeFormat.parse(str[0]);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            if (    term.get("startDate").equals(dateFormat.format(originalStartDate)) &&
                    term.get("endDate").equals(dateFormat.format(originalEndDate)) &&
                    formattedTermin.equals(formatedoriginalTime) &&
                    term.get("Dan").equals((originalDay))) {
                System.out.println("usao");
                termExists = true;
                existingTerm = term;
                break;
            }
        }

        if (!termExists) {
            throw new IllegalArgumentException("Term with start date " + originalStartDate + " and end date " + originalEndDate + " does not exist for room " + roomId + ".");
        }

        if (this.isRoomOccupiedDuringTerm2(roomId, newStartDate, newEndDate,newDay,newTime)) {
            throw new IllegalArgumentException("Room with id " + roomId + " is occupied during the new term dates.");
        }

        existingTerm.put("startDate", dateFormat.format(newStartDate));
        existingTerm.put("endDate", dateFormat.format(newEndDate));
        existingTerm.put("Termin", newTime);
        existingTerm.put("Dan", newDay);
        System.out.println("Term is moved " + existingTerm);
    }

        public void moveTerm(String roomId, Date originalStartDate, Date originalEndDate,
                                        Date newStartDate, Date newEndDate) {
        if (!rooms.containsKey(roomId)) {
            throw new IllegalArgumentException("Room with id " + roomId + " does not exist.");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        boolean termExists = false;
        Map<String, Object> existingTerm = null;
        for (Map<String, Object> term : terms.get(roomId)) {
            String str = (String) term.get("Termin");
            String[] times = str.split("-");
            String startTime = times[0];
            if (term.get("startDate").equals(dateFormat.format(originalStartDate))
                    && term.get("endDate").equals(dateFormat.format(originalEndDate))
                    && startTime.equals(timeFormat.format(originalStartDate))) {
                termExists = true;
                existingTerm = term;
                break;
            }
        }

        if (!termExists) {
            throw new IllegalArgumentException("Term with start date " + originalStartDate + " and end date " + originalEndDate + " does not exist for room " + roomId + ".");
        }

          if (this.isRoomOccupiedDuringTerm(roomId, newStartDate, newEndDate)) {
              throw new IllegalArgumentException("Room with id " + roomId + " is occupied during the new term dates.");
          }

        existingTerm.put("startDate", dateFormat.format(newStartDate));
        existingTerm.put("endDate", dateFormat.format(newEndDate));
        existingTerm.put("Termin", timeFormat.format(newStartDate) + "-" + timeFormat.format(newEndDate));
    }

    public void setTermProperty(String roomId, Date startDate, Date endDate, String propertyKey, Object propertyValue) {
        if (!rooms.containsKey(roomId)) {
            throw new IllegalArgumentException("Room with id " + roomId + " does not exist.");
        }

        if (!defaultTermValues.containsKey(propertyKey)) {
            this.setTermDefaultPropertyValue(propertyKey, propertyValue);
            System.out.println("Property " + propertyKey + " does not exist yet. The entered value " + propertyValue + " has been set as default.");
        }

        for (Map<String, Object> term : terms.get(roomId)) {
            String str = (String) term.get("Termin");
            String[] times = str.split("-");
            String startTime1 = times[0];
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String startTime2 = timeFormat.format(startDate);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

            if (term.get("startDate").equals(dateFormat.format(startDate)) &&
                    term.get("endDate").equals(dateFormat.format(endDate)) &&
                    startTime1.equals(startTime2)) {
                term.put(propertyKey, propertyValue);
            }
        }
    }

    public void setTermProperty1(String roomId, Date startDate, Date endDate, String day, String time, String propertyKey, Object propertyValue) {
        if (!rooms.containsKey(roomId)) {
            throw new IllegalArgumentException("Room with id " + roomId + " does not exist.");
        }

        if (!defaultTermValues.containsKey(propertyKey)) {
            this.setTermDefaultPropertyValue(propertyKey, propertyValue);
            System.out.println("Property " + propertyKey + " does not exist yet. The entered value " + propertyValue + " has been set as default.");
        }

        for (Map<String, Object> term : terms.get(roomId)) {
            String str = (String) term.get("Termin");
            String[] times = str.split("-");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH");
            String[] str1 = time.split("-");

            Date startTime1 = null;
            Date startTime2 = null;
            try {
                startTime1 = timeFormat.parse(times[0]);
                startTime2 = timeFormat.parse(str1[0]);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy.");
            System.out.println(term.get("startDate") + " " + dateFormat.format(startDate));
            System.out.println(term.get("startDate").equals(dateFormat.format(startDate)));
            if (term.get("startDate").equals(dateFormat.format(startDate)) &&
                    term.get("endDate").equals(dateFormat.format(endDate)) &&
                    startTime1.equals(startTime2) &&
                    term.get("Dan").equals(day)) {
                term.put(propertyKey, propertyValue);
            }
        }
    }

    public boolean isRoomOccupiedDuringTerm(String roomId, Date startDate, Date endDate) {
        if (!rooms.containsKey(roomId)) {
            throw new IllegalArgumentException("Room with id " + roomId + " does not exist.");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH");

        for (Map<String, Object> term : terms.get(roomId)) {
            String str = (String) term.get("Termin");
            String[] times = str.split("[-:]");
            String termStartTime = times[0];
            String termEndTime = times[2];

            try {
                //vreme iz trenutnog termina
                Date termStartDateTime = timeFormat.parse(termStartTime);
                Date termEndDateTime = timeFormat.parse(termEndTime);

                //datum iz trenutnog termina
                Date termStartDate = dateFormat.parse((String) term.get("startDate"));
                Date termEndDate = dateFormat.parse((String) term.get("endDate"));

                //datum iz prosledjenog termina
                String d = dateFormat.format(startDate);
                Date startDate1 = dateFormat.parse(d);

                String e = dateFormat.format(startDate);
                Date endDate1 = dateFormat.parse(e);

                //vreme iz prosledjenog termina
                String d1 = dateFormat.format(startDate);
                Date startTime = timeFormat.parse(d1);

                String e1 = dateFormat.format(startDate);
                Date endTime = timeFormat.parse(e1);

                if (startDate1.before(termEndDate) && endDate1.after(termStartDate)) {
                    if (!(endTime.before(termStartDateTime) && startTime.after(termEndDateTime))) {
                        return true;
                    }
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

  public boolean isRoomOccupiedDuringTerm2(String roomId, Date startDate, Date endDate, String day, String time) {
      Map<String, Object> criteria = new HashMap<>();
      criteria.put("room", roomId);
      criteria.put("Dan", day);

      List<Map<String, Object>> filteredTerms = filterTermsByProperties(criteria);

      SimpleDateFormat timeFormat = new SimpleDateFormat("HH");
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy.");

      for (Map<String, Object> term : filteredTerms) {
          try {
              String termTime = (String) term.get("Termin");
              String[] termTimes = termTime.split("-");

              // vreme pocetka i kraja trenutnog termina
              Date termStartTime = timeFormat.parse(termTimes[0]);
              Date termEndTime = timeFormat.parse(termTimes[1]);

              // Start i end date trenutnog termina
              Date termStartDate = dateFormat.parse((String) term.get("startDate"));
              Date termEndDate = dateFormat.parse((String) term.get("endDate"));

              if (!(endDate.before(termStartDate) || startDate.after(termEndDate)) &&
                              !(timeFormat.parse(time.split("-")[1]).before(termStartTime) ||
                              timeFormat.parse(time.split("-")[0]).after(termEndTime))) {
                  return true;
              }
          } catch (ParseException e) {
              e.printStackTrace();
          }
      }
      return false;
  }

    public List<Map<String, Object>> filterTermsByProperties(Map<String, Object> criteria) {
        List<Map<String, Object>> filteredTerms = new ArrayList<>();

        for (String roomId : terms.keySet()) {
                for (Map<String, Object> term : terms.get(roomId)) {
                    boolean termMatchesCriteria = true;
                    for (String propertyKey : criteria.keySet()) {
                        if (propertyKey.equals("room")) {
                            if (!roomId.equals(criteria.get(propertyKey))) {
                                termMatchesCriteria = false;
                                break;
                            }
                        }else if (!term.containsKey(propertyKey) || !term.get(propertyKey).equals(criteria.get(propertyKey))) {
                            termMatchesCriteria = false;
                            break;
                        }
                    }
                    term.put("room",roomId);
                    if (termMatchesCriteria) {
                        filteredTerms.add(term);
                    }
                }
        }
        return filteredTerms;
    }

    //?????
    public List<Map<String, Object>> getFreeTerms2(Map<String, Object> criteria, Date startDate, Date endDate) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy.");

        List<Integer> hours = new ArrayList<>();
        for (int i=9; i<=21; i++){
            hours.add(i);
        }
        Map<String, List<Integer>> freeTermsByDay = new HashMap<>();
        List<Map<String, Object>> freeTerms = new ArrayList<>();
        List<Map<String, Object>> filteredTerms = filterTermsByProperties(criteria);

        for(int j=1; j<=5; j++){
        for (Map<String, Object> term : filteredTerms) {
            boolean validStartDate = false;
            boolean validendDate = false;
            int pocetak = 0;
            int kraj = 0;
            for (Map.Entry<String, Object> entry : term.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (key.equals("startDate")) {
                    try {
                        if (dateFormat.parse((String) value).equals(startDate) ||
                                (dateFormat.parse((String) value).after(startDate) &&
                                        dateFormat.parse((String) value).before(endDate))) {
                            validStartDate = true;
                        }
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                } else if (key.equals("endDate")) {
                    try {
                        if (dateFormat.parse((String) value).equals(endDate) ||
                                (dateFormat.parse((String) value).after(startDate) &&
                                        dateFormat.parse((String) value).before(endDate))) {
                            validendDate = true;
                        }
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                } else if (key.equals("Termin")) {
                    String v = (String) value;
                    String[] times = v.split("[:-]");
                    pocetak = Integer.valueOf(times[0]);
                    kraj = Integer.valueOf(times[2]);

               //     for (int i = pocetak; i <= kraj; i++) {
               //         String day = (String) term.get("Dan");
               //         freeTermsByDay.computeIfAbsent(day, k -> new ArrayList<>()).add(i);
               //     }
                }
            }
        }
        }
        return freeTerms;
    }


        public List<Map<String, Object>> getFreeTerms(String roomId, Date startDate, Date endDate) {
        List<Map<String, Object>> freeTerms = new ArrayList<>();

        for (Map<String, Object> term : terms.get(roomId)) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy.");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH");
                Date termStartDate = dateFormat.parse((String) term.get("startDate"));
                Date termEndDate = dateFormat.parse((String) term.get("endDate"));
                Date termStartTime = timeFormat.parse(((String) term.get("Termin")).split("-")[0]);
                Date termEndTime = timeFormat.parse(((String) term.get("Termin")).split("-")[1]);

                boolean b = (termStartDate.equals(startDate) || termStartDate.after(startDate));
                System.out.println(b);
                if ((termStartDate.equals(startDate) || termStartDate.after(startDate)) &&
                        (termEndDate.equals(endDate) || termEndDate.before(endDate))) {

                    Date currentStart = termStartDate;
                    Date currentEnd = termStartTime;

                    while (currentEnd.before(termEndTime) && currentEnd.before(endDate)) {

                        if (currentStart.before(startDate)) {
                            currentStart = new Date(startDate.getTime());
                        }

                        if (currentStart.before(currentEnd) && currentEnd.before(termEndTime) &&
                                currentEnd.before(endDate)) {

                            Map<String, Object> freeTerm = new HashMap<>();
                            freeTerm.put("Dan", term.get("Dan"));
                            freeTerm.put("Termin", timeFormat.format(currentStart) + "-" + timeFormat.format(currentEnd));
                            freeTerms.add(freeTerm);
                        }

                        currentStart = new Date(currentEnd.getTime());
                        currentEnd = new Date(currentStart.getTime() + 3600000);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return freeTerms;
    }

    public boolean loadScheduleFromFile(String filePath, Date startDate, Date endDate, List<Date> excludedDays) {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] header = reader.readNext();
            for(int i=0; i<header.length; i++){
                defaultTermValues.putIfAbsent(header[i],"");
            }

            Map<String, List<Map<String, Object>>> loadedSchedule = new HashMap<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy.");

            String[] line;
            while ((line = reader.readNext()) != null) {
                String roomName = line[0];  //prva kolona mora da bude room
                Map<String, Object> termMap = new HashMap<>();

                for (int i = 1; i < header.length; i++) {  // ostale kolone
                    String columnName = header[i];
                    String value = line[i];
                    termMap.put(columnName, value);
                }

                loadedSchedule.putIfAbsent(roomName, new ArrayList<>());
                rooms.putIfAbsent(roomName,new HashMap<>());
                loadedSchedule.get(roomName).add(termMap);
            }

            for (String roomId : loadedSchedule.keySet()) {
                if (!rooms.containsKey(roomId)) {
                    addRoom(roomId);
                }
                for (Map<String, Object> term : loadedSchedule.get(roomId)) {
                    Date termStartDate = dateFormat.parse((String) term.get("startDate"));
                    Date termEndDate = dateFormat.parse((String) term.get("endDate"));

                    if (termStartDate.before(startDate) || termEndDate.after(endDate)) {
                        continue;
                    }

                    if (excludedDays.stream().anyMatch(date -> date.after(termStartDate) && date.before(termEndDate))) {
                        continue;
                    }

                    terms.putIfAbsent(roomId, new ArrayList<>());
                    terms.get(roomId).add(term);
                }
            }
            return true;
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
            return false;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean saveSpecificScheduleToFile(String filePath, Map<String, Object> criteria) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath), CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)) {

            List<Map<String, Object>> filteredTerms = filterTermsByProperties(criteria);

            if (filteredTerms.isEmpty()) {
                System.out.println("nema filtriranih termina");
                return false;
            }

            Set<String> allKeys = new HashSet<>();
            for (Map<String, Object> term : filteredTerms) {
                allKeys.addAll(term.keySet());
            }

            String[] header = allKeys.toArray(new String[0]);
            writer.writeNext(header);

            for (Map<String, Object> term : filteredTerms) {
                List<String> values = new ArrayList<>();

                for (String propertyKey : header) {
                    Object propertyValue = term.get(propertyKey);
                    values.add(propertyValue != null ? propertyValue.toString() : "");
                }

                writer.writeNext(values.toArray(new String[0]));
            }

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveScheduleToFile(String filePath) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath), CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END)) {
            Set<String> allKeys = new HashSet<>();
            allKeys.add("room");
            for (List<Map<String, Object>> roomTerms : terms.values()) {
                for (Map<String, Object> term : roomTerms) {
                    allKeys.addAll(term.keySet());
                }
            }
            String[] header = allKeys.toArray(new String[0]);
            writer.writeNext(header);

            for (String roomId : terms.keySet()) {
                List<Map<String, Object>> roomTerms = terms.get(roomId);

                for (Map<String, Object> term : roomTerms) {
                    List<String> values = new ArrayList<>();
                    values.add(roomId);
                    for (String propertyKey : header) {
                        Object propertyValue = term.get(propertyKey);
                        values.add(propertyValue != null ? propertyValue.toString() : "");
                    }

                    writer.writeNext(values.toArray(new String[0]));
                }
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean loadRoomsFromFile(String filePath) {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] header = reader.readNext();

            String[] line;
            while ((line = reader.readNext()) != null) {
                String roomName = line[0];

                rooms.putIfAbsent(roomName, new HashMap<>());

                for (int i = 1; i < header.length; i++) {
                    String columnName = header[i];
                    String value = line[i];
                    rooms.get(roomName).put(columnName, value);
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return true;
    }

public boolean saveRoomsToFile(String filePath) {
    try (CSVWriter writer = new CSVWriter(new FileWriter(filePath), CSVWriter.DEFAULT_SEPARATOR,
            CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER,
            CSVWriter.DEFAULT_LINE_END)) {
        String firstRoomName = rooms.keySet().iterator().next();
        Map<String, Object> firstRoomProperties = rooms.get(firstRoomName);
        String[] header = firstRoomProperties.keySet().toArray(new String[0]);

        List<String> headerList = new ArrayList<>();
        headerList.add("roomName");
        for (String propertyKey : header) {
            headerList.add(propertyKey);
        }
        header = headerList.toArray(new String[0]);

        writer.writeNext(header);

        for (String roomName : rooms.keySet()) {
            Map<String, Object> roomProperties = rooms.get(roomName);
            List<String> values = new ArrayList<>();
            values.add(roomName);

            for (String propertyKey : header) {
                if (!propertyKey.equals("roomName")) {
                    Object propertyValue = roomProperties.get(propertyKey);
                    values.add(propertyValue != null ? propertyValue.toString() : "");
                }
            }
            writer.writeNext(values.toArray(new String[0]));
        }

        return true;
    } catch (IOException e) {
        e.printStackTrace();
        return false;
    }
}

    public void ispisi(List<Map<String, Object>> mapa){
        for (Map<String, Object> element : mapa) {
            for (Map.Entry<String, Object> entry : element.entrySet()) {
                System.out.println(entry.getKey() +":"+ entry.getValue());
            }
            System.out.println();
        }
    }

    public void printOutRooms(){
        for (String room : rooms.keySet()) {
            System.out.println(room.toUpperCase());
            Map<String, Object> properties = rooms.get(room);
            for (String key : properties.keySet()) {
                System.out.println(key + ": " + properties.get(key));
            }
            System.out.println();
        }
    }

    public void printOutTerm(){
        for (String room : terms.keySet()) {
            System.out.println("UCIONICA: " + room.toUpperCase());
            System.out.println();
            System.out.println();
            List<Map<String, Object>> termini = terms.get(room);
            for (Map<String, Object> term : termini) {
                for (String key : term.keySet()) {
                    System.out.println(key + ": " + term.get(key));
                }
                System.out.println();
            }
            System.out.println();
        }
    }
    public List<Map<String, Object>> filterTermsByComparison(String s, Comparable<?> comparable, int i) {
        return null;
    }
}
