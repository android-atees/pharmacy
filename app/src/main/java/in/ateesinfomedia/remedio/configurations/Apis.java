package in.ateesinfomedia.remedio.configurations;

public class Apis {

//    private static final String BASE_URL = "http://192.168.1.105/remedio/Api/";
//    private static final String IMAGE_BASE_URL = "http://192.168.1.105/remedio/assets/";
//    private static final String DUMMY_BASE_URL = "http://192.168.1.147/remedio/Api/";
//    private static final String BASE_URL = "http://202.88.254.210/remedionew/Api/";
//    private static final String IMAGE_BASE_URL = "http://202.88.254.210/remedionew/assets/";

//    private static final String BASE_URL = "http://ateesdemo.com/remedio/Api/";
//    private static final String IMAGE_BASE_URL = "http://ateesdemo.com/remedio/assets/";

//    --------------------------local---------------------------------
//    private static final String BASE_URL = "http://192.168.1.100:8080/remedio/Api/";
//    private static final String IMAGE_BASE_URL = "http://192.168.1.100:8080/remedio/assets/";
//    --------------------------local---------------------------------


    //    --------------------------live---------------------------------
    private static final String BASE_URL = "http://www.remedio.in/Api/";
    private static final String IMAGE_BASE_URL = "http://www.remedio.in/assets/";
//    --------------------------live---------------------------------


//    --------------------------live---------------------------------
//    private static final String BASE_URL = "http://13.235.208.118/remedio/Api/";
//     private static final String IMAGE_BASE_URL = "http://13.235.208.118/remedio/assets/";
//    --------------------------live---------------------------------



    public static final String PRODUCT_IMAGE_BASE_URL = IMAGE_BASE_URL + "w_products/";
    public static final String SUB_CATEGORY_IMAGE_BASE_URL = IMAGE_BASE_URL + "w_subcategory/";
    public static final String API_POST_DOCTORS_LOGO_PATH = IMAGE_BASE_URL + "listofdirectory/";
    public static final String API_POST_SLIDER_IMAGES = IMAGE_BASE_URL + "home_images_app/";
    public static final String API_POST_PRESCRIPTION_IMAGES = IMAGE_BASE_URL + "prescription/";

    public static final String API_POST_USER_REGISTRATION = BASE_URL + "userregistration";
    public static final String API_POST_USER_LOGIN = BASE_URL + "loginuser";
    public static final String API_POST_USER_CHECK_MOBILE_EXIST = BASE_URL + "checkmobileexist";
    public static final String API_POST_USER_FORGOT_PASSWORD = BASE_URL + "forgotpassword";
    public static final String API_POST_USER_UPDATE_PASS_AND_LOGIN = BASE_URL + "updatepasswordandlogin ";
    public static final String API_POST_USER_UPDATE_NUMBER = BASE_URL + "updatePhone";
    public static final String API_POST_USER_UPDATE_NEW_NUMBER = BASE_URL + "checkotpforupdatephone";
    public static final String API_POST_UPLOAD_PRESCRIPTION = BASE_URL + "uploadprescription";
    public static final String API_POST_UPDATE_PROFILE = BASE_URL + "updateProfile";
    public static final String API_POST_ADD_DELIVERY_ADDRESS = BASE_URL + "insert_updatedeliveryaddress";
    public static final String API_POST_ADD_DELIVERY_ADDRESS_PROFILE = BASE_URL + "updatedeliveryaddressforprofile";
    public static final String API_POST_GET_PROFILE = BASE_URL + "getprofile";
    public static final String API_POST_GET_DELIVERY_ADDRESS = BASE_URL + "getdeliveryaddress";
    public static final String API_POST_GET_ALL_DECTORS_LIST = BASE_URL + "getalldoctors";
    public static final String API_POST_GET_DOCTORS_DETAILS = BASE_URL + "doctor_details";
    public static final String API_POST_GET_ORDERS_LIST = BASE_URL + "medicineorderlist";
    public static final String API_POST_REFERAL = BASE_URL + "remedio_shareandearn"; //Not in use
    public static final String API_POST_GET_ORDERS_DETAILS = BASE_URL + "medicineorderdetails";
    public static final String API_POST_GET_ORDERS_CONFIRM = BASE_URL + "orderconfirmation";
    public static final String API_POST_UPLOAD_FCM_TOKEN = BASE_URL + "getfcmtoken";
    public static final String API_POST_UPDATEFCM = BASE_URL + "updateuserforlogin";
    public static final String API_POST_LOGOUT = BASE_URL + "logout";
    public static final String API_POST_GET_EARNINGSES = BASE_URL + "get_earnings";
    public static final String API_POST_GET_NOTIFICATIONS = BASE_URL + "notification_history";
    public static final String API_POST_GET_NOTIFICATIONS_COUNT = BASE_URL + "notification_count";
    public static final String API_POST_GET_CATEGORIES = BASE_URL + "getcategory";
    public static final String API_POST_GET_SUBCATEGORIES = BASE_URL + "getsubcategory";
    public static final String API_POST_GET_SUBCATEGORIES_PRODUCTS = BASE_URL + "productbysubcategory";
    public static final String API_POST_ADD_TO_CART = BASE_URL + "addtocart";
    public static final String API_POST_PLUS_OR_MINUS_OR_REMOVE = BASE_URL + "plus_minus_cart";
    public static final String API_POST_GET_CART_ITEMS = BASE_URL + "cartitems";
    public static final String API_POST_CONFIRM_ORDER = BASE_URL + "completeotcorder";
    public static final String API_POST_CHECK_PINCODE = BASE_URL + "verifypincode";
    public static final String API_POST_CHECK_IS_CART = BASE_URL + "checkproductincart";
    public static final String API_POST_GET_CART_COUNT = BASE_URL + "getcartcount";
    public static final String API_POST_GET_ALL_HOSPITAL = BASE_URL + "getallhospitals";
    public static final String API_POST_GET_OTC_ORDER_DETAILS = BASE_URL + "otcorderdetails";
    public static final String API_POST_GET_ALL_LABORATARIES = BASE_URL + "getalllaboratories";
    public static final String API_POST_GET_SLIDER_IMAGES = BASE_URL + "getsliderforuserside";
    public static final String API_POST_CHECK_UPDATE = BASE_URL + "checkmobileversion";
    public static final String API_POST_UPDATE_REVIEW = BASE_URL + "updatereviewforproduct";
    public static final String API_POST_CHECK_PRODUCT_PURCHASED = BASE_URL + "checkuserbuyproduct";
    public static final String API_POST_OFFERS = BASE_URL + "remedio_offers";
    public static final String API_POST_PRESCRIPTION_TRACKING_DETAILS = BASE_URL + "prescriptiontrackingdetails";
    public static final String API_POST_OTC_TRACKING_DETAILS = BASE_URL + "otctrackingdetails";
    public static final String API_POST_CANCEL_OTC_ORDERS = BASE_URL + "cancel_otc_order_item";
    public static final String API_POST_CANCEL_REASONS = BASE_URL + "get_cancel_otc_reasons";
    // broadcast receiver intent filters

    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
}