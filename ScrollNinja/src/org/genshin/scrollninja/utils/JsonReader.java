package org.genshin.scrollninja.utils;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.OrderedMap;

/**
 * Jsonファイルの読み込み
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class JsonReader
{
	/**
	 * コンストラクタ
	 * @param filePath		Jsonファイルのパス
	 */
	public JsonReader(String filePath)
	{
		jsonDataStack.push(new com.badlogic.gdx.utils.JsonReader().parse(Gdx.files.internal(filePath)));
	}
	
	/**
	 * データを読み込む
	 * @param name		データの名前
	 * @param type		データの型
	 * @return			読み込んだデータ
	 */
	public <T> T read(String name, Class<T> type)
	{
		return json.readValue(name, type, jsonDataStack.peek());
	}
	
	/**
	 * 子ノードへ移動する。
	 * @param name		子ノードの名前
	 */
	public void toChild(String name)
	{
		jsonDataStack.push(getChild(name));
	}
	
	/**
	 * 親ノードへ移動する。
	 */
	public void toParent()
	{
		if(jsonDataStack.size() > 1)
			jsonDataStack.pop();
	}
	
	/**
	 * ルートノードへ移動する。
	 */
	public void toRoot()
	{
		while(jsonDataStack.size() > 1)
			jsonDataStack.pop();
	}
	
	/**
	 * 指定した名前の子ノードが存在するか調べる。
	 * @param name		子ノードの名前
	 * @return			子ノードが存在する場合はtrue
	 */
	public boolean hasChild(String name)
	{
		return getChild(name) != null;
	}
	
	/**
	 * 子ノードを取得する。
	 * @param name		子ノードの名前
	 * @return			子ノード
	 */
	private Object getChild(String name)
	{
		return read(name, OrderedMap.class);
	}
	
	
	/** Jsonオブジェクト */
	private final Json json = new Json();
	
	/** 読み込んだJsonデータのスタック */
	private final LinkedList<Object> jsonDataStack = new LinkedList<Object>();
}
