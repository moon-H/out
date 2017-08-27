package com.whl.framework.utils;


import android.content.Context;
import android.os.Binder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class MLog {
    private static final String TAG = "MLog";
    private static final int LOG_FILE_SIZE_LIMIT = 512 * 1024;
    private static final int LOG_FILE_MAX_COUNT = 2;
    private static final String LOG_FILE_NAME = "FileLog%g.txt";
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss.SSS: ", Locale.getDefault());
    private static final Date date = new Date();
    private static Logger logger;
    private static FileHandler fileHandler;

    private static int flag = 0;

    //----------------------------------------------
    private Context context;

    private static boolean DEBUG = true;


    public MLog(Context context) {
        this.context = context;
    }

    public static void init(String path, String fileName, int sizeLimit, int maxCount, boolean debugAble) {
        DEBUG = debugAble;
        try {

            String parentPath = Environment.getExternalStorageDirectory() + File.separator + path + File.separator;
            File file;
            if (!(file = new File(parentPath)).exists()) {
                file.mkdirs();
            }

            fileHandler = new FileHandler(parentPath + fileName, sizeLimit, maxCount, true);
            fileHandler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord r) {
                    date.setTime(System.currentTimeMillis());
                    return formatter.format(date) + r.getMessage();
                }
            });
            logger = Logger.getLogger(MLog.class.getName());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
            logger.setUseParentHandlers(false);
            Log.d(TAG, "init success");
        } catch (IOException e) {
            Log.d(TAG, "init failure");
            e.printStackTrace();
//            if (flag == 0) {
//                init(path, fileName, sizeLimit, maxCount, debugAble);
//                flag++;
//            }
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            writeLog("D", tag, msg);
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            String error = msg + "\n" + buildException(tr);
            writeLog("D", tag, error);
            Log.d(tag, error);
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUG) {
            writeLog("I", tag, msg);
            Log.i(tag, msg);
        }
    }

    //    public static void i(String tag, String msg) {
    //        if (DEBUG) {
    //            writeLog("I", tag, msg);
    //            Log.i(tag, msg);
    //        }
    //    }

    public static void w(String tag, String msg) {
        if (DEBUG) {
            writeLog("W", tag, msg);
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (DEBUG) {
            writeLog("E", tag, msg);
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable e) {
        if (DEBUG) {
            String error = msg + "\n" + buildException(e);
            writeLog("E", tag, error);
            Log.e(tag, error);
        }
    }

    public static void e(String tag, Throwable ex) {
        if (DEBUG) {
            String exception = buildException(ex);
            writeLog("E", tag, exception);
            Log.e(tag, exception);
        }
    }

    public static void v(String tag, String msg) {
        if (DEBUG) {
            writeLog("V", tag, msg);
            Log.d(tag, msg);
        }
    }

    private static void writeLog(String level, String tag, String msg) {
        if (logger != null) {
            logger.log(Level.INFO, String.format(level + "/%s(%d): %s\n", tag, Binder.getCallingPid(), msg));
        }
    }

    private static String buildException(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        printWriter.close();
        return writer.toString();
    }

}
