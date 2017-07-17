package minasedrak.squawker.following;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

import minasedrak.squawker.R;


/**
 * Created by MinaSedrak on 7/16/2017.
 */

public class FollowingPreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String LOG_TAG = FollowingPreferenceFragment.class.getSimpleName();

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        // Add visualizer preferences Defined in XML
        addPreferencesFromResource(R.xml.following_squawkers);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Preference instructor = findPreference(key);

        if( instructor != null && instructor instanceof SwitchPreferenceCompat){

            boolean isOn = sharedPreferences.getBoolean(key, false);

            if(isOn){

                FirebaseMessaging.getInstance().subscribeToTopic(key);
                Log.i(LOG_TAG, "Subscribing to "+ key);

            }else {

                FirebaseMessaging.getInstance().unsubscribeFromTopic(key);
                Log.i(LOG_TAG, "Un-subscribing to "+ key);
            }

        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add Shared Preference Change Listener
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Remove Shared Preference Change Listener
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
