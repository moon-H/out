package com.whl.client.gateway.model.singleticket;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by liwx on 2016/1/6.
 */
public class MetroMap extends DataSupport implements Serializable {
    private int id;
    private String cityCode;
    private String cityName;
    private int linesCnt;
    private String existsMetroUpdateVesion;
    private String metroMapVersion;
    private String downloadMetroMapUrl;
    private String metroMapVersionDesc;
    private String singleTicketServiceYn;
    private String upgradeMandatoryYn;
    private int width;
    private int height;
    private int progress;
    private String downloadPerSize;
    private String totalSize;
    private int status;
    private int metroMapZipfileSize;
    private String cachedYn;
    private int orderIndex;
    private String serviceStatus;
    private String fullOpeningYN;

    public String getFullOpeningYN() {
        return fullOpeningYN;
    }

    public void setFullOpeningYN(String fullOpeningYN) {
        this.fullOpeningYN = fullOpeningYN;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getLinesCnt() {
        return linesCnt;
    }

    public void setLinesCnt(int linesCnt) {
        this.linesCnt = linesCnt;
    }

    public String getExistsMetroUpdateVesion() {
        return existsMetroUpdateVesion;
    }

    public void setExistsMetroUpdateVesion(String existsMetroUpdateVesion) {
        this.existsMetroUpdateVesion = existsMetroUpdateVesion;
    }

    public String getMetroMapVersion() {
        return metroMapVersion;
    }

    public void setMetroMapVersion(String metroMapVersion) {
        this.metroMapVersion = metroMapVersion;
    }

    public String getDownloadMetroMapUrl() {
        return downloadMetroMapUrl;
    }

    public void setDownloadMetroMapUrl(String downloadMetroMapUrl) {
        this.downloadMetroMapUrl = downloadMetroMapUrl;
    }

    public String getMetroMapVersionDesc() {
        return metroMapVersionDesc;
    }

    public void setMetroMapVersionDesc(String metroMapVersionDesc) {
        this.metroMapVersionDesc = metroMapVersionDesc;
    }

    public String getSingleTicketServiceYn() {
        return singleTicketServiceYn;
    }

    public void setSingleTicketServiceYn(String singleTicketServiceYn) {
        this.singleTicketServiceYn = singleTicketServiceYn;
    }

    public String getUpgradeMandatoryYn() {
        return upgradeMandatoryYn;
    }

    public void setUpgradeMandatoryYn(String upgradeMandatoryYn) {
        this.upgradeMandatoryYn = upgradeMandatoryYn;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getDownloadPerSize() {
        return downloadPerSize;
    }

    public void setDownloadPerSize(String downloadPerSize) {
        this.downloadPerSize = downloadPerSize;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(String totalSize) {
        this.totalSize = totalSize;
    }

    public int getMetroMapZipfileSize() {
        return metroMapZipfileSize;
    }

    public void setMetroMapZipfileSize(int metroMapZipfileSize) {
        this.metroMapZipfileSize = metroMapZipfileSize;
    }

    public String getCachedYn() {
        return cachedYn;
    }

    public void setCachedYn(String cachedYn) {
        this.cachedYn = cachedYn;
    }

    @Override
    public String toString() {
        return "MetroMap{" +
            "cityCode='" + cityCode + '\'' +
            ", cityName='" + cityName + '\'' +
            ", linesCnt=" + linesCnt +
            ", existsMetroUpdateVesion='" + existsMetroUpdateVesion + '\'' +
            ", metroMapVersion='" + metroMapVersion + '\'' +
            ", downloadMetroMapUrl='" + downloadMetroMapUrl + '\'' +
            ", metroMapVersionDesc='" + metroMapVersionDesc + '\'' +
            ", singleTicketServiceYn='" + singleTicketServiceYn + '\'' +
            ", upgradeMandatoryYn='" + upgradeMandatoryYn + '\'' +
            ", width=" + width +
            ", height=" + height +
            ", progress=" + progress +
            ", downloadPerSize='" + downloadPerSize + '\'' +
            ", totalSize='" + totalSize + '\'' +
            ", status=" + status +
            ", metroMapZipfileSize=" + metroMapZipfileSize +
            ", cachedYn='" + cachedYn + '\'' +
            ", orderIndex=" + orderIndex +
            ", serviceStatus='" + serviceStatus + '\'' +
            ", fullOpeningYN='" + fullOpeningYN + '\'' +
            '}';
    }
}
