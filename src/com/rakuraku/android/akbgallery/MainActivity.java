package com.rakuraku.android.akbgallery;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.DialogInterface;
import android.content.Loader;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.rakuraku.android.util.HttpAsyncLoader;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new InstagramFragment()).commit();
		}
	}

	public static class InstagramFragment extends Fragment implements LoaderCallbacks<String>, OnRefreshListener {

		private SwipeRefreshLayout swipeRefreshLayout = null;

		// Instagram URL保持クラス
//		private ImageInfoList image_list = 
//				new ImageInfoList("https://api.instagram.com/v1/tags/akb48/media/recent?access_token=1701240007.b0ed428.945e7ccfec964180b6407e9c76a55c54"); 
		private ImageInfoList image_list = 
				new ImageInfoList("http://api.tiqav.com/search.json?q=test"); 
		
		// Instagram API解析クラス
		private ParseInstagramImage parse = new ParseInstagramImage(this.image_list);
		
		private GridViewAdapter grid_view_adapter = null;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			setRetainInstance(true);

			this.swipeRefreshLayout = (SwipeRefreshLayout)getView().findViewById(R.id.SwipeRefreshLayout);
			this.swipeRefreshLayout.setOnRefreshListener(InstagramFragment.this);
		
	        // プログレスアニメーションの色指定
			this.swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright, 
		            android.R.color.holo_green_light, 
		            android.R.color.holo_orange_light, 
		            android.R.color.holo_red_light);

			GridView gv = (GridView)getView().findViewById(R.id.gridView);

			// カラム数を設定する（縦横向きに応じて値を変える）
			gv.setNumColumns(getResources().getInteger(R.integer.num));
			
			gv.setOnItemClickListener(
					// グリッドビューのクリックリスナー
					new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,	View view, int position, long id) {
							// カスタムビューを設定
		                    LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
		                    final View layout = inflater.inflate(R.layout.dialog, (ViewGroup)getView().findViewById(R.id.alertdialog_layout));
							
		                    // ポップアップする画像を決定
		                    final ImageView im =  (ImageView)layout.findViewById(R.id.dialog_image);
		                    grid_view_adapter.setStandardImage(position,im);
		                    
		                    new AlertDialog.Builder(getActivity())
		            		// レイアウトを設定（カスタムダイアログ）
		            		.setView(layout)

		            		// メッセージを設定
		            		.setMessage("画像を保存しますか？")

		            		// Positiveボタン、リスナーを設定
		            		.setPositiveButton(
		            			"画像を保存",
		            			new DialogInterface.OnClickListener() {

		            				@Override
		            				public void onClick(DialogInterface dialog, int which) {
		            					// 画像を保存する処理
		            				}
		            			})

		            		// Negativeボタン、リスナーを設定
		            		.setNegativeButton(
		            			"キャンセル",
		            			new DialogInterface.OnClickListener() {
		            				@Override
		            				public void onClick(DialogInterface dialog, int which) {
		            				}
		            			})
		            		.show();
		            	}
	        });
			
			// 初回の起動時
			if ( this.grid_view_adapter == null ) {
				this.swipeRefreshLayout.setRefreshing(true); // アニメーション開始
				this.image_list.clear();	// 画像リストをクリアする
				startLoader(0);        		// ローダーの起動
			}
			// 画面が回転されたとき
			else {
				setAdapter(gv); 
			}
		}

		// loaderの開始
		private void startLoader(int id) {
			getLoaderManager().restartLoader(id, null, InstagramFragment.this);
		}

	    // グリッドビューアダプターを作成して、グリッドビューに関連づける
		private void setAdapter(View view) {
			this.grid_view_adapter = new GridViewAdapter(getActivity(),this.image_list.getImageinfo());
			((GridView)view).setAdapter(this.grid_view_adapter);
		}
		
	    // 取得データの更新
		@Override
		public void onRefresh() {
		}

		@Override
		public Loader<String> onCreateLoader(int id, Bundle args) {
			HttpAsyncLoader loader = new HttpAsyncLoader(getActivity(), this.image_list.getNext_url());
			loader.forceLoad();
			return loader;
		}

		@Override
		public void onLoadFinished(Loader<String> loader, String data) {
			this.swipeRefreshLayout.setRefreshing(false);
			if (data == null) return;
			this.parse.loadJson(data); // APIのレスポンスを解析する
			if ( this.grid_view_adapter == null ) { // 初回起動時
		          // アダプタをビューに関連づける
				 setAdapter(getView().findViewById(R.id.gridView));
			}
		}

		@Override
		public void onLoaderReset(Loader<String> loader) {
		}

	}

}
