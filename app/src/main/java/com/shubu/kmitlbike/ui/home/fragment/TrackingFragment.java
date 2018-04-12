package com.shubu.kmitlbike.ui.home.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shubu.kmitlbike.R;
import com.shubu.kmitlbike.ui.base.BaseFragment;
import com.shubu.kmitlbike.ui.common.CONSTANTS;
import com.shubu.kmitlbike.ui.home.fragment.interfaces.BaseBottomSheetFragment;
import com.shubu.kmitlbike.ui.home.fragment.interfaces.ReturnListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class TrackingFragment extends BaseBottomSheetFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String BIKE_NAME = "bike-name";
    private static final String DURATION = "duration";
    private static final String TIMER_UNIT = " min";
    private static final String FINAL_TIME_TEXT = "Please return the bike within ";
    private static final int MIN_TO_MILLI = 60000;

    // TODO: Rename and change types of parameters
    private String bikeName;
    private int bikeDuration;
    private ReturnListener returnListener;
    private CountDownTimer timer;

    @BindView(R.id.TrackingTimerText) TextView timeRemainingText;
    @BindView(R.id.TrackingFinalTimeText) TextView finalTimeText;
    @BindView(R.id.TrackingBikeImage) ImageView bikeImage;


    public TrackingFragment() { }

    // TODO: Rename and change types and number of parameters
    public static TrackingFragment newInstance(String bikeName, int duration) {
        TrackingFragment fragment = new TrackingFragment();
        Bundle args = new Bundle();
        args.putString(BIKE_NAME, bikeName);
        args.putInt(DURATION, duration);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bikeName = getArguments().getString(BIKE_NAME);
            bikeDuration = getArguments().getInt(DURATION);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ReturnListener) {
            returnListener = (ReturnListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    protected void setRideOnClick() {
        bListener.onToggle();
    }

    @Override
    protected void setFooterButton() {
        footerButton.setText("RETURN");
        footerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnListener.onReturn();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Timber.e("hehe");
        View view = inflater.inflate(R.layout.fragment_tracking, container, false);
        ButterKnife.bind(this, view);
        this.setFooterButton();
        this.setRideOnClick();
        this.bikeImage.setImageResource( this.bikeName.equals(CONSTANTS.GIANT_ESCAPE)? R.drawable.giant_escape : R.drawable.la_green);
        this.timeRemainingText.setText(this.bikeDuration + TIMER_UNIT);
        this.finalTimeText.setText(this.getFinalDuration());
        this.initiateTimer();


        return view;
    }

    private void initiateTimer(){
        new CountDownTimer(this.bikeDuration * MIN_TO_MILLI, MIN_TO_MILLI){
            @Override
            public void onTick(long millisUntilFinished) {
                Timber.e("tick");
                timeRemainingText.setText(Long.toString(millisUntilFinished / MIN_TO_MILLI) + TIMER_UNIT);
            }

            @Override
            public void onFinish() {
                timeRemainingText.setText("Overdued");
                finalTimeText.setText("Return this bike asap");
            }
        }.start();
    }

    private String getFinalDuration(){
        //this code is using 1 hour session
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.HOUR_OF_DAY, this.bikeDuration / 60);
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm");
        return formatter.format(cal.getTime());
    }


}
