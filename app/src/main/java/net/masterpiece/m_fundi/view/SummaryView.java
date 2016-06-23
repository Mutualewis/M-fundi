package net.masterpiece.m_fundi.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import net.masterpiece.m_fundi.R;
import net.masterpiece.m_fundi.model.RequestModel;

import java.util.ArrayList;

public class SummaryView extends Activity {

    MyCustomAdapter dataAdapter = null;
    Double sum = 0.00;
    Button btnServiceProceed;

    //create sessions to store/retrieve selections
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public static final String USERPREFERENCES = "UserDetails" ;

    TextView tvFullName,tvAge, tvNationalID, tvTagNo, tvCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);

        getActionBar().setTitle("View Summary");

        setContentView(R.layout.activity_summary);

        tvFullName = (TextView) findViewById(R.id.tvFullName);
        tvAge = (TextView) findViewById(R.id.tvAge);
        tvNationalID = (TextView) findViewById(R.id.tvNationalID);
        tvTagNo = (TextView) findViewById(R.id.tvTagNo);
        tvCost = (TextView) findViewById(R.id.tvCost);

        sharedpreferences = getSharedPreferences(USERPREFERENCES,
                Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        tvFullName.setText(sharedpreferences.getString("expertName",null));
        tvAge.setText(sharedpreferences.getString("age",null));
        tvNationalID.setText(sharedpreferences.getString("expertID",null));
        tvTagNo.setText(sharedpreferences.getString("tag",null));
        tvCost.setText(sharedpreferences.getString("sum",null));
        //Generate list View from ArrayList
        displayListView();
    }

    @Override
    public void onBackPressed (){
        Intent intent = new Intent(SummaryView.this, SelectExpertView.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem menuItem){
        if(menuItem.getItemId() == android.R.id.home){
            Intent intent = new Intent(SummaryView.this, SelectExpertView.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(menuItem);

    }


    private void displayListView() {

        //Array list of services
        ArrayList<RequestModel> requestList = new ArrayList<RequestModel>();
        for (int i = 1; i<Integer.parseInt(sharedpreferences.getString("services_count",null)); i++){
            RequestModel request = new RequestModel(sharedpreferences.getString("services_"+i,null), sharedpreferences.getString("services_cost_"+i,null));
            requestList.add(request);

//            Toast.makeText(getApplicationContext(),sharedpreferences.getString("services_"+i,null), Toast.LENGTH_SHORT).show();
        }
/*
        RequestModel request = new RequestModel("Cooking", "300.00");
        requestList.add(request);
        request = new RequestModel("Laundry", "300.00");
        requestList.add(request);
        request = new RequestModel("House Cleaning", "300.00");
        requestList.add(request);
*/

        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(this,
                R.layout.request_info, requestList);
        ListView listView = (ListView) findViewById(R.id.listViewSummary);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                RequestModel request = (RequestModel) parent.getItemAtPosition(position);
                /*Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + service.getName(),
                        Toast.LENGTH_LONG).show();*/
            }
        });

    }


    private class MyCustomAdapter extends ArrayAdapter<RequestModel> {

        private ArrayList<RequestModel> requestList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<RequestModel> requestList) {
            super(context, textViewResourceId, requestList);
            this.requestList = new ArrayList<RequestModel>();
            this.requestList.addAll(requestList);
        }

        private class ViewHolder {
            TextView service;
            TextView cost;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.request_info, null);

                holder = new ViewHolder();
                holder.service = (TextView) convertView.findViewById(R.id.tvService);
                holder.cost = (TextView) convertView.findViewById(R.id.tvCost);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            RequestModel request = requestList.get(position);

            holder.service.setText(request.getService());
//            holder.code.setGravity(Gravity.RIGHT);
            holder.cost.setText("KES." + request.getCost() + " ");

            return convertView;

        }
    }
}
