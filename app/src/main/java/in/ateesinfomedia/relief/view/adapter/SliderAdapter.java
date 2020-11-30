package in.ateesinfomedia.relief.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.configurations.Apis;
import in.ateesinfomedia.relief.models.products.MediaGalleryEntries;

public class SliderAdapter extends PagerAdapter {

    ArrayList<MediaGalleryEntries> dataList;
    Activity activity;
    int layout;
    String from;

    public SliderAdapter(ArrayList<MediaGalleryEntries> dataList, Activity activity, int layout, String from) {
        this.dataList = dataList;
        this.activity = activity;
        this.layout = layout;
        this.from = from;
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = LayoutInflater.from(activity).inflate(layout, view, false);

        assert imageLayout != null;
        AppCompatImageView imgslider = imageLayout.findViewById(R.id.imgslider);
        CardView lytmain = imageLayout.findViewById(R.id.lytmain);

        final MediaGalleryEntries singleItem = dataList.get(position);

        if (singleItem != null && singleItem.getFile() != null) {
            Glide.with(imgslider)
                    .load(Apis.NEW_PRODUCT_IMAGE_BASE_URL + singleItem.getFile())
                    .into(imgslider);
        }

        view.addView(imageLayout, 0);

        /*lytmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from.equalsIgnoreCase("detail")) {
                    Intent intent = new Intent(activity, FullScreenViewActivity.class);
                    intent.putExtra("pos", position);
                    activity.startActivity(intent);
                } else {

                    if (singleItem.getType().equals("category")) {
                        Intent intent = new Intent(activity, SubCategoryActivity.class);
                        intent.putExtra("id", singleItem.getType_id());
                        intent.putExtra("name", singleItem.getName());
                        activity.startActivity(intent);
                    } else if (singleItem.getType().equals("product")) {
                        Intent intent = new Intent(activity, ProductDetailActivity.class);
                        intent.putExtra("id", singleItem.getType_id());
                        intent.putExtra("vpos", 0);
                        intent.putExtra("from", "share");
                        activity.startActivity(intent);
                    }

                }
            }
        });*/

        return imageLayout;
    }


    @Override
    public int getCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
