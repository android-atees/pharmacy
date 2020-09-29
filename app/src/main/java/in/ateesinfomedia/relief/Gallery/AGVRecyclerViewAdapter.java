package in.ateesinfomedia.relief.Gallery;

import androidx.recyclerview.widget.RecyclerView;

import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;

public abstract class AGVRecyclerViewAdapter<VH extends RecyclerView.ViewHolder>
    extends RecyclerView.Adapter<VH> {
  public abstract AsymmetricItem getItem(int position);
}