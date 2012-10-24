package org.genshin.scrollninja;

import java.util.ArrayList;

public class StructObjectManager {
	public static ArrayList<StructObject> objectList		= new ArrayList<StructObject>();
	
	/**
	 * コンストラクタ
	 */
	StructObjectManager(){}
	
	/**
	 * 生成
	 * @param type			種類
	 * @param x				座標X
	 * @param y				座標Y
	 * @param priority		優先度
	 */
	public static void CreateStructObject( int type, float x, float y, int priority ) {
		StructObject pStructObject = new StructObject(type, x, y,  priority);
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
