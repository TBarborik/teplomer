/**
 * Část aplikace představující landing page.
 * Obsahuje aktuální teplotu, která se
 * každou vteřinu aktualizuje.
 * Používá home_view.xml
 *
 * Poslední modifikace 1.1. 2018
 * @Author Tom Barbořík (xbarbo06)
 */

package fit.imp.xbarbo06.thermometer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends ImpFragment {
    private View view;

    public HomeFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.home_view, container, false);
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    new SetTemperature().execute();
                } catch (Exception e) {
                    Log.e("Error updating", "Updating temperature. Trying again in 1 seconds.");
                }
            }
        }, 0, INTERVAL);

        return view;
    }

    public void updateText(final String str) {
        if (getActivity() == null)
            return;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView) view.findViewById(R.id.tv_current)).setText(str);
            }
        });

    }

    private class SetTemperature extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Temperature current = JsonTemperatureParser.getCurrent();

            if (current == null) {
                //Snackbar.make(view, "Chyba během získávání dat", Snackbar.LENGTH_LONG);
            } else {
                updateText(Float.toString(current.getTemperature()).concat(" °C"));
            }

            return null;
        }
    }
}
