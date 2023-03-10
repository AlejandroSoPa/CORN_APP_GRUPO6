package com.example.corn_app_grupo6.ui.historial;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.corn_app_grupo6.Fragments;
import com.example.corn_app_grupo6.MainActivity;
import com.example.corn_app_grupo6.R;
import com.example.corn_app_grupo6.utils.UtilsHTTP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HistorialFragment extends Fragment {

    private HistorialViewModel mViewModel;
    private ListView linear;
    private Activity activity= getActivity();

    public static HistorialFragment newInstance() {
        return new HistorialFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.activity = getActivity();
        return inflater.inflate(R.layout.fragment_historial, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HistorialViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onStart() {

        super.onStart();

        View vista = this.getView();
        linear=vista.findViewById(R.id.lin);

                SharedPreferences sharedPref = getDefaultSharedPreferences(vista.getContext());
                String token = sharedPref.getString(getString(R.string.token),"noToken");
                JSONObject obj = null;
                try {
                    obj = new JSONObject("{}");
                    obj.put("session", token);
                    UtilsHTTP.sendPOST(Fragments.protocol + "://" + Fragments.host + "/API/transaccions", obj.toString(), (response) -> {
                        activity.runOnUiThread(()->{
                        JSONObject objResponse = null;
                        try {
                            objResponse = new JSONObject(response);
                            if (objResponse.getString("status").equals("OK")) {
                                JSONArray data= objResponse.getJSONArray("result");

                                // TODO put items on list
                                /*
                                for (int i = 0; i < data.length(); i++) {
                                    Log.i("i",data.getJSONObject(i).getString("text"));
                                    TextView t =new TextView(getContext());
                                    t.setText(data.getJSONObject(i).getString("text"));



                                }*/
                                ArrayList<String> r=new ArrayList<>();
                                for (int i = 0; i < data.length(); i++) {
                                    Log.i("i",data.getJSONObject(i).getString("text"));
                                    r.add(data.getJSONObject(i).getString("text"));
                                    r.add("adsa");

                                }
                                ArrayAdapter<String> arraicita=new ArrayAdapter<>(activity.getBaseContext(),android.R.layout.simple_list_item_1,r);
                                linear.setAdapter(arraicita);
                                arraicita.notifyDataSetChanged();
                                Log.i("i",r.toString());
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });});


                } catch (JSONException es) {
                    es.printStackTrace();
                }}

    }