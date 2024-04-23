package data.retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ui.PostReviewResponse;

public interface ApiService {

    @GET("detail/{id}")
    default <RestaurantResponse> Call<RestaurantResponse> getRestaurant(@Path("id") String id) {
        return null;
    }

    @FormUrlEncoded
    @POST("review")
    Call<PostReviewResponse> postReview(
            @Field("id") String id,
            @Field("name") String name,
            @Field("review") String review
    );
}
