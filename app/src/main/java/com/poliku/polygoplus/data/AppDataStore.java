package com.poliku.polygoplus.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.poliku.polygoplus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Small on-device repository used until a server is connected.
 * It deliberately keeps all user-created data in one place so every screen
 * observes the same listings, saved items, messages and account state.
 */
public final class AppDataStore {
    private static final String PREFS = "polygo_local_store";
    private static final String KEY_USER = "user";
    private static final String KEY_LISTINGS = "listings";
    private static final String KEY_FAVORITES = "favorites";
    private static final String KEY_THREADS = "threads";
    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String KEY_TRANSACTIONS = "transactions";
    private static final String KEY_VERIFICATION = "verification";
    private static final String KEY_SEEDED = "seeded";

    private AppDataStore() { }

    private static SharedPreferences prefs(Context context) {
        return context.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static void initialize(Context context) {
        SharedPreferences p = prefs(context);
        if (p.getBoolean(KEY_SEEDED, false)) return;
        JSONArray listings = new JSONArray();
        addListing(listings, "Modern Sofa", "Furniture Store", "180", "4.9", "0.4 km away", R.drawable.bg_product_furniture, "Furniture", "Clean modern sofa in good condition. Pickup near campus.", false);
        addListing(listings, "Gaming Laptop", "Tech World", "2450", "4.8", "0.8 km away", R.drawable.bg_product_electronics, "Electronics", "Reliable gaming laptop, ideal for study and entertainment.", false);
        addListing(listings, "Running Shoes", "Sport Center", "95", "4.7", "1.1 km away", R.drawable.bg_product_fashion, "Fashion", "Lightly used running shoes. Ask for available sizes.", false);
        addListing(listings, "Coffee Maker", "Home Kitchen", "65", "4.9", "0.6 km away", R.drawable.bg_product_home, "Home", "Compact coffee maker for your room or shared kitchen.", false);
        addListing(listings, "Wireless Earbuds", "Sound Box", "120", "4.6", "1.4 km away", R.drawable.bg_product_electronics, "Electronics", "Wireless earbuds with charging case.", false);
        addListing(listings, "Desk Lamp", "Office Pro", "38", "0.9", "0.9 km away", R.drawable.bg_product_home, "Home", "Adjustable desk lamp for late-night study sessions.", false);
        p.edit().putString(KEY_LISTINGS, listings.toString())
                .putStringSet(KEY_FAVORITES, new HashSet<>())
                .putString(KEY_THREADS, "[]")
                .putString(KEY_NOTIFICATIONS, "[]")
                .putString(KEY_TRANSACTIONS, "[]")
                .putBoolean(KEY_VERIFICATION, false)
                .putBoolean(KEY_SEEDED, true)
                .apply();
    }

    private static void addListing(JSONArray list, String title, String seller, String price, String rating, String distance, int imageRes, String category, String description, boolean own) {
        try {
            JSONObject item = new JSONObject();
            item.put("id", UUID.randomUUID().toString());
            item.put("title", title);
            item.put("seller", seller);
            item.put("price", price);
            item.put("rating", rating);
            item.put("distance", distance);
            item.put("imageRes", imageRes);
            item.put("category", category);
            item.put("description", description);
            item.put("owner", own);
            item.put("available", true);
            item.put("createdAt", System.currentTimeMillis());
            list.put(item);
        } catch (JSONException ignored) { }
    }

    private static JSONArray array(Context context, String key) {
        try { return new JSONArray(prefs(context).getString(key, "[]")); }
        catch (JSONException e) { return new JSONArray(); }
    }

    private static void saveArray(Context context, String key, JSONArray value) {
        prefs(context).edit().putString(key, value.toString()).apply();
    }

    public static boolean hasAccount(Context context) { return prefs(context).contains(KEY_USER); }
    public static boolean isLoggedIn(Context context) { return prefs(context).getBoolean("loggedIn", false); }
    public static void logout(Context context) { prefs(context).edit().putBoolean("loggedIn", false).apply(); }

    public static boolean register(Context context, String name, String studentId, String email, String password) {
        if (hasAccount(context)) return false;
        try {
            JSONObject user = new JSONObject();
            user.put("name", name); user.put("studentId", studentId); user.put("email", email); user.put("password", password);
            prefs(context).edit().putString(KEY_USER, user.toString()).putBoolean("loggedIn", true).apply();
            return true;
        } catch (JSONException e) { return false; }
    }

    public static boolean login(Context context, String studentId, String password) {
        try {
            JSONObject user = new JSONObject(prefs(context).getString(KEY_USER, "{}"));
            boolean valid = studentId.equalsIgnoreCase(user.optString("studentId")) && password.equals(user.optString("password"));
            if (valid) prefs(context).edit().putBoolean("loggedIn", true).apply();
            return valid;
        } catch (JSONException e) { return false; }
    }

    public static String userName(Context context) {
        try { return new JSONObject(prefs(context).getString(KEY_USER, "{}")).optString("name", "PolyGo member"); }
        catch (JSONException e) { return "PolyGo member"; }
    }
    public static String userEmail(Context context) { try { return new JSONObject(prefs(context).getString(KEY_USER, "{}")).optString("email", ""); } catch (JSONException e) { return ""; } }
    public static String userStudentId(Context context) { try { return new JSONObject(prefs(context).getString(KEY_USER, "{}")).optString("studentId", ""); } catch (JSONException e) { return ""; } }
    public static String userMobile(Context context) { try { return new JSONObject(prefs(context).getString(KEY_USER, "{}")).optString("mobile", ""); } catch (JSONException e) { return ""; } }

    public static boolean updateProfile(Context context, String name, String email, String mobile) {
        try {
            JSONObject user = new JSONObject(prefs(context).getString(KEY_USER, "{}"));
            user.put("name", name); user.put("email", email); user.put("mobile", mobile);
            prefs(context).edit().putString(KEY_USER, user.toString()).apply();
            return true;
        } catch (JSONException e) { return false; }
    }

    public static boolean changePassword(Context context, String password) {
        try {
            JSONObject user = new JSONObject(prefs(context).getString(KEY_USER, "{}"));
            user.put("password", password);
            prefs(context).edit().putString(KEY_USER, user.toString()).apply();
            return true;
        } catch (JSONException e) { return false; }
    }

    public static List<ProductRecord> getListings(Context context) {
        List<ProductRecord> result = new ArrayList<>();
        JSONArray list = array(context, KEY_LISTINGS);
        for (int i = 0; i < list.length(); i++) {
            try { result.add(ProductRecord.fromJson(list.getJSONObject(i))); }
            catch (JSONException ignored) { }
        }
        return result;
    }

    public static ProductRecord getListing(Context context, String id) {
        for (ProductRecord item : getListings(context)) if (item.id.equals(id)) return item;
        return null;
    }

    public static ProductRecord addUserListing(Context context, String title, String category, String price, String description) { return addUserListing(context, title, category, price, description, ""); }
    public static ProductRecord addUserListing(Context context, String title, String category, String price, String description, String imageUri) {
        JSONArray list = array(context, KEY_LISTINGS);
        ProductRecord product = new ProductRecord(UUID.randomUUID().toString(), title, userName(context), price, "New", "Near campus", imageForCategory(category), imageUri, category, description, true, true);
        try { list.put(product.toJson()); saveArray(context, KEY_LISTINGS, list); } catch (JSONException ignored) { }
        addNotification(context, "Your listing is live", title + " was added to the marketplace.");
        return product;
    }

    public static boolean markSold(Context context, String id) {
        JSONArray list = array(context, KEY_LISTINGS);
        for (int i = 0; i < list.length(); i++) {
            try { JSONObject o = list.getJSONObject(i); if (id.equals(o.optString("id"))) { o.put("available", false); saveArray(context, KEY_LISTINGS, list); return true; } }
            catch (JSONException ignored) { }
        }
        return false;
    }

    private static int imageForCategory(String category) {
        if ("Electronics".equalsIgnoreCase(category)) return R.drawable.bg_product_electronics;
        if ("Fashion".equalsIgnoreCase(category)) return R.drawable.bg_product_fashion;
        if ("Furniture".equalsIgnoreCase(category) || "Home".equalsIgnoreCase(category)) return R.drawable.bg_product_furniture;
        return R.drawable.bg_product_home;
    }

    public static boolean isFavorite(Context context, String id) { return favoriteIds(context).contains(id); }

    public static void toggleFavorite(Context context, String id) {
        Set<String> ids = favoriteIds(context);
        if (!ids.add(id)) ids.remove(id);
        prefs(context).edit().putStringSet(KEY_FAVORITES, ids).apply();
    }

    private static Set<String> favoriteIds(Context context) {
        try {
            return new HashSet<>(prefs(context).getStringSet(KEY_FAVORITES, new HashSet<>()));
        } catch (ClassCastException e) {
            // If the key was previously used to store a String (legacy JSON array), clear it.
            prefs(context).edit().remove(KEY_FAVORITES).apply();
            return new HashSet<>();
        }
    }

    public static List<ProductRecord> getFavorites(Context context) {
        Set<String> ids = favoriteIds(context); List<ProductRecord> result = new ArrayList<>();
        for (ProductRecord item : getListings(context)) if (ids.contains(item.id)) result.add(item);
        return result;
    }

    public static List<ProductRecord> getMyListings(Context context) {
        List<ProductRecord> result = new ArrayList<>();
        for (ProductRecord item : getListings(context)) if (item.owner) result.add(item);
        return result;
    }

    public static String addThread(Context context, String listingId, String otherName, String initialMessage) {
        JSONArray threads = array(context, KEY_THREADS);
        try {
            JSONObject thread = new JSONObject(); String id = UUID.randomUUID().toString(); thread.put("id", id); thread.put("listingId", listingId); thread.put("name", otherName); thread.put("unread", false);
            JSONArray messages = new JSONArray(); JSONObject message = new JSONObject(); message.put("sender", userName(context)); message.put("text", initialMessage); message.put("time", System.currentTimeMillis()); messages.put(message); thread.put("messages", messages); threads.put(thread); saveArray(context, KEY_THREADS, threads);
            return id;
        } catch (JSONException ignored) { return null; }
    }

    public static List<ThreadRecord> getThreads(Context context) {
        List<ThreadRecord> result = new ArrayList<>(); JSONArray list = array(context, KEY_THREADS);
        for (int i = 0; i < list.length(); i++) try { result.add(ThreadRecord.fromJson(list.getJSONObject(i))); } catch (JSONException ignored) { }
        return result;
    }

    public static ThreadRecord getThread(Context context, String id) { for (ThreadRecord t : getThreads(context)) if (t.id.equals(id)) return t; return null; }

    public static void sendMessage(Context context, String threadId, String text) {
        JSONArray threads = array(context, KEY_THREADS);
        for (int i = 0; i < threads.length(); i++) try { JSONObject t = threads.getJSONObject(i); if (threadId.equals(t.optString("id"))) { JSONArray msgs = t.optJSONArray("messages"); if (msgs == null) msgs = new JSONArray(); JSONObject m = new JSONObject(); m.put("sender", userName(context)); m.put("text", text); m.put("time", System.currentTimeMillis()); msgs.put(m); t.put("messages", msgs); saveArray(context, KEY_THREADS, threads); return; } } catch (JSONException ignored) { }
    }

    public static void addNotification(Context context, String title, String body) {
        JSONArray list = array(context, KEY_NOTIFICATIONS); try { JSONObject o = new JSONObject(); o.put("title", title); o.put("body", body); o.put("time", System.currentTimeMillis()); o.put("read", false); list.put(o); saveArray(context, KEY_NOTIFICATIONS, list); } catch (JSONException ignored) { }
    }

    public static void addTransaction(Context context, String listingId, String title, String amount) {
        JSONArray list = array(context, KEY_TRANSACTIONS);
        try { JSONObject o = new JSONObject(); o.put("id", UUID.randomUUID().toString()); o.put("listingId", listingId); o.put("title", title); o.put("amount", amount); o.put("status", "Offer sent"); o.put("time", System.currentTimeMillis()); list.put(o); saveArray(context, KEY_TRANSACTIONS, list); addNotification(context, "Offer sent", "Your offer for " + title + " was saved."); } catch (JSONException ignored) { }
    }

    public static List<String[]> getTransactions(Context context) {
        List<String[]> result = new ArrayList<>(); JSONArray list = array(context, KEY_TRANSACTIONS);
        for (int i = list.length() - 1; i >= 0; i--) try { JSONObject o = list.getJSONObject(i); result.add(new String[]{o.optString("title"), o.optString("amount"), o.optString("status")}); } catch (JSONException ignored) { }
        return result;
    }

    public static List<NotificationRecord> getNotifications(Context context) { List<NotificationRecord> result = new ArrayList<>(); JSONArray list = array(context, KEY_NOTIFICATIONS); for (int i = list.length()-1; i >= 0; i--) try { result.add(NotificationRecord.fromJson(list.getJSONObject(i))); } catch (JSONException ignored) { } return result; }
    public static boolean isVerified(Context context) { return prefs(context).getBoolean(KEY_VERIFICATION, false); }
    public static boolean verifyAccount(Context context, String studentId, String email) {
        try { JSONObject user = new JSONObject(prefs(context).getString(KEY_USER, "{}")); boolean valid = studentId.equalsIgnoreCase(user.optString("studentId")) && email.equalsIgnoreCase(user.optString("email")); if (valid) setVerified(context); return valid; } catch (JSONException e) { return false; }
    }
    public static void setVerified(Context context) { prefs(context).edit().putBoolean(KEY_VERIFICATION, true).apply(); addNotification(context, "Verification complete", "Your PolyGo account is now verified."); }

    public static final class ProductRecord {
        public final String id, title, seller, price, rating, distance, imageUri, category, description; public final int imageRes; public final boolean owner, available;
        public ProductRecord(String id, String title, String seller, String price, String rating, String distance, int imageRes, String category, String description, boolean owner, boolean available) { this(id,title,seller,price,rating,distance,imageRes,"",category,description,owner,available); }
        public ProductRecord(String id, String title, String seller, String price, String rating, String distance, int imageRes, String imageUri, String category, String description, boolean owner, boolean available) { this.id=id; this.title=title; this.seller=seller; this.price=price; this.rating=rating; this.distance=distance; this.imageRes=imageRes; this.imageUri=imageUri; this.category=category; this.description=description; this.owner=owner; this.available=available; }
        static ProductRecord fromJson(JSONObject o) { return new ProductRecord(o.optString("id"), o.optString("title"), o.optString("seller"), o.optString("price"), o.optString("rating"), o.optString("distance"), o.optInt("imageRes", R.drawable.bg_product_home), o.optString("imageUri"), o.optString("category"), o.optString("description"), o.optBoolean("owner"), o.optBoolean("available", true)); }
        JSONObject toJson() throws JSONException { JSONObject o = new JSONObject(); o.put("id",id);o.put("title",title);o.put("seller",seller);o.put("price",price);o.put("rating",rating);o.put("distance",distance);o.put("imageRes",imageRes);o.put("imageUri",imageUri);o.put("category",category);o.put("description",description);o.put("owner",owner);o.put("available",available);return o; }
    }
    public static final class ThreadRecord { public final String id, listingId, name, preview; public final JSONArray messages; ThreadRecord(String id,String listingId,String name,String preview,JSONArray messages){this.id=id;this.listingId=listingId;this.name=name;this.preview=preview;this.messages=messages;} static ThreadRecord fromJson(JSONObject o){JSONArray m=o.optJSONArray("messages"); if(m==null)m=new JSONArray(); String p=m.length()>0?m.optJSONObject(m.length()-1).optString("text",""):""; return new ThreadRecord(o.optString("id"),o.optString("listingId"),o.optString("name"),p,m);} }
    public static final class NotificationRecord { public final String title, body; public NotificationRecord(String title,String body){this.title=title;this.body=body;} static NotificationRecord fromJson(JSONObject o){return new NotificationRecord(o.optString("title"),o.optString("body"));} }
}
