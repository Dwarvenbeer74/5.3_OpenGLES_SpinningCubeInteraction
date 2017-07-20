package dwarvenbeer.a53_opengles_spinningcubeinteraction;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class SpinningCubeActivity extends AppCompatActivity{
    final static String TAG = "OpenSurfaceViewLog";
    final static String X_SPEED = "mXspeed";
    final static String X_SPEED_SAVED = "mXspeedSaved";
    final static String Y_SPEED = "mYspeed";
    final static String Y_SPEED_SAVED = "mYspeedSaved";
    OpenSurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        surfaceView = new OpenSurfaceView(this);
        surfaceView.setRenderer((GLSurfaceView.Renderer) surfaceView);
        setContentView(surfaceView);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putFloat(X_SPEED, surfaceView.getXspeed());
        outState.putFloat(Y_SPEED, surfaceView.getYspeed());
        outState.putFloat(X_SPEED_SAVED, surfaceView.getXspeedSaved());
        outState.putFloat(Y_SPEED_SAVED, surfaceView.getYspeedSaved());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        surfaceView.setXspeed(savedInstanceState.getFloat(X_SPEED));
        surfaceView.setYspeed(savedInstanceState.getFloat(Y_SPEED));
        surfaceView.setXspeedSaved(savedInstanceState.getFloat(X_SPEED_SAVED));
        surfaceView.setYspeedSaved(savedInstanceState.getFloat(Y_SPEED_SAVED));
    }
}
