package b1nd.dodam.neis.schedule.client.util;

import b1nd.dodam.core.exception.global.InternalServerException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NeisScheduleUtil {

    public static List<String> determineGrades(JSONObject mealData) {
        List<String> grades = new ArrayList<>();
        if ("Y".equals(mealData.get("ONE_GRADE_EVENT_YN"))) grades.add("1학년");
        if ("Y".equals(mealData.get("TW_GRADE_EVENT_YN"))) grades.add("2학년");
        if ("Y".equals(mealData.get("THREE_GRADE_EVENT_YN"))) grades.add("3학년");

        if (grades.size() == 3) {
            grades.clear();
            grades.add("전교생");
        }
        return grades;
    }

    public static LocalDate parseDate(String dateString) {
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public static JSONArray getRawData(String response) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject parse = (JSONObject) jsonParser.parse(response);
            JSONArray schoolScheduleInfo = (JSONArray) parse.get("SchoolSchedule");
            JSONObject scheduleInfo = (JSONObject) schoolScheduleInfo.get(1);
            return (JSONArray) scheduleInfo.get("row");
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }
}
