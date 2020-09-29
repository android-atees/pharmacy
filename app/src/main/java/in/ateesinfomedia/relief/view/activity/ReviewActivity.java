package in.ateesinfomedia.relief.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hsalf.smilerating.SmileRating;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.components.LoadingDialog;
import in.ateesinfomedia.relief.configurations.Apis;
import in.ateesinfomedia.relief.configurations.Global;
import in.ateesinfomedia.relief.interfaces.NetworkCallback;
import in.ateesinfomedia.relief.managers.MyPreferenceManager;
import in.ateesinfomedia.relief.managers.NetworkManager;
import in.ateesinfomedia.relief.models.ReviewModel;

import static in.ateesinfomedia.relief.configurations.Global.isChanged;

public class ReviewActivity extends AppCompatActivity implements NetworkCallback {

    private SmileRating smileRating;
    private int ratinglevel = 0;
    private EditText mEd_Review;
    private Button mSubmitBtn;
    private String review = "";
    private String pro_id;
    private MyPreferenceManager manager;
    private int REQUEST_UPDATE_REVIEW_ID = 8999;
    private ReviewModel reviewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        pro_id = getIntent().getStringExtra("pro_id");
        reviewModel = (ReviewModel) getIntent().getSerializableExtra("myReview");

        manager = new MyPreferenceManager(this);
        smileRating = (SmileRating) findViewById(R.id.smile_rating);
        mEd_Review = (EditText) findViewById(R.id.ed_review);
        mSubmitBtn = (Button) findViewById(R.id.btn_submit);
//        smileRating.setIndicator(true);

        smileRating.setOnRatingSelectedListener(new SmileRating.OnRatingSelectedListener() {

            @Override
            public void onRatingSelected(int level, boolean reselected) {
                // level is from 1 to 5 (0 when none selected)
                // reselected is false when user selects different smiley that previously selected one
                // true when the same smiley is selected.
                // Except if it first time, then the value will be false.
                ratinglevel = level;
            }
        });

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                review = mEd_Review.getText().toString();
                if (ratinglevel == 0 && (review.equals("") || review.equals("null") || review.isEmpty())){
                    Toast.makeText(ReviewActivity.this, "Please add any review and rating.", Toast.LENGTH_SHORT).show();
                } else if (ratinglevel == 0){
                    Toast.makeText(ReviewActivity.this, "Please add your rating.", Toast.LENGTH_SHORT).show();
                } else if (review.equals("") || review.equals("null") || review.isEmpty()){
                    Toast.makeText(ReviewActivity.this, "Please add your review.", Toast.LENGTH_SHORT).show();
                } else {
                    doSubmitReview();
                }
            }
        });

        if (reviewModel != null){
            mEd_Review.setText(reviewModel.getReview());
            float rat = Float.parseFloat(reviewModel.getRating());
            final int rating = Math.round(rat);
            Log.d("ratttttttttttt",""+rating);
//            smileRating.setSelectedSmile((int) rating,true);

            smileRating.post(new Runnable() {
                @Override
                public void run() {
                    if (rating == 1){
                        smileRating.setSelectedSmile(0,true);
                    } else if (rating == 2){
                        smileRating.setSelectedSmile(1,true);
                    } else if (rating == 3){
                        smileRating.setSelectedSmile(2,true);
                    } else if (rating == 4){
                        smileRating.setSelectedSmile(3,true);
                    } else if (rating == 5){
                        smileRating.setSelectedSmile(4,true);
                    }
//                    smileRating.setSelectedSmile(rating,true);
                }
            });
        }
    }

    private void doSubmitReview() {

        Map<String,String> map = new HashMap<>();
        map.put("user_id",manager.getdelicioId());
        map.put("product_id",pro_id);
        map.put("rating",""+ratinglevel);
        map.put("comments",review);

        new NetworkManager(this).doPost(map, Apis.API_POST_UPDATE_REVIEW,"REQUEST_UPDATE_REVIEW",REQUEST_UPDATE_REVIEW_ID,this);
        LoadingDialog.showLoadingDialog(this,"Loading...");
    }

    @Override
    public void onResponse(int status, String response, int requestId) {

        LoadingDialog.cancelLoading();
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                Toast.makeText(this, "Something went wrong..Please try again!!", Toast.LENGTH_SHORT).show();
            } else {

                JSONArray jsonArray = jsonObject.optJSONArray("data");
                JSONObject jsonObject1 = jsonArray.optJSONObject(0);
                ReviewModel reviewsModel = new ReviewModel();

                reviewsModel.setId(jsonObject1.optString("id"));
                reviewsModel.setRating(jsonObject1.optString("rating"));
                reviewsModel.setReview(jsonObject1.optString("review"));
                reviewsModel.setProduct_id(jsonObject1.optString("product_id"));
                reviewsModel.setCreate_date(jsonObject1.optString("create_date"));
                reviewsModel.setFirstname(jsonObject1.optString("firstname"));

                Global.LastReviewModel = reviewsModel;
                isChanged = true;

                Toast.makeText(this, "Successfully updated your review.", Toast.LENGTH_SHORT).show();
                finish();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onJsonResponse(int status, String response, int requestId) {

    }
}
