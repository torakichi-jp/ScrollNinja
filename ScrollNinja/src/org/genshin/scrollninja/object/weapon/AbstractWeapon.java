package org.genshin.scrollninja.object.weapon;

import org.genshin.engine.system.PostureInterface;
import org.genshin.scrollninja.object.ObjectInterface;

/**
 * 武器の基本クラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public abstract class AbstractWeapon implements ObjectInterface
{
	/**
	 * コンストラクタ
	 * @param owner		所有者の位置情報
	 */
	public AbstractWeapon(PostureInterface owner)
	{
		this.owner = owner;
	}
	
	/**
	 * 攻撃する。
	 * @param degrees	回転（度）
	 * @param flip		反転フラグ
	 */
	public abstract void attack(float degrees, boolean flip);
	
	@Override
	public void dispose()
	{
		/* 何もしない */
	}

	@Override
	public void update(float deltaTime)
	{
		/* 何もしない */
	}

	@Override
	public void render()
	{
		/* 何もしない */
	}
	
	@Override
	public float getPositionX()
	{
		return owner.getPositionX();
	}

	@Override
	public float getPositionY()
	{
		return owner.getPositionY();
	}

	@Override
	public float getRotation()
	{
		return owner.getRotation();
	}
	
	/** 所有者の位置情報 */
	private final PostureInterface owner;
}
