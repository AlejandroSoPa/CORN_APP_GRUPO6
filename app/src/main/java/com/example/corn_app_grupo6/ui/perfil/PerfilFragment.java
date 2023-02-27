package com.example.corn_app_grupo6.ui.perfil;

import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.corn_app_grupo6.MainActivity;
import com.example.corn_app_grupo6.R;
import com.example.corn_app_grupo6.utils.UtilsHTTP;

import org.json.JSONException;
import org.json.JSONObject;

public class PerfilFragment extends Fragment {

    private PerfilViewModel mViewModel;
    private EditText telefon;
    private EditText nombre;
    private EditText cognoms;
    private EditText email;
    private Button button;
    private TextView info;
    private ProgressBar loading;

    public static PerfilFragment newInstance() {
        return new PerfilFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);

        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onStart() {
        super.onStart();

        View vista = this.getView();
        button = this.getView().findViewById(R.id.butt);
        loading=this.getView().findViewById(R.id.progressBar);
        info=this.getView().findViewById(R.id.info);
        final Activity activity = getActivity();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    loading.setVisibility(View.VISIBLE);
                    button.setEnabled(false);
                    JSONObject obj = new JSONObject("{}");
                    telefon = vista.findViewById(R.id.telefonnumber);
                    nombre = vista.findViewById(R.id.name);
                    cognoms = vista.findViewById(R.id.surname);
                    email = vista.findViewById(R.id.email);

                    obj.put("phone", telefon.getText());
                    obj.put("email", nombre.getText());
                    obj.put("name", cognoms.getText());
                    obj.put("surname", email.getText());
                    UtilsHTTP.sendPOST(MainActivity.protocol + "://" + MainActivity.host  + "/API/singup", obj.toString(), (response) -> {

                        JSONObject objResponse = null;
                        try {
                            objResponse = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            Log.i("c",objResponse.getString("result"));
                            if (objResponse.getString("status").equals("OK")) {

                                MainActivity.user = Integer.valueOf(String.valueOf(telefon.getText()));
                                telefon.setEnabled(false);
                                nombre.setEnabled(false);
                                cognoms.setEnabled(false);
                                email.setEnabled(false);
                                activity.runOnUiThread(()->{Toast.makeText(activity, "Inici de sessió correcte", Toast.LENGTH_SHORT).show();});
                            }
                            else{
                                JSONObject finalObjResponse = objResponse;
                                activity.runOnUiThread(()->{
                                    try {
                                        Toast.makeText(activity, finalObjResponse.getString("result"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                });

                                button.setEnabled(true);

                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                            activity.runOnUiThread(()->{Toast.makeText(activity, "ERROR-No s'ha pogut realitzar la conexió", Toast.LENGTH_SHORT).show();});
                            button.setEnabled(true);

                        }
                        loading.setVisibility(View.INVISIBLE);
                    });


                }
                catch (Exception w) {
                    // TODO: handle exception
                    Log.i("i",w.toString());
                    activity.runOnUiThread(()->{Toast.makeText(activity, "ERROR-No s'ha pogut realitzar la conexió", Toast.LENGTH_SHORT).show();});
                    button.setEnabled(true);

                }
            }
        });
    }
}