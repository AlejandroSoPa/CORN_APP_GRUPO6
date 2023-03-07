package com.example.corn_app_grupo6;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.corn_app_grupo6.ui.perfil.PerfilFragment;
import com.example.corn_app_grupo6.utils.UtilsHTTP;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    public static String session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button log = findViewById(R.id.buttonlog);
        Button sing = findViewById(R.id.buttonsign);
        EditText mail = findViewById(R.id.email_login);
        EditText pass = findViewById(R.id.passwd);

        final Intent intent = new Intent(this, Fragments.class);
        final Intent sign=new Intent(this, SignupActivity.class);

        SharedPreferences sharedPref = getDefaultSharedPreferences(this.getBaseContext());
        String token = sharedPref.getString(getString(R.string.token),"noToken");
        Log.i("i", token);

        final Activity activity = this;
        if(token!="noToken") {
            try {
                JSONObject obj = new JSONObject("{}");

                obj.put("session",token);
                UtilsHTTP.sendPOST(Fragments.protocol + "://" + Fragments.host + "/API/login", obj.toString(), (response) -> {

                    JSONObject objResponse = null;
                    try {
                        objResponse = new JSONObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        Log.i("c", objResponse.getString("result"));
                        if (objResponse.getString("status").equals("OK")) {
                            this.session=token;

                            JSONObject data= objResponse.getJSONObject("data");
                            PerfilFragment.cognomss=(data.getString("surname"));
                            PerfilFragment.emaill=(data.getString("email"));
                            PerfilFragment.telefonn=(String.valueOf(data.getInt("phone")));
                            PerfilFragment.nombree=(data.getString("name"));
                            PerfilFragment.wallett=(String.valueOf(data.getInt("wallet")));
                            Fragments.user=data.getInt("phone");
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }
            ;
        };


        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject obj = new JSONObject("{}");

                    obj.put("email", mail.getText());
                    obj.put("password", pass.getText());
                    UtilsHTTP.sendPOST(Fragments.protocol + "://" + Fragments.host + "/API/login", obj.toString(), (response) -> {

                        JSONObject objResponse = null;
                        try {
                            objResponse = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            Log.i("c", objResponse.getString("result"));
                            if (objResponse.getString("status").equals("OK")) {
                                MainActivity.session=objResponse.getString("result");
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString(getString(R.string.token), objResponse.getString("result"));
                                editor.commit();

                                JSONObject data= objResponse.getJSONObject("data");
                                PerfilFragment.cognomss=(data.getString("surname"));
                                PerfilFragment.emaill=(data.getString("email"));
                                PerfilFragment.telefonn=(String.valueOf(data.getInt("phone")));
                                Fragments.user=data.getInt("phone");
                                PerfilFragment.nombree=(data.getString("name"));
                                PerfilFragment.wallett=(String.valueOf(data.getInt("wallet")));
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            JSONObject finalObjResponse1 = objResponse;
                            activity.runOnUiThread(()->{
                                try {
                                    Toast.makeText(activity, finalObjResponse1.getString("result"), Toast.LENGTH_SHORT).show();
                                } catch (JSONException es) {
                                    es.printStackTrace();
                                }});
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();

                }
                ;
            }
        });
        sing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(sign);
            }
        });
    }

}
