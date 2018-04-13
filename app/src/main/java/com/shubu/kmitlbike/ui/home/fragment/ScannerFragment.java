package com.shubu.kmitlbike.ui.home.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.shubu.kmitlbike.R;
import com.shubu.kmitlbike.ui.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


public class ScannerFragment extends BaseFragment {


    @BindView(R.id.scanner_view) CodeScannerView scannerView;
    private CodeScanner mCodeScanner;

    public ScannerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ScannerFragment newInstance() {
        ScannerFragment fragment = new ScannerFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_scanner, container, false);
        ButterKnife.bind(this,root);
        Activity activity = getActivity();
        mCodeScanner = new CodeScanner(activity, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                Timber.i(result.getText());
                eventBus.getScannerCode().onNext(result);
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onResume(){
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause(){
        mCodeScanner.releaseResources();
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
