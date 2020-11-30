package in.ateesinfomedia.relief.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.asksira.loopingviewpager.LoopingPagerAdapter;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.configurations.Apis;

public class SlidingImagesAdapter extends LoopingPagerAdapter<String> {

    private ImageView mImage;

    public SlidingImagesAdapter(Context context, ArrayList<String> itemList, boolean isInfinite) {
        super(context, itemList, isInfinite);
    }

    @Override
    protected View inflateView(int viewType, int listPosition) {
        return LayoutInflater.from(context).inflate(R.layout.item_pager, null);
    }

    @Override
    protected void bindView(View convertView, int listPosition, int viewType) {

        mImage = (ImageView) convertView.findViewById(R.id.image);

        Glide.with(context).load(Apis.SLIDER_BASE_URL + itemList.get(listPosition)).into(mImage);

    }
}
