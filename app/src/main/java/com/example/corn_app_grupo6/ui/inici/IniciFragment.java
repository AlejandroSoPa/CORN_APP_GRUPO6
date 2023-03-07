package com.example.corn_app_grupo6.ui.inici;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.corn_app_grupo6.Fragments;
import com.example.corn_app_grupo6.MainActivity;
import com.example.corn_app_grupo6.R;

public class IniciFragment extends Fragment {

    private IniciViewModel mViewModel;


    public static IniciFragment newInstance() {
        return new IniciFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inici, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(IniciViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onStart() {

        super.onStart();
        View vista = this.getView();
        final Intent intent = new Intent(super.getActivity(), MainActivity.class);
        Button b=vista.findViewById(R.id.logout);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getDefaultSharedPreferences(vista.getContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.token), "noToken");
                editor.commit();
                startActivity(intent);
            }
        });
    }
}