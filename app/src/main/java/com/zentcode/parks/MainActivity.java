package com.zentcode.parks;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.zentcode.parks.app.Messages;
import com.zentcode.parks.storage.PreferenceManager;
import com.zentcode.parks.storage.Preferences;
import com.zentcode.parks.utils.LocationProvider;
import com.zentcode.parks.utils.LocationEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private PreferenceManager preferenceManager;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private FragmentManager fragmentManager;
    private Location mLocation;
    private NavigationView navigationView;

    protected OnBackPressedListener onBackPressedListener;
    private boolean mToolBarNavigationListenerIsRegistered = false;

    private final int REQUEST_CODE_MULTIPLE_PERMISSIONS = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferenceManager = new PreferenceManager(this);
        LocationProvider locationProvider = new LocationProvider(this);
        fragmentManager = getSupportFragmentManager();

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setHeaderInfo();

        if (Build.VERSION.SDK_INT >= 23)
            requestPermissions();

        locationProvider.runLocationProvider();
    }

    public void setHeaderInfo() {
        View header = navigationView.getHeaderView(0);
        TextView txtUserName = header.findViewById(R.id.txt_username);
        TextView txtUnidad = header.findViewById(R.id.txt_unidad);
        TextView txtDestino = header.findViewById(R.id.txt_destino);
        txtUserName.setText(preferenceManager.getString(Preferences.USER_NAME));
        if (hasUnidad()) {
            txtUnidad.setText(String.format(getResources().getString(R.string.ph_unidad), preferenceManager.getString(Preferences.NOMBRE_UNIDAD)));
            txtDestino.setText(String.format(getResources().getString(R.string.ph_destino), preferenceManager.getString(Preferences.DESTINO)));
        } else {
            txtUnidad.setVisibility(View.GONE);
            txtDestino.setVisibility(View.GONE);
        }
    }

    public Boolean hasUnidad() {
        return preferenceManager.getString(Preferences.NOMBRE_UNIDAD).equals("");
    }

    public void logoutApp() {
        preferenceManager.clearPreference(Preferences.USER_ID);
        preferenceManager.clearPreference(Preferences.USER_NAME);
        preferenceManager.clearPreference(Preferences.API_TOKEN);
        redirectToLogin();
    }

    private void redirectToLogin() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void requestPermissions() {
        List<String> permissionsNeeded = new ArrayList<>();
        final List<String> permissionsList = new ArrayList<>();

        if (addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION) &&
                addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            permissionsNeeded.add("Gps");
        }

        if (addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE) &&
                addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            permissionsNeeded.add("Almacenamiento interno");
        }

        if (addPermission(permissionsList, Manifest.permission.CAMERA)) {
            permissionsNeeded.add("Cámara.");
        }

        if (permissionsList.size() > 0 && permissionsNeeded.size() > 0) {
            String message = "Debe conceder los siguientes permisos a la aplicación: " + permissionsNeeded.get(0);

            for (int i = 1; i < permissionsNeeded.size(); i++)
                message = message + ", " + permissionsNeeded.get(i);

            showPermissionAlert(message,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= 23) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_MULTIPLE_PERMISSIONS);
                            }
                        }
                    });
        }
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            return true;
        }

        return false;
    }

    private void showPermissionAlert(String message, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage(message);
        alertBuilder.setPositiveButton("Aceptar", okListener);
        alertBuilder.setCancelable(false);
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_MULTIPLE_PERMISSIONS) {
            Map<String, Integer> perms = new HashMap<>();
            perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);

            for (int i = 0; i < permissions.length; i++)
                perms.put(permissions[i], grantResults[i]);

            checkPermissions(perms);
        }
    }

    private void checkPermissions(Map<String, Integer> perms) {
        for (Map.Entry<String, Integer> perm : perms.entrySet() ) {
            if (perm.getValue() != PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, perm.getKey())) {
                    showPermissionAlert(Messages.PERMISSION_DENIED_PERMANENT, null);
                } else {
                    showPermissionAlert(Messages.PERMISSION_DENIED, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions();
                        }
                    });
                }
                break;
            }
        }
    }

    public void showBackButton(boolean show) {
        if(show) {
            toggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if(!mToolBarNavigationListenerIsRegistered) {
                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
                mToolBarNavigationListenerIsRegistered = true;
            }
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.setToolbarNavigationClickListener(null);
            mToolBarNavigationListenerIsRegistered = false;
        }
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    public interface OnBackPressedListener {
        void doBack();
    }

    public static class BaseBackPressedListener implements OnBackPressedListener {
        private final FragmentActivity activity;
        private final boolean showAlert;
        private AlertDialog.Builder alertBuilder;
        private AlertDialog alertDialog;

        public BaseBackPressedListener(FragmentActivity activity, Context context, boolean showAlert) {
            this.activity = activity;
            this.showAlert = showAlert;
            alertBuilder = new AlertDialog.Builder(context);
        }

        @Override
        public void doBack() {
            if (showAlert) {
                alertBuilder.setCancelable(false);
                alertBuilder.setTitle("Está seguro que desea abandonar el formulario?");
                alertBuilder.setMessage("Verifique que ha guardado todos los cambios");
                alertBuilder.setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        activity.getSupportFragmentManager().popBackStackImmediate();
                    }
                });
                alertBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog = alertBuilder.create();
                alertDialog.show();
            } else {
                activity.getSupportFragmentManager().popBackStackImmediate();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (onBackPressedListener != null)
                onBackPressedListener.doBack();
            else
                super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout)
            logoutApp();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LocationEvent e) {
        System.out.println("event result: " + e.getLocation().getLongitude() + " - " + e.getLocation().getLatitude());
        this.mLocation = e.getLocation();
    }

    public Location getCurrentLocation(Location location) {
        return mLocation;
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}