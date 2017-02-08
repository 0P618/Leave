package com.goldenratio.leave.bean;

/**
 * Created by Kiuber on 2016/12/18.
 */

public class LeaveBean {
    private String id;
    private String start;
    private String end;
    private String type;
    private String remark;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getType() {
       /* if (status.equals("0")) {
            return "病假";
        } else if (status.equals("1")) {
            return "事假";
        } else if (status.equals("2")) {
            return "其他";
        } else {
            return "未知类型";
        }*/
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        if (status.equals("0")) {
            return "审核失败";
        } else if (status.equals("1")) {
            return "审核通过";
        } else if (status.equals("2")) {
            return "正在审核";
        } else {
            return "未知状态";
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
