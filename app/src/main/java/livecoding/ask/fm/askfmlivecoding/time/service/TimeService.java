package livecoding.ask.fm.askfmlivecoding.time.service;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import livecoding.ask.fm.askfmlivecoding.time.service.rest.Albums;
import livecoding.ask.fm.askfmlivecoding.time.service.rest.TimeRestService;
import rx.Observable;
import rx.Observer;

public class TimeService {

    private static final String TAG = TimeService.class.getName();
    private TimeRestService timeRestService;
    private SimpleDateFormat dateFormat;
    private Date lastGotTime;

    public TimeService(Context context) {
        timeRestService = new TimeRestService(context);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
        prefetchTime();
    }

    public Observable<Albums[]> getJson() {
        return timeRestService.getJson();
    }
    public void getCurrentTime(final Observer<Date> unixTimeObserver) {

        timeRestService.getTime(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    final Date parsedDate = dateFormat.parse(response);
                    Log.d(TAG, "response = " + response + " parsedDate = " + parsedDate.toGMTString());
                    unixTimeObserver.onNext(parsedDate);
                } catch (ParseException e) {
                    unixTimeObserver.onError(e);
                }
                unixTimeObserver.onCompleted();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                unixTimeObserver.onError(error);
                unixTimeObserver.onCompleted();
            }
        });


    }

    public void prefetchTime() {
        getCurrentTime(new Observer<Date>() {


            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Date time) {
                lastGotTime = time;
            }
        });
    }

    public Date getLastGotTime() {
        return lastGotTime;
    }
}
