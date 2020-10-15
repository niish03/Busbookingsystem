package com.shreebrahmanitravels.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class slideradapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public slideradapter(Context context){
        this.context=context;
    }

    public int[] slideImages = {
            R.drawable.oneclickbooking,
            R.drawable.virtualidcard,
            R.drawable.onboardbookinh
    };

    public String[] slideHeadings ={
            "One Click Booking",
            "Virtual ID card",
            "On board booking"
    };

    public String[] slideDescriptions ={
            "You don’t have to rush and go manually to book seat because you already made your special arrangement by own on your fingertips by M-booking.",
            "Reduces Physical identity card, also now you dont have to rush home in case you forgot your Physical ID card , It cover-ups in our Virtual identity card",
            "On board booking is an special feature for guest (Non-Commuters) , Specially designed for Hostel people who travel on weekend."
    };


    @Override
    public int getCount() {
        return slideHeadings.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (ConstraintLayout) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.iv_image_icon);
        TextView slideHeading = (TextView) view.findViewById(R.id.tv_heading);
        TextView slideDescription = (TextView) view.findViewById(R.id.tv_description);

        slideImageView.setImageResource(slideImages[position]);
        slideHeading.setText(slideHeadings[position]);
        slideDescription.setText(slideDescriptions[position]);

        container.addView(view);

        return view;

    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);  //todo: RelativeLayout??
    }
}
