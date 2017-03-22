package com.codemagos.catchmyride.Misc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Sree on 6/5/2016.
 */

public class GIFView extends View{

    Movie movie;
    long moviestart;

    public GIFView(Context context) throws IOException {
        super(context);
    }
    public GIFView(Context context, AttributeSet attrs) throws IOException{
        super(context, attrs);
    }
    public GIFView(Context context, AttributeSet attrs, int defStyle) throws IOException {
        super(context, attrs, defStyle);
    }

    public void loadGIFResource(Context context, int id)
    {
        //turn off hardware acceleration
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        InputStream is=context.getResources().openRawResource(id);
        movie = Movie.decodeStream(is);
    }

    public void loadGIFAsset(Context context, String filename)
    {
        InputStream is;
        try {
            is = context.getResources().getAssets().open(filename);
            movie = Movie.decodeStream(is);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (movie == null) {
            return;
        }
        canvas.scale((float)this.getWidth() / (float)movie.width(),(float)this.getHeight() /      (float)movie.height());
        long now=android.os.SystemClock.uptimeMillis();

        if (moviestart == 0) moviestart = now;

        int relTime;
        relTime = (int)((now - moviestart) % movie.duration());
        movie.setTime(relTime);
        movie.draw(canvas,10,10);
        this.invalidate();
    }
}