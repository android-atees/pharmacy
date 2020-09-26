package in.ateesinfomedia.remedio.view.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import in.ateesinfomedia.remedio.R;
import in.ateesinfomedia.remedio.configurations.Apis;
import in.ateesinfomedia.remedio.interfaces.DoctorsClickListner;
import in.ateesinfomedia.remedio.models.LaboratoryModel;

public class LaborataryAdapter extends RecyclerView.Adapter<LaborataryAdapter.MyViewHolder> {

    private final Context mContext;
    private final DoctorsClickListner mAdapterClickListner;
    private final List<LaboratoryModel> mLaboratoryModel;

    public LaborataryAdapter(Context context, List<LaboratoryModel> laboratoryModels, DoctorsClickListner clickListner) {
        this.mContext = context;
        this.mLaboratoryModel = laboratoryModels;
        this.mAdapterClickListner = clickListner;
    }

    @NonNull
    @Override
    public LaborataryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_laboratory_list, parent, false);

        return new LaborataryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LaborataryAdapter.MyViewHolder holder, int position) {

        holder.mlabName.setText(mLaboratoryModel.get(position).getName());
        holder.mLabMainName.setText(mLaboratoryModel.get(position).getName());
        holder.mAddress.setText(mLaboratoryModel.get(position).getAddress());
        holder.mDepartment.setText(mLaboratoryModel.get(position).getConstituency_name());

        if (mLaboratoryModel.get(position).getImage().equals("")){
            holder.mlabImage.setImageResource(R.drawable.ic_splash);
        } else {
            Glide.with(mContext).load(Apis.API_POST_DOCTORS_LOGO_PATH + mLaboratoryModel.get(position).getImage()).into(holder.mlabImage);
        }

    }

    @Override
    public int getItemCount() {
        return mLaboratoryModel.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mlabName,mLabMainName,mAddress,mDepartment;
        ImageView mlabImage;
        Button mViewMore;

        public MyViewHolder(View itemView) {
            super(itemView);

            mlabName = itemView.findViewById(R.id.lab_name);
            mLabMainName = itemView.findViewById(R.id.lab_main_name);
            mAddress = itemView.findViewById(R.id.address);
            mDepartment = itemView.findViewById(R.id.department);
            mlabImage = itemView.findViewById(R.id.lab_img);
            mViewMore = itemView.findViewById(R.id.btn_viewMore);

            mViewMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LaboratoryModel laboratoryModel = mLaboratoryModel.get(getAdapterPosition());
                    mAdapterClickListner.itemClicked(getAdapterPosition(),mlabImage,mlabName,laboratoryModel);
                }
            });

        }
    }
}
