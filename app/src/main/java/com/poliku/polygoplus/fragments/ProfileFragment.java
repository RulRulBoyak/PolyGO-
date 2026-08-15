package com.poliku.polygoplus.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.poliku.polygoplus.AccountActivity;
import com.poliku.polygoplus.MainActivity;
import com.poliku.polygoplus.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.menuUserProfile).setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), AccountActivity.class));
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        view.findViewById(R.id.menuChangePassword).setOnClickListener(v -> {
            BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
            dialog.setContentView(R.layout.bottom_sheet_change_password);
            dialog.show();
        });

        view.findViewById(R.id.ivLogout).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        // Other menus can be wired here similarly
    }
}