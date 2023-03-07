package com.example.corn_app_grupo6.ui.escanejar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.corn_app_grupo6.Fragments;
import com.example.corn_app_grupo6.MainActivity;
import com.example.corn_app_grupo6.utils.UtilsHTTP;
import com.google.zxing.Result;

import com.example.corn_app_grupo6.R;

import org.json.JSONException;
import org.json.JSONObject;

public class EscanejarFragment extends Fragment {

    private CodeScanner mCodeScanner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final Activity activity = getActivity();

        if (ContextCompat.checkSelfPermission(
                activity.getBaseContext(), Manifest.permission.CAMERA
        ) ==
                PackageManager.PERMISSION_GRANTED) {


        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA

            );
        }

        View root = inflater.inflate(R.layout.fragment_escanejar, container, false);
        CodeScannerView scannerView = root.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(activity, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(activity, result.getText(), Toast.LENGTH_SHORT).show();
                        if(Fragments.user!=0){
                            start_payment(result);
                        }
                        else{
                            Toast.makeText(activity, "Fes login per poder fer transaccions", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });

    private void start_payment(Result result){
        try {
            final Activity activity = getActivity();
            JSONObject obj = new JSONObject("{}");
            obj.put("phone", Fragments.user);
            obj.put("token", result.getText());
            obj.put("session", MainActivity.session);
            UtilsHTTP.sendPOST(Fragments.protocol + "://" + Fragments.host  + "/API/start_payment", obj.toString(), (response) -> {

                JSONObject objResponse = null;
                try {
                    objResponse = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    Log.i("c",objResponse.getString("result"));
                    if (objResponse.getString("status").equals("OK")) {
                        // ha ido bien

                        System.out.println("Ha salido bien");
                        JSONObject obj2 = new JSONObject(objResponse.getString("result"));
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                // Tasques a la interfície gràfica (GUI)
                                try {
                                    new AlertDialog.Builder(getView().getContext())
                                            .setTitle("Aceptar transacció?")
                                            .setMessage("Estàs a punt de transferir "+obj2.get("Quantitat")+" al usuari amb telèfon: "+obj2.get("Desti"))
                                            .setPositiveButton("Yes", (dialog, which) -> {

                                                finish_payment(obj2);
                                                dialog.dismiss();
                                            })
                                            .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                                            .show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                    else{
                        System.out.println("Ha salido mal");
                        JSONObject finalObjResponse = objResponse;
                        activity.runOnUiThread(()->{
                            try {
                                Toast.makeText(activity, finalObjResponse.getString("result"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                        //info.setText("ERROR-No s'ha pogut realitzar la conexio");
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    System.out.println("Excepcion");
                    JSONObject finalObjResponse1 = objResponse;
                    activity.runOnUiThread(()->{
                        try {
                            Toast.makeText(activity, finalObjResponse1.getString("result"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    });
                }

            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void finish_payment(JSONObject json){
        JSONObject obj = null;
        try {
            final Activity activity = getActivity();
            obj = new JSONObject("{}");
            obj.put("phone", Fragments.user);
            obj.put("token", json.get("token"));
            obj.put("accept", true);
            obj.put("amount", json.get("Quantitat"));
            obj.put("session", MainActivity.session);

            UtilsHTTP.sendPOST(Fragments.protocol + "://" + Fragments.host  + "/API/finish_payment", obj.toString(), (response) -> {

                JSONObject objResponse = null;
                try {
                    objResponse = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    Log.i("c",objResponse.getString("result"));
                    if (objResponse.getString("status").equals("OK")) {

                        System.out.println("Ha salido bien, transaccion finalizada");
                        JSONObject finalObjResponse = objResponse;
                        activity.runOnUiThread(()->{
                            try {
                                Toast.makeText(activity, finalObjResponse.getString("result"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });

                    }
                    else{
                        System.out.println("Ha salido mal");
                        JSONObject finalObjResponse1 = objResponse;
                        activity.runOnUiThread(()->{
                            try {
                                Toast.makeText(activity, finalObjResponse1.getString("result"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    System.out.println("Excepcion");

                }

            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}