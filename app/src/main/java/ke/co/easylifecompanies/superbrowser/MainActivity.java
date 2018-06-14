 package ke.co.easylifecompanies.superbrowser;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

 public class MainActivity extends AppCompatActivity {

    WebView webView;
    SwipeRefreshLayout swipe;

    private EditText inputSearchField;
    private Button btnGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(R.layout.actionbar_view);

        inputSearchField = actionBar.getCustomView().findViewById(R.id.input_search);

        swipe = (SwipeRefreshLayout)findViewById(R.id.swipe);
        btnGo = findViewById(R.id.btn_go);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                WebAction();
            }
        });

        WebAction();

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url = inputSearchField.getText().toString();

                if (!TextUtils.isEmpty(url)){

                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.getSettings().setAppCacheEnabled(true);

                    if (Patterns.WEB_URL.matcher(url).matches()){

                        webView.loadUrl("https://"+url);

                    }else {

                        try {
                            String escapedQuery = URLEncoder.encode(url, "UTF-8");
                            webView.loadUrl("https://www.google.com/#q="+escapedQuery);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }


                    swipe.setRefreshing(true);
                    webView.setWebViewClient(new WebViewClient(){

                        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                            webView.loadUrl("file:///android_assets/error.html");

                        }

                        public void onPageFinished(WebView view, String url) {
                            // do your stuff here
                            swipe.setRefreshing(false);
                        }

                    });

                }

            }
        });

    }


    public void WebAction(){

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.loadUrl("https://www.google.com/");
        swipe.setRefreshing(true);
        webView.setWebViewClient(new WebViewClient(){

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                webView.loadUrl("file:///android_assets/error.html");

            }

            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                swipe.setRefreshing(false);
            }

        });

    }


    @Override
    public void onBackPressed(){

        if (webView.canGoBack()){
            webView.goBack();
        }else {
            finish();
        }
    }
}