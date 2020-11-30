package in.ateesinfomedia.relief.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.asksira.loopingviewpager.LoopingViewPager;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rd.PageIndicatorView;
import com.rd.animation.type.AnimationType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.ateesinfomedia.relief.R;
import in.ateesinfomedia.relief.components.LoadingDialog;
import in.ateesinfomedia.relief.interfaces.NetworkCallback;
import in.ateesinfomedia.relief.configurations.Apis;
import in.ateesinfomedia.relief.managers.MyPreferenceManager;
import in.ateesinfomedia.relief.managers.NetworkManager;
import in.ateesinfomedia.relief.models.cart.CartCountResponse;
import in.ateesinfomedia.relief.models.login.CustomerData;
import in.ateesinfomedia.relief.models.login.LoginModel;
import in.ateesinfomedia.relief.models.products.ProductModel;
import in.ateesinfomedia.relief.models.CategoryModel;
import in.ateesinfomedia.relief.models.ConstituencyModel;
import in.ateesinfomedia.relief.models.DeliveryAddressModel;
import in.ateesinfomedia.relief.models.DepartmentModel;
import in.ateesinfomedia.relief.models.DoctorsModel;
import in.ateesinfomedia.relief.models.LaboratoryModel;
import in.ateesinfomedia.relief.models.ProductsModel;
import in.ateesinfomedia.relief.models.products.ProductResponse;
import in.ateesinfomedia.relief.models.slider.HomeBanner;
import in.ateesinfomedia.relief.models.state.AddAddressResponse;
import in.ateesinfomedia.relief.view.activity.ComingSoonActivity;
import in.ateesinfomedia.relief.view.activity.DoctorActivity;
import in.ateesinfomedia.relief.view.activity.CategoryActivity;
import in.ateesinfomedia.relief.view.activity.HospitalActivity;
import in.ateesinfomedia.relief.view.activity.LaborataryActivity;
import in.ateesinfomedia.relief.view.activity.MainActivity;
import in.ateesinfomedia.relief.view.activity.ProductsActivity;
import in.ateesinfomedia.relief.view.activity.SignUpActivity;
import in.ateesinfomedia.relief.view.activity.UploadPresActivity;
import in.ateesinfomedia.relief.view.adapter.SlidingImagesAdapter;

import static in.ateesinfomedia.relief.configurations.Global.CatId;
import static in.ateesinfomedia.relief.configurations.Global.CatProductCount;
import static in.ateesinfomedia.relief.configurations.Global.CatProductList;
import static in.ateesinfomedia.relief.configurations.Global.CategoryList;
import static in.ateesinfomedia.relief.configurations.Global.ConstituencyList;
import static in.ateesinfomedia.relief.configurations.Global.DefaultPageSize;
import static in.ateesinfomedia.relief.configurations.Global.DepartmentList;
import static in.ateesinfomedia.relief.configurations.Global.DoctorsList;
import static in.ateesinfomedia.relief.configurations.Global.HospitalList;
import static in.ateesinfomedia.relief.configurations.Global.ImagArrayList;
import static in.ateesinfomedia.relief.configurations.Global.IsAddressSelected;
import static in.ateesinfomedia.relief.configurations.Global.LaborataryList;
import static in.ateesinfomedia.relief.configurations.Global.ProductList;
import static in.ateesinfomedia.relief.configurations.Global.dialogWarning;

public class HomeFragment extends Fragment implements NetworkCallback {

    private View mView;
    private ImageView mBtnUploadPres;
    private ImageView mBtnWhatsApp;
    private LinearLayout topLayout;
    private LinearLayout middleLayout;
    private LinearLayout bottomLayout;
    //private CardView mCardSurgical,mCardgeneralMedicine,mCardFind,mCardLaboratary,mcardHospital;
    private int REQUEST_GET_DOCTORS_LIST_ID = 8900;
    private int TAG_GET_PROFILE_ID = 7865;
    private MyPreferenceManager manager;
    private List<DepartmentModel> departmentList = new ArrayList<>();
    private List<DoctorsModel> doctorsList = new ArrayList<>();
    private List<ConstituencyModel> constituencyList = new ArrayList<>();
//    private ArrayList<Integer> imagArrayList = new ArrayList<>();
    private ArrayList<String> imagArrayList = new ArrayList<>();
    private LoopingViewPager mLVPSlider;
    private PageIndicatorView mPIVindication;
    private NestedScrollView nestedScrollView;
    private int REQUEST_GET_CATEGORIES_LIST_ID = 7863;
    private List<CategoryModel> mCateList = new ArrayList<>();
    private List<CategoryModel> homeCategoryList = new ArrayList<>();
    private int REQUEST_GET_LABORATARIES_LIST_ID = 9876;
    private ArrayList<LaboratoryModel> mLaborataryList = new ArrayList<>();
    private int REQUEST_GET_HOSPITAL_LIST_ID = 9871;
    private List<LaboratoryModel> mHospitalList = new ArrayList<>();
    private int REQUEST_GET_SLIDER_IMAGES_LIST_ID = 8888;
    private MaterialCardView mCardSurgical,mCardMedicine,mCardNonMedicine,mCardJuniors,mCardOpticals,mCardBookDoctor, mBannerMain;
    private final int REQUEST_PRODUCTS = 8899;
    private static final String TAG = HomeFragment.class.getName();
    private List<ProductModel> mCatProductList = new ArrayList<>();
    private List<ProductsModel> mProductList = new ArrayList<>();
    private int REQUEST_CREATE_CART_ID = 9197;
    private String catToolbarName = "Test";
    private int REQUEST_CART_COUNT = 9117;

    Gson gson = new Gson();

    public static HomeFragment getInstance() {
        HomeFragment homeFragment = new HomeFragment();

        return homeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.fragment_home, container, false);


        manager = new MyPreferenceManager(getActivity());
        mBtnUploadPres = mView.findViewById(R.id.btnUploadPres);
        mBtnWhatsApp = mView.findViewById(R.id.home_call_whats_app_img);
        mLVPSlider = (LoopingViewPager) mView.findViewById(R.id.lvp_news_slider);
        mPIVindication = (PageIndicatorView) mView.findViewById(R.id.pageIndicatorView);
        nestedScrollView = (NestedScrollView) mView.findViewById(R.id.nestedScroll);
        mCardMedicine = (MaterialCardView) mView.findViewById(R.id.home_frag_medicines_card);
        mCardNonMedicine = (MaterialCardView) mView.findViewById(R.id.home_frag_non_medicines_card);
        mCardSurgical = (MaterialCardView) mView.findViewById(R.id.home_frag_surgical_card);
        mCardJuniors = (MaterialCardView) mView.findViewById(R.id.home_frag_juniors_card);
        mCardOpticals = (MaterialCardView) mView.findViewById(R.id.home_frag_opticals_card);
        mCardBookDoctor = (MaterialCardView) mView.findViewById(R.id.home_frag_book_doctor_card);
        mBannerMain = (MaterialCardView) mView.findViewById(R.id.home_banner_mainLayout);
        //topLayout = (LinearLayout) mView.findViewById(R.id.home_frag_top_ll);
        //middleLayout = (LinearLayout) mView.findViewById(R.id.home_frag_middle_ll);
        //bottomLayout = (LinearLayout) mView.findViewById(R.id.home_frag_bottom_ll);
        //mCardgeneralMedicine = (CardView) mView.findViewById(R.id.card_general_med);
        //mCardSurgical = (CardView) mView.findViewById(R.id.card_durgical);
        //mCardFind = (CardView) mView.findViewById(R.id.card_find);
        //mCardLaboratary = (CardView) mView.findViewById(R.id.card_laboratary);
        //mcardHospital = (CardView) mView.findViewById(R.id.card_hospital);

        /*topLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toastComingSoon();
            }
        });

        middleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toastComingSoon();
            }
        });

        bottomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toastComingSoon();
            }
        });*/

        homeCategoryList = CategoryList;

        imagArrayList.clear();

        IsAddressSelected = false;

        postCreateCartId();
        if (ImagArrayList.size() == 0 || ImagArrayList == null){
            //getSlidingImages();
            getHomeBanner();
        } else {
            imagArrayList = ImagArrayList;

            SlidingImagesAdapter sliderStringInfiniteAdapter = new SlidingImagesAdapter(getActivity(), imagArrayList, true);
            mLVPSlider.setAdapter(sliderStringInfiniteAdapter);

            mPIVindication.setAnimationType(AnimationType.THIN_WORM);
            mPIVindication.setCount(mLVPSlider.getIndicatorCount());

            mLVPSlider.setIndicatorPageChangeListener(new LoopingViewPager.IndicatorPageChangeListener() {
                @Override
                public void onIndicatorProgress(int selectingPosition, float progress) {
                }

                @Override
                public void onIndicatorPageChange(int newIndicatorPosition) {
                    mPIVindication.setSelection(newIndicatorPosition);
                }
            });

            mBannerMain.setVisibility(View.VISIBLE);
        }
//        imagArrayList.add(R.drawable.slider_img);
//        imagArrayList.add(R.drawable.slider_img);
//        imagArrayList.add(R.drawable.slider_img);
//        imagArrayList.add(R.drawable.slider_img);



//        SlidingImagesAdapter sliderStringInfiniteAdapter = new SlidingImagesAdapter(getActivity(), imagArrayList, true);
//        mLVPSlider.setAdapter(sliderStringInfiniteAdapter);
//        mPIVindication.setAnimationType(AnimationType.THIN_WORM);
//        mPIVindication.setCount(mLVPSlider.getIndicatorCount());

        /*mLVPSlider.setIndicatorPageChangeListener(new LoopingViewPager.IndicatorPageChangeListener() {
            @Override
            public void onIndicatorProgress(int selectingPosition, float progress) {
            }

            @Override
            public void onIndicatorPageChange(int newIndicatorPosition) {
                mPIVindication.setSelection(newIndicatorPosition);
            }
        });*/

        mBtnUploadPres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IsAddressSelected = false;
                Intent intent = new Intent(getActivity(), UploadPresActivity.class);
                startActivity(intent);
            }
        });

        mBtnWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWhatsApp();
            }
        });

        mCardMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IsAddressSelected = false;
                Intent intent = new Intent(getActivity(), UploadPresActivity.class);
                startActivity(intent);
            }
        });

        mCardNonMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                catToolbarName = "Non Medicine";
                checkHomeCategory("Non Medicine");
            }
        });

        mCardSurgical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                catToolbarName = "Surgicals";
                checkHomeCategory("Surgicals");
            }
        });

        mCardJuniors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                catToolbarName = "Relief Juniors";
                checkHomeCategory("Relief Juniors");
            }
        });

        mCardOpticals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                catToolbarName = "Opticals";
                checkHomeCategory("Opticals");
            }
        });

        mCardBookDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                catToolbarName = "Book Your Doctor";
                checkHomeCategory("Book Your Doctor");
            }
        });

        /*mCardLaboratary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getLaborataries();
            }
        });

        mcardHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getHospitals();
            }
        });

        mCardgeneralMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getCategories();
            }
        });

        mCardSurgical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SurgicalActivity.class);
                startActivity(intent);
            }
        });

        mCardFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDoctersList();
            }
        });*/

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nestedScrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        final int scrollViewHeight = nestedScrollView.getHeight();
                        if (scrollViewHeight > 0) {
                            nestedScrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                            final View lastView = nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);
                            final int lastViewBottom = lastView.getBottom() + nestedScrollView.getPaddingBottom();
                            final int deltaScrollY = lastViewBottom - scrollViewHeight - nestedScrollView.getScrollY();
                            /* If you want to see the scroll animation, call this. */
                            nestedScrollView.smoothScrollBy(0, deltaScrollY);
                            /* If you don't want, call this. */
//                            nestedScrollView.scrollBy(0, deltaScrollY);
                        }
                    }
                });
            }
        },500);

        //getProfile();

        return mView;
    }

    private void checkHomeCategory(String catName) {
        boolean flag = false;
        String catId = "";
        for (int index=0; index<homeCategoryList.size(); index ++) {
            if (homeCategoryList.get(index).getName().equals(catName)) {
                flag = true;
                catId = homeCategoryList.get(index).getId();
                break;
            }
        }
        if (flag) {
            getProductList(catId);
        }
    }

    private void getHomeBanner() {
        LoadingDialog.showLoadingDialog(getActivity(),"Loading...");

        new NetworkManager(requireContext()).doGetCustom(
                null,
                Apis.API_GET_HOME_SLIDER,
                HomeBanner[].class,
                null,
                Apis.ACCESS_TOKEN,
                "REQUEST_GET_SLIDER_IMAGES_LIST",
                REQUEST_GET_SLIDER_IMAGES_LIST_ID,
                this
        );
    }

    private void getProductList(String catId) {
        LoadingDialog.showLoadingDialog(getActivity(),"Loading...");
        CatId = catId;
        String getUrl = Apis.API_GET_PRODUCTS +
                "?searchCriteria[filter_groups][0][filters][0][field]=category_id" +
                "&searchCriteria[filter_groups][0][filters][0][value]="+catId+
                "&fields=total_count,items[sku,name,price,status,visibility,type_id,media_gallery_entries]" +
                "&searchCriteria[pageSize]=" + DefaultPageSize +"&searchCriteria[currentPage]=1";

        /*new NetworkManager(requireContext()).doGet(
                null,
                getUrl,
                Apis.ACCESS_TOKEN,
                "TAG_PRODUCTS",
                REQUEST_PRODUCTS,
                this
        );*/

        String params = "searchCriteria[filter_groups][0][filters][0][field]=category_id" +
                "&searchCriteria[filter_groups][0][filters][0][value]="+catId+
                "&fields=items[sku,name,price,status,visibility,type_id,media_gallery_entries]";

        new NetworkManager(requireContext()).doGetCustom(
                null,
                getUrl,
                ProductResponse.class,
                null,
                Apis.ACCESS_TOKEN,
                "TAG_PRODUCTS",
                REQUEST_PRODUCTS,
                this
        );
    }

    private void postCreateCartId() {
        try {
            String token =manager.getUserToken();

            new NetworkManager(requireContext()).doPostCustom(
                    Apis.API_POST_CREATE_CART_ID,
                    Object.class,
                    null,
                    token,
                    "TAG_CREATE_CART_ID",
                    REQUEST_CREATE_CART_ID,
                    this
            );

            LoadingDialog.showLoadingDialog(requireContext(),"Loading...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCartCount() {
        String cartId = manager.getCartId();
        String getUrl = Apis.API_GET_CART_COUNT + cartId;

        new NetworkManager(requireContext()).doGetCustom(
                null,
                getUrl,
                CartCountResponse[].class,
                null,
                Apis.ACCESS_TOKEN,
                "TAG_CART_COUNT",
                REQUEST_CART_COUNT,
                this
        );
    }

    private void toastComingSoon() {
        Toast.makeText(requireContext(),
                "Coming Soon...",
                Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), ComingSoonActivity.class);
        startActivity(intent);
    }

    private void openWhatsApp() {
        try {
            String url = "https://api.whatsapp.com/send?phone=+919995517342";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getProfile() {
        Map<String,String> map = new HashMap<>();
        map.put("userid",manager.getdelicioId());

        new NetworkManager(getActivity()).doPost(map,Apis.API_POST_GET_PROFILE,"REQUEST_GET_PROFILE",TAG_GET_PROFILE_ID,this);

    }

    private void getSlidingImages() {
        LoadingDialog.showLoadingDialog(getActivity(),"Loading...");
        new NetworkManager(getActivity()).doPost(null, Apis.API_POST_GET_SLIDER_IMAGES,"REQUEST_GET_SLIDER_IMAGES_LIST",REQUEST_GET_SLIDER_IMAGES_LIST_ID,this);
    }

    private void getHospitals() {
        LoadingDialog.showLoadingDialog(getActivity(),"Loading...");
        new NetworkManager(getActivity()).doPost(null, Apis.API_POST_GET_ALL_HOSPITAL,"REQUEST_GET_HOSPITAL_LIST",REQUEST_GET_HOSPITAL_LIST_ID,this);
    }

    private void getLaborataries() {
        LoadingDialog.showLoadingDialog(getActivity(),"Loading...");
        new NetworkManager(getActivity()).doPost(null, Apis.API_POST_GET_ALL_LABORATARIES,"REQUEST_GET_LABORATARIES_LIST",REQUEST_GET_LABORATARIES_LIST_ID,this);
    }

    private void getCategories() {
        LoadingDialog.showLoadingDialog(getActivity(),"Loading...");
        new NetworkManager(getActivity()).doPost(null, Apis.API_POST_GET_CATEGORIES,"REQUEST_GET_CATEGORIES_LIST",REQUEST_GET_CATEGORIES_LIST_ID,this);
    }

    private void getDoctersList() {
        LoadingDialog.showLoadingDialog(getActivity(),"Loading...");
        new NetworkManager(getActivity()).doPost(null, Apis.API_POST_GET_ALL_DECTORS_LIST,"REQUEST_GET_DOCTORS_LIST",REQUEST_GET_DOCTORS_LIST_ID,this);
    }

    @Override
    public void onResponse(int status, String response, int requestId) {
        if (status == NetworkManager.SUCCESS){
            if (requestId == REQUEST_GET_DOCTORS_LIST_ID){
                ProcessJsonGetDoctorsList(response);
            } else if (requestId == REQUEST_GET_CATEGORIES_LIST_ID){
                ProcessJsonGetCategoriesList(response);
            } else if (requestId == REQUEST_GET_LABORATARIES_LIST_ID){
                ProcessJsonGetLaboratariesList(response);
            } else if (requestId == REQUEST_GET_HOSPITAL_LIST_ID){
                ProcessJsonGetHospitalList(response);
            } else if (requestId == REQUEST_GET_SLIDER_IMAGES_LIST_ID){
                ProcessJsonGetSliderList(response);
            } else if(requestId == TAG_GET_PROFILE_ID){
                processGetProfile(response);
            } else if(requestId == REQUEST_PRODUCTS){
                processGetProducts(response);
            } else if (requestId == REQUEST_CREATE_CART_ID) {
                processJsonCreateCartId(response);
            } else if (requestId == REQUEST_CART_COUNT) {
                processJsonCartCount(response);
            }
        } else {
            LoadingDialog.cancelLoading();
            Toast.makeText(getActivity(), "Couldn't load data. The network got interrupted", Toast.LENGTH_LONG).show();
        }
    }

    private void processJsonCartCount(String response) {
        Log.d(TAG, response);
        LoadingDialog.cancelLoading();
        try {
            Type type = new TypeToken<ArrayList<CartCountResponse>>(){}.getType();
            ArrayList<CartCountResponse> cartCountResponseArrayList = gson.fromJson(response, type);
            if (cartCountResponseArrayList != null && !cartCountResponseArrayList.isEmpty()) {
                manager.saveCartCount(cartCountResponseArrayList.get(0).getCount());
                ((MainActivity) getActivity()).updateCartCount();
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Type type = new TypeToken<AddAddressResponse>(){}.getType();
                AddAddressResponse cartData = gson.fromJson(response, type);
                if (cartData != null) {
                    String message = cartData.getMessage();
                    LoadingDialog.cancelLoading();
                    dialogWarning(requireContext(), message);
                } else {
                    serverErrorDialog();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private void processJsonCreateCartId(String response) {
        //Log.d(TAG, response);
        try {
            float quoteId = Float.parseFloat(response);
            manager.saveCartId("" + quoteId);
            getCartCount();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Type type = new TypeToken<AddAddressResponse>(){}.getType();
                AddAddressResponse cartData = gson.fromJson(response, type);
                if (cartData != null) {
                    String message = cartData.getMessage();
                    LoadingDialog.cancelLoading();
                    dialogWarning(requireContext(), message);
                } else {
                    serverErrorDialog();
                }
            } catch (Exception e1) {
                LoadingDialog.cancelLoading();
                e1.printStackTrace();
            }
        }
    }

    private void ProcessJsonGetSliderList(String response) {
        Log.d(TAG, response);
        try {
            Type type = new TypeToken<ArrayList<HomeBanner>>(){}.getType();
            ArrayList<HomeBanner> homeBannerArrayList = gson.fromJson(response, type);
            imagArrayList.clear();
            if (homeBannerArrayList != null && !homeBannerArrayList.isEmpty()) {
                for (int index=0; index<homeBannerArrayList.size(); index ++) {
                    imagArrayList.add(homeBannerArrayList.get(0).getBanner_image_mobile());
                }

                mBannerMain.setVisibility(View.VISIBLE);
            } else {
                mBannerMain.setVisibility(View.GONE);
            }

            ImagArrayList = imagArrayList;

            SlidingImagesAdapter sliderStringInfiniteAdapter = new SlidingImagesAdapter(getActivity(), imagArrayList, true);
            mLVPSlider.setAdapter(sliderStringInfiniteAdapter);

            mPIVindication.setAnimationType(AnimationType.THIN_WORM);
            mPIVindication.setCount(mLVPSlider.getIndicatorCount());

            mLVPSlider.setIndicatorPageChangeListener(new LoopingViewPager.IndicatorPageChangeListener() {
                @Override
                public void onIndicatorProgress(int selectingPosition, float progress) {
                }

                @Override
                public void onIndicatorPageChange(int newIndicatorPosition) {
                    mPIVindication.setSelection(newIndicatorPosition);
                }
            });

            LoadingDialog.cancelLoading();

        } catch (Exception e) {
            e.printStackTrace();
            serverErrorDialog();
        }
    }

    private void serverErrorDialog() {
        try {
            LoadingDialog.cancelLoading();
            dialogWarning(requireContext(), "Sorry ! Can't connect to server, try later");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processGetProducts(String response) {
        if(response == null){
            Log.d(TAG, "processJson: Cant get Category");
            LoadingDialog.cancelLoading();
            dialogWarning(requireContext(), "Sorry ! Can't connect to server, try later");
        } else {
            try {

                mCatProductList.clear();
                Type type = new TypeToken<ProductResponse>(){}.getType();
                ProductResponse pResponse = gson.fromJson(response, type);

                if (pResponse.getItems() != null && !pResponse.getItems().isEmpty()) {
                    mCatProductList = pResponse.getItems();
                }

                if (pResponse.getTotal_count() != null) {
                    try {
                        CatProductCount = Integer.parseInt(pResponse.getTotal_count());
                    } catch (Exception e) {
                        CatProductCount = 0;
                    }
                } else {
                    CatProductCount = 0;
                }

                JSONObject jsonObject1 = new JSONObject(response);
                JSONArray jsonArray1 = jsonObject1.optJSONArray("items");

                mProductList.clear();

                if (jsonArray1 != null) {

                    for (int i = 0;i<jsonArray1.length();i++){

                        JSONObject jsonObject2 = jsonArray1.getJSONObject(i);

                        ProductsModel productsModel = new ProductsModel();
                        productsModel.setProduct_name(jsonObject2.optString("name"));
                        productsModel.setProduct_price(jsonObject2.optString("price"));

                        JSONArray jsonArray2 = jsonObject2.optJSONArray("media_gallery_entries");
                        if (jsonArray2 != null && jsonArray2.length() > 0) {
                            JSONObject jsonObject3 = jsonArray2.getJSONObject(0);

                            productsModel.setProduct_image1(jsonObject3.optString("file"));

                        }

                        mProductList.add(productsModel);
                    }

                }

                CatProductList = mCatProductList;

                ProductList = mProductList;

                LoadingDialog.cancelLoading();

                Intent intent = new Intent(getActivity(), ProductsActivity.class);
                intent.putExtra("name", catToolbarName);
                startActivity(intent);

            } catch (Exception e) {
                e.printStackTrace();
                LoadingDialog.cancelLoading();
                dialogWarning(requireContext(), "Sorry ! Can't connect to server, try later");
            }
        }
    }

    private void processGetProfile(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();
           //     Toast.makeText(getActivity(), "Something went wrong..", Toast.LENGTH_SHORT).show();
            } else {
                LoadingDialog.cancelLoading();
                JSONArray jsonArray1 = jsonObject.optJSONArray("delivery_details");
                if (jsonArray1 != null && !jsonArray1.isNull(0)) {
                    JSONObject jsonObject2 = jsonArray1.optJSONObject(0);

                    DeliveryAddressModel deliveryAddressModel = new DeliveryAddressModel();

                    deliveryAddressModel.setName(jsonObject2.optString("name"));
                    deliveryAddressModel.setNumber(jsonObject2.optString("phone"));
                    deliveryAddressModel.setPinCode(jsonObject2.optString("pincode"));
                    manager.setPincode(jsonObject2.optString("pincode"));
                    deliveryAddressModel.setAddress(jsonObject2.optString("address"));
                    deliveryAddressModel.setLandmark(jsonObject2.optString("landmark"));
                    deliveryAddressModel.setAlternativeNumber(jsonObject2.optString("altertnative_phone"));
                    deliveryAddressModel.setmLat(jsonObject2.optString("latitude"));
                    deliveryAddressModel.setmLong(jsonObject2.optString("longitude"));

                    manager.saveDeliveryAddress(deliveryAddressModel);

                }else{
                    manager.setPincode("");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(getActivity(), "Sorry ! Can't connect to server, try later");
        }
    }

    /*private void ProcessJsonGetSliderList(String response) {
        try {
            JSONObject jsonObject1 = new JSONObject(response);
            boolean error = jsonObject1.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();
           //     Toast.makeText(getActivity(), "Something went wrong..", Toast.LENGTH_SHORT).show();
            } else {
                JSONArray jsonArray = jsonObject1.optJSONArray("data");
                JSONObject jsonObject11 = jsonArray.optJSONObject(0);
                imagArrayList.clear();
                imagArrayList.add(jsonObject11.optString("slider_1"));
                imagArrayList.add(jsonObject11.optString("slider_2"));
                imagArrayList.add(jsonObject11.optString("slider_3"));
                imagArrayList.add(jsonObject11.optString("slider_4"));

                ImagArrayList = imagArrayList;

                SlidingImagesAdapter sliderStringInfiniteAdapter = new SlidingImagesAdapter(getActivity(), imagArrayList, true);
                mLVPSlider.setAdapter(sliderStringInfiniteAdapter);

                mPIVindication.setAnimationType(AnimationType.THIN_WORM);
                mPIVindication.setCount(mLVPSlider.getIndicatorCount());

                try{
                    LoadingDialog.cancelLoading();
                }
                catch (Exception e){
                    LoadingDialog.cancelLoading();
                }

                mLVPSlider.setIndicatorPageChangeListener(new LoopingViewPager.IndicatorPageChangeListener() {
                    @Override
                    public void onIndicatorProgress(int selectingPosition, float progress) {
                    }

                    @Override
                    public void onIndicatorPageChange(int newIndicatorPosition) {
                        mPIVindication.setSelection(newIndicatorPosition);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
        }
    }*/

    private void ProcessJsonGetHospitalList(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();
                Toast.makeText(getActivity(), "There is no Laboratory directories.", Toast.LENGTH_SHORT).show();
            } else {
                JSONObject jsonObject4 = jsonObject.getJSONObject("data");
                JSONArray jsonArray = jsonObject4.optJSONArray("hospitals");
                mHospitalList.clear();
                for (int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                    LaboratoryModel laboratoryModel = new LaboratoryModel();

                    laboratoryModel.setId(jsonObject1.optString("id"));
                    laboratoryModel.setName(jsonObject1.optString("name"));
                    laboratoryModel.setAddress(jsonObject1.optString("address"));
                    laboratoryModel.setPlace(jsonObject1.optString("place"));
                    laboratoryModel.setPincode(jsonObject1.optString("pincode"));
                    laboratoryModel.setPhone(jsonObject1.optString("phone"));
                    laboratoryModel.setAlter_phone(jsonObject1.optString("alter_phone"));
                    laboratoryModel.setEmail(jsonObject1.optString("email"));
                    laboratoryModel.setImage(jsonObject1.optString("image"));
                    laboratoryModel.setDescription(jsonObject1.optString("description"));
                    laboratoryModel.setLat(jsonObject1.optString("lat"));
                    laboratoryModel.setLongi(jsonObject1.optString("long"));
                    laboratoryModel.setConstituency_id(jsonObject1.optString("constituency_id"));
                    laboratoryModel.setConstituency_name(jsonObject1.optString("constituency_name"));

                    mHospitalList.add(laboratoryModel);
                }

                if (ConstituencyList.size() == 0 || ConstituencyList == null){
                    JSONArray jsonArray1 = jsonObject4.optJSONArray("constituency");
                    constituencyList.clear();
                    for (int i = 0;i<jsonArray1.length();i++){
                        JSONObject jsonObject2 = jsonArray1.optJSONObject(i);
                        ConstituencyModel constituencyModel = new ConstituencyModel();

                        constituencyModel.setId(jsonObject2.optString("id"));
                        constituencyModel.setDistrict_id(jsonObject2.optString("district_id"));
                        constituencyModel.setConstituency_name(jsonObject2.optString("constituency_name"));
                        constituencyModel.setStatus(jsonObject2.optString("status"));

                        constituencyList.add(constituencyModel);
                    }
                    ConstituencyList = constituencyList;
                }
                LoadingDialog.cancelLoading();
                HospitalList = mHospitalList;

                Intent intent = new Intent(getActivity(), HospitalActivity.class);
                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
        }
    }

    private void ProcessJsonGetLaboratariesList(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();
                Toast.makeText(getActivity(), "There is no Laboratory directories.", Toast.LENGTH_SHORT).show();
            } else {
                JSONObject jsonObject4 = jsonObject.getJSONObject("data");
                JSONArray jsonArray = jsonObject4.optJSONArray("laboratories");
                mLaborataryList.clear();
                for (int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject1 = jsonArray.optJSONObject(i);

                    LaboratoryModel laboratoryModel = new LaboratoryModel();

                    laboratoryModel.setId(jsonObject1.optString("id"));
                    laboratoryModel.setName(jsonObject1.optString("name"));
                    laboratoryModel.setAddress(jsonObject1.optString("address"));
                    laboratoryModel.setPlace(jsonObject1.optString("place"));
                    laboratoryModel.setPincode(jsonObject1.optString("pincode"));
                    laboratoryModel.setPhone(jsonObject1.optString("phone"));
                    laboratoryModel.setAlter_phone(jsonObject1.optString("alter_phone"));
                    laboratoryModel.setEmail(jsonObject1.optString("email"));
                    laboratoryModel.setImage(jsonObject1.optString("image"));
                    laboratoryModel.setDescription(jsonObject1.optString("description"));
                    laboratoryModel.setLat(jsonObject1.optString("lat"));
                    laboratoryModel.setLongi(jsonObject1.optString("long"));
                    laboratoryModel.setConstituency_id(jsonObject1.optString("constituency_id"));
                    laboratoryModel.setConstituency_name(jsonObject1.optString("constituency_name"));
                    laboratoryModel.setOpening_days(jsonObject1.optString("opening_days"));
                    laboratoryModel.setOpening_time(jsonObject1.optString("opening_time"));

                    mLaborataryList.add(laboratoryModel);
                }

                if (ConstituencyList.size() == 0 || ConstituencyList == null){
                    JSONArray jsonArray1 = jsonObject4.optJSONArray("constituency");
                    constituencyList.clear();
                    for (int i = 0;i<jsonArray1.length();i++){
                         JSONObject jsonObject2 = jsonArray1.optJSONObject(i);
                         ConstituencyModel constituencyModel = new ConstituencyModel();

                        constituencyModel.setId(jsonObject2.optString("id"));
                        constituencyModel.setDistrict_id(jsonObject2.optString("district_id"));
                        constituencyModel.setConstituency_name(jsonObject2.optString("constituency_name"));
                        constituencyModel.setStatus(jsonObject2.optString("status"));

                        constituencyList.add(constituencyModel);
                    }
                    ConstituencyList = constituencyList;
                }
                LoadingDialog.cancelLoading();

                LaborataryList = mLaborataryList;

                Intent intent = new Intent(getActivity(), LaborataryActivity.class);
                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
        }
    }

    private void ProcessJsonGetCategoriesList(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();
        //        Toast.makeText(getActivity(), "Something went wrong..", Toast.LENGTH_SHORT).show();
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("data");

                mCateList.clear();
                for (int i = 0;i<jsonArray.length();i++){

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    CategoryModel categoryModel = new CategoryModel();
                    categoryModel.setId(jsonObject1.optString("id"));
                    categoryModel.setName(jsonObject1.optString("category_name"));
                    categoryModel.setDate(jsonObject1.optString("create_date"));
                    categoryModel.setImage(jsonObject1.optString("image"));

                    mCateList.add(categoryModel);
                }

                LoadingDialog.cancelLoading();
                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                intent.putExtra("list", (Serializable) mCateList);
                startActivity(intent);

            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(getActivity(), "Sorry ! Can't connect to server, try later");
        }
    }

    private void ProcessJsonGetDoctorsList(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean error = jsonObject.optBoolean("error");
            if (error){
                LoadingDialog.cancelLoading();
          //      Toast.makeText(getActivity(), "Something went wrong..", Toast.LENGTH_SHORT).show();
            } else {
                JSONObject jsonObject1 = jsonObject.optJSONObject("data");

                doctorsList.clear();
                departmentList.clear();
                constituencyList.clear();

                JSONArray jsonArray = jsonObject1.optJSONArray("doctors");

                for (int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                    DoctorsModel doctorsModel = new DoctorsModel();

                    doctorsModel.setId(jsonObject2.optString("id"));
                    doctorsModel.setName(jsonObject2.optString("name"));
                    doctorsModel.setAddress(jsonObject2.optString("address"));
                    doctorsModel.setPlace(jsonObject2.optString("place"));
                    doctorsModel.setPincode(jsonObject2.optString("pincode"));
                    doctorsModel.setPhone(jsonObject2.optString("phone"));
                    doctorsModel.setEmail(jsonObject2.optString("email"));
                    doctorsModel.setImage(jsonObject2.optString("image"));
                    doctorsModel.setQualification(jsonObject2.optString("qualification"));
                    doctorsModel.setDept_id(jsonObject2.optString("dept_id"));
                    doctorsModel.setDept_name(jsonObject2.optString("dept_name"));
                    doctorsModel.setConstituency_id(jsonObject2.optString("constituency_id"));
                    doctorsModel.setConstituency_name(jsonObject2.optString("constituency_name"));

                    doctorsList.add(doctorsModel);
                }

                JSONArray jsonArray1 = jsonObject1.optJSONArray("department");

                for (int n = 0;n<jsonArray1.length();n++){

                    JSONObject jsonObject3 = jsonArray1.optJSONObject(n);
                    DepartmentModel departmentModel =new DepartmentModel();

                    departmentModel.setId(jsonObject3.optString("id"));
                    departmentModel.setDept_name(jsonObject3.optString("dept_name"));
                    departmentModel.setStatus(jsonObject3.optString("status"));

                    departmentList.add(departmentModel);
                }

                JSONArray jsonArray2 = jsonObject1.optJSONArray("constituency");

                for (int m = 0;m<jsonArray2.length();m++){

                    JSONObject jsonObject4 = jsonArray2.optJSONObject(m);
                    ConstituencyModel  constituencyModel = new ConstituencyModel();

                    constituencyModel.setId(jsonObject4.optString("id"));
                    constituencyModel.setDistrict_id(jsonObject4.optString("district_id"));
                    constituencyModel.setConstituency_name(jsonObject4.optString("constituency_name"));
                    constituencyModel.setStatus(jsonObject4.optString("status"));

                    constituencyList.add(constituencyModel);
                }

                DepartmentList = departmentList;
                DoctorsList = doctorsList;
                ConstituencyList = constituencyList;

                LoadingDialog.cancelLoading();
                Intent intent = new Intent(getActivity(), DoctorActivity.class);
                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LoadingDialog.cancelLoading();
            dialogWarning(getActivity(), "Sorry ! Can't connect to server, try later");
        }
    }

    @Override
    public void onJsonResponse(int status, String response, int requestId) {

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).updateCartCount();
    }
}