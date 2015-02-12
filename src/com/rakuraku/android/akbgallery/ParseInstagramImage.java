package com.rakuraku.android.akbgallery;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.rakuraku.android.util.ParseJson;

public class ParseInstagramImage extends ParseJson {

	private ImageInfoList image_list = null;

	public ParseInstagramImage(ImageInfoList image_list) {
		super();
		this.image_list = image_list;
	}

	// レスポンスの解析
	@Override
	public void loadJson(String str) {
		try {
			JSONArray result = new JSONArray(str);
			for (int i = 0; i < result.length(); i++) {
				JSONObject item = result.getJSONObject(i);
				// {"id":"5i","ext":"jpg","height":186,"width":251,"source_url":"http://mar.2chan.net/jun/b/src/1287236672399.jpg"}
				String id = item.getString("id");
				String url = "http://img.tiqav.com/" + id + ".th.jpg";
				this.image_list.add(url, url);
			}
		} catch (Exception e) {
			Log.e(this.getClass().getSimpleName(), e.getMessage());
		}
	}
}
