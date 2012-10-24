/**
 * 
 */
package org.genshin.scrollninja.object.player;

/**
 * プレイヤーの操作状態インタフェース
 * @author kou
 * @since 1.0
 * @version 1.0
 */
public interface IPlayerController
{
	/**
	 * プレイヤーの操作状態を更新する。
	 */
	void update();
	
	/**
	 * 移動の操作状態を取得する。
	 * @return	左方向への移動操作がある場合は-1.0f、右方向への移動操作がある場合は1.0f、移動操作がない場合は0.0f
	 */
	float moveLevel();
	
	/**
	 * ダッシュの操作状態を取得する。
	 * @return　ダッシュの操作がある場合はtrue
	 */
	boolean dash();
	
	/**
	 * ジャンプの操作状態を取得する。
	 * @return	ジャンプの操作がある場合はtrue
	 */
	boolean jump();
	
	/**
	 * 攻撃の操作状態を取得する。
	 * @return	攻撃の操作がある場合はtrue
	 */
	boolean attack();
	
	/**
	 * 鉤縄を投げる操作の状態を取得する。
	 * @return	鉤縄を投げる操作がある場合はtrue
	 */
	boolean kaginawaThrow();
}
