package com.example.jerko.fsqvenues.tasks;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.example.jerko.fsqvenues.MainActivity;
import com.example.jerko.fsqvenues.R;
import com.example.jerko.fsqvenues.models.Data;
import com.example.jerko.fsqvenues.models.Venue;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Jerko on 6.4.2017..
 */

public class GetVenuesTask extends AsyncTask<String, Void, Data> {

    private Activity activity;
    private Dialog dialog;
    private Handler handler = new Handler();
    private Runnable runnable;
    private String location;
    private String radius;
    private Boolean firstCall;
    private String accesToken;


    public GetVenuesTask(final Activity activity, String location, String radius, Boolean firstCall, String accesToken) {
        this.activity = activity;
        this.location = location;
        this.radius = radius;
        this.firstCall = firstCall;
        this.accesToken = accesToken;
        runnable = new Runnable() {
            @Override
            public void run() {
                dialog = ProgressDialog.show(activity, null, activity.getResources().getString(R.string.dialog_data), true);
            }
        };
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showDialog();
    }

    @Override
    protected Data doInBackground(String... params) {
        //todo fetch data
        try {

            Calendar c = Calendar.getInstance();
            Date currentTime = c.getTime();
            DateFormat date = new SimpleDateFormat("yyyyMMdd");
            String v = date.format(currentTime);
            Log.d("date", v);

            final String url = "https://api.foursquare.com/v2/venues/explore?ll=" + location + "&radius="  + radius
                    + "&oauth_token=" + accesToken + "&v=" + v;

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            Log.d("REST", url);
            Data data = restTemplate.getForObject(url, Data.class);

            return data;
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Data data) {

        dismissDialog();
        if (data != null && data.getResponse() != null && data.getResponse().getGroups() != null
                && data.getResponse().getGroups().get(0) != null && data.getResponse().getGroups().get(0).getItems() != null){

            List<Venue> venues = new ArrayList<>();
            for (int i= 0; i < data.getResponse().getGroups().get(0).getItems().size(); i++ )
                venues.add(data.getResponse().getGroups().get(0).getItems().get(i).getVenue());

            if (data.getResponse().getHeaderLocation() != null)
                ((MainActivity)activity).updateTitle(data.getResponse().getHeaderLocation());

            if (firstCall) ((MainActivity)activity).initAdapter(venues);
            else ((MainActivity)activity).refreshAdapter(venues);
        }
    }


    private void showDialog(){
        handler.postDelayed(runnable, 500);
    }

    private void dismissDialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }else {
                handler.removeCallbacks(runnable);
            }
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }

    }
}
