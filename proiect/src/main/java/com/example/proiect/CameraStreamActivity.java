package com.example.proiect;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class CameraStreamActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_stream);

        String url = getIntent().getStringExtra("url");
        String nume = getIntent().getStringExtra("nume");

        setTitle("Camera: " + nume);

        webView = findViewById(R.id.webViewStream);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        String html = "<!DOCTYPE html><html><head>"
                + "<style>*{margin:0;padding:0;background:#000;} "
                + "iframe{width:100%;height:100vh;border:none;}</style>"
                + "</head><body>"
                + "<iframe src=\"" + url + "\" "
                + "allow=\"autoplay; encrypted-media\" allowfullscreen></iframe>"
                + "</body></html>";
        webView.loadDataWithBaseURL("https://www.youtube.com", html, "text/html", "utf-8", null);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
