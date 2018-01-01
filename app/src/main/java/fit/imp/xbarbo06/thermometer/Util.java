/**
 * Pomocné funkce.
 * Poslední modifikace 1.1.
 *
 * @author Tom Barbořík (xbarbo06)
 */

package fit.imp.xbarbo06.thermometer;

import java.net.HttpURLConnection;
import java.net.URL;

public class Util {

    /**
     * Zjistí, zda je v řetězci platná adresa
     * @param url String
     * @return boolean
     */
    public static boolean urlExists(String url) {
        HttpURLConnection.setFollowRedirects(false);

        try {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setConnectTimeout(2 * 1000);
            con.setReadTimeout(3 * 1000);
            con.setRequestMethod("HEAD");
            return con.getResponseCode() == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
