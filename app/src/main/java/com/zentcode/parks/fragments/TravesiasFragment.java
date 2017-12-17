package com.zentcode.parks.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.zentcode.parks.MainActivity;
import com.zentcode.parks.R;
import com.zentcode.parks.app.Messages;
import com.zentcode.parks.models.Travesia;
import com.zentcode.parks.network.ObjectRequest;
import com.zentcode.parks.network.Routes;
import com.zentcode.parks.utils.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TravesiasFragment extends Fragment {

    private RecyclerView recyclerView;
    private Integer unidadId;
    private String unidadNombre;
    private String moduloNombre;

    public TravesiasFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unidadId = getArguments().getInt("unidad_id");
        unidadNombre = getArguments().getString("unidad_nombre");
        moduloNombre = getArguments().getString("modulo_nombre");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_travesias, container, false);

        TextView txtTravesia = view.findViewById(R.id.txt_travesia);
        txtTravesia.setText(String.format(getResources().getString(R.string.ph_travesia_fragment), unidadNombre));

        recyclerView = view.findViewById(R.id.recyclerview_travesias);
        recyclerView.setLayoutManager(Helper.getLayoutManager());

        setHeader();
        setBackButton();

        getData();

        return view;
    }

    private void getData() {
        final AlertDialog loading = Helper.showLoading(getContext());
        ObjectRequest request = new ObjectRequest(getContext());
        request.sendRequest(Request.Method.GET, Routes.getTravesiasUrl(unidadId), null, new ObjectRequest.RequestCallback() {
            @Override
            public void success(JSONObject response) {
                Helper.hideLoading(loading);
                try {
                    if (response.has("status")) {
                        Toast.makeText(getContext(), response.getString("status"), Toast.LENGTH_SHORT).show();
                    } else {
                        list(response.getJSONArray("travesias"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void error() {
                Helper.hideLoading(loading);
                Toast.makeText(getContext(), Messages.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void list(JSONArray jsonArray) {
        System.out.println("JSON: " + jsonArray.toString());
        List<Travesia> travesias = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Travesia travesia = new Travesia();
                travesia.setIdServidor(jsonObject.getInt("id"));
                travesia.setDestino(jsonObject.getString("destino"));
                travesia.setResponsable1(jsonObject.getString("responsable1"));
                travesia.setResponsable2(jsonObject.getString("responsable2"));
                travesia.setFechaInicio(jsonObject.getString("fecha_inicio"));
                travesias.add(travesia);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        System.out.println("9999: " + String.valueOf(travesias.size()));
        TravesiasAdapter adapter = new TravesiasAdapter(getContext(), travesias);
        recyclerView.setAdapter(adapter);
    }

    private class TravesiasAdapter extends RecyclerView.Adapter<TravesiasAdapter .CustomViewHolder> {

        private List<Travesia> items;
        private Context mContext;

        TravesiasAdapter (Context context, List<Travesia> list) {
            this.items = list;
            this.mContext = context;
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_travesias, viewGroup, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final CustomViewHolder holder, int position) {
            Travesia travesia = items.get(position);
            holder.txtDestino.setText(travesia.getDestino());
            holder.txtFechaInicio.setText(travesia.getFechaInicio());
            holder.txtResponsable1.setText(travesia.getResponsable1());
            holder.txtResponsable2.setText(travesia.getResponsable2());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView txtDestino;
            TextView txtFechaInicio;
            TextView txtResponsable1;
            TextView txtResponsable2;

            CustomViewHolder(View view) {
                super(view);
                this.txtDestino = view.findViewById(R.id.txt_travesia_destino);
                this.txtFechaInicio = view.findViewById(R.id.txt_travesia_fecha_inicio);
                this.txtResponsable1 = view.findViewById(R.id.txt_travesia_responsable1);
                this.txtResponsable2 = view.findViewById(R.id.txt_travesia_responsable2);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                final Travesia travesia = items.get(getAdapterPosition());
                Toast.makeText(mContext, travesia.getDestino(), Toast.LENGTH_SHORT).show();
//                Fragment fragment = new TravesiasFragment();
//                Bundle params = new Bundle();
//                params.putInt("unidad_id", unidad.getId());
//                params.putString("unidad_nombre", unidad.getNombre());
//                params.putString("modulo_nombre", moduloNombre);
//                fragment.setArguments(params);
//                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
            }
        }
    }

    private void setBackButton() {
        ((MainActivity)getActivity()).showBackButton(true);
        ((MainActivity)getActivity()).setOnBackPressedListener(new MainActivity.BaseBackPressedListener(getActivity(), getContext(), false));
    }

    private void setHeader() {
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Travesias");
    }

    @Override
    public void onResume() {
        super.onResume();
        setHeader();
        setBackButton();
    }
}