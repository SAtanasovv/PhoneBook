package com.satanasov.phonebook.View;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.satanasov.phonebook.R;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }
}