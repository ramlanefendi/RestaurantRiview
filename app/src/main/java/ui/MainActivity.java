// Perbaikan pada kode pertama (MainActivity)
package ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.restaurantriview.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import data.response.CustomerReviewsItem;
import data.retrofit.ApiConfig;
import data.retrofit.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity<RestaurantResponse> extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String RESTAURANT_ID = "uewq1zg2zlskfw1e867";
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvReview.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        binding.rvReview.addItemDecoration(itemDecoration);

        findRestaurant();

        binding.btnSend.setOnClickListener(this::onClick);
    }

    private void findRestaurant() {
        showLoading(true);
        ApiService apiService = ApiConfig.getApiService();
        Call<RestaurantResponse> client = apiService.getRestaurant(RESTAURANT_ID);
        client.enqueue(new Callback<RestaurantResponse>() {
            @Override
            public void onResponse(Call<RestaurantResponse> call, Response<RestaurantResponse> response) {
                showLoading(false);
                if (response.isSuccessful () && response.body () != null) {
                    setRestaurantData (Objects.requireNonNull (response).body ().toString ());
                    setReviewData (response.body ().toString ());
                } else {
                    // Log pesan error dan tampilkan pesan toast ubuntu memberi tahu pengguna tentang kegagalan memuat data restoran
                    Log.e(TAG, "onResponse: Error loading restaurant data");
                    Toast.makeText(MainActivity.this, "Failed to load restaurant data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RestaurantResponse> call, Throwable t) {
                showLoading(false);
                // Log pesan error dan tampilkan pesan toast untuk memberi tahu pengguna tentang kegagalan terhubung ke server
                Log.e(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Failed to connect to the server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postReview(String review) {
        showLoading(true);
        ApiService apiService = ApiConfig.getApiService();
        Call<PostReviewResponse> client = apiService.postReview(RESTAURANT_ID, "Dicoding", review);
        client.enqueue(new Callback<PostReviewResponse>() {
            @Override
            public void onResponse(@NotNull Call<PostReviewResponse> call, @NotNull Response<PostReviewResponse> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    setReviewData(Objects.requireNonNull (response).body().getCustomerReviews().toString ());
                } else {
                    // Log pesan error dan tampilkan pesan toast untuk memberi tahu pengguna tentang kegagalan memposting review
                    Log.e(TAG, "onResponse: Error posting review");
                    Toast.makeText(MainActivity.this, "Failed to post review", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<PostReviewResponse> call, @NotNull Throwable t) {
                showLoading(false);
                // Log pesan error dan tampilkan pesan toast untuk memberi tahu pengguna tentang kegagalan terhubung ke server
                Log.e(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Failed to connect to the server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setReviewData(String string) {
    }

    private <Restaurant> void setRestaurantData(Restaurant restaurant) {
        binding.tvTitle.setText(restaurant.getClass().getModifiers());
        binding.tvDescription.setText(restaurant.getClass().getModifiers());
        Glide.with(MainActivity.this)
                .load("https://restaurant-api.dicoding.dev/images/large/" + restaurant.getClass())
                .into(binding.ivPicture);
    }

    private void setReviewData(List<CustomerReviewsItem> consumerReviews) {
        ArrayList<String> listReview = new ArrayList<>();
        for (CustomerReviewsItem review : consumerReviews) {
            listReview.add(review.getReview() + "\n- " + review.getName());
        }
        ReviewAdapter adapter = new ReviewAdapter(listReview);
        binding.rvReview.setAdapter(adapter);
    }



    private void showLoading(Boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
        }
    }

    private void onClick(View view) {
        String review = Objects.requireNonNull(binding.edReview).getClass().toString();
        if (!review.isEmpty()) {
            postReview(review);
        } else {
            // Menampilkan pesan toast saat review kosong
            Toast.makeText(MainActivity.this, "Please enter a review", Toast.LENGTH_SHORT).show();
        }
        // Sembunyikan keyboard setelah mengirim review
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        final boolean b = imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}