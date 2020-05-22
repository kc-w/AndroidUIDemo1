package com.example.androiduidemo1;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


public class LeftFragment extends Fragment {
    private setWebsite website;
    private ListView lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.leftlayout, null);
        init(root);
        return root;
    }

    private void init(View root) {
        lv = (ListView) root.findViewById(R.id.lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> paren, View view, int position, long id) {
                switch (position) {
                    case 0:
                        website.changeWebsite("http://www.sina.com");
                        break;
                    case 1:
                        website.changeWebsite("http://www.qq.com");
                        break;
                    case 2:
                        website.changeWebsite("http://www.163.com");
                        break;
                    case 3:
                        website.changeWebsite("http://www.taobao.com");
                        break;
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        website = (setWebsite) context;//把activity向下转型成我们定义的接口，注意这里要强转
    }

    //创建回调接口，来回调mainactivity
    public interface setWebsite {
        public void changeWebsite(String url);
    }
}