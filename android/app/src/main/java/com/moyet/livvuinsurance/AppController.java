package com.moyet.livvuinsurance;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class AppController {

    private static final String TAG = "AppController";
    private static Context ctx;
    private RequestQueue mRequestQueue;
    private static AppController mInstance;

   private AppController(Context ctx){
       AppController.ctx =ctx;
       mRequestQueue = getRequestQueue();

   }

    public static synchronized AppController getInstance(Context context) {

        if(mInstance == null){
            mInstance = new AppController(context);
        }
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }

        return mRequestQueue;
    }

   /* public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        Log.i(TAG,"lapa ke okokuqala maybe nyangena");
        Log.i(TAG,"string is " + req);
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }*/

    public <T> void addToRequestQueue(Request<T> request) {

        request.setTag(TAG);
        getRequestQueue().add(request);
    }


}
