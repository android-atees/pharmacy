package in.ateesinfomedia.relief.configurations;

import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import in.ateesinfomedia.relief.models.CartModel;
import in.ateesinfomedia.relief.models.ConstituencyModel;
import in.ateesinfomedia.relief.models.DepartmentModel;
import in.ateesinfomedia.relief.models.DoctorsDetailModel;
import in.ateesinfomedia.relief.models.DoctorsModel;
import in.ateesinfomedia.relief.models.EarningsModel;
import in.ateesinfomedia.relief.models.LaboratoryModel;
import in.ateesinfomedia.relief.models.ProductsModel;
import in.ateesinfomedia.relief.models.ReviewModel;
import in.ateesinfomedia.relief.view.adapter.OtcOrderModel;

public class Global {

    public static List<DepartmentModel> DepartmentList = new ArrayList<>();
    public static List<DoctorsModel> DoctorsList = new ArrayList<>();
    public static List<DoctorsDetailModel> DoctorsDetailsList = new ArrayList<>();
    public static List<ConstituencyModel> ConstituencyList = new ArrayList<>();
    public static List<LaboratoryModel> LaborataryList = new ArrayList<>();
    public static List<LaboratoryModel> HospitalList = new ArrayList<>();
    public static List<EarningsModel> ReferenceEarnList = new ArrayList<>();
    public static List<EarningsModel> SaleEarnList = new ArrayList<>();
    public static List<CartModel> CartList = new ArrayList<>();
    public static List<OtcOrderModel> OTCOrderModelList = new ArrayList<>();
    public static ArrayList<String> ImagArrayList = new ArrayList<>();
    public static List<ReviewModel> ReviewList = new ArrayList<>();
    public static List<ProductsModel> ProductList = new ArrayList<>();
    public static ReviewModel LastReviewModel;
    public static boolean isChanged = false;

    public static String TOTAL_SALES_EARN = "";
    public static String TOTAL_REF_EARN = "";

    public static String COUNT = "0";
    public static String PinCode = "";
    public static double Latitude,Longitude;

    public static void dialogWarning(Context context, String string2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle((CharSequence)"Warning");
        builder.setMessage((CharSequence)string2);
        builder.setPositiveButton((CharSequence)"OK", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialogInterface, int n) {
            }
        });
        builder.show();
    }
}