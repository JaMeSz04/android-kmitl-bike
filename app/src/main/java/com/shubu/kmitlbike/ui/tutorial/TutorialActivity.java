package com.shubu.kmitlbike.ui.tutorial;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;
import com.orhanobut.hawk.Hawk;
import com.shubu.kmitlbike.R;
import com.shubu.kmitlbike.ui.base.BaseActivity;
import com.shubu.kmitlbike.ui.home.HomeActivity;
import com.shubu.kmitlbike.ui.login.LoginActivity;
import com.shubu.kmitlbike.ui.login.LoginPresenter;
import com.shubu.kmitlbike.ui.splash.SplashActivity;

import javax.inject.Inject;

import timber.log.Timber;

public class TutorialActivity extends AppIntro{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Instead of fragments, you can also use our default slide.
        // Just create a `SliderPage` and provide title, description, background and image.
        // AppIntro will do the rest.

        //slide1
        SliderPage sliderPage1 = new SliderPage();
        sliderPage1.setTitle("Grab your KMITL Bike");
        sliderPage1.setDescription("Press the RIDE button at the bottom of the menu to start your journey.\n" +
                "Press the RETURN button to return the bike.");
        sliderPage1.setImageDrawable(R.drawable.tutorial_borrow);
        sliderPage1.setBgColor(Color.parseColor("#90BC4C"));
        addSlide(AppIntroFragment.newInstance(sliderPage1));

        //slide2
        SliderPage sliderPage2 = new SliderPage();
        sliderPage2.setTitle("Your help and data");
        sliderPage2.setDescription("Additional features can be found in the top left menu by simply tap to expand.\n" +
                "You can contact us via contact admin or report icon");
        sliderPage2.setImageDrawable(R.drawable.tutorial_menu);
        sliderPage2.setBgColor(Color.parseColor("#90BC4C"));
        addSlide(AppIntroFragment.newInstance(sliderPage2));

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        //super.onSkipPressed(currentFragment);
        Hawk.put("firstTime","no");
        redirect("splash");
        return;

        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        //super.onDonePressed(currentFragment);
        Hawk.put("firstTime","no");
        redirect("splash");
        return;

        // Do something when users tap on Done button.
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        //super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }


    public void redirect(String where) {
        Timber.e("redirecting");
        Intent intent;
        switch (where){
            case "splash" :
                intent = new Intent(this,SplashActivity.class);
                startActivity(intent);
                break;

        }
    }
}
