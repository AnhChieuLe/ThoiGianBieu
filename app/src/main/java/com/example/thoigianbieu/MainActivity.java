package com.example.thoigianbieu;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.thoigianbieu.fragment.HomeFragment;
import com.example.thoigianbieu.fragment.MonHocFragment;
import com.example.thoigianbieu.fragment.SettingFragment;
import com.example.thoigianbieu.fragment.SuKienFragment;
import com.example.thoigianbieu.fragment.ThoiKhoaBieuFragment;
import com.example.thoigianbieu.setting.SharePreferencesManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;

    public static final int HOME = 0;
    public static final int THOIKHOABIEU = 1;
    public static final int SUKIEN = 2;
    public static final int MONHOC = 3;
    public static final int CAIDAT = 4;
    public static int currentFragment = HOME;

    private TextView tvName;
    private CircleImageView avatar;
    NavigationView navigationView;
    Toolbar toolbar;
    View header;
    GoogleSignInAccount account;

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
            try {
                account = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
                task.getResult(ApiException.class);
                SharePreferencesManager.putEmail(account.getEmail());
                setHeader();
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setControl();

        setNavigation();

        setHeader();

        setHeaderClick();
    }

    private void setHeaderClick() {
        account = GoogleSignIn.getLastSignedInAccount(this);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(account == null){
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                    GoogleSignInClient gsc = GoogleSignIn.getClient(MainActivity.this, gso);
                    Intent intentLogin = gsc.getSignInIntent();
                    activityResultLauncher.launch(intentLogin);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setHeader();
    }

    private void setNavigation(){
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        //Set Default
        rePlaceFragment(new HomeFragment());
        getSupportActionBar().setTitle(R.string.trangchu);
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
    }

    private void setHeader() {
        account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            tvName.setText(account.getDisplayName());
            new getAvatar().execute(account);
        }else{
            tvName.setText(R.string.dangnhaptaikhoancuaban);
            avatar.setImageResource(R.drawable.user_avatar);
        }
    }

    private void setControl() {
        navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);

        header = navigationView.getHeaderView(0);
        tvName = header.findViewById(R.id.tv_header_name);
        avatar = header.findViewById(R.id.imgAvatar);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        HomeFragment home = new HomeFragment();
        ThoiKhoaBieuFragment thoiKhoaBieu = new ThoiKhoaBieuFragment(false);
        SuKienFragment suKien = new SuKienFragment(false);
        MonHocFragment monHoc = new MonHocFragment();
        SettingFragment setting = new SettingFragment(new SettingFragment.ResumeActivity() {
            @Override
            public void reSume() {
                setHeader();
            }
        });

        if(id==R.id.nav_home){
            if(currentFragment != HOME){
                rePlaceFragment(home);
                currentFragment = HOME;
                getSupportActionBar().setTitle(R.string.trangchu);
            }
        }else if(id==R.id.nav_thoikhoabieu){
            if(currentFragment != THOIKHOABIEU){
                rePlaceFragment(thoiKhoaBieu);
                currentFragment = THOIKHOABIEU;
                getSupportActionBar().setTitle(R.string.thoikhoabieu);
            }
        }else if(id==R.id.nav_sukien){
            if(currentFragment != SUKIEN){
                rePlaceFragment(suKien);
                currentFragment = SUKIEN;
                getSupportActionBar().setTitle(R.string.sukien);
            }
        }else if(id==R.id.nav_monhoc){
            if(currentFragment != MONHOC){
                rePlaceFragment(monHoc);
                currentFragment = MONHOC;
                getSupportActionBar().setTitle(R.string.monhoc);
            }
        }
        else if(id==R.id.nav_setting){
            if(currentFragment != CAIDAT){
                rePlaceFragment(setting);
                currentFragment = CAIDAT;
                getSupportActionBar().setTitle(R.string.caidat);
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    private void rePlaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }

    private void rePlaceFragment(SettingFragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    class getAvatar extends AsyncTask<GoogleSignInAccount, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(GoogleSignInAccount... googleSignInAccounts) {
            Bitmap bitmap = null;
            try {
                Uri avatarURI = googleSignInAccounts[0].getPhotoUrl();
                URL url = new URL(avatarURI.toString());
                InputStream inputStream = url.openConnection().getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            avatar.setImageBitmap(bitmap);
        }
    }
}