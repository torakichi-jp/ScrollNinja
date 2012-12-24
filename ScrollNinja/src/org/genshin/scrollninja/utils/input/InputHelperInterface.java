package org.genshin.scrollninja.utils.input;



/**
 * 入力を補助するインタフェース
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public interface InputHelperInterface
{
	/**
	 * 入力情報を更新する。
	 */
	public void update();
	
	/**
	 * プレス入力を取得する。
	 * @return	プレス入力
	 */
	public boolean isPress();

	/**
	 * トリガ入力を取得する。
	 * @return	トリガ入力
	 */
	public boolean isTrigger();

	/**
	 * リリース入力を取得する。
	 * @return	リリース入力
	 */
	public boolean isRelease();
}
