package com.example.laborator9;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ImagesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        ListView lvPhones = findViewById(R.id.lvPhones);
        List<PhoneImage> phoneImages = new ArrayList<>();

        phoneImages.add(new PhoneImage(
                "https://images.samsung.com/is/image/samsung/assets/ro/smartphones/galaxy-s24-ultra/images/galaxy-s24-ultra-highlights-kv.jpg?imbypass=true?q=80&w=500&auto=format&fit=crop",
                "Samsung Galaxy S24 Ultra - Design Premium",
                "https://www.samsung.com/ro/smartphones/galaxy-s24-ultra/"
        ));
        phoneImages.add(new PhoneImage(
                "https://istyle.ro/cdn/shop/files/IMG-10892326_0b4957bb-8d7f-48c2-a57f-20b13eed02ec.jpg?v=1757943310&width=1680?q=80&w=500&auto=format&fit=crop",
                "iPhone 15 Pro - Titanium Blue",
                "https://www.apple.com/ro/iphone-15-pro/"
        ));
        phoneImages.add(new PhoneImage(
                "https://lh3.googleusercontent.com/3GqsER-zuAbB7n6S6Xm3I_7-lEO-Ba3EY9IA7UpBdOnkT7p1K4FVk4P485u3MP7sQ3Mk6HfJSQxkF_rszhfFKAbvwaicasEhsA=s6000-w6000-e365-rw-v0-nu?q=80&w=500&auto=format&fit=crop",
                "Google Pixel 8 Pro - AI Camera",
                "https://store.google.com/product/pixel_8_pro"
        ));
        phoneImages.add(new PhoneImage(
                "https://i02.appmifile.com/mi-com-product/fly-birds/new-xiaomi-14-ultra/PC/pc-14ultra-option-lens.png?f=webp?q=80&w=500&auto=format&fit=crop",
                "Xiaomi 14 Ultra - Leica Optics",
                "https://www.mi.com/ro/product/xiaomi-14-ultra/"
        ));
        phoneImages.add(new PhoneImage(
                "https://www.oneplus.com/content/dam/oasis/page/waffle-en/images-design-phone1-1.png.avif?q=80&w=500&auto=format&fit=crop",
                "OnePlus 12 - Powerhouse",
                "https://www.oneplus.com/ro/12"
        ));

        PhoneAdapter adapter = new PhoneAdapter(this, phoneImages);
        lvPhones.setAdapter(adapter);

        lvPhones.setOnItemClickListener((parent, view, position, id) -> {
            PhoneImage selected = phoneImages.get(position);
            Intent intent = new Intent(ImagesActivity.this, WebViewActivity.class);
            intent.putExtra("url", selected.getWebUrl());
            startActivity(intent);
        });
    }
}
