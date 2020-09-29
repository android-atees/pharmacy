package in.ateesinfomedia.relief.view.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.configurations.Global;
import in.ateesinfomedia.relief.models.EarningsModel;
import in.ateesinfomedia.relief.view.adapter.EarningsAdapter;

public class SaleEarnFragment extends Fragment {


    private View mView;
    private RecyclerView mSaleRecycler;
    private TextView mTvNodata;
    private List<EarningsModel> earningsModelList = new ArrayList<>();
    private EarningsAdapter mAdapter;


    public static SaleEarnFragment getInstance() {
        SaleEarnFragment saleEarnFragment = new SaleEarnFragment();

        return saleEarnFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_sale_earn, container, false);

        mSaleRecycler = (RecyclerView) mView.findViewById(R.id.sale_recycler);
        mTvNodata = (TextView) mView.findViewById(R.id.nodata);



        earningsModelList.clear();
        earningsModelList = Global.SaleEarnList;

        if (earningsModelList.size()<1 || earningsModelList == null){
            mSaleRecycler.setVisibility(View.GONE);
            mTvNodata.setVisibility(View.VISIBLE);
        } else {
            mSaleRecycler.setVisibility(View.VISIBLE);
            mTvNodata.setVisibility(View.GONE);

            mSaleRecycler.setNestedScrollingEnabled(true);
            mAdapter = new EarningsAdapter(getActivity(),earningsModelList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//        ShimmerRecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            mSaleRecycler.setLayoutManager(mLayoutManager/*mLayoutManager*/);
            mSaleRecycler.setItemAnimator(new DefaultItemAnimator());
            mSaleRecycler.setAdapter(mAdapter);
        }
        return mView;
    }
}