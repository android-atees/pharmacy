package in.ateesinfomedia.relief.view.activity;

import android.content.Intent;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.github.paolorotolo.appintro.AppIntro2;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.managers.MyPreferenceManager;
import in.ateesinfomedia.relief.view.fragment.FirstSlide;
import in.ateesinfomedia.relief.view.fragment.SecondSlide;
import in.ateesinfomedia.relief.view.fragment.ThirdSlide;

public class WelcomeActivity extends AppIntro2 {
    private MyPreferenceManager manager;

    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(new FirstSlide());
        addSlide(new SecondSlide());
        addSlide(new ThirdSlide());

//        setFlowAnimation();

        setFadeAnimation();
        showSkipButton(false);
        setIndicatorColor(getResources().getColor(R.color.colorPrimary),getResources().getColor(R.color.grey));

//        setVibrate(true);
//        setVibrateIntensity(30);

        manager = new MyPreferenceManager(this);

//        setCustomTransformer(new ZoomOutPageTransformer());
    }

    private void loadLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        manager.setFirstLaunch(false);
        finish();
    }

    @Override
    public void onSkipPressed() {
//        loadLoginActivity();
//        Toast.makeText(getApplicationContext(), getString(R.string.skip), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDonePressed() {
        loadLoginActivity();
    }

    public void getStarted(View v){
//        loadLoginActivity();
    }

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0.5f);
            }
        }
    }
}