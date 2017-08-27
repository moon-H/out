package com.whl.client.gateway.model.singleticket;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/7 <br>
 * 向服务器上传单程票数据拼接顺序 other+pub+zero+one;
 */
public class HceTicket extends DataSupport implements Serializable {
    private int id;
    private String other;
    private String pub;
    private String zero;
    private String one;
    private String logicCardNum;
    private String issueDate;

    public String getLogicCardNum() {
        return logicCardNum;
    }

    public void setLogicCardNum(String logicCardNum) {
        this.logicCardNum = logicCardNum;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getPub() {
        return pub;
    }

    public void setPub(String pub) {
        this.pub = pub;
    }

    public String getZero() {
        return zero;
    }

    public void setZero(String zero) {
        this.zero = zero;
    }

    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    @Override
    public String toString() {
        return "HceTicket{" +
            "id=" + id +
            ", other='" + other + '\'' +
            ", pub='" + pub + '\'' +
            ", zero='" + zero + '\'' +
            ", one='" + one + '\'' +
            ", logicCardNum='" + logicCardNum + '\'' +
            ", issueDate='" + issueDate + '\'' +
            '}';
    }
}
