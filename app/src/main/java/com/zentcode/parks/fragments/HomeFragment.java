package com.zentcode.parks.fragments;

import android.app.AlertDialog;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zentcode.parks.MainActivity;
import com.zentcode.parks.R;
import com.zentcode.parks.utils.Alerts;
import com.zentcode.parks.utils.Helper;

public class HomeFragment extends Fragment implements View.OnClickListener {

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView txtTravesia = view.findViewById(R.id.txt_travesia);
        TextView txtHistorial = view.findViewById(R.id.txt_historial);
        txtTravesia.setOnClickListener(this);
        txtHistorial.setOnClickListener(this);
        setTextViewDrawableColor(txtTravesia);
        setTextViewDrawableColor(txtHistorial);

        setHeader();

        return view;
    }

    private void setHeader() {
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Inicio");
    }

    @Override
    public void onResume() {
        super.onResume();
        setHeader();
    }

    @Override
    public void onClick(View view) {
        if (Helper.isSynchronized()) {
            switch (view.getId()) {
                case R.id.txt_travesia:
                    Toast.makeText(getContext(), "Travesia", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.txt_historial:
                    Toast.makeText(getContext(), "Historial", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else {
            Alerts.showSyncAlert(getContext());
        }
    }

    private void setTextViewDrawableColor(TextView textView) {
        int color = Helper.getResourceColor(getContext(), R.color.colorText);
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null)
                drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        }
    }
}