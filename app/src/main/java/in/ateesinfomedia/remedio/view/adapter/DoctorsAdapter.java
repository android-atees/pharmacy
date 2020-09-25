package in.ateesinfomedia.remedio.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import in.ateesinfomedia.remedio.interfaces.AdapterClickListner;
import in.ateesinfomedia.remedio.interfaces.DoctorsClickListner;
import in.ateesinfomedia.remedio.models.DoctorsModel;

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.MyViewHolder> {

    private final Context mContext;
    private final List<DoctorsModel> mDoctorsList;
    private final DoctorsClickListner mDoctorsClickListner;
    private DoctorsModel doctorsModel;

    public DoctorsAdapter(Context context, List<DoctorsModel> doctorsList, DoctorsClickListner clickListner) {
        this.mContext = context;
        this.mDoctorsList = doctorsList;
        this.mDoctorsClickListner = clickListner;
    }

    @NonNull
    @Override
    public DoctorsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_docters, parent, false);

        return new DoctorsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorsAdapter.MyViewHolder holder, int position) {

        doctorsModel = mDoctorsList.get(position);

        holder.mTvDoctor_depart.setText(doctorsModel.getDept_name());
        holder.mTvDoctor_quali.setText(doctorsModel.getQualification());
        holder.mTvDoctorName.setText(doctorsModel.getName());
//        if (doctorsModel.getImage().equals("")){
            holder.mImDoctor_image.setImageResource(R.drawable.ic_splash);
//        } else {
//            Glide.with(mContext).load(Apis.API_POST_DOCTORS_LOGO_PATH + doctorsModel.getImage()).into(holder.mImDoctor_image);
//        }
    }

    @Override
    public int getItemCount() {
        return mDoctorsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTvDoctorName,mTvDoctor_quali,mTvDoctor_depart;
        private final Button mBtnAppoinment;
        private final ImageView mImDoctor_image;

        public MyViewHolder(View itemView) {
            super(itemView);

            mTvDoctorName = (TextView) itemView.findViewById(R.id.doc_name);
            mTvDoctor_quali = (TextView) itemView.findViewById(R.id.qualification);
            mTvDoctor_depart = (TextView) itemView.findViewById(R.id.department);
            mImDoctor_image = (ImageView) itemView.findViewById(R.id.doc_img);
            mBtnAppoinment = (Button) itemView.findViewById(R.id.btn_appoinment);

            mBtnAppoinment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DoctorsModel doctorsModel = mDoctorsList.get(getAdapterPosition());
                    mDoctorsClickListner.itemClicked(getAdapterPosition(),mImDoctor_image,mTvDoctorName,doctorsModel);
                }
            });
        }
    }
}