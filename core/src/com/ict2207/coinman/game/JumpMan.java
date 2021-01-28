package com.ict2207.jumpman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import org.w3c.dom.css.Rect;

import java.util.ArrayList;
import java.util.Random;

import jdk.nashorn.internal.runtime.JSErrorType;

public class JumpMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	int score = 0;
	int gameState = 0;
	int stateoftheman = 0; // new int that called Stateman, is he in the 0 position etc
	int runslower = 0; // making the running become slower
	float gravity = 0.2f; // the man will fall
	float velocity = 0; // speed of guy falling
	int Yposofman = 0;
	Texture dizzy;
	Rectangle manRectangle;
	BitmapFont font; // font at the side of the app

	Random random;

	ArrayList<Integer> mushroomXs = new ArrayList<Integer>(); //array list of integers
	ArrayList<Integer> mushroomYs = new ArrayList<Integer>(); // Y axis of the coins
	ArrayList<Rectangle> mushroomRectangles = new ArrayList<Rectangle>();
	Texture mushroom;
	int mushroomCount; // proper spacing of the coin

	ArrayList<Integer> bombX = new ArrayList<Integer>(); //array list of integers
	ArrayList<Integer> bombYs = new ArrayList<Integer>(); // Y axis of the coins
	ArrayList<Rectangle> bombRectangles = new ArrayList<Rectangle>();
	Texture bomb;
	int bombCount; // proper spacing of the bomb

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("gbtb.png"); // background photo
		man = new Texture[4]; // 4 images
		man[0] = new Texture("frame-1.png");
		man[1] = new Texture("frame-2.png");
		man[2] = new Texture("frame-3.png");
		man[3] = new Texture("frame-4.png");
		Yposofman = Gdx.graphics.getHeight() / 2;

		mushroom = new Texture("mushroom.png");
		bomb = new Texture("bomb.png");
		random = new Random(); // random places of bombs

		dizzy = new Texture("dizzy-1.png");

		font = new BitmapFont();
		font.setColor((Color.GREEN));
		font.getData().setScale(11);
	}
	public void makingMushrooms() {
		float height = random.nextFloat() * Gdx.graphics.getHeight(); // chooses where the coin will be on the screen
		mushroomYs.add((int)height);
		mushroomXs.add(Gdx.graphics.getWidth());
	}
	public void makeBomb() {
		float height = random.nextFloat() * Gdx.graphics.getHeight(); // chooses where the coin will be on the screen
		bombYs.add((int)height);
		bombX.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render () { // constantly repeating loops
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // where do u want the photo to start and end

		if (gameState == 1) {
			// GAME IS LIVE NOW!!
			// BOMBS
			if (bombCount < 250) { // if count is 100, running 100 times
				bombCount++;
			} else { // if its not the case
				bombCount = 0;
				makeBomb(); // make a bomb and add another bomb
			}
			bombRectangles.clear();
			for (int i = 0; i < bombX.size(); i++) { // draw this bomb. Do a for loop to know the number of bomb
				batch.draw(bomb, bombX.get(i), bombYs.get(i)); // draw the bomb texture and we know where the bomb x and y position will be
				bombX.set(i, bombX.get(i) - 8); // to show that the bomb will move to the left of the screen
				bombRectangles.add(new Rectangle(bombX.get(i), bombYs.get(i), bomb.getWidth(), bomb.getHeight()));
			}

			// MUSHROOM
			if (mushroomCount < 100) { // if count is 100, running 100 times
				mushroomCount++;
			} else { // if its not the case
				mushroomCount = 0;
				makingMushrooms(); // make a mushroom and add another mushroom
			}

			mushroomRectangles.clear();
			for (int i = 0; i < mushroomXs.size(); i++) { // draw this mushroom. Do a for loop to know the number of mushrooms
				batch.draw(mushroom, mushroomXs.get(i), mushroomYs.get(i)); // draw the coin texture and we know where the coin x and y position will be
				mushroomXs.set(i, mushroomXs.get(i) - 6); // to show that the coin will move to the left of the screen
				mushroomRectangles.add(new Rectangle(mushroomXs.get(i), mushroomYs.get(i), mushroom.getWidth(), mushroom.getHeight()));
			}
			if (Gdx.input.justTouched()) { // taps the screen
				velocity = -9;
			}

			if (runslower < 8) {
				runslower++;
			} else {

				runslower = 0;
				if (stateoftheman < 3) { // For example 0 when it starts , making him look like he is running
					stateoftheman++;
				} else {
					stateoftheman = 0;
				}
			}

			velocity += gravity; //
			Yposofman -= velocity; // man's y position
			if (Yposofman <= 0) { // He will stay at the bottom of the phone
				Yposofman = 0;
			}
		} else if (gameState == 0) { // the starting position of the game
			//waiting the user to start the game
			if (Gdx.input.justTouched()) {
				gameState = 1;
			}
		} else if (gameState == 2) {
			// it is GAME OVER!!!
			// RESTART to the starting position
			if (Gdx.input.justTouched()) {
				gameState = 1;
				Yposofman = Gdx.graphics.getHeight() / 2;
				score = 0;
				velocity = 0;
				mushroomXs.clear();
				mushroomYs.clear();
				mushroomRectangles.clear();
				mushroomCount = 0;

				bombX.clear();
				bombYs.clear();
				bombRectangles.clear();
				bombCount = 0;
			}
		}
		if (gameState == 2) { // game over situation
			 batch.draw(dizzy,Gdx.graphics.getWidth() / 2 - man[stateoftheman].getWidth() / 2, Yposofman);
		} else {
			batch.draw(man[stateoftheman], Gdx.graphics.getWidth() / 2 - man[stateoftheman].getWidth() / 2, Yposofman); // fill center of the screen, figure out the math of it
		}
		manRectangle = new Rectangle(Gdx.graphics.getWidth() / 2 - man[stateoftheman].getWidth() / 2, Yposofman, man[stateoftheman].getWidth(), man[stateoftheman].getHeight());
		// running into a mushroom
		for (int i = 0; i < mushroomRectangles.size(); i++) {
			if (Intersector.overlaps(manRectangle, mushroomRectangles.get(i))) {
				score++;

				mushroomRectangles.remove(i);
				mushroomXs.remove(i);
				mushroomYs.remove(i);
				break;
			}
		}

		// running into a bomb
		for (int i = 0; i < bombRectangles.size(); i++) {
			if (Intersector.overlaps(manRectangle, bombRectangles.get(i))) {
				Gdx.app.log("There is a Bomb!", "Collided with the bomb!");
				gameState = 2;
			}
		}
		font.draw(batch, String.valueOf(score), 100, 200);
		batch.end();
	}

	@Override
	public void dispose(){
		batch.dispose();

	}
}
