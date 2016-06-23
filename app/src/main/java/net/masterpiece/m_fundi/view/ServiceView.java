package net.masterpiece.m_fundi.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.masterpiece.m_fundi.R;
import net.masterpiece.m_fundi.model.ServicesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ServiceView extends Activity {
    MyCustomAdapter dataAdapter = null;
    Double sum = 0.00;
    Button btnServiceProceed;

    int count;

    //create sessions to store/retrieve selections
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public static final String USERPREFERENCES = "UserDetails" ;

    private ArrayList<ServicesModel> servicesList;

    ProgressDialog pDialog;
    AlertDialogManager alert = new AlertDialogManager();

    private static Variables address = new Variables();
    // API urls
    private static String URL_SERVICE = address.getAddress() + "/professions/1/services";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);

        getActionBar().setTitle("House Cleaning");

        setContentView(R.layout.activity_service);


        servicesList = new ArrayList<ServicesModel>();
        sharedpreferences = getSharedPreferences(USERPREFERENCES,
                Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        btnServiceProceed = (Button) findViewById(R.id.btnServiceProceed);

        btnServiceProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sum == 0.00){
                    Toast.makeText(getApplicationContext(),
                            "Please select at list one service to continue",
                            Toast.LENGTH_LONG).show();
                } else {
                    if (sharedpreferences.contains("token"))
                    {
                        Intent intent = new Intent(ServiceView.this, SelectExpertView.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(ServiceView.this, LoginView.class);
                        startActivity(intent);
                    }

                }

            }
        });



        //Generate list View from ArrayList

        new services().execute();

        checkButtonClick();
    }

    @Override
    public void onBackPressed (){
        Intent intent = new Intent(ServiceView.this, HomeView.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem menuItem){
        if(menuItem.getItemId() == android.R.id.home){
            Intent intent = new Intent(ServiceView.this, HomeView.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(menuItem);

    }

    private void displayListView() {

        //Array list of services
//        ArrayList<ServicesModel> servicesList = new ArrayList<ServicesModel>();
//        ServicesModel service = new ServicesModel("300.00","Laundry",false);
//        servicesList.add(service);
//        service = new ServicesModel("300.00","Full House Cleaning",false);
//        servicesList.add(service);
//        service = new ServicesModel("300.00","Cooking",false);
//        servicesList.add(service);
//        service = new ServicesModel("300.00","Furniture and Furnishing",false);
//        servicesList.add(service);

        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(this,
                R.layout.services_info, servicesList);
        ListView listView = (ListView) findViewById(R.id.listViewServices);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                ServicesModel service = (ServicesModel) parent.getItemAtPosition(position);
                /*Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + service.getName(),
                        Toast.LENGTH_LONG).show();*/
            }
        });

    }

    private class MyCustomAdapter extends ArrayAdapter<ServicesModel> {
        private ArrayList<ServicesModel> serviceList;


        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<ServicesModel> serviceList) {
            super(context, textViewResourceId, serviceList);
            this.serviceList = new ArrayList<ServicesModel>();
            this.serviceList.addAll(serviceList);
        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.services_info, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);
                count = 1;

                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        ServicesModel service = (ServicesModel) cb.getTag();
                        String serviceSelected = service.getName();
                        Double selected = Double.parseDouble(service.getCode());
                        if (cb.isChecked()){
                            sum += selected;
                            editor.putString("services_"+count, serviceSelected);
                            editor.putString("services_cost_"+count, service.getCode());
                            count += 1;
                        } else {
                            sum -= selected;
                            editor.remove("services_"+count);
                            editor.remove("services_cost_"+count);
                            count -= 1;
//                            editor.remove("services");
                        }
                        editor.putString("services_count", Integer.toString(count));
                        Toast.makeText(getApplicationContext(),
                                "Total: " + sum,
                                Toast.LENGTH_SHORT).show();
                        editor.putString("sum", Double.toString(sum));
                        editor.commit();

//                        for (int i = 1; i<Integer.parseInt(sharedpreferences.getString("services_count",null)); i++){
//                            Toast.makeText(getApplicationContext(),sharedpreferences.getString("services_"+i,null), Toast.LENGTH_SHORT).show();
//                        }


                        service.setSelected(cb.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            ServicesModel service = serviceList.get(position);

            holder.code.setText("KES." +  service.getCode() + " ");
//            holder.code.setGravity(Gravity.RIGHT);
            holder.name.setText(service.getName());
            holder.name.setChecked(service.isSelected());
            holder.name.setTag(service);

            return convertView;

        }

    }

    private void checkButtonClick() {


    /*    Button myButton = (Button) findViewById(R.id.findSelected);
        myButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");

                ArrayList<Country> countryList = dataAdapter.countryList;
                for(int i=0;i<countryList.size();i++){
                    Country country = countryList.get(i);
                    if(country.isSelected()){
                        responseText.append("\n" + country.getName());
                    }
                }

                Toast.makeText(getApplicationContext(),
                        responseText, Toast.LENGTH_LONG).show();

            }
        });*/

    }


    /**
     * Async task class to get json by making HTTP call
     * */
    private class services extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ServiceView.this);
            pDialog.setMessage("Fetching services...");
            pDialog.setCancelable(true);
            pDialog.show();

        }
        @Override
        protected Void doInBackground(Void... arg0){
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String json = sh.makeServiceCall(URL_SERVICE, ServiceHandler.GET,null);

            //shows the response that we got from the http request on the logcat
            Log.d("Response: ", "> " + json+ " token:"+sharedpreferences.getString("token",null));

            //result = jsonStr;
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        JSONArray services = jsonObj
                                .getJSONArray("data");

                        for (int i = 0; i < services.length(); i++) {
                            JSONObject catObj = (JSONObject) services.get(i);

                            ServicesModel ser = new ServicesModel(catObj.getString("cost"),
                                    catObj.getString("name"),false);
                            servicesList.add(ser);
                        }
                    } else {
                        // Existing data
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // loginErrorMsg.setText("Incorrect PhoneNumber/Password");
                                alert.showAlertDialog(
                                        ServiceView.this,
                                        "Failed!",
                                        "Could not fetch services!",
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
                                ServiceView.this,
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
