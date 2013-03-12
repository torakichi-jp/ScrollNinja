package org.genshin.scrollninja.utils;

/**
 * 汎用ポイント管理
 * @author kou
 * @since		1.0
 * @verison	1.0
 */
public class GeneralPoint
{
	/**
	 * コンストラクタ
	 * @param max		ポイントの最大値
	 */
	public GeneralPoint(float max)
	{
		current = this.max = max;
	}
	
	/**
	 * ポイントを加算する。
	 * @param point		加算するポイント
	 */
	public void add(float point)
	{
		current += point;
		clamp();
	}
	
	/**
	 * 現在のポイントを設定する。
	 * @param point		現在のポイント
	 */
	public void set(float point)
	{
		current = point;
		clamp();
	}
	
	/**
	 * 現在のポイントを取得する。
	 * @return		現在のポイント
	 */
	public float get()
	{
		return current;
	}
	
	/**
	 * ポイントの最大値を取得する。
	 * @return		ポイントの最大値
	 */
	public float getMax()
	{
		return max;
	}
	
	/**
	 * 現在のポイントの割合を取得する。
	 * @return		現在のポイントの割合
	 */
	public float getRatio()
	{
		return max == 0.0f ? 0.0f : current / max;
	}
	
	/**
	 * 現在のポイントが最大値か調べる。
	 * @return		現在のポイントが最大値ならtrue
	 */
	public boolean isMax()
	{
		return current == max;
	}
	
	/**
	 * 現在のポイントが0か調べる。
	 * @return		現在のポイントが0ならtrue
	 */
	public boolean isZero()
	{
		return current == 0.0f;
	}
	
	/**
	 * 現在のポイントを[0, max]の範囲内に丸める。
	 */
	private void clamp()
	{
		current = Math.max(Math.min(current, max), 0.0f);
	}
	
	
	/** 現在のポイント */
	private float current;
	
	/** ポイントの最大値 */
	private float max;
}
