import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.*;

import java.awt.Font;
import java.io.InputStream;
import java.io.IOException;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.*;
import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.TrueTypeFont;

import org.lwjgl.openal.AL;
import org.newdawn.slick.openal.*;

import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Dodger
{
	static Player player;
	static Random rand;
	static int width = 800;
	static int height = 800;
	static List<Enemy> enemy;
	static Music mySound;
	static Text myText;
	static ExecutorService exe = Executors.newFixedThreadPool(4);

	public static void main(String[] args) throws InterruptedException, ExecutionException
	{
		initGL();
		System.out.println("done gl");
		setup();
		start();
		
	}

	/**
	* Initialize openGL
	*/
	public static void initGL()
	{
		try
		{
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle("Dodger");
			Display.create();
		}
		catch(LWJGLException e)
		{
			e.printStackTrace();
			Display.destroy();
			System.exit(0);
		}

		glEnable(GL_TEXTURE_2D);

		glShadeModel(GL_SMOOTH);
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_LIGHTING);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f); 
		glClearDepth(1);

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glMatrixMode(GL_MODELVIEW);
		
		glLoadIdentity();
		glMatrixMode(GL_PROJECTION);
		glOrtho(0, width, height, 0, 1 , -1);
		glMatrixMode(GL_MODELVIEW);

	}

	static Callable<Music> setupMusic = () -> {
		Music sound = new Music();
		return sound;
	};

	/**
	* Setup image of players and enemies
	*/
	public static void setup() throws InterruptedException, ExecutionException 
		{
		rand = new Random();
		int n_enemy = rand.nextInt(11) + 10;
		int x, y, size, speed;
		Future<Music> future = exe.submit(setupMusic);
		player = new Player(350, 650);
		enemy = new ArrayList<>();

		for(int i = 0; i <= n_enemy; i++)
		{
			x = rand.nextInt(width) + 1;
			y = rand.nextInt(height) + 101;;
			size = rand.nextInt(31) + 50;
			speed = rand.nextInt(5) + 5;

			enemy.add(new Enemy(x, y, size, speed));
		}

		while(!future.isDone())
		{
			myText = new Text();
		}
		mySound = future.get();
	}

	/**
	* Draw enemies
	*/
	public static void drawEnemies()
	{
		int x, y, size, speed; // in case reset
		for(Enemy index : enemy)
		{
			index.draw();
			if (index.y > height && rand.nextInt(10) == 4)
			{
				// Reseting
				x = rand.nextInt(width) + 1;
				y = 0;
				size = rand.nextInt(31) + 50;
				speed = rand.nextInt(5) + 5;

				index.reset(x, y, size, speed);
			}
			checkTouch(index);
		}
	}

	/**
	* Check input
	*/
	public static void checkKey()
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
		{
			player.move(5,0);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT))
		{
			player.move(-5,0);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_UP))
		{
			player.move(0,-5);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN))
		{
			player.move(0,5);
		}
	}

	static int[] enemyID = {0,0};
	/**
	* Check if player got killed
	* @param enemy
	*/
	public static void checkTouch(Enemy index)
	{
		boolean sameX = index.x - (0.5 *  index.size) <= player.x && index.x + (0.2 * index.size) >= player.x;
		boolean sameY = index.y - index.size <= player.y && index.y >= player.y;
		boolean sameEnemy = index.size == enemyID[0] && index.speed == enemyID[1];

		if(sameX && sameY && !sameEnemy)
		{
			player.hp = player.hp - 10;
			enemyID[0] = index.size;
			enemyID[1] = index.speed;
			mySound.touched(false);
			if(player.hp <= 0)
			{
				mySound.backgroundStart(0.0f);
				showLose();
			}
		}

	}

	/**
	* Display LOSE
	*/
	public static void showLose()
	{
		for(long wait = 0; wait < 1000000000L; wait++)
		{
			wait = wait;
		}

		while(!Display.isCloseRequested())
		{
			glClear(GL_COLOR_BUFFER_BIT);

			player.fall();
			for(Enemy i : enemy)
			{
				i.fall();
			}

			Display.update();
			Display.sync(60);
		}
		AL.destroy();
		System.exit(0);
	}

	static int score = 0;

	public static void start()
	{
		mySound.backgroundStart(1.0f);

		while(!Display.isCloseRequested())
		{
			glClear(GL_COLOR_BUFFER_BIT);

			score++;
			player.draw();
			drawEnemies();

			checkKey();

			myText.write(10, 20 ,"Score: " + Integer.toString(score));
			myText.write(10, 40, "HP: " + player.hp);
			Display.update();
			Display.sync(60);
		}

		Display.destroy();
		AL.destroy();
		System.exit(0);
	}
}

class Player
{
	public int x, y, hp;
	private Texture player;

	Player(int x, int y)
	{
		this.x = x;
		this.y = y;
		player = init();
		hp = 100;
	}

	/**
	* Initialize player
	*
	* @return   Texture image
	*/
	private Texture init()
	{
		try
		{
			return TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/player.png"));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	* Draw player
	*/
	public void draw()
	{
		Color.white.bind();
		glBindTexture(GL_TEXTURE_2D, player.getTextureID());

		glBegin(GL_QUADS);
			glTexCoord2f(0,0);
			glVertex2f(x, y);

			glTexCoord2f(1,0);
			glVertex2f(x + player.getTextureWidth(), y);

			glTexCoord2f(1,1);
			glVertex2f(x + player.getTextureWidth(), y + player.getTextureHeight());

			glTexCoord2f(0,1);
			glVertex2f(x, y + player.getTextureHeight());
		glEnd();
	}

	/**
	* Move the player
	*
	* @param change_x
	* @param change_y
	*/
	public void move(int change_x, int change_y)
	{
		this.x = this.x + change_x;
		this.y = this.y + change_y;
	}

	public void fall()
	{
		y = y + 10;
		draw();
	}
}

class Enemy
{
	public int x, y, size, speed;
	private Texture enemy;

	Enemy(int x, int y, int size, int speed)
	{
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.size = size;
		enemy = init();
	}

	/**
	* Initialize image
	* 
	* @return  Texture image
	*/
	private Texture init()
	{
		try
		{
			return TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/enemy.png"));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	* Change y coordinate and draw image on screen
	*/
	public void draw()
	{
		this.y = this.y + this.speed;
		Color.white.bind();
		glBindTexture(GL_TEXTURE_2D, enemy.getTextureID());

		glBegin(GL_QUADS);
			glTexCoord2f(0,0);
			glVertex2f(x, y);

			glTexCoord2f(1,0);
			glVertex2f(x + size, y);

			glTexCoord2f(1,1);
			glVertex2f(x + size, y + size);

			glTexCoord2f(0,1);
			glVertex2f(x, y + size);
		glEnd();
	}

	/**
	* Reset everything when enemy is out of screen
	*/
	public void reset(int x, int y, int size, int speed)
	{
		this.x = x;
		this.y = y;
		this.size = size;
		this.speed = speed;
	}

	public void fall()
	{
		y = y + 20;
		draw();
	}
}

class Music
{
	private Audio touchEffect, bgMusic;
	Music()
	{
		init();
	}

	public void init()
	{
		try
		{
			touchEffect = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("res/normal-hitnormal.wav"));
			bgMusic = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/music.ogg"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void touched(boolean override)
	{
		touchEffect.playAsSoundEffect(1.0f, 1.0f, override);
	}

	public void backgroundStart(float volume)
	{
		bgMusic.playAsMusic(volume, 0.0f, false);
	}
}

class Text
{
	private TrueTypeFont font;;

	Text()
	{
		init();
	}

	public void init()
	{
		Font awtFont = new Font("Times New Roman", Font.ITALIC, 24);
		font = new TrueTypeFont(awtFont, true);
	}

	public void write(int X, int Y , String stuff)
	{
		Color.white.bind();
		TextureImpl.bindNone();
		font.drawString(X ,Y, stuff);
	}
}
