package com.zentcode.parks.network;

import com.zentcode.parks.app.App;
import com.zentcode.parks.app.Config;
import com.zentcode.parks.storage.PreferenceManager;
import com.zentcode.parks.storage.Preferences;

public class Routes {

    private static String getBaseUrl() {
        return Config.SERVER_URL;
    }

    public static String getLoginUrl() {
        return getBaseUrl() + "login";
    }

    public static String getModuloUrl() {
        return getBaseUrl() + "modulos" + apiTokenParam();
    }

    public static String getUnidadUrl(Integer moduloId) {
        return getBaseUrl() + "unidades/" + moduloId + apiTokenParam();
    }

    public static String getTravesiaUrl(Integer unidadId) {
        return getBaseUrl() + "travesias/" + unidadId + apiTokenParam();
    }

    public static String getSincronizarUrl(Integer travesiaId) {
        return getBaseUrl() + "sincronizar/" + travesiaId + apiTokenParam();
    }

    public static String getSincronizacionUrl() {
        return getBaseUrl() + "sincronizacionFinal" + apiTokenParam();
    }

    private static String apiTokenParam() {
        PreferenceManager pm = new PreferenceManager(App.getContext());
        return "?api_token=" + pm.getString(Preferences.API_TOKEN);
    }
}