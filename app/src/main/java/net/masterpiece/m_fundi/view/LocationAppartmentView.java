package net.masterpiece.m_fundi.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import net.masterpiece.m_fundi.R;

public class LocationAppartmentView extends Activity {

    Button btnLocationAppartmentProceed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);

        getActionBar().setTitle("Apartment Details");

        setContentView(R.layout.activity_location_apartment);

        btnLocationAppartmentProceed = (Button) findViewById(R.id.btnLocationApartmentProceed);
        btnLocationAppartmentProceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationAppartmentView.this, SummaryView.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed (){
        Intent intent = new Intent(LocationAppartmentView.this, LoginView.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem menuItem){
        if(menuItem.getItemId() == android.R.id.home){
            Intent intent = new Intent(LocationAppartmentView.this, LoginView.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(menuItem);

    }
}
