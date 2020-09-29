package in.ateesinfomedia.relief.recorder;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;


import java.io.File;
import java.io.IOException;

import in.ateesinfomedia.relief.R;

public class RecordView extends RelativeLayout {
    private ImageView smallBlinkingMic, basketImg;
    private Chronometer counterTime;
    private TextView slideToCancel;
    private LinearLayout slideToCancelLayout;
    private ImageView arrow;


    private float initialX, basketInitialY, difX = 0;
    private float cancelBounds = 130;
    private long startTime, elapsedTime = 0;
    private Context context;
    private AlphaAnimation alphaAnimation1, alphaAnimation2;
    private OnAudioRecordListener recordListener;
    private OnBasketAnimationEnd onBasketAnimationEndListener;
    private AnimatedVectorDrawableCompat animatedVectorDrawable;
    private boolean isSwiped = false;
    private boolean isSoundEnabled = true;
    public int RECORD_START = R.raw.record_start;
    private int RECORD_FINISHED = R.raw.record_finished;
    private int RECORD_ERROR = 0 ;// R.raw.record_error;
    private MediaPlayer player;
    private AudioRecording mAudioRecording;
    private File audioDirectory;
    private boolean isRecordingStarted = false;


    public RecordView(Context context) {
        super(context);
        this.context = context;
        init(context, null, -1, -1);
    }


    public RecordView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs, -1, -1);
    }

    public RecordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context, attrs, defStyleAttr, -1);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RecordView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }


    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        View view = View.inflate(context, R.layout.record_view, null);
        addView(view);
        slideToCancelLayout = view.findViewById(R.id.slide_to_cancel_layout);
        arrow = view.findViewById(R.id.arrow);
        slideToCancel = view.findViewById(R.id.slide_to_cancel);
        smallBlinkingMic = view.findViewById(R.id.glowing_mic);
        counterTime = view.findViewById(R.id.counter_tv);
        basketImg = view.findViewById(R.id.basket_img);

        hideViews();


        if (attrs != null && defStyleAttr == -1 && defStyleRes == -1) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RecordView,
                    defStyleAttr, defStyleRes);


            int slideArrowResource = typedArray.getResourceId(R.styleable.RecordView_slide_to_cancel_arrow, -1);
            String slideToCancelText = typedArray.getString(R.styleable.RecordView_slide_to_cancel_text);
            int slideMarginRight = (int) typedArray.getDimension(R.styleable.RecordView_slide_to_cancel_margin_right, 30);


            if (slideArrowResource != -1) {
                Drawable slideArrow = AppCompatResources.getDrawable(getContext(), slideArrowResource);
                arrow.setImageDrawable(slideArrow);
            }

            if (slideToCancelText != null)
                slideToCancel.setText(slideToCancelText);

            setMarginRight(slideMarginRight, true);

            typedArray.recycle();
        }

        animatedVectorDrawable = AnimatedVectorDrawableCompat.create(context, R.drawable.basket_animated);
        mAudioRecording = new AudioRecording();
    }


    private void animateBasket() {
        basketImg.setVisibility(VISIBLE);
        final TranslateAnimation translateAnimation1 = new TranslateAnimation(0, 0, basketInitialY, basketInitialY - 90);
        translateAnimation1.setDuration(250);
        basketImg.startAnimation(translateAnimation1);


        final TranslateAnimation translateAnimation2 = new TranslateAnimation(0, 0, basketInitialY - 130, basketInitialY);
        translateAnimation2.setDuration(350);

        basketImg.setImageDrawable(animatedVectorDrawable);


        translateAnimation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animatedVectorDrawable.start();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        basketImg.startAnimation(translateAnimation2);
                        clearAlphaAnimation();
                        basketImg.setVisibility(INVISIBLE);
                    }
                }, 350);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        translateAnimation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                basketImg.setVisibility(INVISIBLE);

                if (onBasketAnimationEndListener != null)
                    onBasketAnimationEndListener.onAnimationEnd();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    private void hideViews() {
        slideToCancelLayout.setVisibility(GONE);
        smallBlinkingMic.setVisibility(GONE);
        counterTime.setVisibility(GONE);
    }

    private void showViews() {
        slideToCancelLayout.setVisibility(VISIBLE);
        smallBlinkingMic.setVisibility(VISIBLE);
        counterTime.setVisibility(VISIBLE);
    }


    private void moveImageToBack(final RecordButton recordBtn) {

        final ValueAnimator positionAnimator =
                ValueAnimator.ofFloat(recordBtn.getX(), initialX);

        positionAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        positionAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float x = (Float) animation.getAnimatedValue();
                recordBtn.setX(x);
            }
        });

        recordBtn.stopScale();
        positionAnimator.setDuration(0);
        positionAnimator.start();


        // if the move event was not called ,then the difX will still 0 and there is no need to move it back
        if (difX != 0) {
            float x = initialX - difX;
            slideToCancelLayout.animate()
                    .x(x)
                    .setDuration(0)
                    .start();
        }


    }

    private void animateSmallMicAlpha() {


        alphaAnimation1 = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation1.setDuration(500);


        alphaAnimation1.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation arg0) {
                smallBlinkingMic.startAnimation(alphaAnimation2);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationStart(Animation arg0) {
            }

        });

        alphaAnimation2 = new AlphaAnimation(1.0f, 0.0f);

        alphaAnimation2.setDuration(500);


        alphaAnimation2.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation arg0) {
                // start animation1 when animation2 ends (repeat)
                smallBlinkingMic.startAnimation(alphaAnimation1);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationStart(Animation arg0) {
            }

        });

        smallBlinkingMic.startAnimation(alphaAnimation1);


    }

    private void clearAlphaAnimation() {
        if (alphaAnimation1 == null || alphaAnimation2 == null) return;
        alphaAnimation1.cancel();
        alphaAnimation1.reset();
        alphaAnimation1.setAnimationListener(null);
        alphaAnimation2.cancel();
        alphaAnimation2.reset();
        alphaAnimation2.setAnimationListener(null);
        smallBlinkingMic.clearAnimation();
        smallBlinkingMic.setVisibility(View.GONE);
        setVisibility(View.GONE);
    }


    private boolean isLessThanOneSecond(long time) {
        return time <= 1000;
    }


    public void playSound(final int soundRes, final PlayStartSoundComplete playStartSoundComplete) {

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            if (playStartSoundComplete != null) playStartSoundComplete.onComplete();
            return;
        }

        if (isSoundEnabled) {
            if (soundRes == 0)
                return;

            try {
                player = new MediaPlayer();
                AssetFileDescriptor afd = context.getResources().openRawResourceFd(soundRes);
                if (afd == null) return;
                player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                afd.close();
                player.prepare();
                player.start();
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                        if (playStartSoundComplete != null) playStartSoundComplete.onComplete();
                    }
                });
                player.setLooping(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    protected void onActionDown(RecordButton recordBtn, MotionEvent motionEvent) {

        counterTime.setBase(SystemClock.elapsedRealtime());
        startRecord();

        playSound(RECORD_START, new PlayStartSoundComplete() {
            @Override
            public void onComplete() {
                startTime = System.currentTimeMillis();
                mAudioRecording.startRecording();
            }
        });

        recordBtn.startScale();
        initialX = recordBtn.getX();
        basketInitialY = basketImg.getY() + 90;
        showViews();
        animateSmallMicAlpha();

        isSwiped = false;

    }

    protected void onActionMove(RecordButton recordBtn, MotionEvent motionEvent) {


        if (!isSwiped) {


            //Swipe To Cancel
            if (slideToCancelLayout.getX() != 0 && slideToCancelLayout.getX() <= counterTime.getX() + cancelBounds) {
                hideViews();
                moveImageToBack(recordBtn);
                counterTime.stop();
                animateBasket();
                stopRecord(true);

                isSwiped = true;


            } else {


                //if statement is to Prevent Swiping out of bounds
                if (motionEvent.getRawX() < initialX) {
                    recordBtn.animate()
                            .x(motionEvent.getRawX())
                            .setDuration(0)
                            .start();


                    if (difX == 0)
                        difX = (initialX - slideToCancelLayout.getX());


                    slideToCancelLayout.animate()
                            .x(motionEvent.getRawX() - difX)
                            .setDuration(0)
                            .start();


                }


            }

        }
    }

    protected void onActionUp(RecordButton recordBtn) {

        elapsedTime = System.currentTimeMillis() - startTime;

        if (isLessThanOneSecond(elapsedTime) && !isSwiped) {
            stopRecord(true);
            playSound(RECORD_ERROR, null);
        } else {
            if (recordListener != null && !isSwiped) {
                stopRecord(false);
                //recordListener.onFinish(elapsedTime);
            }

            if (!isSwiped)
                playSound(RECORD_FINISHED, null);

        }


        hideViews();


        if (!isSwiped)
            clearAlphaAnimation();


        moveImageToBack(recordBtn);
        counterTime.stop();


    }

    private int dp(float value) {

        if (value == 0) {
            return 0;
        }
        float density = context.getResources().getDisplayMetrics().density;

        return (int) Math.ceil(density * value);
    }


    private void setMarginRight(int marginRight, boolean convertToDp) {
        LayoutParams layoutParams = (LayoutParams) slideToCancelLayout.getLayoutParams();
        if (convertToDp) {
            layoutParams.rightMargin = dp(marginRight);
        } else
            layoutParams.rightMargin = marginRight;

        slideToCancelLayout.setLayoutParams(layoutParams);
    }


    public void setOnRecordListener(OnAudioRecordListener recrodListener) {
        this.recordListener = recrodListener;
    }

    public void setOnBasketAnimationEndListener(OnBasketAnimationEnd onBasketAnimationEndListener) {
        this.onBasketAnimationEndListener = onBasketAnimationEndListener;
    }

    public void setSoundEnabled(boolean isEnabled) {
        isSoundEnabled = isEnabled;
    }


    public void setSlideToCancelTextColor(int color) {
        slideToCancel.setTextColor(color);
    }


    public void setSmallMicIcon(int icon) {
        smallBlinkingMic.setImageResource(icon);
    }

    public void setSlideMarginRight(int marginRight) {
        setMarginRight(marginRight, false);
    }


    public void setCustomSounds(int startSound, int finishedSound, int errorSound) {
        //0 means do not play sound
        RECORD_START = startSound;
        RECORD_FINISHED = finishedSound;
        RECORD_ERROR = errorSound;
    }


    public synchronized void startRecord() {
        if (recordListener != null) {
            OnAudioRecordListener onRecordListener = new OnAudioRecordListener() {

                @Override
                public void onRecordFinished(RecordingItem recordingItem) {
//                    recordTimerStop();
                    isRecordingStarted = false ;
                    recordListener.onRecordFinished(recordingItem);
                }

                @Override
                public void onError(int e) {
                    recordListener.onError(e);
                }

                @Override
                public void onRecordingStarted() {
                    isRecordingStarted = true;
                    recordListener.onRecordingStarted();
                }
            };

            String filePath = audioDirectory  + "/" + System.currentTimeMillis() + ".aac";

            mAudioRecording.setOnAudioRecordListener(onRecordListener);
            mAudioRecording.setFile(filePath);
            Log.d("Record ", "start");
        }
    }

    public boolean isRecordingStarted() {
        return isRecordingStarted;
    }

    public synchronized void stopRecord(final Boolean cancel) {
        if (recordListener != null && mAudioRecording != null) {
            mAudioRecording.stopRecording(cancel);
            isRecordingStarted = false;

            Log.d("Record ", "Cancel "+cancel);

        }
    }

    public void stopRecordingnResetViews(RecordButton button){
        button.setBackgroundResource(0);
        stopRecord(true);
        button.vibrate();
        playSound(RECORD_FINISHED, null);
        hideViews();
        clearAlphaAnimation();
        moveImageToBack(button);
        counterTime.setBase(SystemClock.elapsedRealtime());
        counterTime.stop();

    }

    public synchronized void recordTimerStart(){
        counterTime.start();
    }

    public synchronized void recordTimerStop(){
        counterTime.setBase(SystemClock.elapsedRealtime());
        counterTime.stop();
    }

    public void setAudioDirectory(File audioDirectory) {
        this.audioDirectory = audioDirectory;
    }
}

interface PlayStartSoundComplete {

    void onComplete();
}
