package livecoding.ask.fm.askfmlivecoding.time.service.rest;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import rx.Observable;
import rx.Subscriber;

public class TimeRestService {
    //TODO:   POJO generation  http://pojo.sodhanalibrary.com/
    //TODO: development API https://jsonplaceholder.typicode.com/albums
    private static final String URL = "http://www.timeapi.org/utc/now";
    private static final String JSON_URL = "https://jsonplaceholder.typicode.com/albums";

    private final RequestQueue requestQueue;

    public TimeRestService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void getTime(Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        final StringRequest request = new StringRequest(Request.Method.GET, URL, responseListener, errorListener);
        requestQueue.add(request);
    }

    public Observable<Albums[]> getJson() {
        return Observable.create(new Observable.OnSubscribe<Albums[]>() {
            @Override
            public void call(final Subscriber<? super Albums[]> subscriber) {
                final Response.Listener responseListener = new Response.Listener<Albums[]>() {
                    @Override
                    public void onResponse(Albums[] response) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(response);
                            subscriber.onCompleted();
                        }
                    }
                };
                final Response.ErrorListener errorListener = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onError(error);
                            subscriber.onCompleted();
                        }
                    }
                };

                final JacksonRequest<Albums[]> request = new JacksonRequest<>(Request.Method.GET, JSON_URL, null, Albums[].class, responseListener, errorListener);
                requestQueue.add(request);
            }
        });
    }
}
