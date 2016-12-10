package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
//https://www.youtube.com/watch?v=oRgBRm60jXY&index=3&list=PLyfVjOYzujuisikez6McGsBtKviTa_lty
15.56
public class MyGame extends ApplicationAdapter {
	OrthographicCamera camera;
	SpriteBatch batch;
	Texture dropImage;
	Texture bucketImage;
	Sound dropSound;//используем саунд если продолжительность звука меньше 10 сек
	Music rainMusic;
     Rectangle bucket;
	@Override
	public void create () {
		camera=new OrthographicCamera();
		camera.setToOrtho(false,800,480);//задали разрешение игры

		batch = new SpriteBatch();//класс для рисования 2д изображений
		//ссылки на картинки
		dropImage = new Texture("droplet.png");
		bucketImage = new Texture("bucket.png");
         //cсылки на звуки
		dropSound=Gdx.audio.newSound(Gdx.files.internal("waterdrop.wav"));
		rainMusic=Gdx.audio.newMusic(Gdx.files.internal("undertreeinrain.mp3"));

		rainMusic.setLooping(true);//музыка будет постоянно повторяться
		rainMusic.play();

		bucket=new Rectangle();
		//задаём расположение ведра на экране
		bucket.x =800/2 -64/2; //центрируем по горизонтали (800 -ширина, 64- ширина изображения
		bucket.y = 20;
		bucket.width=64;
		bucket.height=64;


	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);//цвет очистки будет синим
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(bucketImage, bucket.x, bucket.y);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	//	img.dispose();
	}
}
