package com.gtrj.docdeal.ui;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.gc.materialdesigndemo.R;
import com.gtrj.docdeal.bean.ContactItem;
import com.gtrj.docdeal.customView.AlphaView;
import com.gtrj.docdeal.net.WebService;
import com.gtrj.docdeal.util.ContextString;
import com.gtrj.docdeal.util.DocApplication;
import com.gtrj.docdeal.util.PinYin;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by zhang77555 on 2015/3/25.
 */
public class Contacts extends FragmentActivity implements ActionBar.TabListener {
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

    }


    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        if (mViewPager != null) {
            mViewPager.setCurrentItem(tab.getPosition());
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {
        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                default:
                    return new DummySectionFragment(i + "");
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "单位通讯录";
                case 1:
                    return "公共通讯录";
                case 2:
                    return "个人通讯录";
                default:
                    return "test";
            }
        }
    }

    public static class DummySectionFragment extends Fragment implements AlphaView.OnAlphaChangedListener {
        private String type;

        private ProgressBarCircularIndeterminate loading;

        private ListView listView;
        private AlphaView alphaView;
        public TextView overlay;

        private WindowManager windowManager;
        private List<ContactItem> list;
        private ListAdapter adapter;
        private HashMap<String, Integer> alphaIndexer;
        private OverlayThread overlayThread;
        private Context context;
        private View rootView;

        public DummySectionFragment(String type) {
            this.type = type;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_section_dummy, container, false);
            context = rootView.getContext();
            loading = (ProgressBarCircularIndeterminate) rootView.findViewById(R.id.loading);
            loading.setVisibility(View.VISIBLE);

            list = new ArrayList<>();
            alphaIndexer = new HashMap<>();
            overlayThread = new OverlayThread();
            adapter = new ListAdapter(context, list);
            return rootView;
        }

        private final Handler msgHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.arg1) {
                    case 1:
                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                xmlToList(getData(), list);
                                Message msg = msgHandler.obtainMessage();
                                msg.arg1 = 2;
                                msgHandler.sendMessage(msg);
                            }
                        });
                        t.start();
                        break;
                    case 2:
                        loading.setVisibility(View.INVISIBLE);
                        adapter = new ListAdapter(context, list);
                        intitWidget();
                        initOverlay();
                        adapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        };

        private String getData() {
            if ("0".equals(this.type) && DocApplication.lruCache.get("UnitContacts") != null) {
                return DocApplication.lruCache.get("UnitContacts").toString();
            } else if ("1".equals(this.type) && DocApplication.lruCache.get("publicContacts") != null) {
                return DocApplication.lruCache.get("publicContacts").toString();
            } else if ("2".equals(this.type) && DocApplication.lruCache.get("personalContacts") != null) {
                return DocApplication.lruCache.get("personalContacts").toString();
            }
            Map<String, String> requestDatas = new HashMap<>();
            SharedPreferences preferences = context.getSharedPreferences("docdeal", 0);
            String username = preferences.getString("login", "");
            requestDatas.put("orgId", username + ":allusers");
            requestDatas.put("bookType", this.type);
            SoapObject obj = new WebService().GetObject(
                    ContextString.WebServiceURL,
                    ContextString.NameSpace,
                    ContextString.DocContacts,
                    requestDatas);
            Log.e("tag", "getdata");
            String result = obj.getProperty(ContextString.DocContacts + "Return").toString();
            if (obj != null && obj.getPropertyCount() > 0) {
                if ("0".equals(this.type)) {
                    DocApplication.lruCache.put("UnitContacts", result);
                } else if ("1".equals(this.type)) {
                    DocApplication.lruCache.put("publicContacts", result);
                } else {
                    DocApplication.lruCache.put("personalContacts", result);
                }
                return result;
            } else {
                return null;
            }
        }

        public static void xmlToList(String str, List<ContactItem> list) {
            if (str == null || str.equals("")) {
                return;
            }
            try {
                Document document = DocumentHelper.parseText(str);

                Element root = document.getRootElement();

                for (Iterator iter = root.elementIterator(); iter.hasNext(); ) {
                    Element element = (Element) iter.next();
                    ContactItem oh = new ContactItem();
                    if (element.getName().equals("userinfo")) {
                        for (Iterator iter2 = element.elementIterator(); iter2
                                .hasNext(); ) {
                            Element element2 = (Element) iter2.next();
                            if (element2.getName().equals("姓名")) {
                                oh.setName(element2.getText());
                                oh.setAlpha(PinYin.getPinYin(element2.getText()));
                            }
                            if (element2.getName().equals("用户ID")) {
                                oh.setId(element2.getText());
                            }
                            if (element2.getName().equals("手机号码")) {
                                oh.setNumber(element2.getText());
                            }
                        }
                        list.add(oh);
                    }
                }
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }


        private void intitWidget() {
            listView = (ListView) rootView.findViewById(R.id.list_view);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", list.get(position).getId());
                    startActivity(new Intent(context, ContactsDetail.class).putExtras(bundle));
                }
            });
            alphaView = (AlphaView) rootView.findViewById(R.id.alphaView);
            alphaView.setOnAlphaChangedListener(this);
        }

        private void initOverlay() {
            LayoutInflater inflater = LayoutInflater.from(context);
            overlay = (TextView) inflater.inflate(R.layout.overlay, null);
            overlay.setVisibility(View.INVISIBLE);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    PixelFormat.TRANSLUCENT);
            windowManager = (WindowManager) context
                    .getSystemService(context.WINDOW_SERVICE);
            windowManager.addView(overlay, lp);
        }

        private Handler handler = new Handler();

        @Override
        public void OnAlphaChanged(String s, int index) {
            if (overlay != null && s != null && s.trim().length() > 0) {
                overlay.setText(s);
                overlay.setVisibility(View.VISIBLE);
                handler.removeCallbacks(overlayThread);
                handler.postDelayed(overlayThread, 300);
                if (alphaIndexer.get(s) != null) {
                    int position = alphaIndexer.get(s);
                    listView.setSelection(position);
                }
            }
        }

        @Override
        public void onPause() {
            try {
                windowManager.removeView(overlay);
                windowManager=null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPause();
        }

        @Override
        public void onResume() {
            super.onResume();
            Message msg = msgHandler.obtainMessage();
            msg.arg1 = 1;
            msgHandler.sendMessage(msg);
        }

        private class ListAdapter extends BaseAdapter {
            private LayoutInflater inflater;

            public ListAdapter(Context context, List<ContactItem> list) {
                this.inflater = LayoutInflater.from(context);
                for (int i = 0; i < list.size(); i++) {
                    String currentAlpha = list.get(i).getAlpha();
                    String previewAlpha = (i - 1) >= 0 ? list.get(i - 1).getAlpha()
                            : " ";
                    if (!previewAlpha.equals(currentAlpha)) {
                        String alpha = list.get(i).getAlpha();
                        alphaIndexer.put(alpha, i);
                    }
                }
            }

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return list.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder;
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.contacts_list_item, null);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                ContactItem item = list.get(position);
                holder.name.setText(item.getName());
                holder.number.setText(item.getNumber());

                String currentAlpha = list.get(position).getAlpha();
                String previewAlpha = (position - 1) >= 0 ? list.get(position - 1)
                        .getAlpha() : " ";
                if (!previewAlpha.equals(currentAlpha)) {
                    holder.alpha.setVisibility(View.VISIBLE);
                    holder.alpha.setText(currentAlpha);
                } else {
                    holder.alpha.setVisibility(View.GONE);
                }
                return convertView;
            }
        }

        private final class ViewHolder {
            TextView alpha;
            TextView name;
            TextView number;

            public ViewHolder(View v) {
                alpha = (TextView) v.findViewById(R.id.alpha_text);
                name = (TextView) v.findViewById(R.id.name);
                number = (TextView) v.findViewById(R.id.number);
            }
        }


        private class OverlayThread implements Runnable {
            @Override
            public void run() {
                overlay.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
