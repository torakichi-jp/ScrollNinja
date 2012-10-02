package org.genshin.scrollninja;

//========================================
// インポート
//========================================
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

//========================================
// クラス宣言
//========================================
public class CCharacterBase {
	// 変数宣言
	protected int 		m_Hp;						// HP							
	protected int 		m_AttackNum;				// 攻撃力
	protected int 		m_Speed;					// 素早さ
	protected int 		m_TextureNum;				// テクスチャ番号
	protected Vector2 	m_Position;					// 座標
	protected Body		m_Body;						// あたり判定用ボックス
	protected Sprite	m_Sprite;					// スプライト
	
	// コンストラクタ
	public CCharacterBase(){}
	
}