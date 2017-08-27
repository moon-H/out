package com.whl.framework.downloadlibrary;

import java.io.File;

/**
 * Created by aspsine on 15-4-19.
 */
public class CssDownloadInfo {
    private String name;
    private String uri;
    private File dir;
    private int progress;
    private long length;
    private long finished;
    private boolean acceptRanges;

    private int status;

    public CssDownloadInfo() {
    }

    public CssDownloadInfo(String name, String uri, File dir) {
        this.name = name;
        this.uri = uri;
        this.dir = dir;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public File getDir() {
        return dir;
    }

    public void setDir(File dir) {
        this.dir = dir;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getFinished() {
        return finished;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }

    public boolean isAcceptRanges() {
        return acceptRanges;
    }

    public void setAcceptRanges(boolean acceptRanges) {
        this.acceptRanges = acceptRanges;
    }
}
