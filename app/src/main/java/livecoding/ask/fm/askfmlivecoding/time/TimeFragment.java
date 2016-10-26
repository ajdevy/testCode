package livecoding.ask.fm.askfmlivecoding.time;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;

import com.trello.rxlifecycle.components.support.RxFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import livecoding.ask.fm.askfmlivecoding.R;
import livecoding.ask.fm.askfmlivecoding.time.service.TimeService;
import rx.Observer;
import rx.schedulers.Schedulers;

public class TimeFragment extends RxFragment {

    private static final String TAG = TimeFragment.class.getName();
    private Chronometer chronometer;

    private TimeService timeService;
    private Calendar calendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        calendar = Calendar.getInstance();
        return inflater.inflate(R.layout.fragment_time, container, false);
    }

    private void mapAndStartChronometer(Date date) {
        calendar.setTime(date);
        final long chronometerTime = calendar.get(Calendar.MINUTE) * 60 * 1000 + calendar.get(Calendar.SECOND) * 1000;
        Log.d(TAG, "mapAndStartChronometer " + chronometerTime);

        chronometer.setBase(SystemClock.elapsedRealtime() - chronometerTime);
        chronometer.start();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chronometer = (Chronometer) view.findViewById(R.id.time_chronometer);
        final Date prefetchedDate = timeService.getLastGotTime();
        if (prefetchedDate != null) {
            mapAndStartChronometer(prefetchedDate);
        }

        timeService.getCurrentTime(new Observer<Date>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError", e);
            }

            @Override
            public void onNext(final Date date) {
                Log.d(TAG, "onNext " + date);
                if (isAdded()) {
                    chronometer.post(new Runnable() {
                        @Override
                        public void run() {
                            mapAndStartChronometer(prefetchedDate);
                        }
                    });
                }
            }
        });

        timeService.getJson()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(albums -> Log.e(this.getClass().getSimpleName(),"got albums "+ albums));

    }

    public void setTimeService(TimeService timeService) {
        this.timeService = timeService;
    }
}
