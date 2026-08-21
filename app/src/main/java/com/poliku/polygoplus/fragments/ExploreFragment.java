package com.poliku.polygoplus.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.poliku.polygoplus.R;
import com.poliku.polygoplus.ProductDetailActivity;
import com.poliku.polygoplus.data.AppDataStore;
import com.poliku.polygoplus.data.ProductCardAdapter;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ExploreFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        AppDataStore.initialize(requireContext());
        RecyclerView list = view.findViewById(R.id.rvExplore);

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.explore_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        list.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        list.setAdapter(new ProductCardAdapter(AppDataStore.getListings(requireContext()), (adapter, product) -> {
            android.content.Intent intent = new android.content.Intent(requireContext(), ProductDetailActivity.class);
            intent.putExtra(ProductDetailActivity.EXTRA_LISTING_ID, product.id);
            startActivity(intent);
        }));
        return view;
    }
}
