package org.genshin.scrollninja.work.object.background;

import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.work.object.AbstractObject;

/**
 * 背景オブジェクトの基本クラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public abstract class AbstractBackground extends AbstractObject
{
	@Override
	protected int getUpdatePriority()
	{
		return GlobalDefine.UpdatePriority.BACKGROUND;
	}
}
