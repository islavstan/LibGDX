package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;
//https://www.youtube.com/watch?v=oRgBRm60jXY&index=3&list=PLyfVjOYzujuisikez6McGsBtKviTa_lty

public class MyGame extends ApplicationAdapter {
	OrthographicCamera camera;
	SpriteBatch batch;
	Texture dropImage;
	Texture bucketImage;
	Sound dropSound;//используем саунд если продолжительность звука меньше 10 сек
	Music rainMusic;
     Rectangle bucket;
	Vector3 touchPos;//трёхмерный вектор
	Array<Rectangle>rainDrops;
	long lastDropTime;//время последнего появления капли
	@Override
	public void create () {
		camera=new OrthographicCamera();
		camera.setToOrtho(false,800,480);//задали разрешение игры

		batch = new SpriteBatch();//класс для рисования 2д изображений
		//ссылки на картинки
		dropImage = new Texture("humburger.png");
		bucketImage = new Texture("roma.png");
         //cсылки на звуки
		dropSound=Gdx.audio.newSound(Gdx.files.internal("spuk1.wav"));
		rainMusic=Gdx.audio.newMusic(Gdx.files.internal("mario.mp3"));

		rainMusic.setLooping(true);//музыка будет постоянно повторяться
		rainMusic.play();
		touchPos = new Vector3();
		bucket=new Rectangle();
		//задаём расположение ведра на экране
		bucket.x =800/2 -64/2; //центрируем по горизонтали (800 -ширина, 64- ширина изображения
		bucket.y = 20;
		bucket.width=64;
		bucket.height=64;

		rainDrops=new Array<Rectangle>();
		spawnRainDrop();


	}
//метод для создания каплей
	private void spawnRainDrop(){
		Rectangle rainDrop =new Rectangle();
		rainDrop.x = MathUtils.random(0,800-64);
		rainDrop.y = 480;
		rainDrop.width=64;
		rainDrop.height=64;
		rainDrops.add(rainDrop);
		lastDropTime= TimeUtils.nanoTime();//берём текущее время в наносекундах



	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);//цвет очистки будет синим
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(bucketImage, bucket.x, bucket.y);
		for(Rectangle rainDrop: rainDrops){
			batch.draw(dropImage,rainDrop.x,rainDrop.y);
		}

		batch.end();

		if(Gdx.input.isTouched()) { //проверяем есть ли прикосновение к экрану
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);//возвращает нажатую позицию
			camera.unproject(touchPos);
			bucket.x = (int) (touchPos.x - 64 / 2);
		}
			if(bucket.x<0)bucket.x=0;
			if(bucket.x>800-64)bucket.x=800-64;
if(TimeUtils.nanoTime()-lastDropTime>1000000000)spawnRainDrop();
			Iterator<Rectangle>iter = rainDrops.iterator();
			while (iter.hasNext()){
				Rectangle rainDrop = iter.next();
				rainDrop.y -=200*Gdx.graphics.getDeltaTime();
				if(rainDrop.y+64<0){
					iter.remove(); //удаляем каплю если она падает ниже экрана
				}
				if(rainDrop.overlaps(bucket)){//если ведро и капля пересекаются, тоесть мы словили каплю, воспроизводим звук
					dropSound.play();
					iter.remove();

				}
			}

		}




	
	@Override
	public void dispose () {

		dropImage.dispose();
		dropSound.dispose();
		bucketImage.dispose();
		rainMusic.dispose();
		batch.dispose();
	//	img.dispose();
	}
}
