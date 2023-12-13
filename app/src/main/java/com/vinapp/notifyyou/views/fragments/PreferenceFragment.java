package com.vinapp.notifyyou.views.fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.vinapp.notifyyou.R;

public class PreferenceFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences (Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}
