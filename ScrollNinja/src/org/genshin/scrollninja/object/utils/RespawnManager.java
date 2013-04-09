package org.genshin.scrollninja.object.utils;

import org.genshin.scrollninja.object.AbstractUpdatable;
import org.genshin.scrollninja.object.character.AbstractCharacter;
import org.genshin.scrollninja.stage.StageInterface;

/**
 * キャラクターの復活管理
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class RespawnManager extends AbstractUpdatable
{
	/**
	 * コンストラクタ
	 * @param ninja		復活を管理するキャラクタオブジェクト
	 * @param stage		現在のステージオブジェクト
	 */
	public RespawnManager(AbstractCharacter character, StageInterface stage)
	{
		this.character = character;
		this.stage = stage;
	}
	
	@Override
	public void update(float deltaTime)
	{
		//---- 死んでたら復活しよう。
		if(character.isDead())
		{
			character.respawn(stage.getStartPosition());
		}
	}

	@Override
	public void dispose()
	{
		super.dispose();
	}
	
	
	/** 復活を管理するキャラクタオブジェクト */
	private final AbstractCharacter character;
	
	/** 現在のステージオブジェクト */
	private final StageInterface stage;
}
