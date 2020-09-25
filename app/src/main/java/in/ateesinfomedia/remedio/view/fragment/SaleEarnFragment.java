package in.ateesinfomedia.remedio.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.ateesinfomedia.remedio.R;
import in.ateesinfomedia.remedio.configurations.Global;
import in.ateesinfomedia.remedio.models.EarningsModel;
import in.ateesinfomedia.remedio.view.adapter.EarningsAdapter;

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