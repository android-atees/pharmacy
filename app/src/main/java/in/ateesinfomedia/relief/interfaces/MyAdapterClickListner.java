package in.ateesinfomedia.relief.interfaces;

import android.view.View;

/**
 * Created by pc-36 on 20-Dec-17.
 */

public interface MyAdapterClickListner {

    public void clicked(View view, int position);

    public void Applyclicked(View view, int jobid, int position, String type);
}