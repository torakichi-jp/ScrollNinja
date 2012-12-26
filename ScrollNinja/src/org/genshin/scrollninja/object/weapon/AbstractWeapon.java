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
	 * @param directionX	攻撃する方向X
	 * @param directionY	攻撃する方向Y
	 */
	public abstract void attack(float directionX, float directionY);
	
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
