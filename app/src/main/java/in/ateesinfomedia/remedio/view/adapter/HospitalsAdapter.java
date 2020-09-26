package in.ateesinfomedia.remedio.view.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import in.ateesinfomedia.remedio.R;
import in.ateesinfomedia.remedio.interfaces.AdapterClickListner;
import in.ateesinfomedia.remedio.models.DoctorsDetailModel;

public class HospitalsAdapter extends RecyclerView.Adapter<HospitalsAdapter.MyViewHolder> {

    private final Context mContext;
    private final List<DoctorsDetailModel> mDoctorsDetailList;
    private final AdapterClickListner mAdapterClickListner;

    public HospitalsAdapter(Context context, List<DoctorsDetailModel> doctorsDetailList, AdapterClickListner clickListner) {

        this.mContext = context;
        this.mDoctorsDetailList = doctorsDetailList;
        this.mAdapterClickListner = clickListner;
    }

    @NonNull
    @Override
    public HospitalsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_hospitals, parent, false);

        return new HospitalsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalsAdapter.MyViewHolder holder, int position) {

        DoctorsDetailModel doctorsDetailModel = mDoctorsDetailList.get(position);

        holder.mHospitalName.setText(doctorsDetailModel.getHospital_name());
        holder.mWorkingDays.setText(doctorsDetailModel.getConsulting_days());
        holder.mWorkingHours.setText(doctorsDetailModel.getConsulting_time());
        holder.mHospitalAddress.setText(doctorsDetailModel.getHospital_address());
    }

    @Override
    public int getItemCount() {
        return mDoctorsDetailList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mHospitalName,mWorkingDays,mWorkingHours,mHospitalAddress;
        Button mBtnBookNow;

        public MyViewHolder(View itemView) {
            super(itemView);

            mHospitalName = (TextView) itemView.findViewById(R.id.hospital_name);
            mWorkingDays = (TextView) itemView.findViewById(R.id.working_days);
            mWorkingHours = (TextView) itemView.findViewById(R.id.working_hours);
            mHospitalAddress = (TextView) itemView.findViewById(R.id.hospital_address);
            mBtnBookNow = (Button) itemView.findViewById(R.id.btn_book);

            mBtnBookNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DoctorsDetailModel doctorsDetailModel = mDoctorsDetailList.get(getAdapterPosition());
                    mAdapterClickListner.itemClicked(getAdapterPosition(),doctorsDetailModel);
                }
            });
        }
    }
}
