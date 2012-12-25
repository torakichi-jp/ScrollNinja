package org.genshin.scrollninja.stage;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;


/**
 * ステージ
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class Stage extends AbstractStage
{
	/**
	 * コンストラクタ
	 * @param world		所属先となるWorldオブジェクト
	 */
	public Stage(World world)
	{
		//---- 地形を生成
		// 描画オブジェクト
		
		// 衝突判定
		BodyDef bd	= new BodyDef();
		bd.type		= BodyType.StaticBody;		// 動かない物体
		
		FixtureDef fd	= new FixtureDef();
		fd.density = 1.0f;
		fd.friction = 0.5f;

		BodyEditorLoader loader = new BodyEditorLoader( Gdx.files.internal("data/stages/test.json") );
		loader.attachFixture(world.createBody(bd), "bgTest", fd, 38.4f);
		
		setStartPosition(38.4f * 0.5f, 25.0f * 0.5f);
	}
}
