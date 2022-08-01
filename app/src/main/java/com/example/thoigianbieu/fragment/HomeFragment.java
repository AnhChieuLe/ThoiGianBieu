package com.example.thoigianbieu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.thoigianbieu.R;
import com.example.thoigianbieu.SuKienActivity;

public class HomeFragment extends Fragment {
    SuKienFragment suKienFragment;
    ThoiKhoaBieuFragment thoiKhoaBieuFragment;

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            suKienFragment.loadData();
        }
    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        setControl(view);

        setFragment();

        return view;
    }

    private void setControl(View view){
        suKienFragment = new SuKienFragment(true);
        thoiKhoaBieuFragment = new ThoiKhoaBieuFragment(true);
    }

    private void setFragment(){
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_home_thoikhoabieu, thoiKhoaBieuFragment);
        transaction.replace(R.id.frame_home_sukien, suKienFragment);
        transaction.commit();
    }
}
