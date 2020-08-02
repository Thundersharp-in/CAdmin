package com.thundersharp.cadmin.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.thundersharp.cadmin.R;

public class PdfLoader extends AppCompatActivity {

    WebView webView;
    ProgressBar progressBar;
    String url;
    Vibrator vibrator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_loader);

        webView = findViewById(R.id.webview);

        url = getIntent().getStringExtra("url");
        progressBar = findViewById(R.id.prooooo);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        //---you need this to prevent the webview from
        // launching another browser when a url
        // redirection occurs---
        webView.setWebViewClient(new Callback());
        Uri uri = Uri.parse(url);

        webView.loadUrl(uri.toString());

    }

    private class Callback extends WebViewClient {
        boolean loadingFinished = true;
        boolean redirect = false;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
            if (!loadingFinished) {
                redirect = true;
            }

            loadingFinished = false;
            view.loadUrl(urlNewString);
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap facIcon) {
            loadingFinished = false;
            //SHOW LOADING IF IT ISNT ALREADY VISIBLE
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if(!redirect){
                loadingFinished = true;
            }

            if(loadingFinished && !redirect){
                //HIDE LOADING IT HAS FINISHED
                progressBar.setVisibility(View.GONE);
              /*  if (settingsData.getvibratePdf() && settingsData.getHaptic()){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.EFFECT_HEAVY_CLICK));
                    } else {
                        //deprecated in API 26
                        vibrator.vibrate(200);
                    }
                }*/


            } else{
                redirect = false;
            }

        }
    }
}