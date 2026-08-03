package com.poliku.polygoplus.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.poliku.polygoplus.R;
import com.poliku.polygoplus.databinding.FragmentHomeBinding;
import com.poliku.polygoplus.databinding.ItemCategoryBinding;
import com.poliku.polygoplus.databinding.ItemProductCardBinding;
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

        setupCategories();
        setupProducts();
        setupVerificationBanner();
    }

    private void setupCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Electronics", android.R.drawable.ic_menu_camera));
        categories.add(new Category("Fashion", android.R.drawable.ic_menu_gallery));
        categories.add(new Category("Home", android.R.drawable.ic_menu_manage));
        categories.add(new Category("Books", android.R.drawable.ic_menu_agenda));
        categories.add(new Category("Toys", android.R.drawable.ic_menu_day));

        binding.recyclerViewCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerViewCategories.setAdapter(new CategoryAdapter(categories));
    }

    private void setupProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("Modern Sofa", "Furniture Store", R.drawable.ic_launcher_background));
        products.add(new Product("Gaming Laptop", "Tech World", R.drawable.ic_launcher_background));
        products.add(new Product("Running Shoes", "Sport Center", R.drawable.ic_launcher_background));
        products.add(new Product("Coffee Maker", "Home Kitchen", R.drawable.ic_launcher_background));
        products.add(new Product("Wireless Earbuds", "Sound Box", R.drawable.ic_launcher_background));
        products.add(new Product("Desk Lamp", "Office Pro", R.drawable.ic_launcher_background));

        binding.recyclerViewProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.recyclerViewProducts.setAdapter(new ProductAdapter(products));
    }

    private void setupVerificationBanner() {
        // Mock verification status
        binding.verificationBanner.setOnClickListener(v -> {
            // Handle click
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // --- Mock Data Models ---

    public static class Category {
        String name;
        int iconRes;

        Category(String name, int iconRes) {
            this.name = name;
            this.iconRes = iconRes;
        }
    }

    public static class Product {
        String title;
        String seller;
        int imageRes;

        Product(String title, String seller, int imageRes) {
            this.title = title;
            this.seller = seller;
            this.imageRes = imageRes;
        }
    }

    // --- Adapters ---

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

    private static class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
        private final List<Product> products;

        ProductAdapter(List<Product> products) {
            this.products = products;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemProductCardBinding binding = ItemProductCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Product product = products.get(position);
            holder.binding.textViewTitle.setText(product.title);
            holder.binding.textViewSeller.setText(product.seller);
            holder.binding.imageView.setImageResource(product.imageRes);
        }

        @Override
        public int getItemCount() {
            return products.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            ItemProductCardBinding binding;
            ViewHolder(ItemProductCardBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}