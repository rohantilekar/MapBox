package com.rst.mapbox;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rst.mapbox.fragments.FragmentMap;
import com.rst.mapbox.fragments.FragmentPinList;
import com.rst.mapbox.models.PinItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private static final String             TAG_LOCATION = "list_tag";
    private static final String             TAG_MAP      = "map_tag";
    private static final String             URL          = "https://annetog.gotenna.com/development/scripts/get_map_pins.php";
    public static        ArrayList<PinItem> pinItems; // easier to share between fragments without high memory usage
    protected final      String             TAG          = this.getClass().getName();
    private              RequestQueue       requestQueue;
    private              ActionBar          actionBar;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_location_list:
                    actionBar.setTitle(R.string.nav_bottom_pins);
                    FragmentPinList fragmentPinList = new FragmentPinList();
                    replaceFragment(fragmentPinList, TAG_LOCATION);
                    return true;
                case R.id.navigation_maps:
                    actionBar.setTitle(R.string.nav_bottom_map);
                    FragmentMap fragmentMap = new FragmentMap();
                    replaceFragment(fragmentMap, TAG_MAP);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar = getSupportActionBar();


        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        actionBar.setTitle(getString(R.string.nav_bottom_pins));

        pinItems = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        parseJSON();

    }

    private void replaceFragment(Fragment fg, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment prevHotels = fragmentManager.findFragmentByTag(TAG_LOCATION);
        Fragment prevMap = fragmentManager.findFragmentByTag(TAG_MAP);
        switch (tag) {
            case TAG_LOCATION: {
                if (prevHotels == null) {
                    fragmentTransaction.add(R.id.frame_container, fg, TAG_LOCATION);
                } else {
                    fragmentTransaction.show(prevHotels);
                }
                if (prevMap != null) {
                    fragmentTransaction.hide(prevMap);
                }
                break;
            }
            case TAG_MAP: {
                if (prevMap == null) {
                    fragmentTransaction.add(R.id.frame_container, fg, TAG_MAP);
                } else {
                    fragmentTransaction.show(prevMap);
                }
                if (prevHotels != null) {
                    fragmentTransaction.hide(prevHotels);
                }
                break;
            }
        }
        fragmentTransaction.commit();
    }


    private void parseJSON() {

        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject pin = response.getJSONObject(i);

                        String name = pin.getString("name");
                        double longitude = pin.getDouble("longitude");
                        double latitutde = pin.getDouble("latitude");
                        String description = pin.getString("description");


                        PinItem pinItem = new PinItem(name, longitude, latitutde, description);
                        Log.d(TAG, pinItem.toString());
                        pinItems.add(pinItem);
                    }

                    FragmentPinList fh = new FragmentPinList();
                    replaceFragment(fh, TAG_LOCATION);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);

    }
}
