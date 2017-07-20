package dwarvenbeer.a53_opengles_spinningcubeinteraction;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer{
    final static String TAG = "OpenSurfaceViewLog";
    private Cube mCube;
    private float mXrot;
    private float mYrot;

    private float mXspeed;
    private float mYspeed;
    private float mXspeedSaved = 0.0f;
    private float mYspeedSaved = 0.0f;

    private float startX = 0;
    private float startY = 0;
    private float stopX = 0;
    private float stopY = 0;

    public float getXspeed() {return mXspeed;}

    public void setXspeed(float xspeed) {mXspeed = xspeed;}

    public float getYspeed() {return mYspeed;}

    public void setYspeed(float yspeed) {mYspeed = yspeed;}

    public float getXspeedSaved() {return mXspeedSaved;}

    public void setXspeedSaved(float xspeedSaved) {mXspeedSaved = xspeedSaved;}

    public float getYspeedSaved() {return mYspeedSaved;}

    public void setYspeedSaved(float yspeedSaved) {mYspeedSaved = yspeedSaved;}

    public OpenSurfaceView(Context context) {
        super(context);

        requestFocus();
        setFocusableInTouchMode(true);

        mCube = new Cube();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, -10.0f);
        gl.glRotatef(mYrot, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(mXrot, 0.0f, 1.0f, 0.0f);
        mCube.draw(gl);
        gl.glLoadIdentity();
        mXrot += mXspeed;
        mYrot += mYspeed;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startX = event.getX();
            startY = event.getY();
            Log.d(TAG, "startX=" + startX + " ;startY=" +startY);
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            stopX = event.getX();
            stopY = event.getY();

            mXspeed =mXspeedSaved + (stopX-startX)*0.01f;
            mYspeed = mYspeedSaved + (stopY-startY)*0.01f;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            mYspeedSaved = mYspeed;
            mXspeedSaved = mXspeed;

            stopX = event.getX();
            stopY = event.getY();
            if (startX == stopX) {
                mXspeedSaved = 0;
                mXspeed = 0;
            }
            if (startY == stopY) {
                mYspeedSaved = 0;
                mYspeed = 0;
            }
            Log.d(TAG, "stopX=" + stopX + " ;stopY=" +stopY);
            Log.d(TAG, "ACTION_UP:mXspeed=" + mXspeed + ";mYspeed=" + mYspeed);
        }
        return true;
    }

    class Cube {
        private FloatBuffer mVertexBuffer;
        private FloatBuffer mColorBuffer;
        private ByteBuffer mIndexBuffer;

        private float vertices[] = {
                -1.0f, -1.0f, -1.0f,
                1.0f, -1.0f, -1.0f,
                1.0f, 1.0f, -1.0f,
                -1.0f, 1.0f, -1.0f,
                -1.0f, -1.0f, 1.0f,
                1.0f, -1.0f, 1.0f,
                1.0f, 1.0f, 1.0f,
                -1.0f, 1.0f, 1.0f,};

        private float colors[] = {
                0.0f, 1.0f, 0.0f, 1.0f,
                0.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 0.5f, 0.0f, 1.0f,
                1.0f, 0.5f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f, 1.0f,
                0.0f, 0.0f, 1.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 1.0f
        };

        private byte indices[] = {
                0, 4, 5, 0, 5, 1,
                1, 5, 6, 1, 6, 2,
                2, 6, 7, 2, 7, 3,
                3, 7, 4, 3, 4, 0,
                4, 7, 6, 4, 6, 5,
                3, 0, 1, 3, 1, 2
        };

        public Cube() {
            ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
            byteBuf.order(ByteOrder.nativeOrder());
            mVertexBuffer = byteBuf.asFloatBuffer();
            mVertexBuffer.put(vertices);
            mVertexBuffer.position(0);
            byteBuf = ByteBuffer.allocateDirect(colors.length * 4);
            byteBuf.order(ByteOrder.nativeOrder());
            mColorBuffer = byteBuf.asFloatBuffer();
            mColorBuffer.put(colors);
            mColorBuffer.position(0);
            mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
            mIndexBuffer.put(indices);
            mIndexBuffer.position(0);
        }

        public void draw(GL10 gl) {
            gl.glFrontFace(GL10.GL_CW);
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
            gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            gl.glDrawElements(GL10.GL_TRIANGLES, 36, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);
            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        }
    }
}
