package org.genshin.scrollninja.screen;

import org.genshin.scrollninja.utils.input.InputHelperInterface;
import org.genshin.scrollninja.utils.input.KeyboardInputHelper;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

/**
 * デバッグ用のスクリーンの基本クラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public abstract class AbstractDebugScreen extends AbstractScreen
{
	@Override
	public void render(float delta)
	{
		//---- 描画フラグの切り替え
		switchDebugInput.update();
		if( switchDebugInput.isTrigger() )
			renderEnabled = !renderEnabled;
		
		//---- メイン描画
		super.render(delta);
		
		//---- デバッグ用描画
		if(renderEnabled)
			box2dDebugRenderer.render(getWorld(), getCamera().combined);
	}
	
	/** デバッグ表示の切り替え入力用 */
	private final InputHelperInterface switchDebugInput = new KeyboardInputHelper(Keys.NUM_0);
	
	/** Box2Dの衝突判定オブジェクトを描画するためのレンダラ  */
	private static final Box2DDebugRenderer box2dDebugRenderer = new Box2DDebugRenderer();
	
	/** 描画フラグ */
	private static boolean renderEnabled = false;
}
