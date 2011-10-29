/*******************************************************************************
 * basically copied from the gdx-tests
 ******************************************************************************/

package uma.joyofandroid.gdx.tests.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uma.joyofandroid.gdx.tests.AntiAliasingPolygonTest;
import uma.joyofandroid.gdx.tests.BitmapFontMenuTest;
import uma.joyofandroid.gdx.tests.OneBodyMultipleFixturesTest;
import uma.joyofandroid.gdx.tests.RepeatingTextureOverMeshTest;



/** List of GdxTest classes. To be used by the test launchers. If you write your own test, add it in here!
 * 
 * @author badlogicgames@gmail.com */
public class JoaTests {
	public static final Class[] tests = {
		AntiAliasingPolygonTest.class,
		BitmapFontMenuTest.class,
		OneBodyMultipleFixturesTest.class, 
		RepeatingTextureOverMeshTest.class};

	public static String[] getNames () {
		List<String> names = new ArrayList<String>();
		for (Class clazz : tests)
			names.add(clazz.getSimpleName());
		Collections.sort(names);
		return names.toArray(new String[names.size()]);
	}

	public static JoaTest newTest (String testName) {
		try {
			Class clazz = Class.forName("uma.joyofandroid.gdx.tests." + testName);
			return (JoaTest)clazz.newInstance();
		} catch (Exception e1) {
			try {
				Class clazz = Class.forName("uma.joyofandroid.gdx.tests.gles2." + testName);
				return (JoaTest)clazz.newInstance();
			} catch (Exception e2) {
				try {
					Class clazz = Class.forName("uma.joyofandroid.gdx.tests.examples." + testName);
					return (JoaTest)clazz.newInstance();
				} catch(Exception e3) {
					e3.printStackTrace();
					return null;
				}
			}
		}
	}
}
