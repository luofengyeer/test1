package com.cmcc.internalcontact.activity;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cmcc.internalcontact.R;
import com.cmcc.internalcontact.base.RvCommonAdapter;
import com.cmcc.internalcontact.base.RvViewHolder;
import com.cmcc.internalcontact.model.MainInfoBean;
import com.cmcc.internalcontact.utils.Constant;
import com.cmcc.internalcontact.utils.OnItemClickListener;

public class MainAdapter extends RvCommonAdapter<MainInfoBean> {
    private OnItemClickListener<MainInfoBean> onItemClickListener;

    public MainAdapter(Context context) {
        super(context);
    }

    public void setOnItemClickListener(OnItemClickListener<MainInfoBean> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindView(RvViewHolder viewHolder, MainInfoBean item, int position) {
        TextView name = viewHolder.itemView.findViewById(R.id.company_contact_name);
        ImageView icon = viewHolder.itemView.findViewById(R.id.company_contact_icon);
        TextView departmentName = viewHolder.itemView.findViewById(R.id.person_work);
        if (item.getType() == MainInfoBean.TYPE_PERSON) {
            icon.setImageResource(R.drawable.ic_depart_icon);
        } else {
            Glide.with(getContext()).load(item.getAvatar()).apply(Constant.AVATAR_OPTIONS).into(icon);
        }
        name.setText(item.getName());
        departmentName.setText(item.getDepartmentName());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, item, position);
                }
            }
        });
    }

    @Override
    public int getLayoutResId(int viewType) {
        return R.layout.item_contact;
    }
}
