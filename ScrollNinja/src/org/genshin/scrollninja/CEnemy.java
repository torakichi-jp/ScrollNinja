package org.genshin.scrollninja;

import com.badlogic.gdx.math.Vector2;

public class CEnemy extends CCharacterBase {
	// コンストラクタ
	CEnemy(){
		m_Hp = 100;
		m_AttackNum = 0;
		m_Position = new Vector2(0,0);
		m_Speed = 0;
		m_TextureNum = 0;
	}
}
