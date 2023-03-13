package com.example.corn_app_grupo6.ui.perfil;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.corn_app_grupo6.FileUtil;
import com.example.corn_app_grupo6.Fragments;
import com.example.corn_app_grupo6.R;
import com.example.corn_app_grupo6.utils.UtilsHTTP;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class PerfilFragment extends Fragment {

    private PerfilViewModel mViewModel;
    public static EditText telefon;
    public static EditText nombre;
    public static EditText cognoms;
    public static EditText email;
    public static EditText wallet;
    public static ImageView circle;
    public static String telefonn,nombree,cognomss,emaill,wallett;
    private Button button;
    private TextView info;
    private ProgressBar loading;
    public ActivityResultLauncher<Intent> someActivityResultLauncher ;
    public ActivityResultLauncher<Intent> someActivityResultLauncher2 ;
    public static int RC_PHOTO_PICKER = 0;
    public Uri photoURI;
    private static String tipo_img;

    public static PerfilFragment newInstance() {
        return new PerfilFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);
        SharedPreferences sharedPref = getDefaultSharedPreferences(this.getContext());
        this.someActivityResultLauncher= registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            Uri uri = data.getData();
                            File imagen = new File("");
                            try {
                                imagen = FileUtil.from(getContext(), uri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            byte[] fileContent = new byte[0];

                            try {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    fileContent = Files.readAllBytes(imagen.toPath());
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            String base64 = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                base64 = Base64.getEncoder().encodeToString(fileContent);
                            }

                            try {
                                JSONObject obj = new JSONObject("{}");


                                String token = sharedPref.getString(getString(R.string.token),"noToken");
                                obj.put("session", token);
                                obj.put("front", base64);
                                UtilsHTTP.sendPOST(Fragments.protocol + "://" + Fragments.host  + "/API/send_id", obj.toString(), (response) -> {

                                    JSONObject objResponse = null;
                                    try {
                                        objResponse = new JSONObject(response);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        //Log.i("c",objResponse.getString("result"));
                                        if (objResponse.getString("status").equals("OK")) {
                                            getActivity().runOnUiThread(()->{Toast.makeText(getActivity(), "La imatge s'ha enviat correctament", Toast.LENGTH_SHORT).show();});
                                        }
                                    } catch (JSONException ew) {
                                        ew.printStackTrace();
                                        Log.i("i",ew.toString());
                                    }
                                });
                            } catch(Exception e) {
                                // TODO: handle exception
                                Log.i("i",e.toString());
                                getActivity().runOnUiThread(()->{Toast.makeText(getActivity(), "ERROR-No s'ha pogut realitzar la conexió", Toast.LENGTH_SHORT).show();});

                            }
                        }
                    }
                });
        this.someActivityResultLauncher2= registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            Uri uri = data.getData();
                            File imagen = new File("");
                            try {
                                imagen = FileUtil.from(getContext(), uri);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            byte[] fileContent = new byte[0];

                            try {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    fileContent = Files.readAllBytes(imagen.toPath());
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            String base64 = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                base64 = Base64.getEncoder().encodeToString(fileContent);
                            }

                            try {
                                JSONObject obj = new JSONObject("{}");


                                String token = sharedPref.getString(getString(R.string.token),"noToken");
                                obj.put("session", token);
                                obj.put("back", base64);
                                UtilsHTTP.sendPOST(Fragments.protocol + "://" + Fragments.host  + "/API/send_id", obj.toString(), (response) -> {

                                    JSONObject objResponse = null;
                                    try {
                                        objResponse = new JSONObject(response);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        //Log.i("c",objResponse.getString("result"));
                                        if (objResponse.getString("status").equals("OK")) {
                                            getActivity().runOnUiThread(()->{Toast.makeText(getActivity(), "La imatge s'ha enviat correctament", Toast.LENGTH_SHORT).show();});
                                        }
                                    } catch (JSONException ew) {
                                        ew.printStackTrace();
                                        Log.i("i",ew.toString());
                                    }
                                });
                            } catch(Exception e) {
                                // TODO: handle exception
                                Log.i("i",e.toString());
                                getActivity().runOnUiThread(()->{Toast.makeText(getActivity(), "ERROR-No s'ha pogut realitzar la conexió", Toast.LENGTH_SHORT).show();});

                            }
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

    @Override
    public void onStart() {
        super.onStart();

        View vista = this.getView();
        //button = this.getView().findViewById(R.id.butt);
        loading=this.getView().findViewById(R.id.progressBar);
        info=this.getView().findViewById(R.id.info);
        final Activity activity = getActivity();
        wallet=vista.findViewById(R.id.wallet);
        wallet.setText(wallett);
        telefon=vista.findViewById(R.id.telefonnumber);
        telefon.setText(telefonn);
        nombre=vista.findViewById(R.id.name);
        nombre.setText(nombree);
        cognoms=vista.findViewById(R.id.surname);
        cognoms.setText(cognomss);
        email=vista.findViewById(R.id.email);
        email.setText(emaill);
        circle=vista.findViewById(R.id.circu);



        Button b=vista.findViewById(R.id.boton_DNI);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipo_img = "front";
                openSomeActivityForResult(null);
            }
        });

        Button b2=vista.findViewById(R.id.boton_DNI_trasero);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tipo_img = "back";
                openSomeActivityForResult2(null);
            }
        });
                try{
                    loading.setVisibility(View.VISIBLE);

                    JSONObject obj = new JSONObject("{}");

                    SharedPreferences sharedPref = getDefaultSharedPreferences(vista.getContext());
                    String token = sharedPref.getString(getString(R.string.token),"noToken");
                    obj.put("session", token);
                    UtilsHTTP.sendPOST(Fragments.protocol + "://" + Fragments.host  + "/API/get_profile", obj.toString(), (response) -> {

                        JSONObject objResponse = null;
                        try {
                            objResponse = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            //Log.i("c",objResponse.getString("result"));
                            if (objResponse.getString("status").equals("OK")) {
                                JSONObject finalObjResponse =objResponse.getJSONObject("result");

                                activity.runOnUiThread(()->{
                                // TODO here logic info
                                    try {
                                        nombre.setText(finalObjResponse.getString("name"));
                                        cognoms.setText(finalObjResponse.getString("surname"));
                                        email.setText(finalObjResponse.getString("email"));
                                        telefon.setText(String.valueOf( finalObjResponse.getInt("phone")));
                                        wallet.setText(String.valueOf( finalObjResponse.getInt("wallet")));
                                        switch(finalObjResponse.getInt("status")) {
                                            case 1:
                                                circle.setImageDrawable(getResources().getDrawable(R.drawable.blue));
                                                info.setText("NO VERIFICAT");
                                                break;
                                            case 2:
                                                circle.setImageDrawable(getResources().getDrawable(R.drawable.orange));
                                                info.setText("A VERIFICAR");
                                                break;
                                            case 3:
                                                circle.setImageDrawable(getResources().getDrawable(R.drawable.yellow));
                                                info.setText("ACEPTAT");
                                                break;
                                            case 4:
                                                circle.setImageDrawable(getResources().getDrawable(R.drawable.circle));
                                                info.setText("REFUSAT");
                                                break;
                                            default:

                                                break;
                                        }
                                        loading.setVisibility(View.INVISIBLE);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    //status.setText(finalObjResponse.getString("status"));
                                //Toast.makeText(activity, "Inici de sessió correcte", Toast.LENGTH_SHORT).show();});
                            });}
                            else{
                                JSONObject finalObjResponse = objResponse;
                                activity.runOnUiThread(()->{
                                    try {
                                        loading.setVisibility(View.INVISIBLE);
                                        Toast.makeText(activity, finalObjResponse.getString("result"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                });



                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                            Log.i("i",e.toString());
                            activity.runOnUiThread(()->{Toast.makeText(activity, "ERROR-No s'ha pogut realitzar la conexió", Toast.LENGTH_SHORT).show();});
                            activity.runOnUiThread(()->{loading.setVisibility(View.INVISIBLE);});

                        }

                    });


                }
                catch (Exception w) {
                    // TODO: handle exception
                    Log.i("i",w.toString());
                    activity.runOnUiThread(()->{Toast.makeText(activity, "ERROR-No s'ha pogut realitzar la conexió", Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.INVISIBLE);});


                }

    }
    public void openSomeActivityForResult(View view) {
        //Create Intent
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        //Launch activity to get result
        someActivityResultLauncher.launch(intent);
    }
    public void openSomeActivityForResult2(View view) {
        //Create Intent
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        //Launch activity to get result
        someActivityResultLauncher2.launch(intent);
    }
}