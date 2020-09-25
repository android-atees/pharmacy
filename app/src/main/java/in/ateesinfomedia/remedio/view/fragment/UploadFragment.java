package in.ateesinfomedia.remedio.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import in.ateesinfomedia.remedio.R;
import in.ateesinfomedia.remedio.view.activity.UploadPresActivity;

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
