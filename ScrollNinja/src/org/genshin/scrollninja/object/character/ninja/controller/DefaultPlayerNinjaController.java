/**
 * 
 */
package org.genshin.scrollninja.object.character.ninja.controller;

import org.genshin.scrollninja.object.ObjectInterface;
import org.genshin.scrollninja.utils.input.KeyboardInputHelper;
import org.genshin.scrollninja.utils.input.MouseInputHelper;

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
	/**
	 * コンストラクタ
	 * @param ninja		忍者オブジェクト（忍者の向き計算用）
	 * @param cursor	マウスカーソルオブジェクト（忍者の向き計算用）
	 */
	public DefaultPlayerNinjaController(ObjectInterface ninja, ObjectInterface cursor)
	{
		this.ninja = ninja;
		this.cursor = cursor;
	}
	
	@Override
	protected final void initialize()
	{
		registInputHelper(InputType.LEFT, new KeyboardInputHelper(Keys.A));
		registInputHelper(InputType.RIGHT, new KeyboardInputHelper(Keys.D));
		registInputHelper(InputType.DASH, new KeyboardInputHelper(Keys.SHIFT_LEFT));
		registInputHelper(InputType.JUMP, new KeyboardInputHelper(Keys.W));
		registInputHelper(InputType.ATTACK, new MouseInputHelper(Buttons.LEFT));
		registInputHelper(InputType.KAGINAWA, new MouseInputHelper(Buttons.RIGHT));
		registInputHelper(InputType.KAGINAWA_RELEASE, new KeyboardInputHelper(Keys.S));
	}
	

	@Override
	protected final void updateDirection()
	{
		setDirection(
			cursor.getPositionX() - ninja.getPositionX(),
			cursor.getPositionY() - ninja.getPositionY()
		);
	}


	/** 忍者オブジェクト（鉤縄の向き計算用） */
	private final ObjectInterface ninja;
	
	/** マウスカーソルオブジェクト（鉤縄の向き計算用） */
	private final ObjectInterface cursor;
}
