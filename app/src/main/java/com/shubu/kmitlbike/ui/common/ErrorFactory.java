package com.shubu.kmitlbike.ui.common;

import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.shubu.kmitlbike.KMITLBikeApplication;

public class ErrorFactory {
    public static MaterialStyledDialog getErrorDialog(String message){
        return new MaterialStyledDialog.Builder(KMITLBikeApplication.getAppContext())
                .setTitle("Error")
                .setDescription(message).build();
    }

    public static MaterialStyledDialog getUnexpectedErrorDialog(){
        return new MaterialStyledDialog.Builder(KMITLBikeApplication.getAppContext())
                .setTitle("Error")
                .setDescription("Unexpected error occoured... try again").build();
    }
}
