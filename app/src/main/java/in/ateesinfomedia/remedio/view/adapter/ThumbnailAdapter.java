package in.ateesinfomedia.remedio.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import in.ateesinfomedia.remedio.R;
import in.ateesinfomedia.remedio.configurations.Apis;
import in.ateesinfomedia.remedio.interfaces.AdapterClickListner;

public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.MyViewHolder> {

    private final Context mContext;
    private final String[] mString;
    private final AdapterClickListner mAdapterClickListner;

    public ThumbnailAdapter(Context context, String[] strings, AdapterClickListner clickListner) {
        this.mContext = context;
        this.mString = strings;
        this.mAdapterClickListner = clickListner;
    }

    @NonNull
    @Override
    public ThumbnailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_thumbnail, parent, false);

        return new ThumbnailAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ThumbnailAdapter.MyViewHolder holder, int position) {

        Glide.with(mContext).load(Apis.PRODUCT_IMAGE_BASE_URL+ mString[position]).into(holder.thumb);

    }

    @Override
    public int getItemCount() {
        return mString.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView thumb;
        public MyViewHolder(View itemView) {
            super(itemView);

            thumb = (ImageView) itemView.findViewById(R.id.thumb);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapterClickListner.itemClicked(getAdapterPosition(),mString[getAdapterPosition()]);
                }
            });
        }
    }
}
