package org.genshin.scrollninja;

import java.util.ArrayList;

public class StructObjectManager {
	public static ArrayList<StructObject> objectList		= new ArrayList<StructObject>();
	
	/**
	 * コンストラクタ
	 */
	StructObjectManager(){}
	
	/**
	 * 更新
	 */
	public static void Update() {
		for( int i = 0; i < objectList.size(); i ++ ) {
			objectList.get(i).Update();
		}
	}
	
	/**
	 * 描画
	 */
	public static void Draw() {
		for( int i = 0; i < objectList.size(); i ++ ) {
			objectList.get(i).Draw();
		}
	}
	
	/**
	 * 生成
	 * @param type			種類
	 */
	public static void CreateStructObject( int type ) {
		StructObject pStructObject = new StructObject(type);
		objectList.add(pStructObject);
	}
	
	public static void CreateStructObject( int type, float x, float y, int priority ) {
		StructObject pStructObject = new StructObject(type, x, y, priority);
		objectList.add(pStructObject);
	}
	
	/**
	 * リストサイズを返す
	 * @return
	 */
	public static int GetListSize() { return objectList.size(); }
	
	/**
	 * 指定された番号のオブジェクトを返す
	 * @param i		番号
	 * @return
	 */
	public static StructObject GetStructObject(int i) { return objectList.get(i); }
}
