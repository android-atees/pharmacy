package in.ateesinfomedia.remedio.view.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.ateesinfomedia.remedio.R;
import in.ateesinfomedia.remedio.models.NotificationModel;
import in.ateesinfomedia.remedio.view.adapter.DoctorsAdapter;
import in.ateesinfomedia.remedio.view.adapter.NotificationAdapter;

public class NotificationActivity extends AppCompatActivity {

    List<NotificationModel> notificationModelList = new ArrayList<>();
    private RecyclerView notification_recycler;
    private Toolbar toolbar;
    private NotificationAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //must be called before setContentView(...)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            Drawable background = this.getResources().getDrawable(R.drawable.ab_gradient);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(android.R.color.transparent));
//            window.setNavigationBarColor(this.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }

        setContentView(R.layout.activity_notification);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        notification_recycler = (RecyclerView) findViewById(R.id.notification_recycler);
        notificationModelList.clear();
        notificationModelList = (List<NotificationModel>) getIntent().getSerializableExtra("notification_data");

        notification_recycler.setNestedScrollingEnabled(true);
        mAdapter = new NotificationAdapter(this,notificationModelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
//        ShimmerRecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        notification_recycler.setLayoutManager(mLayoutManager/*mLayoutManager*/);
        notification_recycler.setItemAnimator(new DefaultItemAnimator());
        notification_recycler.setAdapter(mAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(NotificationActivity.this,MainActivity.class);
        intent.putExtra("info","notification");
        startActivity(intent);
        finishAffinity();
    }
}