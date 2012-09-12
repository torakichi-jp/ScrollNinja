package org.genshin.scrollninja;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ScrollNinja implements ApplicationListener {
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	private Texture tesTex;
	private Sprite sprite;
	private Sprite tesSpr;

	float rotation;

	@Override
	public void create() {
		rotation = 0;
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(w, h);
		batch = new SpriteBatch();

		texture = new Texture(Gdx.files.internal("data/test_matsu.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		TextureRegion region = new TextureRegion(texture, 0, 0, 800, 600);

		sprite = new Sprite(region);
		sprite.setSize(sprite.getWidth(), sprite.getHeight());
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		sprite.setPosition(-sprite.getWidth()/2, -sprite.getHeight()/2);

		tesTex = new Texture(Gdx.files.internal("data/chara.png"));
		tesTex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion tesReg = new TextureRegion(tesTex, 0, 0, 64, 64);

		tesSpr = new Sprite(tesReg);
		//tesSpr.setSize(0.1f, 0.1f);
		tesSpr.setOrigin(32, 32);
		tesSpr.setPosition(0, 0);
	}

	@Override
	public void dispose() {
		batch.dispose();
		texture.dispose();
		tesTex.dispose();
	}



	@Override
	public void render() {
		//tesSpr.setRotation(rotation);
		rotation++;
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		sprite.draw(batch);
		tesSpr.draw(batch);
		batch.end();

		if (Gdx.input.isKeyPressed(Keys.LEFT))
			tesSpr.setX(tesSpr.getX() - 200 * Gdx.graphics.getDeltaTime());
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			tesSpr.setX(tesSpr.getX() + 200 * Gdx.graphics.getDeltaTime());

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
