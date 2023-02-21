package com.example.corn_app_grupo6.ui.perfil;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

    public static PerfilFragment newInstance() {
        return new PerfilFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);
        button = root.findViewById(R.id.butt);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try{
                    button.setEnabled(false);
                    JSONObject obj = new JSONObject("{}");
                    telefon = telefon.findViewById(R.id.telefonnumber);
                    nombre = nombre.findViewById(R.id.name);
                    cognoms = cognoms.findViewById(R.id.surname);
                    email = email.findViewById(R.id.email);

                    obj.put("phone", String.valueOf(telefon.getText()));
                    obj.put("email", String.valueOf(nombre.getText()));
                    obj.put("name", String.valueOf(cognoms.getText()));
                    obj.put("surname", String.valueOf(email.getText()));
                    UtilsHTTP.sendPOST(MainActivity.protocol + "://" + MainActivity.host + ":" + MainActivity.port + "/API/signup", obj.toString(), (response) -> {

                        JSONObject objResponse = null;
                        try {
                            objResponse = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {

                            if (objResponse.getString("status").equals("OK")) {
                                // ha ido bien
                                System.out.println("Ha salido bien");
                            }
                            else{
                                System.out.println("Ha salido mal");
                                // Ha ido mal
                                //Info.setText("ERROR-No s'ha pogut realitzar la conexio");
                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                            System.out.println("Excepcion");
                            //Info.setText("ERROR-No s'ha pogut realitzar la conexio");
                        }

                        button.setEnabled(true);
                    });
                }
                catch (Exception w) {
                    // TODO: handle exception
                    //Info.setText("ERROR-No s'ha pogut realitzar la conexió");
                    button.setEnabled(true);
                }
            }
        });
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
        // TODO: Use the ViewModel
    }
}