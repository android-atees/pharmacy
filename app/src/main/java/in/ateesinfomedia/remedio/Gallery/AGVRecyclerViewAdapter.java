package in.ateesinfomedia.remedio.Gallery;

import android.support.v7.widget.RecyclerView;

import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;

public abstract class AGVRecyclerViewAdapter<VH extends RecyclerView.ViewHolder>
    extends RecyclerView.Adapter<VH> {
  public abstract AsymmetricItem getItem(int position);
}
