package com.mycompany.MotorPH;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

public class AttendanceRecord {
    private String name;
    private String id;
    private LocalDate date;
    private LocalTime timeIn;
    private LocalTime timeOut;
    private static final String TXT_FILE_PATH = "src/main/resources/AttendanceRecord5.txt";

    public static ArrayList<AttendanceRecord> attendanceRecords;

    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    
    public AttendanceRecord(String name, String id, LocalDate date, LocalTime timeIn, LocalTime timeOut) {
        this.name = name;
        this.id = id;
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }

    static {
        attendanceRecords = loadAttendance();
    }
    
    //LOADS ATTENDANCE FROM A TXT USING TRY-CATCH TO ENSURE PROPER LOADING AND CLOSING
    public static ArrayList<AttendanceRecord> loadAttendance() {
        ArrayList<AttendanceRecord> attendanceRecords = new ArrayList<>();
        
        
        try (BufferedReader br = new BufferedReader(new FileReader(TXT_FILE_PATH))) {
            //FORMATS RECEIVED DATA TO FIT WHAT IS NEEDED
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

            // Read and skip the header
            br.readLine();

            // Process attendance records
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length >= 6) {
                    String name = data[1] + " " + data[2].trim();
                    String id = data[0];
                    LocalDate date = LocalDate.parse(data[3], dateFormat);
                    LocalTime timeIn = LocalTime.parse(data[4], timeFormat);
                    LocalTime timeOut = LocalTime.parse(data[5], timeFormat);

                    attendanceRecords.add(new AttendanceRecord(name, id, date, timeIn, timeOut));
                } else {
                    System.out.println("Invalid data: " + line);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return attendanceRecords;
    }   
    
    //CALCULATES HOURS PER ATTENDANCE RECORD
    private static long calculateHoursWorked(LocalTime timeIn, LocalTime timeOut) {
        Duration duration = Duration.between(timeIn, timeOut);
        return duration.toHours();
    }

    //CALCULATES HOURS WORKED ON A SPECIFIC MONTH OF AN EMPLOYEE
    public static long calculateTotalHoursAndPrint(int year, int month, String targetEmployeeId) {
    long totalHours = 0;
    String employeeName = "";

    for (AttendanceRecord entry : attendanceRecords) {
        if (entry.getId().equals(targetEmployeeId)) {
            // Extract year and month from the entry's date
            int entryYear = entry.getDate().getYear();
            int entryMonth = entry.getDate().getMonthValue();

            if (entryYear == year && entryMonth == month) {
                // Calculate hours worked for the specified month
                long hoursWorked = calculateHoursWorked(entry.getTimeIn(), entry.getTimeOut());               

                // Accumulate total hours
                totalHours += hoursWorked;
                employeeName = entry.getName();
            }
        }
    }

    // Output result
    if (totalHours > 0) {
        System.out.printf("Employee ID: %s, Name: %s, Total Hours: %d%n",
                targetEmployeeId, employeeName, totalHours);
    } else {
        totalHours = 160;
        System.out.printf("Employee ID: %s, Name: %s, Total Hours: %d (Default)%n",
                targetEmployeeId, employeeName, totalHours);
    }

    return totalHours;
}
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * @return the timeIn
     */
    public LocalTime getTimeIn() {
        return timeIn;
    }

    /**
     * @return the timeOut
     */
    public LocalTime getTimeOut() {
        return timeOut;
    }

    /**
     * @return the TXT_FILE_PATH
     */
    public static String getTXT_FILE_PATH() {
        return TXT_FILE_PATH;
    }
    
    public void setTimeIn(String timeIn) {
        this.timeIn = LocalTime.parse(timeIn, timeFormatter);
    }
    
    /**
     * @return the attendanceRecords
     */
    public static ArrayList<AttendanceRecord> getAttendanceRecords() {
        return attendanceRecords;
    }


}
