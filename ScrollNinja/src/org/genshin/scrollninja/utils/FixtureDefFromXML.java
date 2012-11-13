package org.genshin.scrollninja.utils;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * Fixtureの定義をXMLから取得し、生成する。
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class FixtureDefFromXML
{
	/**
	 * コンストラクタ
	 * @param element	XMLエレメントオブジェクト
	 */
	public FixtureDefFromXML(Element element)
	{
		element = element.getChildByName("FixtureDef");
		density		= element.getFloat("Density", 0.0f);
		friction	= element.getFloat("Friction", 0.2f);
		restitution	= element.getFloat("Restitution", 0.0f);
	}
	
	/**
	 * この定義を元にしたFixtureDefオブジェクトを生成する。
	 * @return		この定義を元にしたFixtureDefオブジェクト
	 */
	public FixtureDef createFixtureDef()
	{
		FixtureDef fd = new FixtureDef();
		
		fd.density		= density;
		fd.friction		= friction;
		fd.restitution	= restitution;
		
		return fd;
	}
	
	/** 密度（kg/m^2） */
	private final float density;
	
	/** 摩擦係数 */
	private final float friction;
	
	/** 反発係数 */
	private final float restitution;
}
