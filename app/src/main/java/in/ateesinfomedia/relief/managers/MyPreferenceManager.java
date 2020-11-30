package in.ateesinfomedia.relief.managers;

import android.content.Context;
import android.content.SharedPreferences;

import in.ateesinfomedia.relief.models.DeliveryAddressModel;


/**
 * Created by Android-1 on 9/6/2017.
 */

public class MyPreferenceManager {


    // All Shared Preferences Keys
    private static final String KEY_IS_LOGIN = "key_is_login";
    private static final String IS_FIRST_LAUNCH = "first_launch";
    private static final String IS_FIRST_LOGIN = "first_login";
    private static final String IS_GET_LOCATION = "get_location";
    private static final String KEY_DELICIO_ID = "delicio_id";
    private static final String KEY_IS_CHANGED = "any_changes";
    private static final String KEY_CART_COUNT = "cart_count";

    private static final String KEY_NAME = "name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_NUMBER = "number";
    private static final String KEY_ALTERNATIVE_NUMBER = "alternative_number";
    private static final String KEY_CITY_OR_DISTRICT = "city_or_district";
    private static final String KEY_LOCALITY = "locality";
    private static final String KEY_LANDMARK = "landmark";
    private static final String KEY_STATE = "state";
    private static final String KEY_PINCODE = "pincode";
    private static final String KEY_LAT = "latitude";
    private static final String KEY_LONG = "longitude";
    private static final String KEY_USER_NAME = "username";
    private static final String KEY_USER_MAIL = "mail";
    private static final String KEY_USER_NUMBER = "mobile_number";
    private static final String KEY_USER_UNIQUE_ID = "unique_id";
    private static final String KEY_USER_CART_ID = "cart_id";
    private static final String IS_USERADDRESS = "key_user_address";

    private static final String KEY_USER_TOKEN = "key_user_token";

    private static final String KEY_NOTIFICATION_KEY = "notification_key";
    // Sharedpref file name
    private static final String PREF_NAME = "ait_pref";

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstLaunch(boolean state) {
        editor.putBoolean(IS_FIRST_LAUNCH, state);
        // commit changes
        editor.commit();
    }
    public void setFirstLogin(boolean state) {
        editor.putBoolean(IS_FIRST_LOGIN, state);
        // commit changes
        editor.commit();
    }

    public void setLocation(boolean state) {
        editor.putBoolean(IS_GET_LOCATION, state);
        // commit changes
        editor.commit();
    }

    public void setKeyIsUseraddress(boolean state) {
        editor.putBoolean(IS_USERADDRESS, state);
        // commit changes
        editor.commit();
    }
    public boolean isUserAddress(){return  pref.getBoolean(IS_USERADDRESS,false);}

    public boolean isFirstLaunch() {
        return pref.getBoolean(IS_FIRST_LAUNCH, true);
    }

    public boolean isFirstLogin() {
        return pref.getBoolean(IS_FIRST_LOGIN, true);
    }

    public boolean isGotLocation() {
        return pref.getBoolean(IS_GET_LOCATION, false);
    }


    public boolean isLogin() {
        return pref.getBoolean(KEY_IS_LOGIN, false);
    }


    public void setLogIn(boolean state) {
        editor.putBoolean(KEY_IS_LOGIN, state);
        editor.commit();
    }

    public void savedelicioId(String id){
        // Clearing all data from Shared Preferences
        editor.putString(KEY_DELICIO_ID,id);
        editor.commit();

    }

    public void saveUsereDetails(String name, String mail, String number,String uniqueId){
        // Clearing all data from Shared Preferences
        editor.putString(KEY_USER_NAME,name);
        editor.putString(KEY_USER_MAIL,mail);
        editor.putString(KEY_USER_NUMBER,number);
        editor.putString(KEY_USER_UNIQUE_ID,uniqueId);
        editor.commit();
    }

    public void saveChanged(boolean state){
        editor.putBoolean(KEY_IS_CHANGED,state);
        editor.commit();
    }

    public void saveCartCount(int count){
        editor.putInt(KEY_CART_COUNT,count);
        editor.commit();
    }

    public boolean isChanged(){
        return pref.getBoolean(KEY_IS_CHANGED,false);
    }

    public String getdelicioId(){
        // Clearing all data from Shared Preferences
        return pref.getString(KEY_DELICIO_ID,"");
    }

    public int getCartCount(){
        // Clearing all data from Shared Preferences
        return pref.getInt(KEY_CART_COUNT,0);
    }

    public String getUserNumber(){
        // Clearing all data from Shared Preferences
        return pref.getString(KEY_USER_NUMBER,"");
    }

    public void saveDeliveryAddress(DeliveryAddressModel deliveryAddressModel){

        editor.putString(KEY_NAME,deliveryAddressModel.getName());
        editor.putString(KEY_ADDRESS,deliveryAddressModel.getAddress());
        editor.putString(KEY_NUMBER,deliveryAddressModel.getNumber());
        editor.putString(KEY_ALTERNATIVE_NUMBER,deliveryAddressModel.getAlternativeNumber());
        editor.putString(KEY_CITY_OR_DISTRICT,deliveryAddressModel.getCityOrDistrict());
        editor.putString(KEY_LOCALITY,deliveryAddressModel.getLocality());
        editor.putString(KEY_LANDMARK,deliveryAddressModel.getLandmark());
        editor.putString(KEY_STATE,deliveryAddressModel.getState());
        editor.putString(KEY_PINCODE,deliveryAddressModel.getPinCode());
        editor.putString(KEY_LAT,deliveryAddressModel.getmLat());
        editor.putString(KEY_LONG,deliveryAddressModel.getmLong());
        editor.commit();

    }

    public DeliveryAddressModel getDeliveryAddress(){

        DeliveryAddressModel deliveryAddressModel = new DeliveryAddressModel();

        deliveryAddressModel.setName(pref.getString(KEY_NAME,""));
        deliveryAddressModel.setAddress(pref.getString(KEY_ADDRESS,""));
        deliveryAddressModel.setNumber(pref.getString(KEY_NUMBER,""));
        deliveryAddressModel.setAlternativeNumber(pref.getString(KEY_ALTERNATIVE_NUMBER,""));
        deliveryAddressModel.setCityOrDistrict(pref.getString(KEY_CITY_OR_DISTRICT,""));
        deliveryAddressModel.setState(pref.getString(KEY_STATE,""));
        deliveryAddressModel.setPinCode(pref.getString(KEY_PINCODE,""));
        deliveryAddressModel.setLandmark(pref.getString(KEY_LANDMARK,""));
        deliveryAddressModel.setLocality(pref.getString(KEY_LOCALITY,""));
        deliveryAddressModel.setmLat(pref.getString(KEY_LAT,""));
        deliveryAddressModel.setmLong(pref.getString(KEY_LONG,""));

        return deliveryAddressModel;
    }

    public String getPincode(){
        return pref.getString(KEY_PINCODE,"");
    }

    public void setPincode(String pincode){
        editor.putString(KEY_PINCODE, pincode);
        editor.commit();
    }

    public boolean isAddres (){

        DeliveryAddressModel deliveryAddressModel = new DeliveryAddressModel();

        String name = pref.getString(KEY_NAME,"");

        if (name.equals("") || name.isEmpty()){
            return false;
        } else {
            return true;
        }
    }

    public void setUserNotificationTocken(String tocken) {
        editor.putString(KEY_NOTIFICATION_KEY, tocken);
        editor.commit();
    }

    public String getUserNotificationTocken() {
        return pref.getString(KEY_NOTIFICATION_KEY,"");
    }

    public void setLogOut( ) {
        editor.clear();
        editor.commit();
        setFirstLaunch(false);
        setFirstLogin(false);
    }

    public void saveUserToken(String token){
        editor.putString(KEY_USER_TOKEN, token);
        editor.commit();
    }

    public String getUserToken() {
        return pref.getString(KEY_USER_TOKEN,"");
    }

    public void saveUserName(String name) {
        editor.putString(KEY_USER_NAME,name);
        editor.commit();
    }

    public String getUserName() {
        return pref.getString(KEY_USER_NAME,"");
    }

    public void saveUserEmail(String mail) {
        editor.putString(KEY_USER_MAIL,mail);
        editor.commit();
    }

    public String getUserMail() {
        return pref.getString(KEY_USER_MAIL,"");
    }

    public void saveUserUniqueId(String uniqueId) {
        editor.putString(KEY_USER_UNIQUE_ID,uniqueId);
        editor.commit();
    }

    public String getUserUniqueId() {
        return pref.getString(KEY_USER_UNIQUE_ID,"");
    }

    public void saveCartId(String mail) {
        editor.putString(KEY_USER_CART_ID,mail);
        editor.commit();
    }

    public String getCartId() {
        return pref.getString(KEY_USER_CART_ID,"");
    }

}