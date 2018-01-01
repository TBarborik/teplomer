/**
 * Hlavní třída pro aktivity. Zajišťuje základní inicializace a akce,
 * konkrétně navigaci. Slouží pro dědičnost.
 *
 * Poslední modifikace 1.1. 2018
 * @Author Tom Barbořík (xbarbo06)
 */

package fit.imp.xbarbo06.thermometer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

abstract class ImpActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected int layoutId;
    protected ImpFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
        initializePage(savedInstanceState);
    }

    protected void initialize() {
        setContentView(this.layoutId);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.connection_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_to_thermometer:
                JsonTemperatureParser.setSource(JsonTemperatureParser.SOURCE_THERMOMETER);
                changedSource(JsonTemperatureParser.SOURCE_THERMOMETER);
                return true;

            case R.id.change_to_dumb:
                JsonTemperatureParser.setSource(JsonTemperatureParser.SOURCE_DUMB);
                changedSource(JsonTemperatureParser.SOURCE_DUMB);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        navClicked(id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    abstract protected void initializePage(Bundle savedInstanceState);
    abstract protected void navClicked(int id);
    abstract protected void changedSource(int source);
}
