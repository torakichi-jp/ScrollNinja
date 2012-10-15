package org.genshin.scrollninja;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class ItemManager {
	
	private static ArrayList<Integer>	itemList		= new ArrayList<Integer>();	// アイテムリスト
	public static ArrayList<Item>		onigiriList		= new ArrayList<Item>();	// 管理番号
//	private static ArrayList<Item>		
	
	// コンストラクタ
	private ItemManager(){}
	
	//************************************************************
	// Update
	// 更新処理まとめ
	//************************************************************
	public static void Update() {
		for( int i = 0; i < onigiriList.size(); i ++) {
			onigiriList.get(i).Update();
		}
	}
	
	//************************************************************
	// Draw
	// 描画処理まとめ
	//************************************************************
	public static void Draw(SpriteBatch spriteBatch) {
		for( int i = 0; i < onigiriList.size(); i ++) {
			onigiriList.get(i).Draw(spriteBatch);
		}
	}
	
	//************************************************************
	// CreateItem
	// アイテム生成
	// 同じ種類が既にある場合は最後に追加
	// まだその種類がリストにない場合は新規で追加
	//************************************************************
	public static int CreateItem(int Type, Vector2 pos) {
		
		// 同じ種類のアイテムがないか探す
		for(int i = 0; i < itemList.size(); i ++ ) {
			// 同じ種類発見
			if( itemList.get(i).equals(Type) ) {
				
				switch( Type ) {
				case Item.ONIGIRI:
					int j = onigiriList.size() + 1;
					Item pItem = new Item(Type, j, pos);		// 最後の番号を管理番号に
					onigiriList.add(pItem);					// 追加
				break;
				}
				
				return 0;
			}	
		}
		// なかった
		itemList.add(Type);						// この種類の項目を増やす
		Item pItem = new Item(Type, 1, pos);	// 最初の一つ目なので管理番号は１
		
		switch( Type ) {
		case Item.ONIGIRI:
			onigiriList.add(pItem);					// 追加
			break;
		}
		
		return 0;
	}
	
	//************************************************************
	// DeleteItem
	// アイテム削除と同時にソートして管理番号変更
	//************************************************************
	public static void DeleteItem(int Type, int Num) {
		for(int i = 0; i < itemList.size(); i ++ ) {
			
			// 発見
			if( itemList.get(i).equals(Type) ) {
				for( int j = 0; j < onigiriList.size(); j ++ ) {
					if( onigiriList.get(j).GetNum() == Num ) {
						GameMain.world.destroyBody(onigiriList.get(j).GetBody());
						onigiriList.get(j).SetBody(null);
						onigiriList.get(j).SetSprite(null);
						onigiriList.remove(j);					// 削除！
					}
				}
				
				// 削除に合わせて管理番号変更。とりあえず全部
				for( int j = 0; j < onigiriList.size(); j ++ ) {
					onigiriList.get(j).SetNum(j + 1);
				}
			}
		}
	}
}
