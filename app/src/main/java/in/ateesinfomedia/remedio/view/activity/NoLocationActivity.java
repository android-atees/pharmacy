package in.ateesinfomedia.remedio.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import in.ateesinfomedia.remedio.R;

public class NoLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_location);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
