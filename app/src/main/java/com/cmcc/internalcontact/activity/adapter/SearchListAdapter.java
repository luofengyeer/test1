package com.cmcc.internalcontact.activity.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cmcc.internalcontact.R;
import com.cmcc.internalcontact.model.SearchPersonBean;
import com.cmcc.internalcontact.model.db.DepartModel;
import com.cmcc.internalcontact.model.db.PersonModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索列表适配器
 */
public class SearchListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * 类型-机构
     */
    public static final int TYPE_MECHANISM = 0;
    /**
     * 类型-单位
     */
    public static final int TYPE_COMPANY = 1;
    /**
     * 类型-联系人
     */
    public static final int TYPE_CONTACT = 2;
    /**
     * 类型
     */
    private int type;
    /**
     * 列表数据
     */
    private List<Object> list;
    /**
     * LayoutInflater
     */
    private LayoutInflater layoutInflater;
    /**
     * 上下文句柄
     */
    private Context context;
    /**
     * 回调
     */
    private ISearchListCallBack callBack;
    /**
     * 搜索内容
     */
    private String searchText;

    public SearchListAdapter(Context context, ISearchListCallBack callBack) {
        this.list = new ArrayList<>();
        this.context = context;
        this.callBack = callBack;
        this.layoutInflater = LayoutInflater.from(context);
        type = TYPE_MECHANISM;
    }

    public void setDepartData(List<DepartModel> list, int type) {
        this.type = type;
        this.list.clear();
        if (list != null && list.size() != 0) {
            this.list.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void setContactData(List<SearchPersonBean> list) {
        this.type = TYPE_CONTACT;
        this.list.clear();
        if (list != null && list.size() != 0) {
            this.list.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        switch (type) {
            case TYPE_COMPANY:
                return new CompanyHolder(layoutInflater.inflate(R.layout.item_search_company, null));
            case TYPE_CONTACT:
                return new ContactHolder(layoutInflater.inflate(R.layout.item_search_contact, null));
            default:
                return new MechanismHolder(layoutInflater.inflate(R.layout.item_search_mechanism, null));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Object bean = list.get(i);
        switch (type) {
            case TYPE_COMPANY:
                ((CompanyHolder) viewHolder).update((DepartModel) bean);
                break;
            case TYPE_CONTACT:
                ((ContactHolder) viewHolder).update((SearchPersonBean) bean);
                break;
            default:
                ((MechanismHolder) viewHolder).update((DepartModel) bean);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    /**
     * 机构Holder
     */
    private class MechanismHolder extends RecyclerView.ViewHolder {
        private TextView mechanismName;
        private View itemView;

        public MechanismHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.mechanismName = itemView.findViewById(R.id.tv_mechanism_name);
        }

        private void update(DepartModel departModel) {
            if (departModel == null) {
                return;
            }
            mechanismName.setText(Html.fromHtml(replaceSearchToRed(departModel.getDeptName(), searchText)));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callBack == null) {
                        return;
                    }
                    callBack.clickMechanism(departModel);
                }
            });
        }
    }

    /**
     * 单位Holder
     */
    private class CompanyHolder extends RecyclerView.ViewHolder {
        private TextView companyName;
        private View itemView;

        public CompanyHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.companyName = itemView.findViewById(R.id.tv_company_name);
        }

        private void update(DepartModel departModel) {
            if (departModel == null) {
                return;
            }
            companyName.setText(Html.fromHtml(replaceSearchToRed(departModel.getDeptName(), searchText)));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callBack == null) {
                        return;
                    }
                    callBack.clickCompany(departModel);
                }
            });
        }
    }

    /**
     * 联系人Holder
     */
    private class ContactHolder extends RecyclerView.ViewHolder {
        private ImageView icon;
        private TextView name;
        private TextView departName;
        private View itemView;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.icon = itemView.findViewById(R.id.iv_contact_icon);
            this.name = itemView.findViewById(R.id.tv_contact_name);
            this.departName = itemView.findViewById(R.id.tv_contact_depart_name);
        }

        private void update(SearchPersonBean searchPersonBean) {
            if (searchPersonBean == null || searchPersonBean.getPersonModel() == null) {
                return;
            }
            PersonModel personModel = searchPersonBean.getPersonModel();
            Glide.with(context).setDefaultRequestOptions(
                    new RequestOptions().error(R.mipmap.ic_search_contact).circleCrop())
                    .load(personModel.getHeadPic()).into(icon);
            name.setText(Html.fromHtml(replaceSearchToRed(personModel.getUsername(), searchText)));

            DepartModel depart = searchPersonBean.getDepartModel();
            departName.setText(depart == null ? "" : depart.getDeptName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callBack == null) {
                        return;
                    }
                    callBack.clickContact(personModel);
                }
            });
        }
    }

    private String replaceSearchToRed(String content, String searchText) {
        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(searchText)) {
            return content;
        }
        return content.replace(searchText, "<font color='#FF0000'>" + searchText + "</font>");
    }
}
