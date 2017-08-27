package com.whl.framework.downloadlibrary;

import android.content.Context;

import com.whl.framework.utils.Utils;

import java.io.File;

/**
 * Created by liwx on 2016/1/5.
 */
public class DownloadConstants {
    public static final int STATUS_NOT_DOWNLOAD = 0;
    public static final int STATUS_CONNECTING = 1;
    public static final int STATUS_CONNECT_ERROR = 2;
    public static final int STATUS_DOWNLOADING = 3;
    public static final int STATUS_PAUSED = 4;
    public static final int STATUS_DOWNLOAD_ERROR = 5;
    public static final int STATUS_COMPLETE = 6;
    public static final int STATUS_INSTALLED = 7;
    public static final int STATUS_CANCLED = 8;
    public static final int STATUS_NEED_UPDATE = 9;
    public static final int STATUS_UNZIPING = 10;
    public static final int STATUS_UNZIP_SUCCESS = 11;
    public static final int STATUS_UNZIP_FAILED = 12;

    public static final class CONFIG {
        public static final boolean DEBUG = false;
    }

    public static final int CONNECT_TIME_OUT = 10 * 1000;
    public static final int READ_TIME_OUT = 10 * 1000;
    public static final String GET = "GET";

    public static File getMetroCacheDir(Context context) {
        return Utils.getImgCacheDirectory(context, "Metro");
    }
}
