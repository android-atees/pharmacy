package in.ateesinfomedia.relief.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.configurations.Apis;

public class ImageShowActivity extends AppCompatActivity {

    private ImageView mImPrescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);

        String image = getIntent().getStringExtra("image");
        mImPrescription = (ImageView) findViewById(R.id.presImage);
        Glide.with(this).load(Apis.API_POST_PRESCRIPTION_IMAGES + image).into(mImPrescription);

    }
}