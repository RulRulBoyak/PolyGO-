package com.poliku.polygoplus.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.poliku.polygoplus.R;
import com.poliku.polygoplus.SearchActivity;
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
        setupSearchActions();
        setupVerificationBanner();
        animateHeader();
    }

    private void setupCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Tech", android.R.drawable.ic_menu_camera));
        categories.add(new Category("Fashion", android.R.drawable.ic_menu_gallery));
        categories.add(new Category("Home", android.R.drawable.ic_menu_manage));
        categories.add(new Category("Books", android.R.drawable.ic_menu_agenda));
        categories.add(new Category("Services", android.R.drawable.ic_menu_compass));
        categories.add(new Category("Deals", android.R.drawable.ic_menu_week));

        binding.recyclerViewCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerViewCategories.setAdapter(new CategoryAdapter(categories));
    }

    private void setupProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("Modern Sofa", "Furniture Store", "RM 180", "4.9", "0.4 km away", R.drawable.bg_product_furniture));
        products.add(new Product("Gaming Laptop", "Tech World", "RM 2,450", "4.8", "0.8 km away", R.drawable.bg_product_electronics));
        products.add(new Product("Running Shoes", "Sport Center", "RM 95", "4.7", "1.1 km away", R.drawable.bg_product_fashion));
        products.add(new Product("Coffee Maker", "Home Kitchen", "RM 65", "4.9", "0.6 km away", R.drawable.bg_product_home));
        products.add(new Product("Wireless Earbuds", "Sound Box", "RM 120", "4.6", "1.4 km away", R.drawable.bg_product_electronics));
        products.add(new Product("Desk Lamp", "Office Pro", "RM 38", "4.8", "0.9 km away", R.drawable.bg_product_home));

        binding.recyclerViewProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.recyclerViewProducts.setAdapter(new ProductAdapter(products));
    }

    private void setupSearchActions() {
        View.OnClickListener openSearch = v -> startActivity(new Intent(requireContext(), SearchActivity.class));
        binding.searchBarCard.setOnClickListener(openSearch);
        binding.editTextSearch.setOnClickListener(openSearch);
        binding.buttonFilters.setOnClickListener(v ->
                Toast.makeText(requireContext(), "Filters coming soon", Toast.LENGTH_SHORT).show());
    }

    private void setupVerificationBanner() {
        binding.verificationBanner.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Verified sellers are checked before listing.", Toast.LENGTH_SHORT).show();
        });
    }

    private void animateHeader() {
        animateIn(binding.searchBarCard, 0);
        animateIn(binding.recyclerViewCategories, 1);
        animateIn(binding.bentoFeatured, 2);
        animateIn(binding.bentoSmall1, 3);
        animateIn(binding.bentoSmall2, 4);
        animateIn(binding.verificationBanner, 5);
    }

    private static void animateIn(View view, int index) {
        view.setAlpha(0f);
        view.setTranslationY(28f);
        view.animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(index * 70L)
                .setDuration(360L)
                .setInterpolator(new DecelerateInterpolator())
                .start();
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
        String price;
        String rating;
        String distance;
        int imageRes;

        Product(String title, String seller, String price, String rating, String distance, int imageRes) {
            this.title = title;
            this.seller = seller;
            this.price = price;
            this.rating = rating;
            this.distance = distance;
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
            animateIn(holder.binding.getRoot(), position);
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
            holder.binding.textViewPrice.setText(product.price);
            holder.binding.textViewRating.setText(product.rating);
            holder.binding.textViewDistance.setText(product.distance);
            holder.binding.imageView.setImageResource(product.imageRes);
            holder.binding.buttonFavorite.setColorFilter(0xFF222222);
            holder.binding.buttonFavorite.setSelected(false);
            holder.binding.buttonFavorite.setOnClickListener(v -> {
                v.setSelected(!v.isSelected());
                holder.binding.buttonFavorite.setColorFilter(v.isSelected() ? 0xFFFF385C : 0xFF222222);
                v.animate()
                        .scaleX(v.isSelected() ? 1.14f : 1f)
                        .scaleY(v.isSelected() ? 1.14f : 1f)
                        .setDuration(120L)
                        .withEndAction(() -> v.animate().scaleX(1f).scaleY(1f).setDuration(120L).start())
                        .start();
            });
            animateIn(holder.binding.getRoot(), position);
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
