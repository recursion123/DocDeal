package com.gtrj.docdeal.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.widgets.Dialog;
import com.gc.materialdesign.widgets.PopWindow;
import com.gc.materialdesigndemo.R;
import com.gtrj.docdeal.ui.DocMainDetail;
import com.gtrj.docdeal.ui.DocMainDetailText;


import org.textmining.text.extraction.WordExtractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by zhang77555 on 2015/1/20.
 */
public class DocDetailAdapter extends RecyclerView.Adapter<DocDetailAdapter.DetailViewHolder> {
    public Map[] detailData;
    private int w_screen;
    public List<String> accessorys;

    public DocDetailAdapter(Map[] detailData, List accessorys) {
        this.detailData = detailData;
        this.accessorys = accessorys;
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public void onBindViewHolder(DetailViewHolder detailViewHolder, int i) {


        if (detailData[0] != null) {
            TableRow title = new TableRow(detailViewHolder.cardBaseInfo.getContext());
            title.setGravity(Gravity.CENTER);

            TextView titleText = new TextView(detailViewHolder.cardBaseInfo.getContext());
            titleText.setText("基本信息");
            titleText.setTextSize(18);
            titleText.setHeight(120);
            titleText.setGravity(Gravity.CENTER);
            title.addView(titleText);
            detailViewHolder.cardBaseInfo.addView(title);

            for (Object e : detailData[0].keySet()) {
                TableRow tr = new TableRow(detailViewHolder.cardBaseInfo.getContext());
                tr.setBackgroundResource(R.drawable.cell_shape);
                tr.setGravity(Gravity.CENTER_VERTICAL);

                TextView tv = new TextView(detailViewHolder.cardBaseInfo.getContext());
                tv.setText(e.toString());
                tv.setHeight(120);
                tv.setWidth(w_screen * 30 / 109);
                tv.setGravity(Gravity.CENTER);
                tr.addView(tv);

                TextView tv2 = new TextView(detailViewHolder.cardBaseInfo.getContext());
                tv2.setText(detailData[0].get(e).toString());
                tv2.setGravity(Gravity.CENTER);
                tv2.setWidth(w_screen * 67 / 109);
                tr.addView(tv2);
                detailViewHolder.cardBaseInfo.addView(tr);
            }
            detailViewHolder.cardBaseInfo.getChildAt(detailViewHolder.cardBaseInfo.getChildCount() - 1).setBackgroundResource(R.drawable.cell_shape_bottom);
        }


        if (detailData[1] != null) {
            TableRow title = new TableRow(detailViewHolder.cardFormInfo.getContext());
            title.setGravity(Gravity.CENTER);

            TextView titleText = new TextView(detailViewHolder.cardFormInfo.getContext());
            titleText.setText("表单信息");
            titleText.setTextSize(18);
            titleText.setHeight(120);
            titleText.setGravity(Gravity.CENTER);
            title.addView(titleText);
            detailViewHolder.cardFormInfo.addView(title);

            for (Object e : detailData[1].keySet()) {
                TableRow tr = new TableRow(detailViewHolder.cardFormInfo.getContext());
                tr.setBackgroundResource(R.drawable.cell_shape);
                tr.setGravity(Gravity.CENTER_VERTICAL);

                TextView tv = new TextView(detailViewHolder.cardFormInfo.getContext());
                tv.setText(e.toString());
                tv.setHeight(120);
                tv.setWidth(w_screen * 30 / 109);
                tv.setGravity(Gravity.CENTER);
                tr.addView(tv);
                if (((String[]) detailData[1].get(e))[1].equals("true")) {
                    BootstrapEditText bet = new BootstrapEditText(detailViewHolder.cardFormInfo.getContext());
                    bet.setText(((String[]) detailData[1].get(e))[0]);
                    bet.setGravity(Gravity.CENTER);
                    bet.setWidth(w_screen * 67 / 109);
                    tr.addView(bet);
                    detailViewHolder.cardFormInfo.addView(tr);
                } else {
                    if (((String[]) detailData[1].get(e))[0] != null && !((String[]) detailData[1].get(e))[0].equals("")) {
                        TextView tv2 = new TextView(detailViewHolder.cardFormInfo.getContext());
                        tv2.setText(((String[]) detailData[1].get(e))[0]);
                        tv2.setGravity(Gravity.CENTER);
                        tv2.setWidth(w_screen * 67 / 109);
                        tr.addView(tv2);
                        detailViewHolder.cardFormInfo.addView(tr);
                    }
                }
            }
            detailViewHolder.cardFormInfo.getChildAt(detailViewHolder.cardFormInfo.getChildCount() - 1).setBackgroundResource(R.drawable.cell_shape_bottom);
        }
        detailViewHolder.docText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DocMainDetail.docPath != null) {
                    final Dialog dialog = new Dialog(v.getContext(), "提示", "请选择打开方式");
                    dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent("android.intent.action.VIEW");
                            intent.addCategory("android.intent.category.DEFAULT");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Uri uri = Uri.fromFile(new File(DocMainDetail.docPath));
                            intent.setDataAndType(uri, "application/msword");
                            v.getContext().startActivity(intent);
                        }
                    });
                    dialog.setOnCancelButtonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            WordExtractor w = new WordExtractor();
                            InputStream is = null;
                            String str = null;
                            try {
                                is = new FileInputStream(new File(DocMainDetail.docPath));
                                str = w.extractText(is).replaceAll("-\\s\\d+\\s-", "").replaceAll("", "");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(v.getContext(), DocMainDetailText.class);
                            intent.putExtra("text", str);
                            v.getContext().startActivity(intent);
                        }
                    });
                    dialog.show();
                    dialog.getButtonAccept().setText("word方式");
                    dialog.getButtonCancel().setText("文本方式");
                } else {
                    final Dialog dialog = new Dialog(v.getContext(), "提示", "没有可以查看的正文");
                    dialog.show();
                    dialog.getButtonAccept().setText("确定");
                    dialog.getButtonCancel().setVisibility(BootstrapButton.INVISIBLE);
                }
            }
        });
        detailViewHolder.docAccessory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopWindow popWindow = new PopWindow(v.getContext(), "附件列表");
                popWindow.show();
                if (accessorys.size() == 0) {
                    TextView textView=new TextView(v.getContext());
                    textView.setTextSize(18);
                    textView.setText("没有可以查看的附件！");
                    popWindow.addNewView(textView);
                } else {
                    for (String s : accessorys) {
                        Button btn = new Button(v.getContext());
                        btn.setBackgroundColor(Color.alpha(0));
                        btn.setText(s);
                        btn.setTextColor(Color.parseColor("#1E88E5"));
                        btn.setBackground(v.getContext().getResources().getDrawable(R.drawable.item_select));
                        popWindow.addNewView(btn);
                    }
                }
            }
        });
    }

    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        DisplayMetrics dm = viewGroup.getContext().getResources().getDisplayMetrics();
        w_screen = dm.widthPixels;
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.carddetail, viewGroup, false);
        return new DetailViewHolder(itemView);
    }

    public static class DetailViewHolder extends RecyclerView.ViewHolder {
        protected TableLayout cardBaseInfo;
        protected TableLayout cardFormInfo;
        protected BootstrapButton docText;
        protected BootstrapButton docAccessory;

        public DetailViewHolder(View v) {
            super(v);
            cardBaseInfo = (TableLayout) v.findViewById(R.id.cardBaseInfo);
            cardFormInfo = (TableLayout) v.findViewById(R.id.cardFormInfo);
            docText = (BootstrapButton) v.findViewById(R.id.docText);
            docAccessory = (BootstrapButton) v.findViewById(R.id.docAccessory);
        }
    }
}
