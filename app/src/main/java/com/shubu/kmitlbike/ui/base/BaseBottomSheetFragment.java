package com.shubu.kmitlbike.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shubu.kmitlbike.R;
import com.shubu.kmitlbike.ui.base.BaseFragment;
import com.shubu.kmitlbike.ui.home.fragment.interfaces.BottomSheetListener;
import com.shubu.kmitlbike.ui.home.fragment.interfaces.ScannerListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public abstract class BaseBottomSheetFragment extends BaseFragment {
    @BindView(R.id.Ride_Button)
    public LinearLayout rideButton;

    @BindView(R.id.ButtomSheetControlActionButton)
    public TextView rideButtonText;

    @BindView(R.id.bottom_sheet_footer_button)
    public Button footerButton;

    @BindView(R.id.BottomSheetControlReloadButton)
    ImageButton reloadButton;
    @BindView(R.id.BottomSheetControlGetLocation)
    ImageButton getLocationButton;

    public BottomSheetListener bListener;

    protected abstract void setRideOnClick();

    protected abstract void setFooterButton();

    protected void setbListener(){
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bListener.onRefreshBike();
            }
        });
        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bListener.onRefreshLocation();
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BottomSheetListener) {
            bListener = (BottomSheetListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        bListener = null;
    }
}
