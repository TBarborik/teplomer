/**
 * Část aplikace zobrazující graf. Na ose x je
 * čas, ve kterém proběhlo měření, na ose y
 * je hodnota měření ve °C. Po kliknutí na
 * puntík konkrétního měření je zobrazen
 * detail, tj. čas a naměřená hodnota.
 * Používá chart_view.xml
 *
 * Poslední modifikace 1.1. 2018
 * @Author Tom Barbořík (xbarbo06)
 */

package fit.imp.xbarbo06.thermometer;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.db.chart.listener.OnEntryClickListener;
import com.db.chart.model.LineSet;
import com.db.chart.renderer.AxisRenderer;
import com.db.chart.view.LineChartView;

import java.util.ArrayList;

public class ChartFragment extends ImpFragment {

    private View view;
    private int tempLength = 0;
    private SwipeRefreshLayout swipeContainer;
    private ArrayList<Temperature> temperatures = new ArrayList<>();
    private LineChartView lineChart;
    private LineSet dataset;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.chart_view, container, false);
        lineChart = (LineChartView) view.findViewById(R.id.linechart);
        setChart();
        new ChartFragment.SetTemperature().execute();

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    new ChartFragment.SetTemperature().execute();
                } catch (Exception e) {
                    //Log.e("Error updating", "Updating temperature. Trying again in 3 seconds.");
                }
            }
        });

        swipeContainer.setColorSchemeResources(R.color.colorLight, R.color.colorDark);

        return view;
    }

    private void setChart() {
        lineChart.setBorderSpacing(30);
        lineChart.setXLabels(AxisRenderer.LabelPosition.OUTSIDE);

        lineChart.setOnEntryClickListener(new OnEntryClickListener() {
            @Override
            public void onClick(int setIndex, int entryIndex, Rect rect) {
                final Snackbar sb = Snackbar.make(view, temperatures.get(entryIndex).toString(), Snackbar.LENGTH_LONG).setActionTextColor(Color.RED);
                sb.setAction(R.string.close, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sb.dismiss();
                    }
                });
                sb.show();
            }
        });

        dataset = new LineSet();
    }


    public void updateChart() {
        for (; this.tempLength < this.temperatures.size(); this.tempLength++) {
            dataset.addPoint("", this.temperatures.get(this.tempLength).getTemperature());
        }

        dataset.setDotsRadius(12.f)
                .setDotsColor(getResources().getColor(R.color.colorDark))
                .setColor(getResources().getColor(R.color.colorLight))
                .setSmooth(true);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lineChart.reset();
                lineChart.addData(dataset);
                lineChart.show();
                swipeContainer.setRefreshing(false);
            }
        });
    }

    public void updateTemperatures(Temperature[] temperatures) {
        for (Temperature temp : temperatures) {
            if (!this.temperatures.contains(temp)) {
                this.temperatures.add(temp);
            }
        }

        updateChart();
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
