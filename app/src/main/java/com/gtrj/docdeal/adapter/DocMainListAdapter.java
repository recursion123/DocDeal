package com.gtrj.docdeal.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gc.materialdesigndemo.R;
import com.gtrj.docdeal.bean.DocInfo;
import com.gtrj.docdeal.util.StringUtil;

import java.util.List;

/**
 * Created by zhang77555 on 2014/12/24.
 */
public class DocMainListAdapter extends RecyclerView.Adapter<DocMainListAdapter.ContactViewHolder> {

    public List<DocInfo> contactList;

    public DocMainListAdapter(List<DocInfo> contactList) {
        this.contactList = contactList;
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        DocInfo ci = contactList.get(contactList.size()-1-i);
        contactViewHolder.vTitle.setText(StringUtil.replaceBlank(ci.getTitle().length() > 15 ? ci.getTitle().substring(0, 15) + "..." : ci.getTitle()));
        contactViewHolder.vDate.setText(ci.getDate());
        contactViewHolder.vSender.setText(ci.getSender());
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.cardview, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView vTitle;
        protected TextView vSender;
        protected TextView vDate;

        public ContactViewHolder(View v) {
            super(v);
            vTitle =  (TextView) v.findViewById(R.id.title);
            vSender = (TextView)  v.findViewById(R.id.sender);
            vDate = (TextView)  v.findViewById(R.id.date);
        }
    }
}
