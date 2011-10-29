/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package uma.joyofandroid.gdx.tests;

import java.util.ArrayList;
import java.util.Random;

import uma.joyofandroid.gdx.tests.utils.JoaTest;
import uma.joyofandroid.gdx.utils.BitmapFontMenu;
import uma.joyofandroid.gdx.utils.BitmapFontMenu.AnchorPosition;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class BitmapFontMenuTest extends JoaTest {

	SpriteBatch batcher;

	BitmapFont bitmapFont1;
	BitmapFont bitmapFont2;

	protected int graphicsWidth;
	protected int graphicsHeight;

	protected OrthographicCamera guiCam;

	String fontName = "comic-cyan-blue-bold-black-32";

	String menuString1 = "fun\n\"change font\"\n\"new anchor pos\"\n\"switch menu\" uma";
	String menuString2 = "well\nnonsense\n\"change anchor\"\n\"switch menu\"\numa";
	
	String oha = "";

	ArrayList<ArrayList<Rectangle>> allRectangleLines;

	private ShapeRenderer renderer;

	Vector3 touchPoint;

	BitmapFontMenu bfm;
	
	Vector2 anchor1;
	Vector2 anchor2;

	@Override
	public void create() {
		// TODO Auto-generated method stub

		graphicsWidth = Gdx.graphics.getWidth();
		graphicsHeight = Gdx.graphics.getHeight();

		anchor1 = new  Vector2(0,0);
		anchor2 = new  Vector2(graphicsWidth/10,graphicsHeight/10);
		
		guiCam = new OrthographicCamera(graphicsWidth, graphicsHeight);
		
		
		
		guiCam.position.set(graphicsWidth / 2, graphicsHeight / 2, 0);

		renderer = new ShapeRenderer();

		bitmapFont1 = new BitmapFont(Gdx.files.internal("data/fonts/"
				+ fontName + ".fnt"), Gdx.files.internal("data/fonts/"
				+ fontName + ".png"), false, true);

		bitmapFont2 = new BitmapFont();

		batcher = new SpriteBatch();

		touchPoint = new Vector3();

		
		bfm = new BitmapFontMenu(bitmapFont1, menuString1, anchor1,
				BitmapFontMenu.AnchorPosition.CENTERED,5);

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
		// TODO Auto-generated method stub

		GLCommon gl = Gdx.gl;
		// gl.glClearColor(1, 0, 0, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		//renderer.setProjectionMatrix(guiCam.combined);
		//bfm.debugDrawRectangle(renderer);
		
		
		batcher.setProjectionMatrix(guiCam.combined);

		batcher.begin();
		bfm.drawMenus(batcher);

		if (Gdx.input.justTouched()) {

			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(),
					0));

			String menuPoint = bfm.checkTouchPoint(touchPoint.x, touchPoint.y);

			if (menuPoint != null) {
				oha = menuPoint;
				
				
				if (menuPoint.compareTo("change font") == 0) {
					bfm.setFont((bfm.getFont() == bitmapFont1) ? bitmapFont2 : bitmapFont1);
				} else if (menuPoint.compareTo("new anchor pos") == 0) {
					AnchorPosition ap;
					do {
						ap = getRandomAnchorPosition();
					} while (ap == bfm.getAnchorPosition());
					bfm.setAnchorPosition(ap);
					oha += ": " + ap.name();
				} else if (menuPoint.compareTo("change anchor") == 0) {
					bfm.setAnchor(bfm.getAnchor() == anchor1 ? anchor2 : anchor1);
					oha += ": " + bfm.getAnchor().toString();
				} else if (menuPoint.compareTo("switch menu") == 0) {
					bfm.setMenuString(bfm.getMenuString() == menuString1 ? menuString2 : menuString1);
				}
				
			} else {
				oha = "";
			}
		}

		bitmapFont2.draw(batcher, oha, -graphicsWidth/2+10, -graphicsHeight/2+30);

		batcher.end();

	}
	
	private AnchorPosition getRandomAnchorPosition() {
		//just a demo ;)
		return AnchorPosition.values()[new Random().nextInt(AnchorPosition.values().length)]; 
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
