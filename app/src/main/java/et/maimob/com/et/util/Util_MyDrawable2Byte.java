//package et.maimob.com.et.util;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.PixelFormat;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//
//import java.io.ByteArrayOutputStream;
//
///**
// * Created by HJ on 2015/7/8.
// */
//public class Util_MyDrawable2Byte {
//
//    /**
//     * drawable 转byte
//     *
//     * @param bm
//     * @return
//     */
//    public static byte[] DrawableToBytes(Drawable bm) {
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        drawableToBitmap(bm).compress(Bitmap.CompressFormat.PNG, 100, baos);
//        return baos.toByteArray();
//    }
//
//    /**
//     * byte 转drawable
//     *
//     * @param b
//     * @return
//     */
//    public static Drawable Bytes2Drawable(byte[] b) {
//
//        if (b.length != 0) {
//            return new BitmapDrawable(BitmapFactory.decodeByteArray(b, 0, b.length));
//        } else {
//            return null;
//        }
//    }
//
//
//    /**
//     * drawable 转bitmap
//     *
//     * @param drawable
//     * @return
//     */
//    public static Bitmap drawableToBitmap(Drawable drawable) {
//
//        Bitmap bitmap = Bitmap.createBitmap(
//                drawable.getIntrinsicWidth(),
//                drawable.getIntrinsicHeight(),
//                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
//                        : Bitmap.Config.RGB_565);
//        Canvas canvas = new Canvas(bitmap);
//        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//        drawable.draw(canvas);
//        return bitmap;
//    }
//}
