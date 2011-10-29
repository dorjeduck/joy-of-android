package uma.joyofandroid.gdx.tests;



import uma.joyofandroid.gdx.tests.utils.JoaTest;
import uma.joyofandroid.gdx.utils.antialiasing.AntiAliasedPolygonT2;
import uma.joyofandroid.gdx.utils.antialiasing.ColoredPolygon;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class AntiAliasingPolygonTest extends JoaTest {

	
	static final int NUMBER_OF_STARS = 3;
	
	ColoredPolygon coloredPolygons[] = new ColoredPolygon[NUMBER_OF_STARS];

	OrthographicCamera cam;
	
	SpriteBatch batch;
	BitmapFont font;

	@Override
	public void create() {
		// TODO Auto-generated method stub

		cam = new OrthographicCamera();

		cam.viewportWidth = 8;
		cam.viewportHeight = 12;
		
		font = new BitmapFont();
		batch = new SpriteBatch();

	// cam.position.set(-3,-2,0);

		Color[] colors = new Color[] { 
				new Color(0, 0, 255, 255),
				new Color(255, 255, 255, 255), 
				new Color(100, 0, 100, 255), 
				new Color(255, 255, 0, 255), 
				new Color(255, 0, 0, 255),
				new Color(100, 100, 255, 255), 
				
				new Color(0, 255, 255, 255), 
				new Color(255, 255, 0, 255) };

		Vector2[] vertices = { 
				new Vector2(0,0.6f),
				new Vector2(0.15f,0.15f),
				new Vector2(0.6f,0),
				new Vector2(0.15f,-0.15f),
				new Vector2(0,-0.6f),
				new Vector2(-0.15f,-0.15f),
				new Vector2(-0.6f,0),
				new Vector2(-0.15f,0.15f)};

		
		
		for (int i=0;i<NUMBER_OF_STARS;i++) {
			//coloredPolygons[i] = new ColoredPolygon(vertices, colors);
			coloredPolygons[i] = new AntiAliasedPolygonT2(vertices, colors,0.04f);
			
			coloredPolygons[i].transform(new Vector2((float)(-4+Math.random()*8),((float)(-3+Math.random()*6))), 2, 2, 0);
		}
		
		
		

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void render() {
		GL10 gl = Gdx.app.getGraphics().getGL10();
		// TODO Auto-generated method stub

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glEnable(GL10.GL_BLEND); // alpha values
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_SRC_ALPHA);

		cam.update();
		cam.apply(gl);

		//
		for (int i=0;i<NUMBER_OF_STARS;i++) {
			coloredPolygons[i].transform(new Vector2(0,0), 1, 1, 1);
			coloredPolygons[i].drawYourself();
		}
		// System.out.println(Gdx.graphics.getFramesPerSecond());
		
		
		batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		batch.begin();
		font.draw(batch, "fps:" + Gdx.graphics.getFramesPerSecond(), 3, 20);
		batch.end();

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
