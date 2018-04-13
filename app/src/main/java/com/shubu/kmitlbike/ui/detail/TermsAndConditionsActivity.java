package com.shubu.kmitlbike.ui.detail;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.shubu.kmitlbike.BuildConfig;
import com.shubu.kmitlbike.R;
import com.shubu.kmitlbike.ui.base.BaseActivity;
import javax.inject.Inject;

public class TermsAndConditionsActivity extends BaseActivity implements TermsAndConditionsMVPView {


    @Inject TermsAndConditionsPresenter presenter;

    private WebView webView;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_terms_and_conditions_acitivity);
        webView = (WebView) findViewById(R.id.InfoWebview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new CustomJS(this), "HtmlViewer");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:window.HtmlViewer.showHTML" +
                        "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
            }
        });
        webView.loadUrl(BuildConfig.BASE_URL + "api/v1/info/terms_conditions");
    }

    class CustomJS {

        private Context ctx;

        CustomJS(Context ctx) {
            this.ctx = ctx;
        }

        public void showHTML(String html) {
            new AlertDialog.Builder(ctx).setTitle("HTML").setMessage(html)
                    .setPositiveButton(android.R.string.ok, null).setCancelable(false).create().show();
        }

    }


}
