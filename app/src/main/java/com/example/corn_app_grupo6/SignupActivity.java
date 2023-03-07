package com.example.corn_app_grupo6;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.corn_app_grupo6.ui.perfil.PerfilFragment;
import com.example.corn_app_grupo6.utils.UtilsHTTP;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        Button regist=findViewById(R.id.button);
        Button back=findViewById(R.id.button2);
        EditText mail=findViewById(R.id.mail);
        EditText telefon=findViewById(R.id.telefon);
        EditText nom=findViewById(R.id.nom);
        EditText cognom=findViewById(R.id.cognom);
        EditText passwd=findViewById(R.id.password);
        TextView err=findViewById(R.id.error);
        final Intent intent = new Intent(this, Fragments.class);

        SharedPreferences sharedPref = getDefaultSharedPreferences(this.getBaseContext());

        Intent login=new Intent(this, MainActivity.class);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(login);
            }

        });
        final Activity activity = this;
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passwd.getText().length()==0||mail.getText().length()==0||telefon.getText().length()==0||nom.getText().length()==0||cognom.getText().length()==0){
                    err.setText("Omple tots els camps");
                }
                else{
                    err.setText("");
                    activity.runOnUiThread(()->{
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject("{}");
                            obj.put("email", mail.getText());
                            obj.put("password", passwd.getText());
                            obj.put("name", nom.getText());
                            obj.put("surname", cognom.getText());
                            obj.put("phone", telefon.getText());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        UtilsHTTP.sendPOST(Fragments.protocol + "://" + Fragments.host + "/API/singup", obj.toString(), (response) -> {

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
                                    PerfilFragment.nombree=(data.getString("name"));
                                    PerfilFragment.wallett=(String.valueOf(data.getInt("wallet")));
                                    Fragments.user=data.getInt("phone");

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
                            });


                    ;
                }
            }
        });

    }
}
