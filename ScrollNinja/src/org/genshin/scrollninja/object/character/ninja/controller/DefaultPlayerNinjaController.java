/**
 * 
 */
package org.genshin.scrollninja.object.character.ninja.controller;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;

/**
 * プレイヤーが操作する忍者の操作状態を管理するオブジェクトのデフォルト設定。
 * @author	kou
 * @since		1.0
 * @version	1.0
 */
public class DefaultPlayerNinjaController extends AbstractPlayerNinjaController
{
	@Override
	protected void initialize()
	{
		registKey(InputType.LEFT, Keys.A);
		registKey(InputType.RIGHT, Keys.D);
		registKey(InputType.DASH, Keys.SHIFT_LEFT);
		registKey(InputType.JUMP, Keys.W);
		registMouse(InputType.ATTACK, Buttons.LEFT);
		registMouse(InputType.KAGINAWA, Buttons.RIGHT);
	}
}
