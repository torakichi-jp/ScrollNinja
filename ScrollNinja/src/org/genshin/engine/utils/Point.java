package org.genshin.engine.utils;

/**
 * 整数座標
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class Point
{
	/**
	 * コンストラクタ
	 */
	public Point()
	{
		this(0, 0);
	}
	
	/**
	 * コンストラクタ
	 * @param x		X座標
	 * @param y		Y座標
	 */
	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	
	/** X座標 */
	public int x;
	
	/** Y座標 */
	public int y;
}
