// Perbaikan pada kode ketiga (PostReviewResponse)
package ui;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostReviewResponse {

    @SerializedName("customerReviews")
    public final ThreadLocal<List<CustomerReviewsItem>> customerReviews = new ThreadLocal<List<CustomerReviewsItem>>();
    private final List<CustomerReviewsItem> customerReviews1;

    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;

    public <CustomerReviewsItem> PostReviewResponse(List<CustomerReviewsItem> customerReviews) {
        customerReviews1 = (List<PostReviewResponse.CustomerReviewsItem>) customerReviews;
        this.customerReviews.set((List<PostReviewResponse.CustomerReviewsItem>) customerReviews);
    }

    public <CustomerReviewsItem> List<CustomerReviewsItem> getCustomerReviews() {
        return (List<CustomerReviewsItem>) customerReviews;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    private class CustomerReviewsItem {
    }
}
