package com.rakuraku.android.akbgallery;

import java.util.LinkedList;
import java.util.List;

public class ImageInfoList {

	// 画像情報のリスト
	private List<ImageInfo> image_info = new LinkedList<ImageInfo>();

	// 次の画像を取得するためのURL
	private String next_url = null;

	public ImageInfoList(String next_url) {
		this.next_url = next_url;
	}
	public List<ImageInfo> getImageinfo() {
		return this.image_info;
	}
	public String getNext_url() {
		return this.next_url;
	}
	public void setNext_url(String next_url) {
		this.next_url = next_url;
	}
	// 保持しているリストのクリア
	public void clear() {
		this.image_info.clear();
	}
	// 保持しているリストに画像追加（URL）を追加する
	public void add(String thumbnail, String standard) {
		ImageInfo img = new ImageInfo();
        img.setThumbnail(thumbnail);
        img.setStandard(standard);
        this.image_info.add(img);
	}
}
