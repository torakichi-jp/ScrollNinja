package org.genshin.scrollninja;

import java.util.ArrayList;

// TODO ステージ毎にCreateして、ArrayListとして保持しておく必要はない？
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
		BackgroundList.add(pBackground);								// リストに追加
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
				BackgroundList.get(i).Release();
			}
		}
		BackgroundList = null;
	}
}