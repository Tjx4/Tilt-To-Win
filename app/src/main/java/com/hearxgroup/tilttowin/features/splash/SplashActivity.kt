gpackage com.hearxgroup.tilttowin.features.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hearxgroup.tilttowin.extensions.FADE_IN_ACTIVITY
import com.hearxgroup.tilttowin.extensions.navigateToActivity
import com.hearxgroup.tilttowin.features.game.GameActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigateToActivity(GameActivity::class.java, null, FADE_IN_ACTIVITY)
        finish()
    }
}