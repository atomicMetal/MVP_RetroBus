package com.mobotechnology.bipinpandey.retrofit_handdirty.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by android2 on 3/3/17.
 */

public class ExpertListResponse extends AbstractApiResponse {

    @SerializedName("info")
    @Expose
    ArrayList<ExpertInfo> expertInfo;

    public ArrayList<ExpertInfo> getExpertInfo() {
        return expertInfo;
    }

    public void setExpertInfo(ArrayList<ExpertInfo> expertInfo) {
        this.expertInfo = expertInfo;
    }
}
