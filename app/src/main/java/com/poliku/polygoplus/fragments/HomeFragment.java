package com.poliku.polygoplus.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.poliku.polygoplus.R;
import com.poliku.polygoplus.SearchActivity;
import com.poliku.polygoplus.AccountActivity;
import com.poliku.polygoplus.ProductDetailActivity;
import com.poliku.polygoplus.data.AppDataStore;
import com.poliku.polygoplus.data.ProductCardAdapter;
import com.poliku.polygoplus.databinding.FragmentHomeBinding;
import com.poliku.polygoplus.databinding.ItemCategoryBinding;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppDataStore.initialize(requireContext());
        setupHeader();
        setupCategories();
        setupProducts();
        setupSearchActions();
    }

    private void setupHeader() {
        binding.ivProfilePic.setOnClickListener(v -> startActivity(new Intent(requireContext(), AccountActivity.class)));
    }

    private void setupCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Food", android.R.drawable.ic_menu_today));
        categories.add(new Category("Drink", android.R.drawable.ic_menu_compass));
        categories.add(new Category("Tech", android.R.drawable.ic_menu_camera));
        categories.add(new Category("Books", android.R.drawable.ic_menu_agenda));
        categories.add(new Category("Repair", android.R.drawable.ic_menu_manage));

        binding.recyclerViewCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerViewCategories.setAdapter(new CategoryAdapter(categories));
    }

    private void setupProducts() {
        binding.recyclerViewProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.recyclerViewProducts.setAdapter(new ProductCardAdapter(AppDataStore.getListings(requireContext()), (adapter, product) -> {
            Intent intent = new Intent(requireContext(), ProductDetailActivity.class);
            intent.putExtra(ProductDetailActivity.EXTRA_LISTING_ID, product.id);
            startActivity(intent);
        }));
    }

    private void setupSearchActions() {
        View.OnClickListener openSearch = v -> startActivity(new Intent(requireContext(), SearchActivity.class));
        binding.searchBarCard.setOnClickListener(openSearch);
        binding.btnExploreNow.setOnClickListener(openSearch);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static class Category {
        String name;
        int iconRes;

        Category(String name, int iconRes) {
            this.name = name;
            this.iconRes = iconRes;
        }
    }

    // --- Category adapter ---

    private static class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
        private final List<Category> categories;

        CategoryAdapter(List<Category> categories) {
            this.categories = categories;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemCategoryBinding binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Category category = categories.get(position);
            holder.binding.textViewCategoryName.setText(category.name);
            holder.binding.imageViewCategory.setImageResource(category.iconRes);
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), SearchActivity.class);
                intent.putExtra(SearchActivity.EXTRA_CATEGORY, category.name);
                v.getContext().startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return categories.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            ItemCategoryBinding binding;
            ViewHolder(ItemCategoryBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }

}
