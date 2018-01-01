/**
 * Nadřazená třída pro jednotivé fragmenty.
 *
 * Poslední modifikace 1.1. 2018
 * @author Tom Barbořík (xbarbo06)
 */

package fit.imp.xbarbo06.thermometer;

import android.app.Fragment;

import java.util.Timer;

public class ImpFragment extends Fragment {
    public final int INTERVAL = 1000;
    protected Timer timer = null;

    public Timer getTimer() {
        return timer;
    }
}
