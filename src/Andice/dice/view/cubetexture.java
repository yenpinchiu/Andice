package Andice.dice.view;

import Andice.dice.view.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class cubetexture {
    Bitmap mBitmap1;   
    Bitmap mBitmap2;   
    Bitmap mBitmap3;   
    Bitmap mBitmap4;   
    Bitmap mBitmap5;   
    Bitmap mBitmap6; 
    cubetexture(Context cont){
       mBitmap1 = BitmapFactory.decodeResource(cont.getResources(), R.drawable.b1);   
       mBitmap2 = BitmapFactory.decodeResource(cont.getResources(), R.drawable.b2);   
       mBitmap3 = BitmapFactory.decodeResource(cont.getResources(), R.drawable.b3);   
       mBitmap4 = BitmapFactory.decodeResource(cont.getResources(), R.drawable.b4);   
       mBitmap5 = BitmapFactory.decodeResource(cont.getResources(), R.drawable.b5);   
       mBitmap6 = BitmapFactory.decodeResource(cont.getResources(), R.drawable.b6); 
    }
}
