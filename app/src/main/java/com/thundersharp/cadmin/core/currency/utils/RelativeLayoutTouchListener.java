package com.thundersharp.cadmin.core.currency.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;

import com.thundersharp.cadmin.R;
import com.thundersharp.cadmin.ui.activity.CurrencyConverter;


public class RelativeLayoutTouchListener implements View.OnTouchListener {

    static final String logTag = "ActivitySwipeDetector";
    private Activity activity;
    static final int MIN_DISTANCE = 100;// TODO change this runtime based on screen resolution. for 1920x1080 is to small the 100 distance
    private float downX, downY, upX, upY;

    // private MainActivity mMainActivity;

    public RelativeLayoutTouchListener(CurrencyConverter mainActivity) {
        activity = mainActivity;
    }

    public void onRightToLeftSwipe() {
        Log.i(logTag, "RightToLeftSwipe!");
        Log.i(logTag, "LeftToRightSwipe!");
        CurrencyConverter.relativeOne.setBackgroundResource(R.drawable.gradient_blue);
        CurrencyConverter.countryTo.setTextColor(Color.parseColor("#2A93D5"));
        CurrencyConverter.currency_to.setTextColor(Color.parseColor("#2A93D5"));
        CurrencyConverter.line.setBackgroundColor(Color.parseColor("#2A93D5"));

        ColorStateList csl = AppCompatResources.getColorStateList(CurrencyConverter.ctx, R.color.blue);
        Drawable drawableone = CurrencyConverter.ctx.getResources().getDrawable(R.drawable.ic_keyboard_arrow_down);
        DrawableCompat.setTintList(drawableone, csl);
        CurrencyConverter.arrowDown.setImageDrawable(drawableone);

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(CurrencyConverter.relativeOne.getWindowToken(), 0);

    }

    public void onLeftToRightSwipe() {
        CurrencyConverter.relativeOne.setBackgroundResource(R.drawable.gradient_red);
        CurrencyConverter.countryTo.setTextColor(Color.parseColor("#FA898A"));
        CurrencyConverter.currency_to.setTextColor(Color.parseColor("#FA898A"));
        CurrencyConverter.line.setBackgroundColor(Color.parseColor("#FA898A"));

        ColorStateList csl = AppCompatResources.getColorStateList(CurrencyConverter.ctx, R.color.peach);
        Drawable drawableone = CurrencyConverter.ctx.getResources().getDrawable(R.drawable.ic_keyboard_arrow_down);
        DrawableCompat.setTintList(drawableone, csl);
        CurrencyConverter.arrowDown.setImageDrawable(drawableone);

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(CurrencyConverter.relativeOne.getWindowToken(), 0);

        // activity.doSomething();
    }

//    public void onTopToBottomSwipe() {
//        Log.i(logTag, "onTopToBottomSwipe!");
//        Toast.makeText(activity, "onTopToBottomSwipe", Toast.LENGTH_SHORT).show();
//        // activity.doSomething();
//    }
//
//    public void onBottomToTopSwipe() {
//        Log.i(logTag, "onBottomToTopSwipe!");
//        Toast.makeText(activity, "onBottomToTopSwipe", Toast.LENGTH_SHORT).show();
//        // activity.doSomething();
//    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                return false;
            }
            case MotionEvent.ACTION_UP: {
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // swipe horizontal?
                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    // left or right
                    if (deltaX < 0) {
                        this.onLeftToRightSwipe();
                        return false;
                    }
                    if (deltaX > 0) {
                        this.onRightToLeftSwipe();
                        return false;
                    }
                } else {
                    Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long horizontally, need at least " + MIN_DISTANCE);
                    // return false; // We don't consume the event
                }

                // swipe vertical?
                if (Math.abs(deltaY) > MIN_DISTANCE) {
                    // top or down
                    if (deltaY < 0) {
//                        this.onTopToBottomSwipe();
                        return false;
                    }
                    if (deltaY > 0) {
//                        this.onBottomToTopSwipe();
                        return false;
                    }
                } else {
                    Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long vertically, need at least " + MIN_DISTANCE);
                    // return false; // We don't consume the event
                }

                return false; // no swipe horizontally and no swipe vertically
            }// case MotionEvent.ACTION_UP:
        }
        return false;
    }

}