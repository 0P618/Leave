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
        if (type.equals("病假")) {
            return "0";
        } else if (type.equals("事假")) {
            return "1";
        } else if (type.equals("其他")) {
            return "2";
        } else if (type.equals("0")) {
            return "病假";
        } else if (type.equals("1")) {
            return "事假";
        } else if (type.equals("2")) {
            return "其他";
        } else {
            return "null";
        }
//        return type;
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
        } else if (status.equals("3")) {
            return "未审核";
        } else if (status.equals("4")) {
            return "超出假期";
        } else {
            return "未知状态";
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
