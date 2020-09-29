package in.ateesinfomedia.relief.interfaces;

/**
 * Created by Android-1 on 3/29/2017.
 */

public interface NetworkCallback {
//    void onResponse(int status, String response);

    void onResponse(int status, String response, int requestId);
    void onJsonResponse(int status, String response, int requestId);
}
