package org.genshin.scrollninja.screen;

import org.genshin.scrollninja.GlobalParam;
import org.genshin.scrollninja.object.AbstractObject;
import org.genshin.scrollninja.object.character.ninja.PlayerNinja;
import org.genshin.scrollninja.object.gui.Cursor;
import org.genshin.scrollninja.render.CameraTranslater;
import org.genshin.scrollninja.stage.Stage;
import org.genshin.scrollninja.stage.StageInterface;

import com.badlogic.gdx.physics.box2d.World;

/**
 * ゲームスクリーン
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class GameScreen extends AbstractDebugScreen		// FIXME リリース時はAbstractScreenを継承する。
{
	/**
	 * コンストラクタ
	 */
	public GameScreen()
	{
		final World world = getWorld();
		
		//---- カーソル
		Cursor cursor = getCursor();

		GlobalParam.INSTANCE.currentUpdatableManager.add(cursor, 0);
		GlobalParam.INSTANCE.currentRenderableManager.add(cursor, 0);
		
		//---- ステージを生成する。
		stage = new Stage(world);
		
		//---- 忍者を生成する。
		AbstractObject player = new PlayerNinja(world, stage.getStartPosition(), getCursor());
		
		GlobalParam.INSTANCE.currentUpdatableManager.add(player, 0);
		GlobalParam.INSTANCE.currentRenderableManager.add(player, 0);
		
		//---- カメラの追従設定
		final CameraTranslater cameraTranslater = new CameraTranslater(getCamera());
		cameraTranslater.addTargetObject(getCursor());
		cameraTranslater.addTargetObject(player);
		cameraTranslater.setTranslateArea(0.0f, 0.0f, 38.4f, 25.0f);

		GlobalParam.INSTANCE.currentUpdatableManager.add(cameraTranslater, 0);
	}

	@Override
	protected Cursor createCursor()
	{
		return new Cursor(getCamera(), 2.0f * GlobalParam.INSTANCE.WORLD_SCALE);
	}
	
	
	/** ステージオブジェクト */
	private final StageInterface stage;
}
