package org.genshin.scrollninja;

import java.util.ArrayList;

public class PlayerManager {
	private static ArrayList<Player> playerList = new ArrayList<Player>();
	
	private PlayerManager(){}
	
	//************************************************************
	// CreatePlayer
	// プレイヤー作ります
	//************************************************************
	public static int CreatePlayer(String Name) {
		if( playerList.contains(Name) ) {		// 既にその名前が作られている場合はエラー
			return -1;		// エラー処理
		}
		
		Player pPlayer = new Player(Name);		// オブジェクトを生成（&初期化）して
		playerList.add(pPlayer);						// リストに追加
		
		return 1;
	}
	
	//************************************************************
	// GetPlayer
	// 指定した名前のプレイヤーを返す
	//************************************************************
	public static Player GetPlayer(String Name){
		for(int i = 0; i < playerList.size(); i ++) {
			if( playerList.get(i).GetName().equals(Name) ) {
				return playerList.get(i);			// 引数で渡されたオブジェクトのポインタを返す
			}
		}
		return null;
	}
}
