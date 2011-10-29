/*******************************************************************************
 * basically copied from the gdx-test preoject
 ******************************************************************************/

package uma.joyofandroid.gdx.tests.android;

import uma.joyofandroid.gdx.tests.utils.JoaTest;
import uma.joyofandroid.gdx.tests.utils.JoaTests;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;


public class JoaTestActivity extends AndroidApplication {

	public void onCreate (Bundle bundle) {
		super.onCreate(bundle);

		Bundle extras = getIntent().getExtras();
		String testName = (String)extras.get("test");

		JoaTest test = JoaTests.newTest(testName);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useGL20 = test.needsGL20();
		config.numSamples = 2;
		initialize(test, config);
	}
}
