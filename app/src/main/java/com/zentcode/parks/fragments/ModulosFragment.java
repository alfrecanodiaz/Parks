package com.zentcode.parks.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.zentcode.parks.MainActivity;
import com.zentcode.parks.R;
import com.zentcode.parks.app.Messages;
import com.zentcode.parks.models.Modulo;
import com.zentcode.parks.network.ObjectRequest;
import com.zentcode.parks.network.Routes;
import com.zentcode.parks.utils.Helper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ModulosFragment extends Fragment {

    private RecyclerView recyclerView;

    public ModulosFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_modulos, container, false);

        recyclerView = view.findViewById(R.id.recyclerview_modulos);
        recyclerView.setLayoutManager(Helper.getLayoutManager());

        setHeader();
        setBackButton();

        getData();

        return view;
    }

    private void getData() {
        final AlertDialog loading = Helper.showLoading(getContext());
        ObjectRequest request = new ObjectRequest(getContext());
        request.sendRequest(Request.Method.GET, Routes.getModulosUrl(), null, new ObjectRequest.RequestCallback() {
            @Override
            public void success(JSONObject response) {
                Helper.hideLoading(loading);
                try {
                    if (response.has("status")) {
                        Toast.makeText(getContext(), response.getString("status"), Toast.LENGTH_SHORT).show();
                    } else {
                        list(response.getJSONArray("modulos"));
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
        List<Modulo> modulos = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Modulo modulo = new Modulo();
                modulo.setId(jsonObject.getInt("id"));
                modulo.setNombre(jsonObject.getString("nombre"));
                modulos.add(modulo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ModulosAdapter adapter = new ModulosAdapter(getContext(), modulos);
        recyclerView.setAdapter(adapter);
    }

    private class ModulosAdapter extends RecyclerView.Adapter<ModulosAdapter.CustomViewHolder> {

        private List<Modulo> items;
        private Context mContext;

        ModulosAdapter(Context context, List<Modulo> list) {
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
            Modulo modulo= items.get(position);
            holder.btnModulo.setText(modulo.getNombre());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            Button btnModulo;

            CustomViewHolder(View view) {
                super(view);
                this.btnModulo = view.findViewById(R.id.simple_item);
                btnModulo.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                final Modulo modulo = items.get(getAdapterPosition());
                Fragment fragment = new UnidadesFragment();
                Bundle params = new Bundle();
                params.putInt("modulo_id", modulo.getId());
                params.putString("modulo_nombre", modulo.getNombre());
                fragment.setArguments(params);
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
            }
        }
    }

    private void setBackButton() {
        ((MainActivity)getActivity()).showBackButton(true);
        ((MainActivity)getActivity()).setOnBackPressedListener(new MainActivity.BaseBackPressedListener(getActivity(), getContext(), false));
    }

    private void setHeader() {
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Módulos");
    }

    @Override
    public void onResume() {
        super.onResume();
        setHeader();
        setBackButton();
    }
}