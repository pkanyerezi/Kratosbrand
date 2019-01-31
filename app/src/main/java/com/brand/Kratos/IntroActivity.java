package com.brand.Kratos;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.MessageButtonBehaviour;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import agency.tango.materialintroscreen.animations.IViewTranslation;

public class IntroActivity extends MaterialIntroActivity {
    SharedPreferences pref;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableLastSlideAlphaExitTransition(true);

        getBackButtonTranslationWrapper()
                .setEnterTranslation(new IViewTranslation() {
                    @Override
                    public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
                        view.setAlpha(percentage);
                    }
                });

        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.first_slide_background)
                        .buttonsColor(R.color.first_slide_buttons)
                        .image(R.drawable.one)
                        .build());

       /* addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.first_slide_background)
                        .buttonsColor(R.color.first_slide_buttons)
                        .image(R.drawable.one)
                        .title("Organize your time with us")
                        .description("Would you try?")
                        .build());*/
        /*
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //showMessage("We provide solutions to make you love your work");
                    }
                }, "Work with love"));*/

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.second_slide_background)
                .buttonsColor(R.color.second_slide_buttons)
                .image(R.drawable.two)
                .build());

        //addSlide(new CustomSlide());

        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.third_slide_background)
                        .buttonsColor(R.color.third_slide_buttons)
                         .image(R.drawable.three)
                        .build());
               /* new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // showMessage("Try us!");
                    }
                }, "Tools"));*/

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.fourth_slide_background)
                .buttonsColor(R.color.fourth_slide_buttons)
                .image(R.drawable.four)
                .build());
    }

    @Override
    public void onFinish() {
        super.onFinish();
      //  Toast.makeText(this, "Try this library in your project! :)", Toast.LENGTH_SHORT).show();
        pref = getApplicationContext().getSharedPreferences("Settings_details", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("logged_in", false);
                editor.putBoolean("intro_screen", true);
                editor.commit();
        Intent intent = new Intent(IntroActivity.this, Login.class);
        // Intent intent = new Intent(SplashScreen.this, LoginTrue.class);
        IntroActivity.this.startActivity(intent);
        finish();
    }
}
