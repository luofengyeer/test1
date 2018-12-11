package com.cmcc.internalcontact.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cmcc.internalcontact.R;
import com.cmcc.internalcontact.activity.adapter.ISearchListCallBack;
import com.cmcc.internalcontact.activity.adapter.SearchListAdapter;
import com.cmcc.internalcontact.base.BaseActivity;
import com.cmcc.internalcontact.base.MyObserver;
import com.cmcc.internalcontact.model.SearchPersonBean;
import com.cmcc.internalcontact.model.db.DepartModel;
import com.cmcc.internalcontact.model.db.PersonModel;
import com.cmcc.internalcontact.usecase.SearchList;
import com.cmcc.internalcontact.utils.Constant;
import com.cmcc.internalcontact.utils.view.CommonToolBar;
import com.cmcc.internalcontact.utils.view.OnToolBarButtonClickListener;
import com.cmcc.internalcontact.utils.view.ToolBarButtonType;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends BaseActivity {
    private static final String TAG = SearchActivity.class.getSimpleName();
    @BindView(R.id.toolbar_search)
    CommonToolBar toolbarSearch;
    @BindView(R.id.et_search)
    EditText etSearch;
    /*@BindView(R.id.rb_mechanism)
    RadioButton rbMechanism;*/
/*    @BindView(R.id.v_mechanism_line)
    View vMechanismLine;*/
    @BindView(R.id.rb_company)
    RadioButton rbCompany;
    @BindView(R.id.v_company_line)
    View vCompanyLine;
    @BindView(R.id.rb_contacts)
    RadioButton rbContacts;
    @BindView(R.id.v_contacts_line)
    View vContactsLine;
    @BindView(R.id.rg_tab)
    RadioGroup rgTab;
    @BindView(R.id.tv_cancel_search)
    TextView tvCancelSearch;
    @BindView(R.id.iv_clear_et)
    ImageView ivClearEt;
    @BindView(R.id.rv_search)
    RecyclerView rvSearch;
    /**
     * 列表适配器
     */
    private SearchListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_search);
        ButterKnife.bind(this);
        initView();
    }


    /**
     * 初始化视图相关
     */
    private void initView() {
        rvSearch.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchListAdapter(this, new SearchListCallBack());
        rvSearch.setItemAnimator(new DefaultItemAnimator());
        rvSearch.setAdapter(adapter);
        toolbarSearch.setBarButtonClickListener(new SearchBarListener());
        etSearch.addTextChangedListener(new SearchEditWatcher());
        rgTab.setOnCheckedChangeListener(new SearchTabGroupListener());
    }

    @OnClick({R.id.tv_cancel_search, R.id.iv_clear_et})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel_search:
                this.finish();
                break;
            case R.id.iv_clear_et:
                etSearch.setText("");
                ivClearEt.setVisibility(View.GONE);
                break;
        }
    }

   /* private void searchMechanism(String search) {
        if (TextUtils.isEmpty(search)) {
            adapter.setDepartData(null, SearchListAdapter.TYPE_MECHANISM);
            return;
        }
        new SearchList().searchMechanism(search).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<List<DepartModel>>(this) {
                    @Override
                    public void onNext(List<DepartModel> departModels) {
                        adapter.setSearchText(search);
                        adapter.setDepartData(departModels, SearchListAdapter.TYPE_MECHANISM);
                    }
                });
    }
*/
    private void searchCompany(String search) {
        if (TextUtils.isEmpty(search)) {
            adapter.setDepartData(null, SearchListAdapter.TYPE_COMPANY);
            return;
        }
        new SearchList().searchCompany(search).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<List<DepartModel>>(this) {
                    @Override
                    public void onNext(List<DepartModel> departModels) {
                        adapter.setSearchText(search);
                        adapter.setDepartData(departModels, SearchListAdapter.TYPE_COMPANY);
                    }
                });
    }

    private void searchContact(String search) {
        if (TextUtils.isEmpty(search)) {
            adapter.setContactData(null);
            return;
        }
        new SearchList().searchPerson(search).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<List<SearchPersonBean>>(this) {
                    @Override
                    public void onNext(List<SearchPersonBean> searchPersonBeans) {
                        adapter.setSearchText(search);
                        adapter.setContactData(searchPersonBeans);
                    }
                });
    }

    private class SearchTabGroupListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            vCompanyLine.setVisibility(checkedId == R.id.rb_company ? View.VISIBLE : View.INVISIBLE);
            vContactsLine.setVisibility(checkedId == R.id.rb_contacts ? View.VISIBLE : View.INVISIBLE);
//            vMechanismLine.setVisibility(checkedId == R.id.rb_mechanism ? View.VISIBLE : View.INVISIBLE);
            String search = etSearch.getText().toString();
            switch (checkedId) {
                case R.id.rb_company://单位
                    searchCompany(search);
                    break;
                case R.id.rb_contacts://联系人
                    searchContact(search);
                    break;
                default://机构
//                    searchMechanism(search);
                    break;
            }
        }
    }

    private class SearchEditWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String search = etSearch.getText().toString();
            ivClearEt.setVisibility(TextUtils.isEmpty(search) ? View.GONE : View.VISIBLE);
            int checkRBid = rgTab.getCheckedRadioButtonId();
            switch (checkRBid) {
                case R.id.rb_company:
                    searchCompany(search);
                    break;
                case R.id.rb_contacts:
                    searchContact(search);
                    break;
                default:
//                    searchMechanism(search);
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private class SearchBarListener implements OnToolBarButtonClickListener {

        @Override
        public void onClick(View v, ToolBarButtonType type) {
            SearchActivity.this.finish();
        }
    }

    private class SearchListCallBack implements ISearchListCallBack {

        @Override
        public void clickMechanism(DepartModel departModel) {
            if (departModel == null) {
                return;
            }
            Intent intent = new Intent();
            intent.putExtra(Constant.INTENT_DATA_DEPART, departModel);
            setResult(RESULT_OK, intent);
            finish();
        }

        @Override
        public void clickCompany(DepartModel departModel) {
            if (departModel == null) {
                return;
            }
            Intent intent = new Intent();
            intent.putExtra(Constant.INTENT_DATA_DEPART, departModel);
            setResult(RESULT_OK, intent);
            finish();
        }

        @Override
        public void clickContact(PersonModel personModel) {
            UserDetailDialogFragment.show(SearchActivity.this, personModel);
        }
    }
}
