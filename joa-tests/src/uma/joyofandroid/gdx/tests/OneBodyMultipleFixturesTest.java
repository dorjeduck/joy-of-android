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

import uma.joyofandroid.gdx.tests.utils.JoaTest;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;


public class OneBodyMultipleFixturesTest extends JoaTest implements InputProcessor {

	
	   Body m_body;

	   /** the camera **/
	   protected OrthographicCamera camera;

	   /** the renderer **/
	   protected Box2DDebugRenderer renderer;

	   SpriteBatch batch;
	   BitmapFont font;

	   /** our box2D world **/
	   protected World world;

	   /** ground body to connect the mouse joint to **/
	   protected Body groundBody;

	   /** our mouse joint **/
	   protected MouseJoint mouseJoint = null;

	   /** a hit body **/
	   protected Body hitBody = null;

	   protected long round = 0;

	   @Override
	   public void create() {
	      // setup the camera. In Box2D we operate on a
	      // meter scale, pixels won't do it. So we use
	      // an orthographic camera with a viewport of
	      // 48 meters in width and 32 meters in height.
	      // We also position the camera so that it
	      // looks at (0,16) (that's where the middle of the
	      // screen will be located).
	      camera = new OrthographicCamera(Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/10);
	      camera.position.set(0, 15, 0);

	      // create the debug renderer
	      renderer = new Box2DDebugRenderer();

	      // create the world
	      world = new World(new Vector2(0, -10), true);

	      // we also need an invisible zero size ground body
	      // to which we can connect the mouse joint
	      BodyDef bodyDef = new BodyDef();
	      groundBody = world.createBody(bodyDef);

	      // call abstract method to populate the world
	      createWorld(world);

	      batch = new SpriteBatch();
	      font = new BitmapFont();

	      // register ourselfs as an InputProcessor
	      Gdx.input.setInputProcessor(this);

	   }

	   protected void createWorld(World world) {

	      // next we create a static ground platform. This platform
	      // is not moveable and will not react to any influences from
	      // outside. It will however influence other bodies. First we
	      // create a PolygonShape that holds the form of the platform.
	      // it will be 100 meters wide and 2 meters high, centered
	      // around the origin
	      PolygonShape groundPoly = new PolygonShape();
	      groundPoly.setAsBox(Gdx.graphics.getWidth()/10, 1);
	      

	      // next we create the body for the ground platform. It's
	      // simply a static body.
	      BodyDef groundBodyDef = new BodyDef();
	      groundBodyDef.type = BodyType.StaticBody;

	      groundBody = world.createBody(groundBodyDef);
	      

	      // finally we add a fixture to the body using the polygon
	      // defined above. Note that we have to dispose PolygonShapes
	      // and CircleShapes once they are no longer used. This is the
	      // only time you have to care explicitely for memomry managment.
	      Fixture gr = groundBody.createFixture(groundPoly, 10);
	      gr.setFriction(10);

	      groundPoly.dispose();

	      BodyDef boxBodyDef = new BodyDef();
	      boxBodyDef.type = BodyType.DynamicBody;
	      boxBodyDef.position.set(new Vector2(0, 20));

	      Body parentBody = world.createBody(boxBodyDef);

	      PolygonShape boxPoly = new PolygonShape();

	      Vector2[] vertices = new Vector2[4];
	      vertices[0] = new Vector2(-0.7f, 0);
	      vertices[1] = new Vector2(0.7f, 0);
	      vertices[2] = new Vector2(0.7f, 10);
	      vertices[3] = new Vector2(-0.7f, 10);

	      boxPoly.set(vertices);

	      FixtureDef fDef = new FixtureDef();
	      fDef.shape = boxPoly;
	      fDef.density = 0.5f;

	      parentBody.createFixture(fDef);

	      vertices[0] = new Vector2(-0.7f, -2);
	      vertices[1] = new Vector2(8.7f, -2);
	      vertices[2] = new Vector2(8.7f, 0);
	      vertices[3] = new Vector2(-0.7f, 0);

	      boxPoly.set(vertices);

	 
	      parentBody.createFixture(fDef);

	      vertices[0] = new Vector2(3, 3);
	      vertices[1] = new Vector2(5, 4);
	      vertices[2] = new Vector2(5, 7);
	      vertices[3] = new Vector2(3, 7);

	      boxPoly.set(vertices);

	      parentBody.createFixture(fDef);
	      
	      
	      vertices[0] = new Vector2(-7, -6);
	      vertices[1] = new Vector2(-1, -6);
	      vertices[2] = new Vector2(-1, -5);
	      vertices[3] = new Vector2(-7, -5);

	      boxPoly.set(vertices);

	      parentBody.createFixture(fDef);

	      // we are done, all that's left is disposing the boxPoly
	      boxPoly.dispose();

	   }

	   @Override
	   public void resume() {
	      // TODO Auto-generated method stub

	   }

	   @Override
	   public void render() {

	      round++;

	      // update the world with a fixed time step
	      long startTime = System.nanoTime();
	      world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);
	      float updateTime = (System.nanoTime() - startTime) / 1000000000.0f;

	      startTime = System.nanoTime();
	      // clear the screen and setup the projection matrix
	      GL10 gl = Gdx.app.getGraphics().getGL10();
	      gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	      camera.update();
	      camera.apply(gl);

	      // render the world using the debug renderer
	      renderer.render(world,camera.combined);
	      float renderTime = (System.nanoTime() - startTime) / 1000000000.0f;

	      batch.begin();
	      font.draw(batch, "fps:" + Gdx.graphics.getFramesPerSecond()
	            + ", update: " + updateTime + ", render: " + renderTime
	            + ", round: " + round, 0, 20);
	      batch.end();

	   }

	   @Override
	   public void resize(int width, int height) {
	      // TODO Auto-generated method stub

	   }

	   @Override
	   public void pause() {
	      // TODO Auto-generated method stub

	   }

	   @Override
	   public void dispose() {
	      renderer.dispose();
	      world.dispose();

	      renderer = null;
	      world = null;
	      mouseJoint = null;
	      hitBody = null;
	   }

	   @Override
	   public boolean keyDown(int keycode) {
	      return false;
	   }

	   @Override
	   public boolean keyTyped(char character) {
	      return false;
	   }

	   @Override
	   public boolean keyUp(int keycode) {
	      return false;
	   }

	   /**
	    * we instantiate this vector and the callback here so we don't irritate the
	    * GC
	    **/
	   Vector3 testPoint = new Vector3();
	   QueryCallback callback = new QueryCallback() {
	      @Override
	      public boolean reportFixture(Fixture fixture) {
	         // if the hit point is inside the fixture of the body
	         // we report it
	         if (fixture.testPoint(testPoint.x, testPoint.y)) {
	            hitBody = fixture.getBody();
	            return false;
	         } else
	            return true;
	      }
	   };

	   @Override
	   public boolean touchDown(int x, int y, int pointer, int button) {
	      // translate the mouse coordinates to world coordinates
	      camera.unproject(testPoint.set(x, y, 0));
	      // ask the world which bodies are within the given
	      // bounding box around the mouse pointer
	      hitBody = null;
	      world.QueryAABB(callback, testPoint.x - 0.0001f, testPoint.y - 0.0001f,
	            testPoint.x + 0.0001f, testPoint.y + 0.0001f);

	      if (hitBody == groundBody)
	         hitBody = null;

	      // ignore kinematic bodies, they don't work with the mouse joint
	      if (hitBody != null && hitBody.getType() == BodyType.KinematicBody)
	         return false;

	      // if we hit something we create a new mouse joint
	      // and attach it to the hit body.
	      if (hitBody != null) {
	         MouseJointDef def = new MouseJointDef();
	         def.bodyA = groundBody;
	         def.bodyB = hitBody;
	         def.collideConnected = true;
	         def.target.set(testPoint.x, testPoint.y);
	         def.maxForce = 1000.0f * hitBody.getMass();

	         mouseJoint = (MouseJoint) world.createJoint(def);
	         hitBody.setAwake(true);
	      }

	      return false;
	   }

	   /** another temporary vector **/
	   Vector2 target = new Vector2();

	   @Override
	   public boolean touchDragged(int x, int y, int pointer) {
	      // if a mouse joint exists we simply update
	      // the target of the joint based on the new
	      // mouse coordinates
	      if (mouseJoint != null) {
	         camera.unproject(testPoint.set(x, y, 0));
	         mouseJoint.setTarget(target.set(testPoint.x, testPoint.y));
	      }
	      return false;
	   }

	   @Override
	   public boolean touchUp(int x, int y, int pointer, int button) {
	      // if a mouse joint exists we simply destroy it
	      if (mouseJoint != null) {
	         world.destroyJoint(mouseJoint);
	         mouseJoint = null;
	      }
	      return false;
	   }

	   @Override
	   public boolean touchMoved(int x, int y) {
	      return false;
	   }

	   @Override
	   public boolean scrolled(int amount) {
	      return false;
	   }

	}
