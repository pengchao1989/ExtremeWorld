package com.jixianxueyuan.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pengchao on 6/4/15.
 */
public class BuildQRCode {
    public final static int WIDTH = 480;
    public final static int HEIGHT = 480;

    public static boolean encode(String content, String path)
    {

        try
        {
            MultiFormatWriter formatWriter = new MultiFormatWriter();
            Map hints = new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = formatWriter.encode(content, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
            //MatrixToImageWriter.writeToFile(bitMatrix, "png", imageFile);

        } catch (WriterException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return true;
    }

    public static Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int desiredWidth, int desiredHeight) throws WriterException
    {
        // 可以指定其他颜色，让二维码变成彩色效果
        final int WHITE = 0xFFFFFFFF;
        final int BLACK = 0xFF53619c;

        HashMap<EncodeHintType, String> hints = null;
        String encoding = guessAppropriateEncoding(contents);
        if (encoding != null)
        {
            hints = new HashMap<EncodeHintType, String>(2);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result = writer.encode(contents, format, desiredWidth, desiredHeight, hints);
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        // All are 0, or black, by default
        for (int y = 0; y < height; y++)
        {
            int offset = y * width;
            for (int x = 0; x < width; x++)
            {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents)
    {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++)
        {
            if (contents.charAt(i) > 0xFF)
            {
                return "UTF-8";
            }
        }
        return null;
    }


    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }

        if (logo == null) {
            return src;
        }

        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }

        //logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }

        return bitmap;
    }

}
