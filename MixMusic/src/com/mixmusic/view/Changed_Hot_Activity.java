package com.mixmusic.view;

import java.util.HashMap;
import java.util.List;

import com.mixmusic.R;
import com.mixmusic.adapter.ChangedListAdapter;
import com.mixmusic.adapter.FindListAdapter;
import com.mixmusic.adapter.LibraryListAdapter;
import com.mixmusic.biz.BizManager;
import com.mixmusic.listview.refresh.PullDownView;
import com.mixmusic.listview.refresh.PullDownView.OnPullDownListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Changed_Hot_Activity extends Activity implements
		OnPullDownListener, OnItemClickListener {

	private final static String TAG = "Find_Ranking_Activity";
	private Context mContext;
	private Handler resultHandler;
	private PullDownView mPullDownView;
	private ListView listview_hot;
	private List<HashMap<String, Object>> ringList;
	private ChangedListAdapter adapter;
	private int index = 1;
	private boolean isRefer = false;
	private boolean isMore = false;
	private BizManager biz = BizManager.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changed_hot_list);
		mContext = Changed_Hot_Activity.this;
		initView();
		initHandler();
		initData(index);
	}

	/**
	 * 加载控件
	 */
	private void initView() {
		// TODO Auto-generated method stub
		mPullDownView = (PullDownView) findViewById(R.id.changed_hot_pull_down_view);
		mPullDownView.setOnPullDownListener(this);
		// 不获取隐藏更新
		mPullDownView.enableAutoFetchMore(false, 1);

		listview_hot = mPullDownView.getListView();
		listview_hot.setDivider(getResources().getDrawable(R.drawable.line));
		listview_hot.setOnItemClickListener(this);
	}

	/**
	 * 加载数据
	 */
	private void initData(int page) {
		// TODO Auto-generated method stub
		biz.getRingList(mContext, page, 15, 1, resultHandler);
	}

	/**
	 * 加载Handler
	 */
	@SuppressLint("HandlerLeak")
	private void initHandler() {
		// TODO Auto-generated method stub
		resultHandler = new Handler() {
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {

				if (msg.what == 1) {
					List<HashMap<String, Object>> data = (List<HashMap<String, Object>>) msg.obj;
					if (index == 1) {
						// 数据赋值
						ringList = data;
						if (isRefer) {
							adapter.setData(ringList);
							adapter.notifyDataSetChanged();
							// 诉它数据刷新完毕;
							mPullDownView.notifyDidRefresh();
							isRefer = false;
						} else {

							initListAdapter(ringList, listview_hot);
							// 诉它数据加载完毕;
							mPullDownView.notifyDidLoad();
						}
					} else {
						ringList.addAll(data);
						adapter.notifyDataSetChanged();
						// 告诉它获取更多完毕
						mPullDownView.notifyDidMore();
						isMore = false;
					}

				}

				super.handleMessage(msg);
			}
		};
	}

	/**
	 * 配置Adapter
	 * 
	 * @param ringList
	 * @param listview_hot
	 */
	private void initListAdapter(List<HashMap<String, Object>> ringList,
			ListView listview_hot) {
		// TODO Auto-generated method stub
		if (null == adapter) {
			adapter = new ChangedListAdapter(mContext, ringList);
			listview_hot.setAdapter(adapter);

		} else {
			adapter.setData(ringList);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		// TODO Auto-generated method stub
		HashMap<String, Object> data = ringList.get(position);
		 Intent i = new Intent();
		 i.putExtra("selectId", String.valueOf(data.get("id")));
		 i.putExtra("selectName", String.valueOf(data.get("musicName")));
		 if (getParent() == null) {
		 setResult(Activity.RESULT_OK, i);
		 } else {
		 getParent().setResult(Activity.RESULT_OK, i);
		 }
		 finish();
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				isRefer = true;
				index = 1;
				initData(index);
			}
		}).start();
	}

	@Override
	public void onMore() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				isMore = true;
				index++;

				initData(index);
			}
		}).start();
	}
}
