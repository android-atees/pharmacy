package in.ateesinfomedia.remedio.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import in.ateesinfomedia.remedio.R;
import in.ateesinfomedia.remedio.configurations.Apis;

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