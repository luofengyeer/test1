package com.cmcc.internalcontact.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.util.LongSparseArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmcc.internalcontact.R;
import com.cmcc.internalcontact.base.BaseActivity;
import com.cmcc.internalcontact.model.MainInfoBean;
import com.cmcc.internalcontact.model.db.DepartModel;
import com.cmcc.internalcontact.usecase.LoadContactList;
import com.cmcc.internalcontact.utils.ArraysUtils;
import com.cmcc.internalcontact.utils.OnItemClickListener;
import com.cmcc.internalcontact.utils.view.CommonToolBar;
import com.cmcc.internalcontact.utils.view.FlexibleDividerDecoration;
import com.cmcc.internalcontact.utils.view.HorizontalDividerItemDecoration;
import com.cmcc.internalcontact.utils.view.OnToolBarButtonClickListener;
import com.cmcc.internalcontact.utils.view.ToolBarButtonType;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity implements OnItemClickListener<MainInfoBean> {

    @BindView(R.id.toolbar_main)
    CommonToolBar toolbarMain;
    @BindView(R.id.tab_main)
    TabLayout tabMain;
    @BindView(R.id.main_list)
    RecyclerView contactRecyclerView;
    private MainAdapter adapter;
    private LongSparseArray<DepartModel> departPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        departPath = new LongSparseArray<>();
        initView();
        initTab();
        loadDepartment(0);
        toolbarMain.setBarButtonClickListener(new OnToolBarButtonClickListener() {
            @Override
            public void onClick(View v, ToolBarButtonType type) {
                switch (type) {
                    case LEFT_FIRST_BUTTON:
                        if (departPath == null || departPath.size() == 0) {
                            back2Root();
                            return;
                        }
                        int index = departPath.size() - 1;
                        if (index < 0 || index > departPath.size()) {
                            back2Root();
                            return;
                        }
                        DepartModel departModel = departPath.valueAt(index);
                        loadDepartment(departModel.getId());
                        break;
                }
            }
        });
    }

    private void back2Root() {
        toolbarMain.setButtonVisibility(ToolBarButtonType.LEFT_FIRST_BUTTON, View.GONE);
        loadDepartment(0);
    }
    private void initView() {
        adapter = new MainAdapter(this);
        adapter.setOnItemClickListener(this);
        contactRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactRecyclerView.setAdapter(adapter);
        contactRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .sizeResId(R.dimen.common_widget_divider_height).showLastDivider()
                .sizeProvider(new FlexibleDividerDecoration.SizeProvider() {
                    @Override
                    public int dividerSize(int position, RecyclerView parent) {
                        return getResources().getDimensionPixelOffset(R.dimen.common_widget_divider_height);
                    }
                }).colorProvider(new FlexibleDividerDecoration.ColorProvider() {
                    @Override
                    public int dividerColor(int position, RecyclerView parent) {
                        return getColor(R.color.common_divider_color);
                    }
                })
                .build());
    }

    @OnClick(R.id.view_main_search_lay)
    public void jump2Search() {

    }

    private void loadContact(long departId) {
        new LoadContactList().loadPersons(departId).subscribe(new Observer<List<MainInfoBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<MainInfoBean> mainInfoBeans) {
                adapter.setDataList(mainInfoBeans);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void loadDepartment(long departId) {
        new LoadContactList().loadDepartData(departId).subscribe(new Observer<List<MainInfoBean>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<MainInfoBean> mainInfoBeans) {
                if (ArraysUtils.isListEmpty(mainInfoBeans)) {
                    loadContact(departId);
                    return;
                }
                adapter.setDataList(mainInfoBeans);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
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

    @Override
    public void onItemClick(View view, MainInfoBean data, int position) {
        if (data == null) {
            return;
        }
        switch (data.getType()) {
            case MainInfoBean.TYPE_DEPART:
                DepartModel data1 = (DepartModel) data.getData();
                departPath.append(data1.getId(), data1);
                break;
            case MainInfoBean.TYPE_PERSON:
                break;
        }
    }
}
