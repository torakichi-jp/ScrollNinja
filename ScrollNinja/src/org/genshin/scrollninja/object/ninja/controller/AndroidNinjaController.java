package org.genshin.scrollninja.object.ninja.controller;

import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.object.ObjectInterface;
import org.genshin.scrollninja.utils.input.InputHelperInterface;
import org.genshin.scrollninja.utils.input.MouseInputHelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.math.Vector2;

/**
 * アンドロイド用忍者操作
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class AndroidNinjaController implements NinjaControllerInterface
{
	/**
	 * コンストラクタ
	 * @param ninja		忍者オブジェクト
	 * @param cursor	マウスカーソルオブジェクト
	 */
	public AndroidNinjaController(ObjectInterface ninja, ObjectInterface cursor)
	{
		inputHelper = new MouseInputHelper(Buttons.LEFT);
	}
	
	@Override
	public void update(float deltaTime)
	{
		inputHelper.update();
		
		//---- ダッシュ制御
		if(inputHelper.isTrigger())
		{
			dash = timer < 0.5f;
			timer = 0.0f;
		}
		timer += deltaTime;
		
		//---- 移動制御
		direction.x = Gdx.input.getX() - GlobalDefine.INSTANCE.CLIENT_WIDTH * 0.5f;
		movePower = inputHelper.isPress() ? Math.signum(direction.x) : 0.0f;
	}
	
	@Override
	public float getMovePower()
	{
		return movePower;
	}
	
	@Override
	public Vector2 getDirection()
	{
		return direction;
	}
	
	@Override
	public boolean isMoveStart()
	{
		return inputHelper.isTrigger();
	}
	
	@Override
	public boolean isDash()
	{
		return dash;
	}
	
	@Override
	public boolean isDashStart()
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean isJump()
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean isAerialJump()
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean isLeaveSnapCeiling()
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean isAttack()
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean isKaginawaSlack()
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean isKaginawaShrink()
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean isKaginawaHang()
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean isKaginawaRelease()
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	
	/** 動く力 */
	private float movePower = 0.0f;
	
	/** 向き */
	private final Vector2 direction = new Vector2();
	
	/** 入力管理 */
	private final InputHelperInterface inputHelper;
	
	/** ダブルクリック検出用たいまー */
	private float timer = 0.0f;
	
	/** ダッシュフラグ */
	private boolean dash = false;
}
