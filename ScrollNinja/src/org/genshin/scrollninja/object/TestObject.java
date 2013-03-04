package org.genshin.scrollninja.object;

import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.utils.debug.DebugString;
import org.genshin.scrollninja.utils.input.InputHelperInterface;
import org.genshin.scrollninja.utils.input.KeyboardInputHelper;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

/**
 * 実験用オブジェクト
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class TestObject extends AbstractObject
{
	/**
	 * コンストラクタ
	 */
	public TestObject(Vector2 position)
	{
		this.position.set(position);
	}
	
	@Override
	public void update(float deltaTime)
	{
		//---- 入力更新！
		upInput.update();
		downInput.update();
		leftInput.update();
		rightInput.update();
		triggerInput.update();
		dashInput.update();
		
		//---- うごくよ！
		final float spd = this.speed * (dashInput.isPress() ? 10.0f : 1.0f);
		if(triggerInput.isPress())
		{
			if(upInput.isTrigger())		position.y += spd;
			if(downInput.isTrigger())	position.y -= spd;
			if(leftInput.isTrigger())	position.x -= spd;
			if(rightInput.isTrigger())	position.x += spd;
		}
		else
		{
			if(upInput.isPress())		position.y += spd;
			if(downInput.isPress())		position.y -= spd;
			if(leftInput.isPress())		position.x -= spd;
			if(rightInput.isPress())	position.x += spd;
		}
		
		//---- デバッグ出力
		DebugString.add("Test Position: " + position);
	}
	
	@Override
	public void dispose()
	{
		position = null;
		
		super.dispose();
	}
	
	@Override
	public float getPositionX()
	{
		return position.x;
	}
	
	@Override
	public float getPositionY()
	{
		return position.y;
	}
	
	@Override
	public float getRotation()
	{
		return 0;
	}
	
	
	/** 座標 */
	private Vector2 position = new Vector2();
	
	/** 速度 */
	private final float speed = 1.0f * GlobalDefine.INSTANCE.WORLD_SCALE;
	
	/** 入力オブジェクト */
	private final InputHelperInterface upInput = new KeyboardInputHelper(Keys.W);
	private final InputHelperInterface downInput = new KeyboardInputHelper(Keys.S);
	private final InputHelperInterface leftInput = new KeyboardInputHelper(Keys.A);
	private final InputHelperInterface rightInput = new KeyboardInputHelper(Keys.D);
	private final InputHelperInterface triggerInput = new KeyboardInputHelper(Keys.CONTROL_LEFT);
	private final InputHelperInterface dashInput = new KeyboardInputHelper(Keys.SHIFT_LEFT);
}
