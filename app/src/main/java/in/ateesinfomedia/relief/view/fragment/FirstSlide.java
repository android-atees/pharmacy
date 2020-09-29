package in.ateesinfomedia.relief.view.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.ateesinfomedia.relief.R;

public class FirstSlide extends Fragment {

    private View mView;
    private TextView mTxtHeading,mTxtContent;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.first_slider, container, false);

        //mTxtHeading = (TextView) mView.findViewById(R.id.txt1);

//        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "Futura_Medium.otf");
//        mTxtContent.setTypeface(font);
        return mView;
    }
}
