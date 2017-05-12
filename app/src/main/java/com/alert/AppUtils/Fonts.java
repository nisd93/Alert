package com.alert.AppUtils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Android on 6/15/2015.
 */


public class Fonts {

    public  static  Typeface setUtahCondensed(Context context){
        Typeface  tf = Typeface.createFromAsset(context.getAssets(), "fonts/Nunito-Regular.ttf");

        return tf;
    }

    public  static  Typeface setUtahCondensedBold(Context context){
        Typeface  tf = Typeface.createFromAsset(context.getAssets(), "fonts/Nunito-Bold.ttf");

        return tf;
    }

}
