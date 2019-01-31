package com.brand.Kratos;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.LinearLayout;

@CoordinatorLayout.DefaultBehavior(CardViewAwareScrollingViewBehavior.class)
public class CustomLinearScroll extends LinearLayout {

    public CustomLinearScroll(Context context) {
        super(context);
    }

    public CustomLinearScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLinearScroll(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}