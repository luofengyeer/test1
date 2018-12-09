package com.cmcc.internalcontact.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmcc.internalcontact.R;
import com.cmcc.internalcontact.model.db.DepartModel;
import com.cmcc.internalcontact.utils.ArraysUtils;
import com.cmcc.internalcontact.utils.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class ContactLevelPathAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnItemClickListener<DepartModel> itemClickListener;
    private List<DepartModel> mList = new ArrayList<>();
    private Context context;

    public ContactLevelPathAdapter(Context context) {
        this.context = context;
    }

    public void setItemClickListener(OnItemClickListener<DepartModel> itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void addData(DepartModel data) {
        mList.add(data);
        notifyDataSetChanged();
    }

    public boolean back() {
        if (ArraysUtils.isListEmpty(mList)) {
            return false;
        }
        int index = mList.size() - 1;
        mList.remove(index);
        notifyDataSetChanged();
        return true;
    }

    public DepartModel jump2Position(DepartModel departmentBean) {
        if (ArraysUtils.isListEmpty(mList)) {
            mList.add(departmentBean);
            notifyDataSetChanged();
            return departmentBean;
        }
        int position = mList.indexOf(departmentBean);
        if (position < 0) {
            mList.add(departmentBean);
            notifyDataSetChanged();
            return departmentBean;
        }
        for (int i = mList.size() - 1; i >= 0; i--) {
            if (i > position) {
                mList.remove(i);
                notifyDataSetChanged();
            }
        }
        return mList.get(position);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_level_form_contact_path, parent, false);
        return new DepartmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder == null) {
            return;
        }
        final DepartModel departmentBean = mList.get(position);

        DepartmentViewHolder rootViewHolder = (DepartmentViewHolder) holder;
        if (departmentBean != null) {
            boolean isLastOne = isLastOne(position, departmentBean);
            rootViewHolder.setName(departmentBean, isLastOne);
        }

        rootViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (isLastOne(position, departmentBean)) {
                    return;
                }*/
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, departmentBean, position);
                }
                return;
            }
        });
    }

    private boolean isLastOne(int position, DepartModel departmentBean) {
        if (ArraysUtils.isListEmpty(mList)) {
            return false;
        }
        return position + 1 == mList.size() && departmentBean.getId() > 0;
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public List<DepartModel> getDatas() {
        return mList;
    }

    public DepartModel getData(int position) {
        return mList.get(position);
    }

    public void clear() {
        if(ArraysUtils.isListEmpty(mList)){
            return;
        }
        mList.clear();
        notifyDataSetChanged();
    }

    private class DepartmentViewHolder extends RecyclerView.ViewHolder {
        private TextView departmentNameTv;

        public DepartmentViewHolder(View itemView) {
            super(itemView);
            departmentNameTv = itemView.findViewById(R.id.level_form_contact_department_name);
        }


        public void setName(DepartModel departmentBean, boolean isLastOne) {
            if (departmentBean == null) {
                return;
            }
            departmentNameTv.setText(departmentBean.getDeptName());
           /* if (isLastOne) {
                departmentNameTv.setTextColor(context.getResources().getColor(R.color.common_widget_text_colorC3_3));
            } else {
                departmentNameTv.setTextColor(context.getResources().getColor(R.color.common_widget_theme_color_C1_1));
            }*/
        }
    }
}
