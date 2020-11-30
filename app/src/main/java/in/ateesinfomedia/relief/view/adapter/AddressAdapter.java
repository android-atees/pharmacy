package in.ateesinfomedia.relief.view.adapter;

/*
 *Created by Adithya T Raj on 07-11-2020
 */

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.interfaces.AddressClickListner;
import in.ateesinfomedia.relief.models.login.CustomerAddress;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {

    private final AddressClickListner mAddressClickListner;
    private final List<CustomerAddress> mAddressList;
    private final Context mContext;
    private int selectedPosition = -1;

    public AddressAdapter(Context mContext, List<CustomerAddress> mAddressList, AddressClickListner mAddressClickListner) {
        this.mAddressClickListner = mAddressClickListner;
        this.mAddressList = mAddressList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_address, parent, false);

        return new AddressAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(mAddressList.get(position), mAddressClickListner);
        //Log.d("AddressAdapter", mAddressList.get(position).getLastname());
        if (selectedPosition == position) {
            holder.mainLayout.setBackgroundColor(Color.parseColor("#d3d3d3"));
        } else {
            holder.mainLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    @Override
    public int getItemCount() {
        return mAddressList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mCustomerFullName, mStreet, mCity, mPhone, mEdit, mDelete;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mCustomerFullName = (TextView) itemView.findViewById(R.id.address_user_name_txt);
            mStreet = (TextView) itemView.findViewById(R.id.address_user_street_txt);
            mCity = (TextView) itemView.findViewById(R.id.address_user_city_txt);
            mPhone = (TextView) itemView.findViewById(R.id.address_user_phone_txt);
            mEdit = (TextView) itemView.findViewById(R.id.address_user_edit_txt);
            mDelete = (TextView) itemView.findViewById(R.id.address_user_delete_txt);
            mainLayout = (LinearLayout) itemView.findViewById(R.id.address_mainLayout);
        }

        void bind(CustomerAddress item, AddressClickListner listener) {
            String fullName = item.getFirstname() + " " + item.getLastname();
            mCustomerFullName.setText(fullName);
            StringBuilder street = new StringBuilder();
            for (int i = 0; i < item.getStreet().size(); i++) {
                street.append(item.getStreet().get(i)).append(", ");
            }
            mStreet.setText(street);
            StringBuilder city = new StringBuilder();
            city.append(item.getCity()).append(", ").append(item.getPostcode());
            mCity.setText(city);
            mPhone.setText(item.getTelephone());

            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedPosition = getAdapterPosition();
                    listener.onAddressItemClicked(getAdapterPosition(), item);
                    notifyDataSetChanged();
                }
            });

            mEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onAddressEditClicked(getAdapterPosition(), item);
                }
            });

            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onAddressDeleteClicked(getAdapterPosition(), item);
                }
            });
        }
    }

}
