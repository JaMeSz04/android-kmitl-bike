package com.shubu.kmitlbike.ui.main;

import com.shubu.kmitlbike.ui.base.MvpView;

import java.util.List;

public interface MainMvpView extends MvpView {

    void showPokemon(List<String> pokemon);

    void showProgress(boolean show);

    void showError();

}