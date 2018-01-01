/**
 * Část aplikace, která vypisuje pod sebou do seznamu
 * historii všech měření.
 * Používá history_view.xml
 *
 * Poslední modifikace 1.1. 2018
 * @Author Tom Barbořík (xbarbo06)
 */

package fit.imp.xbarbo06.thermometer;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

public class HistoryFragment extends ImpFragment {
    private View view;
    private int tempLength = 0;
    private SwipeRefreshLayout swipeContainer;
    private ArrayList<Temperature> temperatures = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.history_view, container, false);
        new HistoryFragment.SetTemperature().execute();

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    new HistoryFragment.SetTemperature().execute();
                } catch (Exception e) {
                    Log.e("Error updating", "Updating temperature. Trying again in 3 seconds.");
                }
            }
        });

        swipeContainer.setColorSchemeResources(R.color.colorLight, R.color.colorDark);
        return view;
    }

    public void updateTextArea() {

        StringBuilder sb = new StringBuilder();

        for (int i = this.temperatures.size() - 1; i >= this.tempLength; i--) {
            sb.append(this.temperatures.get(i));

            if (i > 0) {
                sb.append('\n');
            }
        }

        this.tempLength = this.temperatures.size();

        final String history_str = sb.toString();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView tv = ((TextView) view.findViewById(R.id.tv_history));
                tv.setText(history_str + tv.getText());
                swipeContainer.setRefreshing(false);
            }
        });
    }

    public void updateTemperatures(Temperature[] temperatures) {
        int i = 0;
        for (Temperature temp: temperatures) {
            if (!this.temperatures.contains(temp)) {
                this.temperatures.add(temp);
                i++;
            }
        }

        updateTextArea();
    }


    private class SetTemperature extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Temperature temperatures[] = JsonTemperatureParser.getAllData();

            if (temperatures == null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeContainer.setRefreshing(false);
                    }
                });
                //Snackbar.make(view, "Chyba během získávání dat", Snackbar.LENGTH_LONG);
            } else {
                updateTemperatures(temperatures);
            }

            return null;
        }
    }

}
