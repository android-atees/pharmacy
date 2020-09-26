package in.ateesinfomedia.remedio.view.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import in.ateesinfomedia.remedio.R;
import in.ateesinfomedia.remedio.configurations.Apis;
import in.ateesinfomedia.remedio.interfaces.AdapterClickListner;
import in.ateesinfomedia.remedio.models.SubCategoryModel;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {

    private final Context mContext;
    private final List<SubCategoryModel> mStringList;
    private final AdapterClickListner mAdapterClickListner;

    public SubCategoryAdapter(Context context, List<SubCategoryModel> stringList, AdapterClickListner clickListner) {
        this.mContext = context;
        this.mStringList = stringList;
        this.mAdapterClickListner = clickListner;
    }

    @NonNull
    @Override
    public SubCategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_sub_category, parent, false);

        return new SubCategoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryAdapter.MyViewHolder holder, int position) {

        holder.mCateName.setText(mStringList.get(position).getName());
        Glide.with(mContext).load(Apis.SUB_CATEGORY_IMAGE_BASE_URL+ mStringList.get(position).getImage()).into(holder.mCateImage);

    }

    @Override
    public int getItemCount() {
        return mStringList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mCateName;
        ImageView mCateImage;

        public MyViewHolder(View itemView) {
            super(itemView);

            mCateImage = (ImageView) itemView.findViewById(R.id.cate_image);
            mCateName = (TextView) itemView.findViewById(R.id.cate_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SubCategoryModel subCategoryModel = mStringList.get(getAdapterPosition());
                    mAdapterClickListner.itemClicked(getAdapterPosition(),subCategoryModel);
                }
            });
        }
    }
}
