package com.rakuraku.android.akbgallery;

import java.util.Iterator;
import com.fasterxml.jackson.databind.JsonNode;
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
		JsonNode root = getJsonNode(str);
		if (root != null){
            // 次のURLを取得
			this.image_list.setNext_url(root.path("pagination").path("next_url").asText());
			Iterator<JsonNode> ite = root.path("data").elements();
			while (ite.hasNext()) {
				JsonNode j = ite.next();
                // 画像情報（URL）をリストに追加
                this.image_list.add(j.path("images").path("thumbnail").path("url").asText(),
                						j.path("images").path("standard_resolution").path("url").asText());
			}
		}
	}
}
