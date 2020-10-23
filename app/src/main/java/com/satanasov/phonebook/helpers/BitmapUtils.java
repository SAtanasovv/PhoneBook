package com.satanasov.phonebook.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class BitmapUtils {

    public BitmapUtils() {
    }

    private byte[] convertToBytes(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0,stream);
        return stream.toByteArray();
    }

    private Bitmap getImageFromBytes(byte[] image){
        return BitmapFactory.decodeByteArray(image,0,image.length);
    }
}
