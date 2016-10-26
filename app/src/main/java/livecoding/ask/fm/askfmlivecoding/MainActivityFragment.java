package livecoding.ask.fm.askfmlivecoding;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import livecoding.ask.fm.askfmlivecoding.time.TimeFragment;
import livecoding.ask.fm.askfmlivecoding.time.service.TimeService;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private View mButton;
    private TimeFragment timeFragment;
    private TimeService timeService;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mButton = view.findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAdded()) {
                    timeFragment = new TimeFragment();
                    timeFragment.setTimeService(timeService);
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_container, timeFragment, timeFragment.getTag())
                            .addToBackStack(timeFragment.getTag())
                            .commit();
                }
            }
        });
    }

    public void setTimeService(TimeService timeService) {
        this.timeService = timeService;
        if (timeFragment != null)
            timeFragment.setTimeService(timeService);
    }

    public TimeService getTimeService() {
        return timeService;
    }
}
