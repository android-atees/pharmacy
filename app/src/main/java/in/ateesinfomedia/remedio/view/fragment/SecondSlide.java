package in.ateesinfomedia.remedio.view.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.ateesinfomedia.remedio.R;

public class SecondSlide extends Fragment {

    private View mView;
    private TextView mTxtHeading;
    private TextView mTxtContent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.second_slider, container, false);

        mTxtContent = (TextView) mView.findViewById(R.id.txt1);

//        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "Futura_Medium.otf");
//        mTxtContent.setTypeface(font);

        return mView;
    }
}
