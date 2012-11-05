/**
 * 
 */
package org.genshin.scrollninja.object.player;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;

/**
 * デフォルト状態に設定されたプレイヤーコントローラー。<br>
 * プレイヤーと入力状態とを仲介する。
 * @author	kou
 * @since		1.0
 * @version	1.0
 */
public class DefaultPlayerController extends BasePlayerController
{
	@Override
	protected void initialize()
	{
		registKey(State.LEFT, Keys.A);
		registKey(State.RIGHT, Keys.D);
		registKey(State.DASH, Keys.SHIFT_LEFT);
		registKey(State.JUMP, Keys.W);
		registMouse(State.ATTACK, Buttons.LEFT);
		registMouse(State.KAGINAWA, Buttons.RIGHT);
	}
}
