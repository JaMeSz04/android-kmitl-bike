package com.shubu.kmitlbike.ui.detail;

import com.shubu.kmitlbike.data.model.Pokemon;
import com.shubu.kmitlbike.data.model.Statistic;
import com.shubu.kmitlbike.ui.base.MvpView;

public interface DetailMvpView extends MvpView {

    void showPokemon(Pokemon pokemon);

    void showStat(Statistic statistic);

    void showProgress(boolean show);

    void showError();

}