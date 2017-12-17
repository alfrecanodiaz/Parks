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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.zentcode.parks.MainActivity;
import com.zentcode.parks.R;
import com.zentcode.parks.app.Messages;
import com.zentcode.parks.models.Unidad;
import com.zentcode.parks.network.ObjectRequest;
import com.zentcode.parks.network.Routes;
import com.zentcode.parks.utils.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UnidadesFragment extends Fragment {

    private RecyclerView recyclerView;
    private Integer moduloId;
    private String moduloNombre;

    public UnidadesFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moduloId = getArguments().getInt("modulo_id");
        moduloNombre = getArguments().getString("modulo_nombre");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unidades, container, false);

        TextView txtUnidad = view.findViewById(R.id.txt_unidad);
        txtUnidad.setText(String.format(getResources().getString(R.string.ph_unidad_fragment), moduloNombre));

        recyclerView = view.findViewById(R.id.recyclerview_unidades);
        recyclerView.setLayoutManager(Helper.getLayoutManager());

        setHeader();
        setBackButton();

        getData();

        return view;
    }

    private void getData() {
        final AlertDialog loading = Helper.showLoading(getContext());
        ObjectRequest request = new ObjectRequest(getContext());
        request.sendRequest(Request.Method.GET, Routes.getUnidadesUrl(moduloId), null, new ObjectRequest.RequestCallback() {
            @Override
            public void success(JSONObject response) {
                Helper.hideLoading(loading);
                try {
                    if (response.has("status")) {
                        Toast.makeText(getContext(), response.getString("status"), Toast.LENGTH_SHORT).show();
                    } else {
                        list(response.getJSONArray("unidades"));
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
        List<Unidad> unidades= new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Unidad unidad = new Unidad();
                unidad.setId(jsonObject.getInt("id"));
                unidad.setNombre(jsonObject.getString("nombre"));
                unidades.add(unidad);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        UnidadesAdapter adapter = new UnidadesAdapter(getContext(), unidades);
        recyclerView.setAdapter(adapter);
    }

    private class UnidadesAdapter extends RecyclerView.Adapter<UnidadesAdapter.CustomViewHolder> {

        private List<Unidad> items;
        private Context mContext;

        UnidadesAdapter(Context context, List<Unidad> list) {
            this.items = list;
            this.mContext = context;
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.simple_list, viewGroup, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final CustomViewHolder holder, int position) {
            Unidad unidad= items.get(position);
            holder.btnUnidad.setText(unidad.getNombre());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            Button btnUnidad;

            CustomViewHolder(View view) {
                super(view);
                this.btnUnidad = view.findViewById(R.id.simple_item);
                btnUnidad.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                final Unidad unidad = items.get(getAdapterPosition());
                Toast.makeText(mContext, unidad.getNombre(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setBackButton() {
        ((MainActivity)getActivity()).showBackButton(true);
        ((MainActivity)getActivity()).setOnBackPressedListener(new MainActivity.BaseBackPressedListener(getActivity(), getContext(), false));
    }

    private void setHeader() {
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Unidades");
    }

    @Override
    public void onResume() {
        super.onResume();
        setHeader();
        setBackButton();
    }
}