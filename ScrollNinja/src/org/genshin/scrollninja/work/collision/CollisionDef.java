package org.genshin.scrollninja.work.collision;

import java.util.HashMap;
import java.util.Map;

import org.genshin.scrollninja.GlobalDefine;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.OrderedMap;


/**
 * 衝突判定の初期化用定義クラス
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class CollisionDef implements Json.Serializable
{
	@Override
	public void write(Json json)
	{
		/* 今回は必要ないので、何もしない */
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void read(Json json, OrderedMap<String, Object> jsonData)
	{
		//---- BodyDef
		{
			final OrderedMap<String, Object> bodyMap = json.readValue("body", OrderedMap.class, jsonData);

			bodyDef.type = json.readValue("isDynamic", Boolean.class, bodyMap) ? BodyType.DynamicBody : BodyType.StaticBody;
			bodyDef.bullet = json.readValue("isBullet", Boolean.class, bodyMap);
			bodyDef.fixedRotation = json.readValue("isFixedRotation", Boolean.class, bodyMap);
		}
		
		//---- FixtureDefs
		{
			final float worldScale = GlobalDefine.INSTANCE.WORLD_SCALE;
			final Array<OrderedMap<String, Object>> fixtureMaps = json.readValue("fixtures", Array.class, jsonData);
			for(OrderedMap<String, Object> fixtureMap : fixtureMaps)
			{
				final FixtureDef fixtureDef = new FixtureDef();
				
				//---- Shape
				{
					// 円
					if( fixtureMap.containsKey("circle") )
					{
						final OrderedMap<String, Object> shapeMap = json.readValue("circle", OrderedMap.class, fixtureMap);
						final CircleShape shape = new CircleShape();
						
						float radius = json.readValue("radius", Float.class, shapeMap);
						final Vector2 position = json.readValue("position", Vector2.class, shapeMap);
						
						radius *= worldScale;
						position.mul(worldScale);
						
						shape.setRadius(radius);
						shape.setPosition(position);
						
						fixtureDef.shape = shape;
					}
					// 矩形
					else if( fixtureMap.containsKey("rectangle") )
					{
						final OrderedMap<String, Object> shapeMap = json.readValue("rectangle", OrderedMap.class, fixtureMap);
						final PolygonShape shape = new PolygonShape();
						
						final Vector2 size = json.readValue("size", Vector2.class, shapeMap);
						final Vector2 offset = json.readValue("offset", Vector2.class, shapeMap);
						final Float degrees = json.readValue("degrees", Float.class, shapeMap);
						
						size.mul(worldScale);
						offset.mul(worldScale);
						
						shape.setAsBox(size.x, size.y, offset, degrees * MathUtils.degreesToRadians);
						
						fixtureDef.shape = shape;
					}
					// BodyEditor
					else if( fixtureMap.containsKey("bodyEditor") )
					{
						final OrderedMap<String, Object> shapeMap = json.readValue("bodyEditor", OrderedMap.class, fixtureMap);
					}
				}
				
				//---- Filter
				{
					final OrderedMap<String, Object> filterMap = json.readValue("filter", OrderedMap.class, fixtureMap);
					final Array<String> ignoreCategories = json.readValue("ignoreCategories", Array.class, filterMap);
					final CategoryBitsFactory cbFactory = CategoryBitsFactory.getInstance();
					final Filter filter = fixtureDef.filter;
					
					filter.categoryBits = cbFactory.get(json.readValue("category", String.class, filterMap));
					
					for(String ignoreCategory : ignoreCategories)
					{
						filter.maskBits &= ~cbFactory.get(ignoreCategory);
					}
				}

				//---- Other Fields
				fixtureDef.density = json.readValue("density", Float.class, fixtureMap);
				fixtureDef.friction = json.readValue("friction", Float.class, fixtureMap);
				fixtureDef.restitution = json.readValue("restitution", Float.class, fixtureMap);
				fixtureDef.isSensor = json.readValue("isSensor", Boolean.class, fixtureMap);
				
				//---- マップに登録
				fixtureDefs.put(json.readValue("name", String.class, fixtureMap), fixtureDef);
			}
		}
	}
	
	/** Bodyの初期化用定義 */
	public final BodyDef bodyDef = new BodyDef();
	
	/** Fixtureの初期化用定義のマップ */
	public final Map<String, FixtureDef> fixtureDefs = new HashMap<String, FixtureDef>();
}
