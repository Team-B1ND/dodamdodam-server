package b1nd.dodam.codenary.client.support;

import b1nd.dodam.codenary.client.data.res.ConferenceRes;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class CodenaryItemParser {

    private CodenaryItemParser() {}

    public static List<ConferenceRes> parse(String json) {
        List<ConferenceRes> conferences = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                conferences.add(ConferenceRes.builder()
                        .title(jsonObject.getString("title"))
                        .organization(jsonObject.getString("organization"))
                        .startDate(parseDate(jsonObject.getString("start_date")))
                        .endDate(parseDate(jsonObject.getString("end_date")))
                        .eventType(jsonObject.getString("event_type"))
                        .link(jsonObject.getString("link"))
                        .build());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return conferences;
    }

    private static LocalDate parseDate(String date) {
        return LocalDate.parse(date.split("T")[0]);
    }

}
