package com.shubu.kmitlbike.ui.home.fragment.interfaces;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.shubu.kmitlbike.R;
import com.shubu.kmitlbike.ui.base.BaseFragment;
import com.shubu.kmitlbike.ui.home.fragment.interfaces.BottomSheetListener;
import com.shubu.kmitlbike.ui.home.fragment.interfaces.ScannerListener;

import butterknife.BindView;

public abstract class BaseBottomSheetFragment extends BaseFragment {
    @BindView(R.id.Ride_Button)
    public LinearLayout rideButton;

    @BindView(R.id.bottom_sheet_footer_button)
    public Button footerButton;

    public BottomSheetListener bListener;

    protected abstract void setRideOnClick();

    protected abstract void setFooterButton();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.setRideOnClick();
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
