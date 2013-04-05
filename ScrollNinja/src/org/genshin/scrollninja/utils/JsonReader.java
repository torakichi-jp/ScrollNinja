package org.genshin.scrollninja.utils;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

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
	 * 次のノードへ移動する。
	 * @param name		ノードの名前
	 */
	public void nextNode(String name)
	{
		jsonDataStack.push(read(name, Object.class));
	}
	
	/**
	 * 前のノードへ移動する。
	 */
	public void prevNode()
	{
		if(jsonDataStack.size() > 1)
			jsonDataStack.pop();
	}
	
	/**
	 * ルートノードへ移動する。
	 */
	public void rootNode()
	{
		while(jsonDataStack.size() > 1)
			jsonDataStack.pop();
	}
	
	
	/** Jsonオブジェクト */
	private final Json json = new Json();
	
	/** 読み込んだJsonデータのスタック */
	private final LinkedList<Object> jsonDataStack = new LinkedList<Object>();
}
