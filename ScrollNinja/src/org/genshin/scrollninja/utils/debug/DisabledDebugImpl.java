package org.genshin.scrollninja.utils.debug;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.World;

/**
 * リリース時の実装オブジェクト
 * @author kou
 * @since		1.0
 * @version	1.0
 */
final class DisabledDebugImpl implements DebugImplInterface
{
	@Override
	public void updateDebugCommand()
	{
		/** 何もしない */
	}

	@Override
	public void renderCollision(World world, Camera camera)
	{
		/** 何もしない */
	}
	
	@Override
	public void renderLog()
	{
		/** 何もしない */
	}

	@Override
	public void logToScreen(String message)
	{
		/** 何もしない */
	}
	
	@Override
	public void logToConsole(String message)
	{
		/** 何もしない */
	}
}
