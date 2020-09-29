package in.ateesinfomedia.relief.view.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.ateesinfomedia.relief.R;

public class UploadFragment extends Fragment {

    private View mView;

    public static UploadFragment getInstance() {
        UploadFragment uploadFragment = new UploadFragment();

        return uploadFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_upload, container, false);



        return mView;
    }
}
