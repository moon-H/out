package com.whl.framework.utils;


import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils {
    private static final String TAG = "Utils";
    static DecimalFormat df = new DecimalFormat("######0.00");

    //缓存路径SD卡 清除缓存不删除
    public static File getCacheDirectory(Context context, String parentCacheDir) {
        File appCacheDir = null;
        if ("mounted".equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            appCacheDir = new File(Environment.getExternalStorageDirectory(), parentCacheDir);
        }
        if (appCacheDir == null || !appCacheDir.exists() && !appCacheDir.mkdirs()) {
            appCacheDir = context.getCacheDir();
            // appCacheDir = context.getDir(fileName, Context.MODE_PRIVATE);
        }
        return appCacheDir;
    }

    public static File getInternalCacheDir(Context context) {
        return context.getCacheDir();

    }

    //缓存路径 清除缓存将被删除
    public static File getImgCacheDirectory(Context context, String parentCacheDir) {
        File appCacheDir = null;
        if ("mounted".equals(Environment.getExternalStorageState()) && Utils.hasExternalStoragePermission(context.getApplicationContext())) {
            appCacheDir = new File(context.getExternalFilesDir(""), parentCacheDir);
        }
        if (appCacheDir == null || !appCacheDir.exists() && !appCacheDir.mkdirs()) {
            appCacheDir = context.getCacheDir();
            // appCacheDir = context.getDir(fileName, Context.MODE_PRIVATE);
        }
        return appCacheDir;
    }

    public static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
        return perm == 0;
    }

    /**
     * px 转 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * dp 转 PX
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px 转 sp
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * sp 转 px
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 隐藏键盘
     */
    public static void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showSysSoftInput(final Context context) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }
        }, 100);
    }

    public static void showSysSoftInput(EditText edit, boolean show) {
        Class<EditText> cls = EditText.class;
        try {
            Method setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            setShowSoftInputOnFocus.setAccessible(true);
            setShowSoftInputOnFocus.invoke(edit, show);
            // setShowSoftInputOnFocus.invoke(ePeople, false);
        } catch (NoSuchMethodException e) {
            //            MLog.e("showSysSoftInput NoSuchMethodException ::", e);
        } catch (IllegalArgumentException e) {
            //            MLog.e("showSysSoftInput IllegalArgumentException ::", e);
        } catch (IllegalAccessException e) {
            //            MLog.e("showSysSoftInput IllegalAccessException ::", e);
        } catch (InvocationTargetException e) {
            //            MLog.e("showSysSoftInput InvocationTargetException ::", e);
        }
        try {
            Method setShowSoftInputOnFocus2 = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
            setShowSoftInputOnFocus2.setAccessible(true);
            setShowSoftInputOnFocus2.invoke(edit, show);
        } catch (NoSuchMethodException e) {
            //            MLog.e("showSysSoftInput NoSuchMethodException :: ", e);
        } catch (IllegalArgumentException e) {
            //            MLog.e("showSysSoftInput IllegalArgumentException :: ", e);
        } catch (IllegalAccessException e) {
            //            MLog.e("showSysSoftInput IllegalAccessException :: ", e);
        } catch (InvocationTargetException e) {
            //            MLog.e("showSysSoftInput InvocationTargetException ::", e);
        }
    }

    /**
     * String[0] is asset ID,String[1] is asset name
     */
    public static String[] parseQrCode(String str) throws Exception {
        String[] resultArray = new String[2];
        if (str.startsWith("cssweb") && !TextUtils.isEmpty(str)) {
            String[] array = str.split("\\|");
            resultArray[0] = array[1];
            resultArray[1] = array[2];
            return resultArray;
        } else {
            throw new Exception("Unkown QR CODE");
        }
    }

    public static Bitmap readBitmapFromRes(Context context, int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    public static String getDecimal2(double data) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");// 保持小数点下2位
        return decimalFormat.format(data);
    }

    public static boolean isNumberOrEnglish(CharSequence str) {
        Pattern p = Pattern.compile("^[A-Za-z0-9]+$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static boolean isEnglish(CharSequence str) {
        Pattern p = Pattern.compile("^[A-Za-z]+$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static String generatePassword(String inputString) {
        return encodeByMD5(inputString);
    }


    public static String encodeByMD5(String originString) {
        if (originString != null) {
            try {
                // 创建具有指定算法名称的信息摘要
                MessageDigest md = MessageDigest.getInstance("MD5");
                // 使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
                byte[] results = md.digest(originString.getBytes());
                // 将得到的字节数组变成字符串返回
                String resultString = byteArrayToHexString(results);
                return resultString.toUpperCase(Locale.getDefault());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 转换字节数组为十六进制字符串
     *
     * @param
     * @return 十六进制字符串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    /**
     * 将一个字节转化成十六进制形式的字符串
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    // 十六进制下数字到字符的映射数组
    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static boolean isBackgroundRunning(Context context) {
        String processName = context.getApplicationInfo().packageName;

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

        if (activityManager == null)
            return false;
        // get running application processes
        List<ActivityManager.RunningAppProcessInfo> processList = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo process : processList) {
            if (process.processName.startsWith(processName)) {
                // boolean isBackground = process.importance !=
                // android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                // && process.importance != android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;
                boolean isBackground = process.importance != android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
                boolean isLockedState = keyguardManager.inKeyguardRestrictedInputMode();
                if (isBackground || isLockedState)
                    return true;
                else
                    return false;
            }
        }
        return false;
    }

    public static boolean isEmail(String email) {
        if (null == email || "".equals(email))
            return false;
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");// 复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isID(String idNo) {
        if (null == idNo || "".equals(idNo))
            return false;
        Matcher m;
        if (idNo.length() == 15) {
            Pattern p = Pattern.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$");// 复杂匹配
            m = p.matcher(idNo);
        } else if (idNo.length() == 18) {
            Pattern p = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4}$");// 复杂匹配
            m = p.matcher(idNo);
        } else {
            return false;
        }

        return m.matches();
    }

    public static String convertDate2Local(String strDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date;
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return strDate;
        }

        SimpleDateFormat dateFormat;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return dateFormat.format(date);
    }

    public static boolean isOkResponse(String response) {
        if (TextUtils.isEmpty(response)) {
            return false;
        } else {
            if (response.endsWith("9000")) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static String formatDouble2f(double value) {
        return df.format(value);
    }

    private static AssetManager assetManager;
    private static BitmapFactory.Options opt;

    public static Bitmap getBitmapFromAsset(Context context, String fileName) {
        if (assetManager == null)
            assetManager = context.getResources().getAssets();
        InputStream is = null;
        try {
            is = assetManager.open(fileName);
            return BitmapFactory.decodeStream(is, null, getBitmapOpt());
        } catch (Exception e) {
            Log.d(TAG, "read bitmap from assets failed! " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return null;
    }

    public static Bitmap getBitmapFromDiskCach(String cachDir, String fileName) {
        //        MLog.d(TAG, "cacheDir = " + cachDir + " fileName = " + fileName);
        FileInputStream fileInputStream = null;
        try {
            File file = new File(cachDir + File.separator + fileName);
            if (file.exists()) {
                fileInputStream = new FileInputStream(file);
                //            return BitmapFactory.decodeFile(cachDir + File.separator + fileName);
                return BitmapFactory.decodeStream(fileInputStream, null, getBitmapOpt());            //        }
            }
        }
        //        catch (OutOfMemoryError outOfMemoryError) {
        //            MLog.d(TAG, "getBitmapFromDiskCach outOfMemoryError = " + outOfMemoryError.getMessage());
        //            System.gc();
        //            System.runFinalization();
        //            outOfMemoryError.printStackTrace();
        //        }
        catch (Exception e) {
            Log.d(TAG, "read bitmap from assets failed! " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (fileInputStream != null)
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }

    public static boolean getBitmapFromDiskCach2(String cachDir, String fileName, OutputStream outputStream) {
        //        MLog.d(TAG, "cacheDir = " + cachDir + " fileName = " + fileName);
        FileInputStream fileInputStream = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            File file = new File(cachDir + File.separator + fileName);
            if (file.exists()) {
                fileInputStream = new FileInputStream(file);
                in = new BufferedInputStream(fileInputStream, 8 * 1024);
                out = new BufferedOutputStream(outputStream, 8 * 1024);
                //                MLog.d(TAG, "#######trace2");
                int b;
                while ((b = in.read()) != -1) {
                    out.write(b);
                }
                out.flush();
                return true;
            }
        } catch (Exception e) {
            Log.d(TAG, "read bitmap from assets failed! " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (fileInputStream != null)
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (out != null)
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return false;
    }

    public static BitmapFactory.Options getBitmapOpt() {

        if (opt == null)
            opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        //        opt.inJustDecodeBounds = false;
        //        opt.inPurgeable = true;
        //        opt.inInputShareable = true;
        // 获取资源图片
        return opt;

    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 保存图片原宽高值
        final int height = options.outHeight;
        final int width = options.outWidth;
        // 初始化压缩比例为1
        int inSampleSize = 1;

        // 当图片宽高值任何一个大于所需压缩图片宽高值时,进入循环计算系统
        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // 压缩比例值每次循环两倍增加,
            // 直到原图宽高值的一半除以压缩值后都~大于所需宽高值为止
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Context context, String fileName, int reqWidth, int reqHeight) {

        // 首先不加载图片,仅获取图片尺寸
        final BitmapFactory.Options options = new BitmapFactory.Options();
        // 当inJustDecodeBounds设为true时,不会加载图片仅获取图片尺寸信息
        options.inJustDecodeBounds = true;
        // 此时仅会将图片信息会保存至options对象内,decode方法不会返回bitmap对象
        //        BitmapFactory.decodeResource(res, resId, options);
        if (assetManager == null)
            assetManager = context.getResources().getAssets();
        InputStream is = null;
        try {
            is = assetManager.open(fileName);
            BitmapFactory.decodeStream(is, null, options);


            // 计算压缩比例,如inSampleSize=4时,图片会压缩成原图的1/4
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            MLog.d(TAG, "inSampleSize = " + options.inSampleSize);
            // 当inJustDecodeBounds设为false时,BitmapFactory.decode...就会返回图片对象了
            options.inJustDecodeBounds = false;
            // 利用计算的比例值获取压缩后的图片对象
            return BitmapFactory.decodeStream(is, null, options);
        } catch (Exception e) {
            Log.d(TAG, "read bitmap from assets failed! " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }

    private static long lastClickTime;

    //判断是否快速点击几次
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    private static final DecimalFormat DF = new DecimalFormat("0.00");

    public static String getDownloadPerSize(long finished, long total) {
        return DF.format((float) finished / (1024 * 1024)) + "MB/" + DF.format((float) total / (1024 * 1024)) + "MB";
    }

    public static String getTotalPerSize(long total) {
        return DF.format((float) total / (1024 * 1024)) + "MB";
    }


    public static boolean deleteFile(String filePath) {
        File folder = new File(filePath);
        boolean result = folder.delete();
        MLog.d(TAG, "delete result = " + result);
        return result;
    }

    public static String readFileContent(String strFilePath) {
        MLog.d(TAG, "readFileContent filePath =" + strFilePath);
        String content = ""; //文件内容字符串
        File file = new File(strFilePath);
        if (file.isDirectory()) {
            MLog.d(TAG, "The File doesn't not exist.");
        } else {
            InputStream instream = null;
            try {
                instream = new FileInputStream(file);

                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line;
                //分行读取
                while ((line = buffreader.readLine()) != null) {
                    content += line;
                }
            } catch (Exception e) {
                e.printStackTrace();
                MLog.e(TAG, "read file content occur an error :: ", e);
                return content;
            } finally {
                try {
                    if (instream != null)
                        instream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    MLog.e(TAG, "read file content occur an error :: ", e);
                }
            }
        }
        return content;
    }

    //将字符串转换为颜色值
    public static int getColorFromString(String s) {
        int color = Color.parseColor(s);
        return color;
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public static void recursionDeleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                recursionDeleteFile(f);
            }
            file.delete();
        }
    }

    /**
     * 使用MD5算法对传入的key进行加密并返回。
     */
    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = HexConverter.bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    public static int converVersionStrToInt(String version) {
        if (TextUtils.isEmpty(version))
            return 0;
        String strings = version.replace(".", "");
        return Integer.valueOf(strings);
    }

    /**
     * 格式化hexstring类型的余额
     */
    public static String parseMoneyHex(String hexBalance) {
        Integer parseAmount = Integer.parseInt(hexBalance, 16);
        String balance = null;
        if (0 <= parseAmount % 100) {
            balance = parseAmount / 100 + "." + String.format("%02d", parseAmount % 100);
        } else {
            balance = String.format(Locale.getDefault(), "%d", parseAmount / 100);
        }
        return balance;
    }

    /**
     * 分转元
     */
    public static String parseMoney(double amount) {
        return Utils.formatDouble2f(amount / 100.00);
    }

    /**
     * 分转元
     */
    public static String parseMoney(String amount) {
        double value = Double.valueOf(amount);
        return Utils.formatDouble2f(value / 100.00);
    }

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");

    public static SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public static void runOnUiThread(Runnable r) {
        Looper.prepare();
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(r);
        Looper.loop();
    }

    public static String byteToBit(byte b) {
        return "" + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1) + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1) + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1) + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    }

    public static String getMetaDataValue(Context context, String name) {
        String value = getMetaData(context, name);
        return (value == null) ? "" : value;
    }

    public static String getChannelId(Context context) {
        String value = getMetaData(context, "UMENG_CHANNEL");
        return (value == null) ? "" : value;
    }

    private static String getMetaData(Context context, String name) {
        Object value = null;
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                value = applicationInfo.metaData.get(name);
            }
        } catch (PackageManager.NameNotFoundException e) {
            MLog.d(TAG, "Could not read the name in the manifest file.", e);
        }

        if (value == null) {
            MLog.d(TAG, "The name '" + name + "' is not defined in the manifest file's meta data.");
            return "";
        }
        return value.toString();

    }

    public static String getAssetFileContent(Context context, String path) {
        AssetManager assetManager = context.getAssets();
        String content = "";
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = assetManager.open(path);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return content;
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    public static InputStream Bitmap2InputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }

    public static boolean isInMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * 字符串左补字符
     *
     * @param param
     * @param size
     * @param insertStr 需要补充的字符
     * @return
     * @throws Exception
     */
    public static String addLeftChar(String param, int size, String insertStr) {
        if (size <= param.length()) {
            return param;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i <= size - param.length(); i++) {
            sb.append(insertStr);
        }
        sb.append(param);
        return sb.toString();
    }

}
