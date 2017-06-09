package com.fxdsse.SEhomework.Util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.fxdsse.SEhomework.WebViewActivity;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.List;

public class AeolosOnBannerClickListener implements OnBannerClickListener {
    private Context context;
    private List<String> url_lst = new ArrayList<>();

    public AeolosOnBannerClickListener(Context context, List<String> url_lst) {
        this.context = context;
        this.url_lst = url_lst;
    }

    @Override
    public void OnBannerClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString(WebViewActivity.URL, url_lst.get(position - 1));
        context.startActivity((new Intent(context, WebViewActivity.class)).putExtras(bundle));
    }
}
