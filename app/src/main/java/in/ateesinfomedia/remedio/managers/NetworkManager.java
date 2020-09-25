package in.ateesinfomedia.remedio.managers;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import in.ateesinfomedia.remedio.components.MainApplication;
import in.ateesinfomedia.remedio.interfaces.NetworkCallback;
import in.ateesinfomedia.remedio.components.VolleyMultipartRequest;

/**
 * Created by Android-1 on 9/6/2017.
 */

public class NetworkManager {
    public static final String TAG = NetworkManager.class.getSimpleName();

    public static final int SUCCESS = 0;
    public static final int EXCEPTION = -1;
    public static final int ERROR = -2;
    private static final int MY_SOCKET_TIMEOUT_MS = 20 * 1000;

    private Context mContext;

    public NetworkManager(Context context){
        mContext = context;
    }
    public void doGet(final String getParam , final String url, final String requestTag, final int requestId, final NetworkCallback responseCallback){

        String mUrl = url;

        if (getParam != null){
            mUrl = url+"?"+getParam;

        }

        Log.d(TAG, "doGetUrl: "+mUrl);

        StringRequest postRequest = new StringRequest(Request.Method.GET, mUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                responseCallback.onResponse(SUCCESS,response,requestId);
                Log.d(TAG, "doGetResponse: "+response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                responseCallback.onResponse(ERROR,error.getMessage(),requestId);
                Log.d(TAG, "doGetError: "+error.getMessage());
            }
        });

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MainApplication.getInstance().addToRequestQueue(postRequest, requestTag);


    }

    public void doPost(final Map<String,String> postParam , final String url, final String requestTag, final int requestId, final NetworkCallback responseCallback){

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                responseCallback.onResponse(SUCCESS,response, requestId);
           //     Log.d(TAG, "doPostResponse: "+response);
                Log.v("??_n_callback", "doPostResponse: "+url+" , "+requestTag+" , "+requestId+" , "+response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                responseCallback.onResponse(ERROR,error.getMessage(),requestId);
                Log.d(TAG, "doPostError: "+error.getMessage());

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> stringMap = postParam;
                return stringMap;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        postRequest.setShouldCache(false);
        MainApplication.getInstance().addToRequestQueue(postRequest, requestTag);
    }

    public void doJSONPost(final JSONObject jsonObject , final String url, final String requestTag, final int requestId, final NetworkCallback responseCallback){

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
//
                        responseCallback.onJsonResponse(SUCCESS,response.toString(),requestId);
                        Log.d("doJSONPostResponse",response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        responseCallback.onJsonResponse(ERROR,error.toString(),requestId);
                        Log.d(TAG, "doJSONPostError: "+error.getMessage());
                    }
                });

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MainApplication.getInstance().addToRequestQueue(jsObjRequest, requestTag);


    }

    public void doPostMultiData(final Map<String, String> stringParam, final Map<String, VolleyMultipartRequest.DataPart> dataParam , final String url, final String requestTag, final int requestId, final NetworkCallback responseCallback){

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                responseCallback.onJsonResponse(SUCCESS,resultResponse,requestId);
                // parse success output
            }
        }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                responseCallback.onJsonResponse(ERROR,error.toString(),requestId);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("api_token", "gh659gjhvdyudo973823tt9gvjf7i6ric75r76");
//                params.put("name", "Angga");
//                params.put("location", "Indonesia");
//                params.put("about", "UI/UX Designer");
//                params.put("contact", "angga@email.com");
                return stringParam;
            }

            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
//                Map<String, DataPart> params = new HashMap<>();
//                // file name could found file base or direct access from real path
//                // for now just get bitmap data from ImageView
//                params.put("avatar", new DataPart("file_avatar.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), mAvatarImage.getDrawable()), "image/jpeg"));
//                params.put("cover", new DataPart("file_cover.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), mCoverImage.getDrawable()), "image/jpeg"));

                return dataParam;
            }
        };

        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MainApplication.getInstance().addToRequestQueue(multipartRequest, requestTag);


    }

    public byte[] getBytearrayFromBitmap(Bitmap bitmap){
//        byte[] buffer = new byte[0];
        if (bitmap !=null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            byte[] buffer = out.toByteArray();
            return buffer;
        }
        return null;
    }
}
