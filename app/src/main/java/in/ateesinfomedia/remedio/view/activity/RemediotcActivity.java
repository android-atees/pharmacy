package in.ateesinfomedia.remedio.view.activity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import im.delight.android.webview.AdvancedWebView;
import in.ateesinfomedia.remedio.R;

public class RemediotcActivity extends AppCompatActivity implements AdvancedWebView.Listener{

   private AdvancedWebView mWebView;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remediotc);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Terms And Condition");

        url="file:///android_asset/terms_and_conditions.htm";
        mWebView=findViewById(R.id.tcpage);
        mWebView.setListener(this, this);
        mWebView.loadUrl(url);
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(String url) {

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
