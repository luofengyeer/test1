package com.cmcc.internalcontact.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.cmcc.internalcontact.R;
import com.cmcc.internalcontact.base.BaseActivity;
import com.cmcc.internalcontact.base.MyObserver;
import com.cmcc.internalcontact.model.MainInfoBean;
import com.cmcc.internalcontact.model.db.DepartModel;
import com.cmcc.internalcontact.model.db.PersonModel;
import com.cmcc.internalcontact.model.http.LoginResponseBean;
import com.cmcc.internalcontact.usecase.LoadContactList;
import com.cmcc.internalcontact.utils.ArraysUtils;
import com.cmcc.internalcontact.utils.OnItemClickListener;
import com.cmcc.internalcontact.utils.SharePreferencesUtils;
import com.cmcc.internalcontact.utils.Utils;
import com.cmcc.internalcontact.utils.view.CommonToolBar;
import com.cmcc.internalcontact.utils.view.HorizontalDividerItemDecoration;
import com.cmcc.internalcontact.utils.view.OnToolBarButtonClickListener;
import com.cmcc.internalcontact.utils.view.ToolBarButtonType;
import com.cmcc.internalcontact.utils.view.VerticalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.cmcc.internalcontact.usecase.LoginUsecase.TAG_USE_INFO;
import static com.cmcc.internalcontact.utils.Constant.INTENT_DATA_DEPART;

public class MainActivity extends BaseActivity implements OnItemClickListener<MainInfoBean> {

    @BindView(R.id.toolbar_main)
    CommonToolBar toolbarMain;
    @BindView(R.id.tab_main)
    LinearLayout tabMain;
    @BindView(R.id.main_list)
    RecyclerView contactRecyclerView;
    @BindView(R.id.tv_duty_call)
    TextView tvDutyCall;
    @BindView(R.id.duty_call_lay)
    LinearLayout layDutyCall;
    @BindView(R.id.tv_fax)
    TextView tvFax;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    private MainAdapter adapter;
    @BindView(R.id.iv_path_lay)
    ImageView pathIcon;
    @BindView(R.id.path_lay)
    LinearLayout pathLay;
    @BindView(R.id.rc_path_lay)
    RecyclerView pathRecyclerView;
    @BindView(R.id.tv_person_count)
    TextView tvCount;


    private ContactLevelPathAdapter pathAdapter;
    private static final int SEARCH_REQUEST_CODE = 1001;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initPathAapter();
        initTab();
        initToolbar();
        initContactDefaultList();
    }

    private void initToolbar() {
        toolbarMain.setBarButtonClickListener(new OnToolBarButtonClickListener() {
            @Override
            public void onClick(View v, ToolBarButtonType type) {
                switch (type) {
                    case LEFT_FIRST_BUTTON:
                        if (!pathAdapter.back()) {
                            back2Root();
                            return;
                        }
                        int index = pathAdapter.getDatas().size() - 1;
                        if (index < 0 || index > pathAdapter.getDatas().size()) {
                            back2Root();
                            return;
                        }
                        DepartModel departModel = pathAdapter.getData(index);
                        loadDepartment(departModel);
                        break;
                }
            }
        });
    }

    @OnClick({R.id.tab_contact, R.id.tab_search, R.id.tab_mine})
    public void onTabClick(View v) {
        switch (v.getId()) {
            case R.id.tab_contact:
                back2Root();
                break;
            case R.id.tab_search:
                jump2Search();
                break;
            case R.id.tab_mine:
                startActivity(new Intent(MainActivity.this, MineActivity.class));
                break;
        }
    }

    private void back2Root() {
        toolbarMain.setButtonVisibility(ToolBarButtonType.LEFT_FIRST_BUTTON, View.GONE);
        pathIcon.setVisibility(View.GONE);
        pathLay.setVisibility(View.GONE);
        pathAdapter.clear();
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
        startActivityForResult(new Intent(MainActivity.this, SearchActivity.class), SEARCH_REQUEST_CODE);
    }

    private void loadContactCount() {
        new LoadContactList().getPersonsCount().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<Long>(this) {
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
        //父部门为空代表是第一次加载
        pathLay.setVisibility(View.VISIBLE);
        toolbarMain.setButtonVisibility(ToolBarButtonType.LEFT_FIRST_BUTTON, View.VISIBLE);
        pathAdapter.jump2Position(departModel);
        layDutyCall.setVisibility(View.VISIBLE);
        setDepartTels(departModel);
        new LoadContactList().loadPersons(departModel.getDeptCode()).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<List<MainInfoBean>>(this) {

                    @Override
                    public void onNext(List<MainInfoBean> mainInfoBeans) {
                        adapter.setDataList(mainInfoBeans);
                    }
                });
    }

    private void setDepartTels(DepartModel departTels) {
        if (departTels == null) {
            return;
        }
        List<String> tels = new ArrayList<>();
        if (!TextUtils.isEmpty(departTels.getTel1())) {
            tels.add(departTels.getTel1());
        }
        if (!TextUtils.isEmpty(departTels.getTel2())) {
            tels.add(departTels.getTel2());

        }
        if (!TextUtils.isEmpty(departTels.getTel3())) {
            tels.add(departTels.getTel3());
        }
        if (!TextUtils.isEmpty(departTels.getTel4())) {
            tels.add(departTels.getTel4());
        }
        if (!TextUtils.isEmpty(departTels.getTel5())) {
            tels.add(departTels.getTel5());
        }
        tvDutyCall.setMovementMethod(LinkMovementMethod
                .getInstance());
        String telsString = buildPhone(tels);
        if (!TextUtils.isEmpty(telsString)) {
            tvDutyCall.setText(Utils.addClickablePart(this, telsString), TextView.BufferType.SPANNABLE);
        }
        List<String> faxs = new ArrayList<>();
        if (!TextUtils.isEmpty(departTels.getFax1())) {
            faxs.add(departTels.getFax1());
        }
        if (!TextUtils.isEmpty(departTels.getFax2())) {
            faxs.add(departTels.getFax2());
        }
        if (!TextUtils.isEmpty(departTels.getFax3())) {
            faxs.add(departTels.getFax3());
        }
        if (!TextUtils.isEmpty(departTels.getFax4())) {
            faxs.add(departTels.getFax4());
        }
        if (!TextUtils.isEmpty(departTels.getFax5())) {
            faxs.add(departTels.getFax5());
        }
        String faxsString = buildPhone(tels);
        if (!TextUtils.isEmpty(faxsString)) {
            tvFax.setText(faxsString);
        }
        List<String> emails = new ArrayList<>();
        if (!TextUtils.isEmpty(departTels.getEmail1())) {
            emails.add(departTels.getEmail1());
        }
        if (!TextUtils.isEmpty(departTels.getEmail2())) {
            emails.add(departTels.getEmail2());
        }
        if (!TextUtils.isEmpty(departTels.getEmail3())) {
            emails.add(departTels.getEmail3());
        }
        String emailsString = buildPhone(emails);
        if (!TextUtils.isEmpty(faxsString)) {
            tvEmail.setText(emailsString);
        }
    }

    private String buildPhone(List<String> strings) {
        if (ArraysUtils.isListEmpty(strings)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.size(); i++) {
            sb.append(strings.get(i));
            sb.append("\n");
        }
        return sb.toString();
    }

    private void loadDepartment(DepartModel parentModel) {
        pathIcon.setVisibility(View.VISIBLE);
        layDutyCall.setVisibility(View.GONE);
        String departId = parentModel == null ? "-1" : parentModel.getDeptCode();
        new LoadContactList().loadDepartData(departId).subscribe(new MyObserver<List<MainInfoBean>>(this) {
            @Override
            public void onNext(List<MainInfoBean> mainInfoBeans) {

                if (ArraysUtils.isListEmpty(mainInfoBeans)) {
                    loadContact(parentModel);
                    return;
                }
                //父部门为空代表是第一次加载
                if (parentModel != null) {
                    pathLay.setVisibility(View.VISIBLE);
                    toolbarMain.setButtonVisibility(ToolBarButtonType.LEFT_FIRST_BUTTON, View.VISIBLE);
                    pathAdapter.jump2Position(parentModel);
                }
                adapter.setDataList(mainInfoBeans);
            }

        });
    }


    /**
     * 加载默认联系人路径和信息
     */
    private void initContactDefaultList() {
        String personJson = SharePreferencesUtils.getInstance().getString(TAG_USE_INFO, null);
        if (TextUtils.isEmpty(personJson)) {
            loadDepartment(null);
            return;
        }
        LoginResponseBean.UserInfo userInfo = JSONObject.parseObject(personJson, LoginResponseBean.UserInfo.class);
        if (userInfo == null) {
            loadDepartment(null);
            return;
        }
        List<DepartModel> departPath = new LoadContactList().getDepartPath(userInfo.getUserId());
        if (ArraysUtils.isListEmpty(departPath)) {
            loadDepartment(null);
            return;
        }
        DepartModel rootDepart = departPath.get(0);
        loadContact(rootDepart);
        pathAdapter.clear();
        for (int i = departPath.size() - 1; i >= 0; i--) {
            DepartModel departModel = departPath.get(i);
            if (departModel == null) {
                continue;
            }
            pathAdapter.addData(departModel);
        }
    }

    private void initTab() {
        buildTab(R.id.tab_contact,"通讯录", R.drawable.common_widget_home_menu_contact_selecter);
        buildTab(R.id.tab_search,getString(R.string.search), R.drawable.common_widget_home_menu_search_selecter);
        buildTab(R.id.tab_mine,"我的", R.drawable.common_widget_home_menu_mine_selecter);
    }

    @NonNull
    private void buildTab(int tabId,String name, int iconId) {
        View contactTabView = findViewById(tabId);
        TextView tabName = contactTabView.findViewById(R.id.tv_main_tab_name);
        ImageView tabIcon = contactTabView.findViewById(R.id.tv_main_tab_icon);
        tabIcon.setImageResource(iconId);
        tabName.setText(name);
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
                if ("黄页".equals(data1.getDeptName())) {
                    startActivity(new Intent(MainActivity.this, YellowPageActivity.class));
                    return;
                }

                loadDepartment(data1);
                break;
            case MainInfoBean.TYPE_PERSON:
                UserDetailDialogFragment.show(MainActivity.this, (PersonModel) data.getData());
                break;
        }
    }

/*    private View buildPathLayout(String name) {
        View inflate = getLayoutInflater().inflate(R.layout.layout_contact_path_view, null);
        TextView viewById = inflate.findViewById(R.id.tv_path2);
        viewById.setText(name);
        return inflate;
    }*/

    private void initPathAapter() {
        pathAdapter = new ContactLevelPathAdapter(this);
        pathRecyclerView.setAdapter(pathAdapter);
        pathRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        pathRecyclerView.addItemDecoration(new VerticalDividerItemDecoration.Builder(this)
                .drawable(R.drawable.img_right_arrow)
                .build());
        pathAdapter.setItemClickListener(new OnItemClickListener<DepartModel>() {
            @Override
            public void onItemClick(View view, DepartModel data, int position) {
                loadDepartment(data);
                pathAdapter.jump2Position(data);
            }
        });
    }

    @Override
    public void onTokenValid() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SEARCH_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            DepartModel departModel = data.getParcelableExtra(INTENT_DATA_DEPART);
            if (departModel != null) {
                pathAdapter.clear();
                loadDepartment(departModel);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
