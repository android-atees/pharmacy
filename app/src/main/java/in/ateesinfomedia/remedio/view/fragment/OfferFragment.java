package in.ateesinfomedia.remedio.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.ateesinfomedia.remedio.R;
import in.ateesinfomedia.remedio.components.LoadingDialog;
import in.ateesinfomedia.remedio.configurations.Apis;
import in.ateesinfomedia.remedio.interfaces.AdapterClickListner;
import in.ateesinfomedia.remedio.interfaces.NetworkCallback;
import in.ateesinfomedia.remedio.interfaces.OfferCategoryClickListner;
import in.ateesinfomedia.remedio.managers.NetworkManager;
import in.ateesinfomedia.remedio.models.CategoryModel;
import in.ateesinfomedia.remedio.models.ProductsModel;
import in.ateesinfomedia.remedio.view.activity.ProductDetailsActivity;
import in.ateesinfomedia.remedio.view.adapter.OfferAdapter;
import in.ateesinfomedia.remedio.view.adapter.OfferProductsAdapter;

public class OfferFragment extends Fragment implements NetworkCallback, OfferCategoryClickListner, AdapterClickListner {

    private View mView;
    private int REQUEST_OFFERS_ID = 9999;
    private List<CategoryModel> mCategoriesList = new ArrayList<>();
    private List<ProductsModel> mOfferList = new ArrayList<>();
    private List<ProductsModel> mFilteredOfferList = new ArrayList<>();
    private RecyclerView mCateRecycler,mmProductRecycler;
    private LinearLayoutManager mLayoutManager3;

    public static OfferFragment getInstance() {
        OfferFragment offerFragment = new OfferFragment();

        return offerFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_offer, container, false);

        mCateRecycler = (RecyclerView) mView.findViewById(R.id.offerCateRecycler);
        mmProductRecycler = (RecyclerView) mView.findViewById(R.id.offerproductsrecycler);

        mCateRecycler.setNestedScrollingEnabled(true);
        mLayoutManager3 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        mCateRecycler.setLayoutManager(/*linearLayoutManager*/mLayoutManager3);
        mCateRecycler.setItemAnimator(new DefaultItemAnimator());
        OfferAdapter offerAdapter= new OfferAdapter(getActivity(),mCategoriesList,this);
        mCateRecycler.setAdapter(offerAdapter);

        mmProductRecycler.setNestedScrollingEnabled(true);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mmProductRecycler.setLayoutManager(/*linearLayoutManager*/mLayoutManager2);
        mmProductRecycler.setItemAnimator(new DefaultItemAnimator());
        OfferProductsAdapter offerProductsAdapter= new OfferProductsAdapter(getActivity(),mOfferList,this);
        mmProductRecycler.setAdapter(offerProductsAdapter);

        getOffers();

        return mView;
    }

    private void getOffers() {

        new NetworkManager(getActivity()).doPost(null, Apis.API_POST_OFFERS,"REQUEST_OFFERS",REQUEST_OFFERS_ID,this);
        LoadingDialog.showLoadingDialog(getActivity(),"Loading....");
    }

    @Override
    public void onResponse(int status, String response, int requestId) {
        if (status == NetworkManager.SUCCESS){
            if (requestId == REQUEST_OFFERS_ID){
                processOffers(response);
            }
        } else {
            LoadingDialog.cancelLoading();
        }
    }

    private void processOffers(String response) {
        LoadingDialog.cancelLoading();
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                Toast.makeText(getActivity(), "No offers available..", Toast.LENGTH_SHORT).show();
            } else {
                JSONArray jsonArray = jsonObject.optJSONArray("category");

                for (int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                    CategoryModel categoryModel = new CategoryModel();

                    categoryModel.setId(jsonObject1.optString("category_id"));
                    categoryModel.setName(jsonObject1.optString("sub_category_1_name"));
                    categoryModel.setImage(jsonObject1.optString("image"));

                    mCategoriesList.add(categoryModel);
                }

                JSONArray jsonArray1 = jsonObject.optJSONArray("data");

                mOfferList.clear();
                for (int n = 0;n<jsonArray1.length();n++){

                    JSONObject jsonObject1 = jsonArray1.optJSONObject(n);

                    ProductsModel productsModel = new ProductsModel();
                    productsModel.setId(jsonObject1.optString("id"));
//                    productsModel.set(jsonObject1.optString("category_id"));
//                    productsModel.setC(jsonObject1.optString("category_name"));
//                    productsModel.setId(jsonObject1.optString("sub_category_id"));
                    productsModel.setSub_category_name(jsonObject1.optString("sub_category_name"));
//                    productsModel.setBrand_id(jsonObject1.optString("brand_name"));
                    productsModel.setProduct_name(jsonObject1.optString("product_name"));
                    productsModel.setProduct_description(jsonObject1.optString("product_description"));
                    productsModel.setProduct_highlights(jsonObject1.optString("product_highlights"));
                    productsModel.setProduct_total(jsonObject1.optString("product_price"));
                    productsModel.setProduct_total_offer(jsonObject1.optString("product_offer_price"));
                    productsModel.setProduct_image1(jsonObject1.optString("product_image1"));
                    productsModel.setProduct_image2(jsonObject1.optString("product_image2"));
                    productsModel.setProduct_image3(jsonObject1.optString("product_image3"));
                    productsModel.setProduct_image4(jsonObject1.optString("product_image4"));

                    mOfferList.add(productsModel);

                }

                OfferAdapter offerAdapter= new OfferAdapter(getActivity(),mCategoriesList,this);
                mCateRecycler.setAdapter(offerAdapter);

//                mCateRecycler.findViewHolderForAdapterPosition(1).itemView.findViewById(R.id.main).performClick();
//
                OfferProductsAdapter offerProductsAdapter = new OfferProductsAdapter(getActivity(),mOfferList,this);
                mmProductRecycler.setAdapter(offerProductsAdapter);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onJsonResponse(int status, String response, int requestId) {

    }

    @Override
    public void titleSelected(String name, int position) {

        mFilteredOfferList.clear();
        for (int i = 0;i<mOfferList.size();i++){
            String subname = mOfferList.get(i).getSub_category_name();

            if (subname.equals(name)){
                ProductsModel productsModel = new ProductsModel();
                productsModel = mOfferList.get(i);

                mFilteredOfferList.add(productsModel);
            }
        }

        if (mFilteredOfferList.size() == 0 || mFilteredOfferList == null){
            Toast.makeText(getActivity(), "There is no items.", Toast.LENGTH_SHORT).show();
        } else {
            OfferProductsAdapter offerProductsAdapter = new OfferProductsAdapter(getActivity(), mFilteredOfferList, this);
            mmProductRecycler.setAdapter(offerProductsAdapter);
        }
    }

    @Override
    public void itemClicked(int position, Object object) {

        ProductsModel productsModel = (ProductsModel) object;

        Intent intent = new Intent(getActivity(),ProductDetailsActivity.class);
        intent.putExtra("details",productsModel);
        startActivity(intent);
    }
}
