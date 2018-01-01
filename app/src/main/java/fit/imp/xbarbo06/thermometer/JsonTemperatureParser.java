/**
 * Stahuje data ze zdroje ohledně teploty ve formátu JSON.
 * Ten parsuje a vrátí výsledek. Data stahuje z webového
 * serveruna adrese uložené v konstantě BASE_URL.
 *
 * Poslední modifikace 1.1. 2018
 * @Author Tom Barbořík (xbarbo06)
 */

package fit.imp.xbarbo06.thermometer;

import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class JsonTemperatureParser {
    static final int ERR_MALFORMED_URL = 1;
    static final int ERR_WRONG_URL = 4;
    static final int ERR_IO = 2;
    static final int ERR_JSON = 3;
    static final int SOURCE_THERMOMETER = 0;
    static final int SOURCE_DUMB = 1;

    static final String BASE_URL[] = {"http://192.168.4.1/", "http://imp.pbstyle.cz/"};
    static final String HISTORY_URL[] = {BASE_URL[0] + "history", BASE_URL[1] + "history"};

    private static int source_url = SOURCE_THERMOMETER;

    private URL url;
    private int err = 0;

    public JsonTemperatureParser(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            this.err = ERR_MALFORMED_URL;
        }
    }

    public JsonTemperatureParser() {
        this(JsonTemperatureParser.BASE_URL[getSource()]);
    }

    private Temperature getCurrentInner() {
        String jsonStr = this.read();

        if (this.err != 0)
            return null;

        try {
            JSONObject jsonTemp = new JSONObject(jsonStr);
            return new Temperature((float) jsonTemp.getDouble("temp"), jsonTemp.getLong("timestamp"));
        } catch (JSONException e) {
            this.err = ERR_JSON;
        }

        return null;
    }

    private Temperature[] getAllDataInner() {
        String jsonStr = this.read();

        if (this.err != 0)
            return null;

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            JSONArray history = jsonObj.getJSONArray("history");
            Temperature temp_arr[] = new Temperature[history.length()];

            for (int i = 0; i < history.length(); i++) {
                JSONObject d = history.getJSONObject(i);
                float temp = (float) d.getDouble("temp");
                long timestamp = d.getLong("timestamp");

                temp_arr[i] = new Temperature(temp, timestamp);
            }

            return temp_arr;
        } catch (JSONException e) {
            this.err = ERR_JSON;
        }

        return null;
    }

    private String read() {

        if (this.err != 0)
            return null;

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(2 * 1000);
            conn.setReadTimeout(3 * 1000);

            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            return sb.toString();
        } catch (Exception e) {
            this.err = ERR_IO;
        }

        return null;
    }

    public int getErr() {
        return this.err;
    }

    @Nullable
    public static Temperature getCurrent() {
        if (!Util.urlExists(JsonTemperatureParser.BASE_URL[getSource()]))
            return null;

        JsonTemperatureParser jtp = new JsonTemperatureParser(JsonTemperatureParser.BASE_URL[getSource()]);
        if (jtp.getErr() != 0)
            return null;

        return jtp.getCurrentInner();
    }

    @Nullable
    public static Temperature[] getAllData() {
        if (!Util.urlExists(JsonTemperatureParser.HISTORY_URL[getSource()]))
            return null;

        JsonTemperatureParser jtp = new JsonTemperatureParser(JsonTemperatureParser.HISTORY_URL[getSource()]);
        if (jtp.getErr() != 0)
            return null;

        return jtp.getAllDataInner();
    }

    public static void setSource(int url) {
        JsonTemperatureParser.source_url = url;
    }

    public static int getSource() {
        return JsonTemperatureParser.source_url;
    }
}
