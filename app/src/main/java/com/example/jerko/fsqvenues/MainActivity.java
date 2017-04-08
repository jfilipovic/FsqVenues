package com.example.jerko.fsqvenues;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jerko.fsqvenues.adapters.RecViewAdapter;
import com.example.jerko.fsqvenues.fsqHelp.ExampleTokenStore;
import com.example.jerko.fsqvenues.fsqHelp.FsqLoginHelper;
import com.example.jerko.fsqvenues.models.Venue;
import com.example.jerko.fsqvenues.tasks.GetVenuesTask;
import com.foursquare.android.nativeoauth.FoursquareOAuth;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecViewAdapter adapter;
    private TextView radiusTextView;
    private SeekBar radiusPicker;
    private LocationManager locationManager;
    private Boolean firstCall = true;
    private int progres = 950;
    private RelativeLayout mainLayout;
    private FsqLoginHelper fsqLoginHelper;


    private static final int REQUEST_CODE_FSQ_CONNECT = 200;
    private static final int REQUEST_CODE_FSQ_TOKEN_EXCHANGE = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = (RelativeLayout)findViewById(R.id.main_layout_wrapper);
        radiusTextView = (TextView)findViewById(R.id.radius_number);
        radiusPicker = (SeekBar)findViewById(R.id.radius_picker);
        radiusPicker.setProgress(950);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000*60*5,
                100, mLocationListener);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        radiusPicker.setOnSeekBarChangeListener(mSeekBarListener);
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            showSettingsAlert();
        }

        fsqLoginHelper = new FsqLoginHelper(MainActivity.this);
        ensureUi();

    }

    private SeekBar.OnSeekBarChangeListener mSeekBarListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
            progres = progresValue;
            radiusTextView.setText(Integer.toString(progres));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            fetchData(Integer.toString(progres));
            firstCall = false;
        }
    };


    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //code here
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public void initAdapter(List<Venue> venues){
        adapter = new RecViewAdapter(this, venues);
        recyclerView.setAdapter(adapter);
    }

    public void refreshAdapter(List<Venue> venues){
        adapter.swap(venues);
    }

    public void updateTitle(String title){
        (MainActivity.this).getSupportActionBar().setTitle(title);
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        alertDialog.setTitle("GPS settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                MainActivity.this.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();


    }

    public void fetchData(String progres){
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            showSettingsAlert();
        } else {

            Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (location != null) new GetVenuesTask(MainActivity.this, location.getLatitude() + "," + location.getLongitude(),
                    progres, firstCall, ExampleTokenStore.get().getToken()).execute();
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_FSQ_CONNECT:
                fsqLoginHelper.onCompleteConnect(resultCode, data);
                break;

            case REQUEST_CODE_FSQ_TOKEN_EXCHANGE:
                fsqLoginHelper.onCompleteTokenExchange(resultCode, data);
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Update the UI. If we already fetched a token, we'll just show a success
     * message.
     */
    public void ensureUi() {
        boolean isAuthorized = !TextUtils.isEmpty(ExampleTokenStore.get().getToken());

        mainLayout.setVisibility(isAuthorized ? View.VISIBLE : View.GONE);

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setVisibility(isAuthorized ? View.GONE : View.VISIBLE);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the native auth flow.
                Intent intent = FoursquareOAuth.getConnectIntent(MainActivity.this, fsqLoginHelper.CLIENT_ID);

                // If the device does not have the Foursquare app installed, we'd
                // get an intent back that would open the Play Store for download.
                // Otherwise we start the auth flow.
                if (FoursquareOAuth.isPlayStoreIntent(intent)) {
                    Toast.makeText(MainActivity.this, "Foursquare App not installed", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                } else {
                    startActivityForResult(intent, REQUEST_CODE_FSQ_CONNECT);
                }
            }
        });
    }

}
