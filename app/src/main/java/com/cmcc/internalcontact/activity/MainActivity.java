package com.cmcc.internalcontact.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmcc.internalcontact.R;
import com.cmcc.internalcontact.base.BaseActivity;
import com.cmcc.internalcontact.utils.view.CommonToolBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar_main)
    CommonToolBar toolbarMain;
    @BindView(R.id.tab_main)
    TabLayout tabMain;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initTab();

    }

    @OnClick(R.id.view_main_search_lay)
    public void jump2Search() {

    }

    private void initContact() {

    }

    private void initTab() {
        tabMain.addTab(buildTab("通讯录", R.drawable.common_widget_home_menu_contact_selecter));
        tabMain.addTab(buildTab("搜索", R.drawable.ic_search));
        tabMain.addTab(buildTab("我的", R.drawable.common_widget_home_menu_mine_selecter));
        tabMain.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setMenuRes(tab, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setMenuRes(tab, false);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @NonNull
    private TabLayout.Tab buildTab(String name, int iconId) {
        TabLayout.Tab tab = tabMain.newTab();
        tab.setCustomView(R.layout.item_tab_main);
        ImageView tabIcon = tab.getCustomView().findViewById(R.id.tv_main_tab_icon);
        TextView tabName = tab.getCustomView().findViewById(R.id.tv_main_tab_name);
        tabName.setText(name);
        tabIcon.setImageResource(iconId);
        tab.setTag(iconId);
        return tab;
    }

    /***
     * 设置Menu图标文字等资源
     * @param tab
     * @param isSelected 是否选中
     */
    private void setMenuRes(TabLayout.Tab tab, boolean isSelected) {
        View tabItem = tab.getCustomView();
        ImageView tabIcon = tabItem.findViewById(R.id.tv_main_tab_icon);
        TextView tabName = tabItem.findViewById(R.id.tv_main_tab_name);
        tabIcon.setImageTintList(isSelected ? getResources().getColorStateList(R.color.common_theme_color) : getResources().getColorStateList(R.color.common_greyColor));
        tabName.setTextColor(isSelected ? getResources().getColorStateList(R.color.common_theme_color) : getResources().getColorStateList(R.color.common_greyColor));
    }
}
