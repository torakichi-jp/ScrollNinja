package org.genshin.scrollninja;

//========================================
// インポート
//========================================
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;

//========================================
// クラス宣言
//========================================
public class StageObject extends ObJectBase {
	private String		name;			// 名前
	private Vector2		position;		// 座標

	// コンストラクタ
	StageObject(String Name) {
		name		= new String(Name);
		position	= new Vector2(0,0);

		Create();
	}

	public void Create() {
		Texture texture = new Texture(Gdx.files.internal("data/obj_gravestone.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion tmpRegion = new TextureRegion(texture, 0, 0, 256, 256);
		sprite = new Sprite(tmpRegion);
		sprite.setPosition(-sprite.getWidth() * 0.5f, -sprite.getHeight() * 0.5f);
		sprite.setScale(0.1f);
	}

	//************************************************************
	// Get
	// ゲッターまとめ
	//************************************************************
	public StageObject GetStageObject() { return this; }
	public String GetName(){ return name; }

	//************************************************************
	// Set
	// セッターまとめ
	//************************************************************
}
