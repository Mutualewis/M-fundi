package net.masterpiece.m_fundi.view;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import net.masterpiece.m_fundi.R;
import net.masterpiece.m_fundi.model.ExpertsModel;
import net.masterpiece.m_fundi.model.ServicesModel;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.SupportMapFragment;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SelectExpertView extends Activity implements LocationListener  {
    //create sessions to store/retrieve selections
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public static final String USERPREFERENCES = "UserDetails";

    MyCustomAdapter dataAdapter = null;
    Button btnExpertGoBack;

    String sum;

    private ArrayList<ExpertsModel> expertsList;

    ProgressDialog pDialog;
    AlertDialogManager alert = new AlertDialogManager();

    private static Variables address = new Variables();
    // API urls
    private static String URL_EXPERTS = address.getAddress() + "/experts/profession/1";


    //current location
    String currentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);

        getActionBar().setTitle("Select an Expert");
        setContentView(R.layout.activity_select_expert);

        expertsList = new ArrayList<ExpertsModel>();

        btnExpertGoBack = (Button) findViewById(R.id.btnExpertGoBack);
        btnExpertGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectExpertView.this, ServiceView.class);
                startActivity(intent);
            }
        });


        sharedpreferences = getSharedPreferences(USERPREFERENCES,
                Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        //retrieve stored data
        sum = sharedpreferences.getString("sum", null);


        //Generate list View from ArrayList
        displayListView();

        //get current location
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Creating an empty criteria object

        Criteria criteria = new Criteria();

        // Getting the name of the provider that meets the criteria

        String provider = locationManager.getBestProvider(criteria, true);

        // We can use the provider immediately to get the last known location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            onLocationChanged(location);
        }

        locationManager.requestLocationUpdates(provider, 20000, 0, this);

        new experts().execute();
    }
    @Override
    public void onLocationChanged(Location location) {
        //refresh the map
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        currentLocation = latitude + "," + longitude;
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
    @Override
    public void onBackPressed (){
        Intent intent = new Intent(SelectExpertView.this, ServiceView.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem menuItem){
        if(menuItem.getItemId() == android.R.id.home){
            Intent intent = new Intent(SelectExpertView.this, ServiceView.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(menuItem);

    }


    private void displayListView() {

        //Array list of experts
//        ArrayList<ExpertsModel> expertsList = new ArrayList<ExpertsModel>();
//        ExpertsModel expert = new ExpertsModel("15","Lewis Mutua Mwangi", "23", "291266209", "SN/002/2016");
//        expertsList.add(expert);
//        expert = new ExpertsModel("60","Peter Odhiambo Otieno", "34", "200066209", "SN/003/2016");
//        expertsList.add(expert);
//        expert = new ExpertsModel("75","Mercy Masika Lokundu", "20", "243266209", "SN/006/2016");
//        expertsList.add(expert);


        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(this,
                R.layout.experts_info, expertsList);
        ListView listView = (ListView) findViewById(R.id.listViewExpert);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                ExpertsModel service = (ExpertsModel) parent.getItemAtPosition(position);
                /*Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + service.getName(),
                        Toast.LENGTH_LONG).show();*/
            }
        });

    }



    private class MyCustomAdapter extends ArrayAdapter<ExpertsModel> {

        private ArrayList<ExpertsModel> expertsList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<ExpertsModel> expertsList) {
            super(context, textViewResourceId, expertsList);
            this.expertsList = new ArrayList<ExpertsModel>();
            this.expertsList.addAll(expertsList);
        }

        private class ViewHolder {
            TextView tvArrivalTime;
            TextView tvFullName;
            TextView tvAge;
            TextView tvNationalID;
            TextView tvTagNo;

            Button btnSelectExpert;

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.experts_info, null);

                holder = new ViewHolder();
                holder.tvArrivalTime = (TextView) convertView.findViewById(R.id.tvArrivalTime);
                holder.tvFullName = (TextView) convertView.findViewById(R.id.tvFullName);
                holder.tvAge = (TextView) convertView.findViewById(R.id.tvAge);
                holder.tvNationalID = (TextView) convertView.findViewById(R.id.tvNationalID);
                holder.tvTagNo = (TextView) convertView.findViewById(R.id.tvTagNo);
                holder.btnSelectExpert = (Button) convertView.findViewById(R.id.btnSelectExpert);

                convertView.setTag(holder);

                holder.btnSelectExpert.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        /*CheckBox cb = (CheckBox) v ;
                        ServicesModel service = (ServicesModel) cb.getTag();*/

                        /*Button btnExpert = (Button) v;
                        ExpertsModel expertsModel = (ExpertsModel) btnExpert.getTag();*/


                        ExpertsModel expert = expertsList.get(position);

                        /*Toast.makeText(getApplicationContext(),
                                "Total: ",
                                Toast.LENGTH_SHORT).show();*/

                        editor.putString("arrivalTime", expert.getArrivalTime());
                        editor.putString("expertName", expert.getName());
                        editor.putString("age", expert.getAge());
                        editor.putString("expertID", expert.getID());
                        editor.putString("tag", expert.getTag());
                        editor.commit();

                        Intent intent = new Intent(SelectExpertView.this, SummaryView.class);
                        startActivity(intent);
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            ExpertsModel expert = expertsList.get(position);

            holder.tvArrivalTime.setText(expert.getArrivalTime());
            holder.tvFullName.setText(expert.getName());
            holder.tvAge.setText(expert.getAge());
            holder.tvNationalID.setText(expert.getID());
            holder.tvTagNo.setText(expert.getTag());

            return convertView;

        }

    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class experts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SelectExpertView.this);
            pDialog.setMessage("Looking for experts...");
            pDialog.setCancelable(true);
            pDialog.show();

        }
        @Override
        protected Void doInBackground(Void... arg0){
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("token", sharedpreferences.getString("token",null)));
            params.add(new BasicNameValuePair("location", currentLocation));
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String json = sh.makeServiceCall(URL_EXPERTS, ServiceHandler.GET, params);

            //shows the response that we got from the http request on the logcat
            Log.d("Response: ", "> " + json+ " token:"+sharedpreferences.getString("token",null));

            //result = jsonStr;
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray experts = jsonObj
                                .getJSONArray("data");

                        for (int i = 0; i < experts.length(); i++) {
                            JSONObject catObj = (JSONObject) experts.get(i);

                            ExpertsModel ser = new ExpertsModel(catObj.getString("time"),
                                    catObj.getString("name"),Integer.toString(catObj.getInt("age")),catObj.getString("id_number"),catObj.getString("badge"));
                            expertsList.add(ser);
                        }
                    } else {
                        // Existing data
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // loginErrorMsg.setText("Incorrect PhoneNumber/Password");
                                alert.showAlertDialog(
                                        SelectExpertView.this,
                                        "Failed!",
                                        "Could not locate experts!",
                                        false);
                                pDialog.dismiss();
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                //Log.e("JSON Data", "Didn't receive any data from server!");
                // Error in connection
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // loginErrorMsg.setText("Incorrect PhoneNumber/Password");
                        alert.showAlertDialog(
                                SelectExpertView.this,
                                "Error!",
                                "Internet Connection Error!",
                                false);
                        pDialog.dismiss();
                        alert.notify();
                    }
                });
                try {
                    alert.wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                // Go to previous page
                Intent intCategory = new Intent(getApplicationContext(), HomeView.class);
                intCategory.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intCategory);
            }
            return null;
        }
        protected void onPostExecute(Void result) {
            // dismiss the dialog once done
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            displayListView();
        }
    }
}
