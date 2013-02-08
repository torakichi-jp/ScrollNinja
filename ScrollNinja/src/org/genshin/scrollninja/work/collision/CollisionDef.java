package org.genshin.scrollninja.work.collision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.genshin.scrollninja.GlobalDefine;

import com.badlogic.gdx.Gdx;
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
		json.writeFields(this);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void read(Json json, OrderedMap<String, Object> jsonData)
	{
		final float worldScale = GlobalDefine.INSTANCE.WORLD_SCALE;
		
		//---- BodyDef
		if(jsonData.containsKey("body"))
		{
			final OrderedMap<String, Object> bodyMap = json.readValue("body", OrderedMap.class, jsonData);
			
			bodyDef.type = json.readValue("isDynamic", Boolean.class, bodyMap) ? BodyType.DynamicBody : BodyType.StaticBody;
			bodyDef.bullet = json.readValue("isBullet", Boolean.class, bodyMap);
			bodyDef.fixedRotation = json.readValue("isFixedRotation", Boolean.class, bodyMap);
		}
		
		//---- FixtureDefs
		if(jsonData.containsKey("fixtures"))
		{
			final Array<OrderedMap<String, Object>> fixtureMaps = json.readValue("fixtures", Array.class, jsonData);
			for(OrderedMap<String, Object> fixtureMap : fixtureMaps)
			{
				final FixtureDef fixtureDef = new FixtureDef();
				
				//---- Shape以外
				readFixtureDef(json, fixtureMap, fixtureDef);
				
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
				}
				
				//---- マップに登録
				fixtureDefs.put(json.readValue("name", String.class, fixtureMap), fixtureDef);
			}
		}
		
		//---- BodyEditorFixtureDefs
		if(jsonData.containsKey("bodyEditorFixtures"))
		{
			final Array<OrderedMap<String, Object>> bodyEditorFixtureMaps = json.readValue("bodyEditorFixtures", Array.class, jsonData);
			bodyEditorFixtureDefs.ensureCapacity(bodyEditorFixtureMaps.size);
			
			for(OrderedMap<String, Object> bodyEditorFixtureMap : bodyEditorFixtureMaps)
			{
				final BodyEditorFixtureDef bodyEditorFixtureDef = new BodyEditorFixtureDef();
				final String path = json.readValue("filePath", String.class, bodyEditorFixtureMap);
				final Array<OrderedMap<String, Object>> fixtureMaps = json.readValue("fixtures", Array.class, bodyEditorFixtureMap);
				
				bodyEditorFixtureDef.jsonString = Gdx.files.internal(path).readString();
				bodyEditorFixtureDef.fixtureDefPairs.ensureCapacity(fixtureMaps.size);
				for(OrderedMap<String, Object> fixtureMap : fixtureMaps)
				{
					final FixtureDefPair fixtureDefPair = new FixtureDefPair();
					fixtureDefPair.fixtureDef = new FixtureDef();
					
					readFixtureDef(json, fixtureMap, fixtureDefPair.fixtureDef);

					fixtureDefPair.name = json.readValue("name", String.class, fixtureMap);
					fixtureDefPair.scale = json.readValue("scale", Float.class, fixtureMap);
					
					fixtureDefPair.scale *= worldScale;
					
					bodyEditorFixtureDef.fixtureDefPairs.add(fixtureDefPair);
				}
				
				bodyEditorFixtureDefs.add(bodyEditorFixtureDef);
			}
		}
	}
	

	@SuppressWarnings("unchecked")
	private void readFixtureDef(Json json, OrderedMap<String, Object> fixtureMap, FixtureDef outFixtureDef)
	{
		//---- Filter
		{
			final OrderedMap<String, Object> filterMap = json.readValue("filter", OrderedMap.class, fixtureMap);
			final Array<String> ignoreCategories = json.readValue("ignoreCategories", Array.class, filterMap);
			final CategoryBitsFactory cbFactory = CategoryBitsFactory.getInstance();
			final Filter filter = outFixtureDef.filter;
			
			filter.categoryBits = cbFactory.get(json.readValue("category", String.class, filterMap));
			
			for(String ignoreCategory : ignoreCategories)
			{
				filter.maskBits &= ~cbFactory.get(ignoreCategory);
			}
		}

		//---- Other Fields
		outFixtureDef.density = json.readValue("density", Float.class, fixtureMap);
		outFixtureDef.friction = json.readValue("friction", Float.class, fixtureMap);
		outFixtureDef.restitution = json.readValue("restitution", Float.class, fixtureMap);
		outFixtureDef.isSensor = json.readValue("isSensor", Boolean.class, fixtureMap);
	}
	
	/** Bodyの初期化用定義 */
	public final BodyDef bodyDef = new BodyDef();
	
	/** Fixtureの初期化用定義のマップ */
	public final Map<String, FixtureDef> fixtureDefs = new HashMap<String, FixtureDef>();
	
	/** BodyEditorを使ったFixtureの初期化用定義の配列 */
	public final ArrayList<BodyEditorFixtureDef> bodyEditorFixtureDefs = new ArrayList<BodyEditorFixtureDef>(1);
	
	
	/**
	 * BodyEditorで出力したJsonファイルからFixtureを生成する際の初期化用定義
	 */
	public class BodyEditorFixtureDef
	{
		/** Jsonファイルのテキストデータ */
		public String jsonString;
		
		/**  */
		public final ArrayList<FixtureDefPair> fixtureDefPairs = new ArrayList<FixtureDefPair>(1);
	}
	
	/**
	 * 
	 */
	public class FixtureDefPair
	{
		public String name;
		public float scale;
		public FixtureDef fixtureDef;
	}
}
