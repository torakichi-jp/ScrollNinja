package org.genshin.scrollninja.utils;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Jsonユーティリティ
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public class JsonUtils
{
	/**
	 * Jsonファイルからデータを読み込む。
	 * @param filePath		Jsonファイルのパス
	 * @param type			読み込むデータの型
	 * @return				読み込んだデータ
	 */
	public static <T> T read(String filePath, Class<T> type)
	{
		try
		{
			final ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.readValue(Gdx.files.internal(filePath).read(), type);
		}
		catch (JsonParseException e) { e.printStackTrace(); }
		catch (JsonMappingException e) { e.printStackTrace(); }
		catch (IOException e) { e.printStackTrace(); }
		
		return null;
	}
}
