package com.example.thoigianbieu;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.thoigianbieu.databinding.ActivityBackupBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

public class BackupActivity extends AppCompatActivity {
    ActivityBackupBinding binding;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    GoogleSignInAccount account;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
            try {
                task.getResult(ApiException.class);
                setAccount();
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBackupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
        gsc = GoogleSignIn.getClient(this, gso);

        setToolbar();
        setAccount();
        setLogoutButton();

        showDialog();
    }

    private void setToolbar(){
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    void setAccount(){
        account = GoogleSignIn.getLastSignedInAccount(this);
        if(account == null){
            binding.tvUsername.setVisibility(View.GONE);
            binding.tvEmail.setVisibility(View.GONE);
            binding.btnLogout.setText(R.string.dangnhap);
        }else {
            binding.tvUsername.setVisibility(View.VISIBLE);
            binding.tvEmail.setVisibility(View.VISIBLE);
            binding.btnLogout.setText(R.string.dangxuat);

            binding.tvUsername.setText(account.getDisplayName());
            binding.tvEmail.setText(account.getEmail());
        }
    }

    private void setLogoutButton() {
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = GoogleSignIn.getLastSignedInAccount(BackupActivity.this);
                if(account != null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(BackupActivity.this);
                    builder.setTitle(R.string.canhbao)
                            .setMessage(R.string.xacnhandangxuat)
                            .setPositiveButton(R.string.dangxuat, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    gsc.signOut();
                                    setAccount();
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
                }else {
                    Intent signInIntent = gsc.getSignInIntent();
                    activityResultLauncher.launch(signInIntent);
                }
            }
        });
    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.canhbao)
                .setMessage(R.string.featurenotsupport)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        onBackPressed();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.backgroud_dialog);
        dialog.show();
    }
}