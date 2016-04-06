package br.com.casasdocodigo.boaviagem;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceActivity;

/**
 * Created by eits on 02/10/2015.
 */
public class ConfiguracoesActivity extends PreferenceActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }
}
