package com.zentcode.parks.utils;

import android.app.AlertDialog;
import android.content.Context;

public class Alerts {

    public static void showSyncAlert(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Atención!");
        builder.setMessage("No se ha sincronizado ninguna travesia.");
        builder.setPositiveButton("Aceptar", null);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
