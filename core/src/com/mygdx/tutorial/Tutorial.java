package com.mygdx.tutorial;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.TimeUtils;

import com.badlogic.gdx.utils.Array;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector3;

import java.util.Iterator;

public class Tutorial extends ApplicationAdapter
{
	private Vector2 stageSize;

	private Texture dropImage;
	private Texture bucketImage;
	private Sound dropSound;
	private Music rainMusic;

	private OrthographicCamera camera;
	private SpriteBatch batch;

	private Rectangle bucket;

	private Array<Rectangle> raindrops;
	private long lastDropTime;

	private void spawnRaindrop()
	{
		Rectangle raindrop = new Rectangle();

		raindrop.x = MathUtils.random(0, 800-64);
		raindrop.y = 480;
		raindrop.width = 64;
		raindrop.height = 64;

		raindrops.add(raindrop);

		lastDropTime = TimeUtils.nanoTime();
	}
	
	@Override
	public void create ()
	{
		stageSize = new Vector2(800, 480);

		// load the images for the droplet and the bucket, 64x64 pixels each
		dropImage = new Texture(Gdx.files.internal("droplet.png"));
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));

		// load the drop sound effect and the rain background "music"
		dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

		// start the playback of the background music immediately
		rainMusic.setLooping(true);
		rainMusic.play();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, stageSize.x, stageSize.y);

		batch = new SpriteBatch();

		bucket = new Rectangle(0.5f * stageSize.x, 0.5f * stageSize.y, bucketImage.getWidth(), bucketImage.getHeight());

		bucket.x -= 0.5f * bucketImage.getWidth();
		bucket.y = 20;

		raindrops = new Array<Rectangle>();
		spawnRaindrop();

		Gdx.gl.glClearColor(0, 123.f / 255.f, 167.f / 255.f, 1);
	}

	@Override
	public void render ()
	{
		// update
		if(TimeUtils.timeSinceNanos(lastDropTime) > 1e9)
			spawnRaindrop();

		Iterator<Rectangle> iter = raindrops.iterator();

		while(iter.hasNext())
		{
			// update
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();

			// handle collisions
			if(raindrop.overlaps(bucket))
			{
				dropSound.play();
				iter.remove();
			}

			if(raindrop.y + 64 < 0)
				iter.remove();
		}

		// cleanup
		for (int i = 0; i < raindrops.size; i++)
		{
			if (raindrops.get(i).y < -64.0f)
				raindrops.removeIndex(i);
		}

		if(Gdx.input.isTouched())
		{
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - 64 / 2;
		}

		if(bucket.x < 0)
			bucket.x = 0;

		if(bucket.x > 800 - 64)
			bucket.x = 800 - 64;

		camera.update();

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);

		batch.begin();

		batch.draw(bucketImage, bucket.x, bucket.y);

		for(Rectangle raindrop : raindrops)
		{
			batch.draw(dropImage, raindrop.x, raindrop.y);
		}

		batch.end();
	}
	
	@Override
	public void dispose ()
	{
		dropImage.dispose();
		bucketImage.dispose();

		dropSound.dispose();
		rainMusic.dispose();

		batch.dispose();
	}
}
