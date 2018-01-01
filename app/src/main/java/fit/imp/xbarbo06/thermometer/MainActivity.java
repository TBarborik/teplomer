/**
 * Hlavní aktivita. Zajišťuje přepínání částí (fragmentů) podle navigace
 * a inicializace defaultní obrazovky.
 * Používá activity_main.xml, content_main.xml
 *
 * Poslední modifikace 1.1. 2018
 * @Author Tom Barbořík (xbarbo06)
 */

package fit.imp.xbarbo06.thermometer;

import android.app.Fragment;
import android.os.Bundle;
import android.app.FragmentTransaction;

public class MainActivity extends ImpActivity {
    private int fragmentId = -1;

    public MainActivity() {
        super();
        this.layoutId = R.layout.activity_main;
    }

    @Override
    protected void initializePage(Bundle savedInstanceState) {
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            showDefault();
        }
    }

    @Override
    protected void navClicked(int id) {
        if (id == R.id.nav_home && this.fragmentId != 0) {
            showHome();
        } else if (id == R.id.nav_chart && this.fragmentId != 1) {
            showChart();
        } else if (id == R.id.nav_history && this.fragmentId != 2) {
            showHistory();
        }
    }

    @Override
    protected void changedSource(int source) {
        Fragment fragment = null;

        //switch (fragmentId) {
        //    case 0: showHome();
        //    case 1: showChart();
        //    case 2: showHistory();
        //}
    }

    private void showDefault() {
        fragmentId = 0;
        HomeFragment hf = new HomeFragment();
        hf.setArguments(getIntent().getExtras());
        getFragmentManager().beginTransaction().add(R.id.fragment_container, hf).commit();
        currentFragment = hf;
    }

    private void showHome() {
        fragmentId = 0;
        HomeFragment hf = new HomeFragment();
        Bundle args = new Bundle();
        hf.setArguments(args);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, hf);
        //transaction.addToBackStack("home");
        transaction.commit();
        currentFragment = hf;
    }

    private void showChart() {
        if (fragmentId == 0)
            this.currentFragment.getTimer().cancel();

        fragmentId = 1;
        ChartFragment chf = new ChartFragment();
        Bundle args = new Bundle();
        chf.setArguments(args);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, chf);
        //transaction.addToBackStack("chart");
        transaction.commit();
        currentFragment = chf;
    }

    private void showHistory() {
        if (fragmentId == 0)
            this.currentFragment.getTimer().cancel();

        fragmentId = 2;

        HistoryFragment hf = new HistoryFragment();
        Bundle args = new Bundle();
        hf.setArguments(args);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, hf);
        //transaction.addToBackStack("history");
        transaction.commit();
        currentFragment = hf;
    }
}
