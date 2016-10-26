package livecoding.ask.fm.askfmlivecoding;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import livecoding.ask.fm.askfmlivecoding.time.service.TimeService;

public class MainActivity extends FragmentActivity {

    private MainActivityFragment mainActivityFragment;
    private TimeService timeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        timeService = new TimeService(this);

        mainActivityFragment = new MainActivityFragment();
        mainActivityFragment.setTimeService(timeService);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, mainActivityFragment, mainActivityFragment.getTag())
                .commit();
    }

}
