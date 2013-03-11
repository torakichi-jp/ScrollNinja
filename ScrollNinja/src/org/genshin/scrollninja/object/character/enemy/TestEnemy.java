package org.genshin.scrollninja.object.character.enemy;

import org.genshin.scrollninja.GlobalDefine;
import org.genshin.scrollninja.render.AnimationRenderObject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * 実験用の敵オブジェクト
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class TestEnemy extends AbstractEnemy
{
	public TestEnemy(World world, Vector2 position)
	{
		super("data/jsons/collision/enemy.json", world);
		
		//---- 描画オブジェクトを生成する。
		final AnimationRenderObject ro = new AnimationRenderObject("data/jsons/render/enemy_sprite.json", "data/jsons/render/enemy_animation.json", this, GlobalDefine.RenderDepth.ENEMY);
		ro.setAnimation("Walk");
		addRenderObject(ro);
		
		//---- 初期座標を設定する。
		getCollisionObject().getBody().setTransform(position, 0.0f);
	}
	
	@Override
	public void update(float deltaTime)
	{
		super.update(deltaTime);
	}
	
}
