package org.genshin.scrollninja.utils.input;

import com.badlogic.gdx.Gdx;

/**
 * マウスの入力を補助するクラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class MouseInputHelper extends AbstractInputHelper
{
	/**
	 * コンストラクタ
	 * @param button		ボタンコード(com.badlogic.gdx.Input.Buttons)
	 * @see com.badlogic.gdx.Input.Buttons
	 */
	public MouseInputHelper(int button)
	{
		this.button = button;
	}
	
	@Override
	protected boolean getCurrentInput()
	{
		return Gdx.input.isButtonPressed(button);
	}
	
	/** キーコード */
	private final int button;
}
