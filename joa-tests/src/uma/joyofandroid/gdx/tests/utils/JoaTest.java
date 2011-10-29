/*******************************************************************************
 * basically copied from the gdx-tests
 ******************************************************************************/


package uma.joyofandroid.gdx.tests.utils;

import com.badlogic.gdx.ApplicationListener;

public abstract class JoaTest implements ApplicationListener {
	public boolean needsGL20 () {
		return false;
	}

	public void create () {
	}

	public void resume () {
	}

	public void render () {
	}

	public void resize (int width, int height) {
	}

	public void pause () {
	}

	public void dispose () {
	}
}
