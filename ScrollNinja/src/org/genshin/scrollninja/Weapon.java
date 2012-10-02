package org.genshin.scrollninja;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class Weapon {
	
	private int m_texture;
	private Vector2 position;
	private ArrayList<WeaponProto> weapons;
	

	
	public Weapon()
	{
		this.position = new Vector2(0,0);
	}
}
