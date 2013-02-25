package org.genshin.engine.system;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 何らかの処理を一括管理する基本クラス
 * @author kou
 * @since		1.0
 * @version	1.0
 *
 * @param <T>		管理するオブジェクトの型
 */
public abstract class AbstractProcessManager<T>
{
	/**
	 * 管理するオブジェクトを追加する。
	 * @param object		追加するオブジェクト
	 * @param priority		処理優先度
	 */
	public void add(T object, int priority)
	{
		List<T> list = objects.get(priority);
		
		//---- 対応する処理優先度のリストがまだ存在していない場合、
		//     新しいリストを生成し、マップに追加する
		if(list == null)
		{
			list = new LinkedList<T>();
			objects.put(priority, list);
		}
		
		//---- 対応する処理優先度のリストにオブジェクトを追加する。
		list.add(object);
		objectCount++;
	}
	
	/**
	 * 管理するオブジェクトを削除する。
	 * @param object		削除するオブジェクト
	 * @param priority		処理優先度
	 */
	public void remove(T object, int priority)
	{
		final List<T> list = objects.get(priority);
		
		//---- 対応する処理優先度のリストが存在しない場合、何もしない
		if(list == null)
			return;
		
		//---- リストからオブジェクトを削除する。
		if(list.remove(object))
			objectCount--;
	}
	
	/**
	 * 管理するオブジェクトを全て削除する。
	 */
	public void clear()
	{
		for(List<T> list:objects.values())
		{
			list.clear();
		}
		objectCount = 0;
	}
	
	/**
	 * 管理オブジェクトの数を取得する。
	 * @return		管理オブジェクトの数
	 */
	public int size()
	{
		return objectCount;
	}
	
	/**
	 * 処理優先度順に処理を実行する。
	 */
	protected void process()
	{
		//---- 実行リストに登録する。
		for(Map.Entry<Integer, List<T>> entry : objects.entrySet())
		{
			for(T object : entry.getValue())
			{
				executionList.add(object);
			}
		}
		
		//---- 実行リストのプロセスを実行する。
		for(T object : executionList)
		{
			processOne(object);
		}
		executionList.clear();
	}
	
	/**
	 * 1つのオブジェクトの処理を実行する。
	 * @param object	処理を実行するオブジェクト
	 */
	protected abstract void processOne(T object);

	
	/** 管理オブジェクトを格納するマップ */
	private final Map<Integer, List<T>> objects = new TreeMap<Integer, List<T>>();
	
	/** 実行するプロセスのリスト */
	private final ArrayList<T> executionList = new ArrayList<T>();
	
	/** 管理オブジェクトの数 */
	private int objectCount = 0;
}
