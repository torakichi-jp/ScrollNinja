package org.genshin.scrollninja.screen;

import java.util.EnumMap;
import java.util.Map;

import org.genshin.scrollninja.Global;
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
abstract class AbstractDebugScreen extends AbstractScreen
{
	/**
	 * コンストラクタ
	 */
	public AbstractDebugScreen()
	{
		for(InputType type : InputType.values())
			inputs.put(type, type.getInputHelper());
	}
	
	@Override
	public void render(float delta)
	{
		//---- メイン描画
		super.render(delta);
		
		//---- 入力の更新
		for(InputHelperInterface input : inputs.values())
			input.update();
		
		//---- 描画フラグの切り替え
		if( inputs.get(InputType.SwitchDebug).isTrigger() )
			renderEnabled = !renderEnabled;
		
		//---- デバッグ用描画
		if(renderEnabled)
			box2dDebugRenderer.render(getWorld(), getCamera().combined);
		
		//---- スクリーン切り替え
		if			( inputs.get(InputType.ToTitle).isRelease() )	Global.game.setScreen(new TitleScreen());
		else if		( inputs.get(InputType.ToMap).isRelease() )		Global.game.setScreen(new MapScreen());
		else if		( inputs.get(InputType.ToGame).isRelease() )	Global.game.setScreen(new GameScreen());
	}
	
	
	/** デバッグ入力群 */
	private final Map<InputType, InputHelperInterface> inputs = new EnumMap<InputType, InputHelperInterface>(InputType.class);
	
	/** Box2Dの衝突判定オブジェクトを描画するためのレンダラ  */
	private static final Box2DDebugRenderer box2dDebugRenderer = new Box2DDebugRenderer();
	
	/** 描画フラグ */
	private static boolean renderEnabled = false;
	
	
	/**
	 * 入力の種類
	 */
	private enum InputType
	{
		SwitchDebug		{ public InputHelperInterface getInputHelper() { return new KeyboardInputHelper(Keys.NUM_0); } },
		ToTitle			{ public InputHelperInterface getInputHelper() { return new KeyboardInputHelper(Keys.F1); } },
		ToMap			{ public InputHelperInterface getInputHelper() { return new KeyboardInputHelper(Keys.F2); } },
		ToGame			{ public InputHelperInterface getInputHelper() { return new KeyboardInputHelper(Keys.F3); } },
		;
		
		public abstract InputHelperInterface getInputHelper();
	}
}
