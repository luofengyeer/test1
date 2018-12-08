package com.cmcc.internalcontact.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.util.LongSparseArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmcc.internalcontact.R;
import com.cmcc.internalcontact.base.BaseActivity;
import com.cmcc.internalcontact.base.MyObserver;
import com.cmcc.internalcontact.model.MainInfoBean;
import com.cmcc.internalcontact.model.db.DepartModel;
import com.cmcc.internalcontact.model.db.PersonModel;
import com.cmcc.internalcontact.usecase.LoadContactList;
import com.cmcc.internalcontact.utils.ArraysUtils;
import com.cmcc.internalcontact.utils.OnItemClickListener;
import com.cmcc.internalcontact.utils.view.CommonToolBar;
import com.cmcc.internalcontact.utils.view.HorizontalDividerItemDecoration;
import com.cmcc.internalcontact.utils.view.OnToolBarButtonClickListener;
import com.cmcc.internalcontact.utils.view.ToolBarButtonType;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements OnItemClickListener<MainInfoBean> {

    @BindView(R.id.toolbar_main)
    CommonToolBar toolbarMain;
    @BindView(R.id.tab_main)
    TabLayout tabMain;
    @BindView(R.id.main_list)
    RecyclerView contactRecyclerView;
    @BindView(R.id.tv_duty_call)
    TextView tvDutyCall;
    @BindView(R.id.tv_fax)
    TextView tvFax;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    private MainAdapter adapter;
    private LongSparseArray<DepartModel> departPath;
    @BindView(R.id.iv_path_lay)
    ImageView pathIcon;
    @BindView(R.id.path_lay)
    LinearLayout pathLay;
    @BindView(R.id.tv_person_count)
    TextView tvCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        departPath = new LongSparseArray<>();
        initView();
        initTab();
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
                        loadDepartment(departModel);
                        departPath.removeAt(index);
                        break;
                }
            }
        });
    }

    private void back2Root() {
        toolbarMain.setButtonVisibility(ToolBarButtonType.LEFT_FIRST_BUTTON, View.GONE);
        pathIcon.setVisibility(View.GONE);
        pathLay.setVisibility(View.GONE);
        loadDepartment(null);
    }

    private void initView() {
        adapter = new MainAdapter(this);
        adapter.setOnItemClickListener(this);
        contactRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactRecyclerView.setAdapter(adapter);
        contactRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .sizeResId(R.dimen.common_widget_divider_height).showLastDivider()
                .color(getColor(R.color.common_divider_color))
                .build());
        loadContactCount();
    }

    @OnClick(R.id.view_main_search_lay)
    public void jump2Search() {

    }

    private void loadContactCount() {
        new LoadContactList().getPersonsCount().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObserver<Long>() {
            @Override
            public void onNext(Long aLong) {
                tvCount.setText(getString(R.string.person_count, aLong));
            }
        });
    }

    private void loadContact(DepartModel departModel) {
        if (departModel == null) {
            return;
        }
        new LoadContactList().loadPersons(departModel.getId()).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new MyObserver<List<MainInfoBean>>() {

            @Override
            public void onNext(List<MainInfoBean> mainInfoBeans) {
                adapter.setDataList(mainInfoBeans);
            }
        });
    }

    private void loadDepartment(DepartModel parentModel) {
        pathIcon.setVisibility(View.VISIBLE);
        long departId = parentModel == null ? 0 : parentModel.getId();
        new LoadContactList().loadDepartData(departId).subscribe(new MyObserver<List<MainInfoBean>>() {
            @Override
            public void onNext(List<MainInfoBean> mainInfoBeans) {
                if (ArraysUtils.isListEmpty(mainInfoBeans)) {
                    loadContact(parentModel);
                    return;
                }
                adapter.setDataList(mainInfoBeans);
            }

        });
    }

    private void initTab() {
        tabMain.addTab(buildTab("通讯录", R.drawable.common_widget_home_menu_contact_selecter), false);
        tabMain.addTab(buildTab("搜索", R.drawable.ic_search), false);
        tabMain.addTab(buildTab("我的", R.drawable.common_widget_home_menu_mine_selecter), false);
        tabMain.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                new LoadContactList().saveDepartments(null);
                setMenuRes(tab, true);
                loadDepartment(null);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setMenuRes(tab, false);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabMain.getTabAt(0).select();
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
                pathLay.setVisibility(View.VISIBLE);
                pathLay.addView(buildPathLayout(data1.getDeptName()), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                loadDepartment(data1);
                break;
            case MainInfoBean.TYPE_PERSON:
                UserDetailDialogFragment.show(MainActivity.this, (PersonModel) data.getData());
                break;
        }
    }

    private View buildPathLayout(String name) {
        View inflate = getLayoutInflater().inflate(R.layout.layout_contact_path_view, null);
        TextView viewById = inflate.findViewById(R.id.tv_path2);
        viewById.setText(name);
        return inflate;
    }
}
