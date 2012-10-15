package org.genshin.scrollninja;

import java.util.ArrayList;

public class ItemManager {
	
	private static ArrayList<Integer>	itemList		= new ArrayList<Integer>();	// アイテムリスト
	private static ArrayList<Item>		onigiriList		= new ArrayList<Item>();	// 管理番号
//	private static ArrayList<Item>		
	
	// コンストラクタ
	private ItemManager(){}
	
	//************************************************************
	// CreateItem
	// アイテム生成
	// 同じ種類が既にある場合は最後に追加			まだその種類がリストにない場合は新規で追加
	//************************************************************
	public static int CreateItem(int Type) {
		
		// 同じ種類のアイテムがないか探す
		for(int i = 0; i < itemList.size(); i ++ ) {
			// 同じ種類発見
			if( itemList.get(i).equals(Type) ) {
				
				switch( Type ) {
				case Item.ONIGIRI:
					int j = onigiriList.size() + 1;
					Item pItem = new Item(Type, j);		// 最後の番号を管理番号に
					onigiriList.add(pItem);					// 追加
				break;
				}
			}	
		}
		
		// なかった
		itemList.add(Type);					// この種類の項目を増やす
		Item pItem = new Item(Type, 1);		// 最初の一つ目なので管理番号は１
		onigiriList.add(pItem);				// 追加
		
		return 2;						// 新規追加
	}
}
