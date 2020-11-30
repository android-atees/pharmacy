package in.ateesinfomedia.relief.components;

/*
 *Created by Adithya T Raj on 02-11-2020
 */

import android.app.Activity;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.models.products.MediaGalleryEntries;

public class AppHelpers {

    public static void addMarkers(int currentPage, ArrayList<MediaGalleryEntries> imglist, LinearLayout mMarkersLayout, Activity activity) {

        if (activity != null) {
            TextView[] markers = new TextView[imglist.size()];

            mMarkersLayout.removeAllViews();

            for (int i = 0; i < markers.length; i++) {
                markers[i] = new TextView(activity);
                markers[i].setText(Html.fromHtml("&#8226;"));
                markers[i].setTextSize(35);
                markers[i].setTextColor(activity.getResources().getColor(R.color.overlay_white));
                mMarkersLayout.addView(markers[i]);
            }
            if (markers.length > 0)
                markers[currentPage].setTextColor(activity.getResources().getColor(R.color.colorPrimary));
        }
    }

    public static String convertDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(dateString);
            SimpleDateFormat reqFormat = new SimpleDateFormat("MMM dd, yyyy, hh:mm aa");
            String dateTime = reqFormat.format(date);
            return dateTime;
        } catch (Exception e) {
            e.printStackTrace();
            return dateString;
        }
    }

}
