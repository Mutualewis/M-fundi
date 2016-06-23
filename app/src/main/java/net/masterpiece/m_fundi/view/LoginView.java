package net.masterpiece.m_fundi.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import net.masterpiece.m_fundi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONStringer;

public class LoginView extends Activity {
    //Declarations
    Button btnLogin, btnRegisterUserProceed, btnForgotPass;
    EditText etUname, etPass, editTextFullName, editTextEmail, editTextPassword, editTextPhone;
    TextView tvCountryCode;
    Button btnVerifyTel;
    CheckBox cbSignIn;

    private static Variables address = new Variables();
    // API urls
    private static String URL_LOGIN = address.getAddress() + "/auth";

    private static String URL_VERIFY = address.getAddress() + "/phone/verify";

    private static String URL_REGISTRATION = address.getAddress() + "/register";

    ProgressDialog pDialog;
    AlertDialogManager alert = new AlertDialogManager();

    private static final Pattern EMAIL_PATTERN = Pattern
            .compile("[a-zA-Z0-9+._%-+]{1,100}" + "@"
                    + "[a-zA-Z0-9][a-zA-Z0-9-]{0,10}" + "(" + "."
                    + "[a-zA-Z0-9][a-zA-Z0-9-]{0,20}"+
                    ")+");
    //create sessions to store/retrieve selections
    SharedPreferences sharedpreferences;

    SharedPreferences.Editor editor;
    public static final String USERPREFERENCES = "UserDetails" ;

    String userName;

    TextView tvForgotPassword;


    private static final Pattern USERNAME_PATTERN = Pattern
            .compile("[a-zA-Z0-9]{1,250}");
    private static final Pattern PASSWORD_PATTERN = Pattern
            .compile("[a-zA-Z0-9+_.]{4,16}");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getActionBar().setDisplayUseLogoEnabled(false);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayUseLogoEnabled(false);

        getActionBar().setTitle("Sign Up/Sign In to M-Fundi");
        setContentView(R.layout.activity_login);


        //
        sharedpreferences = getSharedPreferences(USERPREFERENCES,
                Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();


        editTextFullName  = (EditText) findViewById(R.id.editTextFullName);
        editTextEmail  = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword  = (EditText) findViewById(R.id.editTextPassword);
        editTextPhone  = (EditText) findViewById(R.id.editTextPhone);

        tvCountryCode  = (TextView) findViewById(R.id.tvCountryCode);
        btnVerifyTel  = (Button) findViewById(R.id.btnVerifyTel);


        btnRegisterUserProceed  = (Button) findViewById(R.id.btnRegisterUserProceed);

        cbSignIn = (CheckBox) findViewById(R.id.cbSignIn);
        cbSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cbSignIn.isChecked()){
                    //hide full name, tvCountryCode, tel, verify
                    editTextFullName.setVisibility(View.GONE);
                    tvCountryCode.setVisibility(View.GONE);
                    editTextPhone.setVisibility(View.GONE);
                    btnVerifyTel.setVisibility(View.GONE);

                    btnRegisterUserProceed.setText("Sign In");
                } else {
                    //hide full name, tvCountryCode, tel, verify
                    editTextFullName.setVisibility(View.VISIBLE);
                    tvCountryCode.setVisibility(View.VISIBLE);
                    editTextPhone.setVisibility(View.VISIBLE);
                    btnVerifyTel.setVisibility(View.VISIBLE);

                    btnRegisterUserProceed.setText("Sign Up");
                }

            }
        });



        btnVerifyTel = (Button) findViewById(R.id.btnVerifyTel);
        btnVerifyTel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new verify().execute();
            }
        });

        btnRegisterUserProceed = (Button) findViewById(R.id.btnRegisterUserProceed);
        btnRegisterUserProceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (btnRegisterUserProceed.getText() == "Sign In") {
                    checkLogin();
                } else {
                    checkRegistration();
                }
            }

        });

    }


    private boolean CheckEmail(String email) {
        // TODO Auto-generated method stub
        return EMAIL_PATTERN.matcher(email).matches();
    }

    protected void checkLogin() {
        // TODO Auto-generated method stub
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        editTextEmail.setHintTextColor(Color.parseColor("#99000000"));
        editTextPassword.setHintTextColor(Color.parseColor("#99000000"));

        if(email.equals("") || password.equals("")){
            if(email.equals("")){
                Toast.makeText(LoginView.this, "ENTER USERNAME",
                        Toast.LENGTH_LONG).show();
                editTextEmail.setHintTextColor(Color.parseColor("#99ff0000"));
                return;
            }
            if(password.equals("")){
                Toast.makeText(LoginView.this, "ENTER PASSWORD",
                        Toast.LENGTH_LONG).show();
                editTextPassword.setHintTextColor(Color.parseColor("#99ff0000"));
                return;
            }

        } {
            if (!CheckEmail(email)) {
                Toast.makeText(LoginView.this, "ENTER VALID EMAIL ADDRESS",
                        Toast.LENGTH_LONG).show();
                return;
            }
        }
        //login
        new login().execute();
        // Go to select expert
/*        Intent intCategory = new Intent(getApplicationContext(), SelectExpertView.class);
        intCategory.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intCategory);*/
    }


    @Override
    public void onBackPressed (){
        Intent intent = new Intent(LoginView.this, ServiceView.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem menuItem){
        if(menuItem.getItemId() == android.R.id.home){
            Intent intent = new Intent(LoginView.this, ServiceView.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(menuItem);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_login_view, menu);
        return true;
    }

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
*//*        if (id == R.id.action_settings) {
            return true;
        }*//*
        if(menuItem.getItemId() == android.R.id.home){
            Intent intent = new Intent(LoginView.this, SelectExpertView.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }*/

    protected void checkRegistration() {
        // TODO Auto-generated method stub
        //check if verify is showing
        if(btnVerifyTel.getVisibility() == View.VISIBLE){
            alert.showAlertDialog(
                    LoginView.this,
                    "Failed!",
                    "Please verify phone number first!",
                    false);
        } else {
            //verify full name, email, password, telephone
            String name = editTextFullName.getText().toString();
            String password = editTextPassword.getText().toString();
            String email = editTextEmail.getText().toString();
            String tel = editTextPhone.getText().toString();

            editTextFullName.setHintTextColor(Color.parseColor("#99000000"));
            editTextPassword.setHintTextColor(Color.parseColor("#99000000"));
            editTextEmail.setHintTextColor(Color.parseColor("#99000000"));
            editTextPhone.setHintTextColor(Color.parseColor("#99000000"));

            //etEmail.setHintTextColor(Color.parseColor("#99ff0000"));

            if (password.equals("") || name.equals("") || email.equals("") || tel.equals("")) {
                if (name.equals("")) {
                    Toast.makeText(LoginView.this, "ENTER NAME",
                            Toast.LENGTH_LONG).show();
                    editTextFullName.setHintTextColor(Color.parseColor("#99ff0000"));
                    return;
                }
                if (email.equals("")) {
                    Toast.makeText(LoginView.this, "ENTER EMAIL ADDRESS",
                            Toast.LENGTH_LONG).show();
                    editTextEmail.setHintTextColor(Color.parseColor("#99ff0000"));
                    return;
                }
                if (password.equals("")) {
                    Toast.makeText(LoginView.this, "ENTER PASSWORD",
                            Toast.LENGTH_LONG).show();
                    editTextPassword.setHintTextColor(Color.parseColor("#99ff0000"));
                    return;
                }

            } else {
                if (!CheckPassword(password)) {
                    Toast.makeText(LoginView.this, "ENTER VALID PASSWORD",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                if (!CheckEmail(email)) {
                    Toast.makeText(LoginView.this, "ENTER VALID EMAIL ADDRESS",
                            Toast.LENGTH_LONG).show();
                    return;
                }
            }
            //register
            new register().execute();
        }

    }
    private boolean CheckPassword(String password) {
        // TODO Auto-generated method stub
        return PASSWORD_PATTERN.matcher(password).matches();
    }
    private boolean CheckUsername(String username) {
        // TODO Auto-generated method stub
        return USERNAME_PATTERN.matcher(username).matches();
    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class verify extends AsyncTask<Void, Void, Void> {
        String tel = "254" + editTextPhone.getText().toString().trim();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginView.this);
            pDialog.setMessage("Verifying...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("phone_number", tel));

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String json = sh.makeServiceCall(URL_VERIFY, ServiceHandler.POST,params);

            //shows the response that we got from the http request on the logcat
            Log.d("Response: ", "> " + json);
            //result = jsonStr;
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    if (jsonObj != null) {
                        String status = jsonObj.get("status").toString();
                        if (status.equals("success")){
                            // Existing data
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // loginErrorMsg.setText("Incorrect PhoneNumber/Password");
                                    alert.showAlertDialog(
                                            LoginView.this,
                                            "Success!",
                                            "Verification Successfully!",
                                            false);
                                    pDialog.dismiss();

                                    btnVerifyTel.setVisibility(View.GONE);
                                }
                            });

                        }else{
                            // Existing data
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // loginErrorMsg.setText("Incorrect PhoneNumber/Password");
                                    alert.showAlertDialog(
                                            LoginView.this,
                                            "Failed!",
                                            "Verification Failed! Check telephone format.",
                                            false);
                                    pDialog.dismiss();
                                }
                            });



                        }
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
                                LoginView.this,
                                "Error!",
                                "Internet Connection Error!",
                                false);
                        pDialog.dismiss();
                    }
                });
                // Go to previous page
                // Intent intCategory = new Intent(getApplicationContext(), Welcome.class);
                // intCategory.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // startActivity(intCategory);
            }
            return null;
        }
        protected void onPostExecute(Void result) {
            // dismiss the dialog once done
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            //add intent
        }
    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class register extends AsyncTask<Void, Void, Void> {
        //register full name, email, password, telephone
        String name = editTextFullName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String tel = "254" + editTextPhone.getText().toString().trim();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginView.this);
            pDialog.setMessage("Registering...");
            pDialog.setCancelable(false);
            pDialog.show();

        }
        @Override
        protected Void doInBackground(Void... arg0){

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("phone_number", tel));


            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String json = sh.makeServiceCall(URL_REGISTRATION, ServiceHandler.POST,params);

            //shows the response that we got from the http request on the logcat
            Log.d("Response: ", "> " + json);
            //result = jsonStr;
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    String token = null;
                    if (jsonObj != null) {
                        String status = jsonObj.get("status").toString();
                        if (status.equals("success")) {
                            // Existing data
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // loginErrorMsg.setText("Incorrect PhoneNumber/Password");
                                    alert.showAlertDialog(
                                            LoginView.this,
                                            "Success!",
                                            "Registration Successfully!",
                                            false);
                                    pDialog.dismiss();
                                }
                            });
                            token = jsonObj.get("token").toString();
                            //store user id and name
                            editor.putString("token", token);

                            editor.commit();


                            // Go to select expert
                            Intent intCategory = new Intent(getApplicationContext(), SelectExpertView.class);
                            intCategory.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intCategory);

                        } else {
                            // Existing data
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // loginErrorMsg.setText("Incorrect PhoneNumber/Password");
                                    alert.showAlertDialog(
                                            LoginView.this,
                                            "Failed!",
                                            "Registration Failed! Check your details.",
                                            false);
                                    pDialog.dismiss();
                                }
                            });


                        }
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
                                LoginView.this,
                                "Error!",
                                "Internet Connection Error!",
                                false);
                        pDialog.dismiss();
                    }
                });
                // Go to previous page
//			Intent intCategory = new Intent(getApplicationContext(), Welcome.class);
//			intCategory.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intCategory);
            }
            return null;
        }
        protected void onPostExecute(Void result) {
            // dismiss the dialog once done
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }


    /**
     * Async task class to get json by making HTTP call
     * */
    private class login extends AsyncTask<Void, Void, Void> {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginView.this);
            pDialog.setMessage("Sign in...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("username", email));
            params.add(new BasicNameValuePair("password", password));

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String json = sh.makeServiceCall(URL_LOGIN, ServiceHandler.POST,params);

            //shows the response that we got from the http request on the logcat
            Log.d("Response: ", "> " + json);
            //result = jsonStr;
            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    String token = null;
                    if (jsonObj != null) {
                        String status = jsonObj.get("status").toString();
                        if (status.equals("success")) {
                            // Existing data
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // loginErrorMsg.setText("Incorrect PhoneNumber/Password");
                                    alert.showAlertDialog(
                                            LoginView.this,
                                            "Success!",
                                            "Login Successfully!",
                                            false);
                                    pDialog.dismiss();
                                }
                            });
                            token = jsonObj.get("token").toString();
                            //store user id and name
                            editor.putString("token", token);

                            editor.commit();


                            // Go to select expert
                            Intent intCategory = new Intent(getApplicationContext(), SelectExpertView.class);
                            intCategory.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intCategory);

                        } else {
                            // Existing data
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // loginErrorMsg.setText("Incorrect PhoneNumber/Password");
                                    alert.showAlertDialog(
                                            LoginView.this,
                                            "Failed!",
                                            "Login Failed! Check your details.",
                                            false);
                                    pDialog.dismiss();
                                }
                            });


                        }
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
                                LoginView.this,
                                "Error!",
                                "Internet Connection Error!",
                                false);
                        pDialog.dismiss();
                    }
                });
                // Go to previous page
                // Intent intCategory = new Intent(getApplicationContext(), Welcome.class);
                // intCategory.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // startActivity(intCategory);
            }
            return null;
        }
        protected void onPostExecute(Void result) {
            // dismiss the dialog once done
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            //add intent
        }
    }

}
