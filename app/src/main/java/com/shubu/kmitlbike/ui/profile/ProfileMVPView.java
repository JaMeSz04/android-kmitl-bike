package com.shubu.kmitlbike.ui.profile;

import com.shubu.kmitlbike.data.model.History;
import com.shubu.kmitlbike.data.model.ProfileHistory;
import com.shubu.kmitlbike.ui.base.MvpView;

import java.util.List;

public interface ProfileMVPView extends MvpView {
    void onHistoryListLoad(List<ProfileHistory> histories);
    void onHistoryLoad(History history);
}
