package org.genshin.scrollninja;

import java.util.ArrayList;

public class BackgroundManager {
	private static ArrayList<Background> BackgroundList		= new ArrayList<Background>();

	// コンストラクタ
	private BackgroundManager(){}

	//************************************************************
	// CreateBackground
	// 生成
	//************************************************************
	public static void CreateBackground(int num, boolean createFlag) {
		if (BackgroundList == null)
			BackgroundList		= new ArrayList<Background>();

		Background pBackground = new Background(num, createFlag);		// オブジェクトを生成（&初期化）して
		BackgroundList.add(pBackground);					// リストに追加
	}

	//************************************************************
	// GetBackground
	// 参照
	//************************************************************
	public static Background GetBackground(int num){
		for(int i = 0; i < BackgroundList.size(); i ++) {
			if( BackgroundList.get(i).GetBackgroundNum() == num) {
				return BackgroundList.get(i);
			}
		}

		return null;
	}

	public static void dispose() {
		if (BackgroundList != null) {
			for (int i = 0; i < BackgroundList.size(); i++) {
				// TODO なぜ３番目にBodyが入ってるんだろう？
				if (i == 2) {
					BackgroundList.get(i).Release();
				} else {
					BackgroundList.get(i).sprite = null;
				}
			}
		}
		BackgroundList = null;
	}
}