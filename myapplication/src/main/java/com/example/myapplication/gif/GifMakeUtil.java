package com.example.myapplication.gif;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


import com.example.myapplication.App;
import com.example.myapplication.util.ImageUtil;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class GifMakeUtil {

    public static String createGif(String filename, @NotNull List<String> paths, int fps, int width, int height) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        AnimatedGifEncoder localAnimatedGifEncoder = new AnimatedGifEncoder();
        localAnimatedGifEncoder.start(baos);//start
        localAnimatedGifEncoder.setRepeat(0);//gif。0
        localAnimatedGifEncoder.setTransparent(0x00);//gif。0
        localAnimatedGifEncoder.setFrameRate(100);
//        localAnimatedGifEncoder.setDelay(fps);
        if (!paths.isEmpty()) {
            for (int i = 0; i < paths.size(); i++) {
                Bitmap resizeBm = ImageUtil.resizeImage2(paths.get(i), width, height);
                localAnimatedGifEncoder.addFrame(resizeBm);
                resizeBm.recycle();
            }
        }
        localAnimatedGifEncoder.finish();//finish

        File file = new File(App.instance.getFilesDir() + "/gif");
        if (!file.exists()) file.mkdir();
        String path = App.instance.getFilesDir() + "/gif/" + filename + ".gif";
        File gifFile = new File(path);
        if (gifFile.exists()) gifFile.delete();
        gifFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(gifFile);
        baos.writeTo(fos);
        baos.flush();
        fos.flush();
        baos.close();
        fos.close();

        return path;
    }

}
