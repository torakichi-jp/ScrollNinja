package org.genshin.scrollninja.utils.input;

/**
 * 入力を補助する基本クラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
abstract class AbstractInputHelper implements InputHelperInterface
{
	@Override
	public void update()
	{
		prev = current;
		current = getCurrentInput();
	}

	@Override
	public final boolean isPress()
	{
		return current;
	}

	@Override
	public final boolean isTrigger()
	{
		return !prev && current;
	}

	@Override
	public final boolean isRelease()
	{
		return prev && !current;
	}
	
	/**
	 * 現在の入力状態を取得する。
	 * @return		現在の入力状態
	 */
	protected abstract boolean getCurrentInput();
	
	
	/** 入力状態 */
	private boolean prev, current;
}
