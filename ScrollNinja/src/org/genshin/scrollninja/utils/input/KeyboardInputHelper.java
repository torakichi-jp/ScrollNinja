package org.genshin.scrollninja.utils.input;

import com.badlogic.gdx.Gdx;

/**
 * キーボードの入力を補助するクラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class KeyboardInputHelper extends AbstractInputHelper
{
	/**
	 * コンストラクタ
	 * @param key		キーコード(com.badlogic.gdx.Input.Keys)
	 * @see com.badlogic.gdx.Input.Keys
	 */
	public KeyboardInputHelper(int key)
	{
		this.key = key;
	}
	
	@Override
	protected boolean getCurrentInput()
	{
		return Gdx.input.isKeyPressed(key);
	}
	
	/** キーコード */
	private final int key;
}
