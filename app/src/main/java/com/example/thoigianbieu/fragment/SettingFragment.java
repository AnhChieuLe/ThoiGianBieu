package com.example.thoigianbieu.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.example.thoigianbieu.R;
import com.example.thoigianbieu.MyApplication;
import com.example.thoigianbieu.setting.SharePreferencesManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.jakewharton.processphoenix.ProcessPhoenix;

public class SettingFragment extends PreferenceFragmentCompat {
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    GoogleSignInAccount account;

    Preference loginPreferences;
    ResumeActivity resumeActivity;

    public SettingFragment(ResumeActivity resumeActivity) {
        this.resumeActivity = resumeActivity;
    }

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
            try {
                account = GoogleSignIn.getLastSignedInAccount(getActivity());
                task.getResult(ApiException.class);

                loginPreferences.setTitle(R.string.dangxuat);
                SharePreferencesManager.putEmail(account.getEmail());
                loginPreferences.setSummary(SharePreferencesManager.getEmail());
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    });

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting_resource, rootKey);

        setControl();

        SwitchPreference includeMonHoc = findPreference("PREF_TKB_INCLUDE_MH");
        assert includeMonHoc != null;
        includeMonHoc.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                MyApplication.updateTKBWidget(getActivity().getApplication());
                return true;
            }
        });

        ListPreference darkModeStatus = findPreference("PREF_INTERFACE");
        assert darkModeStatus != null;
        darkModeStatus.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                int oldVl = SharePreferencesManager.getInterface();
                int newVl = Integer.parseInt(newValue.toString());
                int current = -1;

                int nightModeFlags = getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                if(nightModeFlags == Configuration.UI_MODE_NIGHT_YES){
                    current = 1;
                }
                if(nightModeFlags == Configuration.UI_MODE_NIGHT_NO){
                    current = 0;
                }

                if(oldVl == 0){
                    if(newVl == 1){
                        ProcessPhoenix.triggerRebirth(getActivity());
                    }
                    if(newVl == 2 && current != oldVl){
                        ProcessPhoenix.triggerRebirth(getActivity());
                    }
                }

                if(oldVl == 1){
                    if(newVl == 0){
                        ProcessPhoenix.triggerRebirth(getActivity());
                    }
                    if(newVl == 2 && current != oldVl){
                        ProcessPhoenix.triggerRebirth(getActivity());
                    }
                }

                if(oldVl == 2){
                    if(newVl != current){
                        ProcessPhoenix.triggerRebirth(getActivity());
                    }
                }

                return true;
            }
        });


        setTitle();
        loginPreferences.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                if(account == null){
                    Intent loginIntent = gsc.getSignInIntent();
                    activityResultLauncher.launch(loginIntent);
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(R.string.canhbao)
                            .setMessage("Xác nhận đăng xuất")
                            .setPositiveButton(R.string.dangxuat, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    signOut();
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(R.string.huy, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.backgroud_dialog);
                    dialog.show();
                }

                return true;
            }
        });
    }

    private void signOut(){

        gsc.signOut();
        account = GoogleSignIn.getLastSignedInAccount(getActivity());

        loginPreferences.setTitle(R.string.dangnhap);
        SharePreferencesManager.putEmail("");
        loginPreferences.setSummary(SharePreferencesManager.getEmail());

        resumeActivity.reSume();
        //startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    private void setTitle(){
        if(account == null){
            loginPreferences.setTitle(R.string.dangnhap);
        }else{
            loginPreferences.setTitle(R.string.dangxuat);
            loginPreferences.setSummary(SharePreferencesManager.getEmail());
        }
    }

    private void setControl() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(getActivity(), gso);
        account = GoogleSignIn.getLastSignedInAccount(getActivity());

        loginPreferences = findPreference("PREF_LOGIN");
    }

    public interface ResumeActivity{
        void reSume();
    }
}
