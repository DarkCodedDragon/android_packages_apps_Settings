/*
 * Copyright (C) 2012 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.cyanogenmod;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

/**
 * Performance Settings
 */
public class PerformanceSettings extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {
    private static final String TAG = "PerformanceSettings";

    private static final String PROCESSOR = "processor";

    private static final String MEMORY_MANAGEMENT = "memory_management";

    private static final String USE_DITHERING_PREF = "pref_use_dithering";

    private static final String USE_DITHERING_PERSIST_PROP = "persist.sys.use_dithering";

    private static final String USE_DITHERING_DEFAULT = "1";

    private static final String PROXIMITY_DISABLE_PREF = "proximity_disable";
            
    private static final String PROXIMITY_DISABLE_PROP = "gsm.proximity.enable";

    private static final String PROXIMITY_DISABLE_DEFAULT = "1";

    private static final String USE_16BPP_ALPHA_PREF = "pref_use_16bpp_alpha";

    private static final String USE_16BPP_ALPHA_PROP = "persist.sys.use_16bpp_alpha";

    private static final String SCROLLINGCACHE_PREF = "pref_scrollingcache";

    private static final String SCROLLINGCACHE_PERSIST_PROP = "persist.sys.scrollingcache";

    private static final String SCROLLINGCACHE_DEFAULT = "1";

    private static final String DISABLE_BOOTANIMATION_PREF = "pref_disable_bootanimation";

    private static final String DISABLE_BOOTANIMATION_PERSIST_PROP = "persist.sys.nobootanimation";

    private static final String DISABLE_BOOTANIMATION_DEFAULT = "0";

    private CheckBoxPreference mUseDitheringPref;

    private CheckBoxPreference mDisableProximityPref;

    private CheckBoxPreference mUse16bppAlphaPref;

    private ListPreference mScrollingCachePref;

    private CheckBoxPreference mDisableBootanimPref;

    private PreferenceScreen mProcessor;

    private PreferenceScreen mMemoryManagement;

    private AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getPreferenceManager() != null) {

            addPreferencesFromResource(R.xml.performance_settings);

            PreferenceScreen prefSet = getPreferenceScreen();

            mProcessor = (PreferenceScreen) prefSet.findPreference(PROCESSOR);
            mMemoryManagement = (PreferenceScreen) prefSet.findPreference(MEMORY_MANAGEMENT);
            mUseDitheringPref = (CheckBoxPreference) prefSet.findPreference(USE_DITHERING_PREF);
            mDisableProximityPref = (CheckBoxPreference) prefSet.findPreference(PROXIMITY_DISABLE_PREF);
            mUse16bppAlphaPref = (CheckBoxPreference) prefSet.findPreference(USE_16BPP_ALPHA_PREF);
            mDisableBootanimPref = (CheckBoxPreference) prefSet
                    .findPreference(DISABLE_BOOTANIMATION_PREF);

            String useDithering = SystemProperties.get(USE_DITHERING_PERSIST_PROP,
                    USE_DITHERING_DEFAULT);
            mUseDitheringPref.setChecked("1".equals(useDithering));

            String disableProximity = SystemProperties.get(PROXIMITY_DISABLE_PROP, PROXIMITY_DISABLE_DEFAULT);
            mDisableProximityPref.setChecked("1".equals(disableProximity));

            String use16bppAlpha = SystemProperties.get(USE_16BPP_ALPHA_PROP, "0");
            mUse16bppAlphaPref.setChecked("1".equals(use16bppAlpha));

            mScrollingCachePref = (ListPreference) prefSet.findPreference(SCROLLINGCACHE_PREF);
            mScrollingCachePref.setValue(SystemProperties.get(SCROLLINGCACHE_PERSIST_PROP,
                    SystemProperties.get(SCROLLINGCACHE_PERSIST_PROP, SCROLLINGCACHE_DEFAULT)));
            mScrollingCachePref.setOnPreferenceChangeListener(this);

            String disableBootanimation = SystemProperties.get(DISABLE_BOOTANIMATION_PERSIST_PROP,
                    DISABLE_BOOTANIMATION_DEFAULT);
            mDisableBootanimPref.setChecked("1".equals(disableBootanimation));

            /* Display the warning dialog */
            alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle(R.string.performance_settings_warning_title);
            alertDialog.setMessage(getResources().getString(R.string.performance_settings_warning));
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                    getResources().getString(com.android.internal.R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });

            alertDialog.show();
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mUseDitheringPref) {
            SystemProperties.set(USE_DITHERING_PERSIST_PROP,
                    mUseDitheringPref.isChecked() ? "1" : "0");
        } else if (preference == mDisableProximityPref) {
            SystemProperties.set(PROXIMITY_DISABLE_PROP,
                    mDisableProximityPref.isChecked() ? "1" : "0");
        } else if (preference == mUse16bppAlphaPref) {
            SystemProperties.set(USE_16BPP_ALPHA_PROP,
                    mUse16bppAlphaPref.isChecked() ? "1" : "0");
        } else if (preference == mDisableBootanimPref) {
            SystemProperties.set(DISABLE_BOOTANIMATION_PERSIST_PROP,
                    mDisableBootanimPref.isChecked() ? "1" : "0");
        } else {
            // If we didn't handle it, let preferences handle it.
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }

        return true;
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mScrollingCachePref) {
            if (newValue != null) {
                SystemProperties.set(SCROLLINGCACHE_PERSIST_PROP, (String)newValue);
                return true;
            }
        }

        return false;
    }

}
