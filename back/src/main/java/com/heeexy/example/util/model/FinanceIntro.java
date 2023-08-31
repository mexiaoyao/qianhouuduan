package com.heeexy.example.util.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FinanceIntro implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private Byte indexType;

    private String codeNumber;

    private String sharesName;

    private String sharesAlise;

    private Long sharesTotalNumber;

    private Long sharesAllowTotalNumber;

    private Byte status;

    private Date loadTime;

    private Date createTime;

    private Date updateTime;

    private String remarks;

    // 构造方法和其他方法省略
    @Override
    public String toString() {
        return "FinanceIntro{" +
                "id=\"" + id + "\"," +
                "indexType=\"" + indexType + "\"," +
                "codeNumber=\"" + codeNumber + "\"," +
                "sharesName=\"" + sharesName + "\"," +
                "sharesAlise=\"" + sharesAlise + "\"," +
                "sharesTotalNumber=\"" + sharesTotalNumber + "\"," +
                "sharesAllowTotalNumber=\"" + sharesAllowTotalNumber + "\"," +
                "status=\"" + status + "\"," +
                "loadTime=\"" + loadTime + "\"," +
                "createTime=\"" + createTime + "\"," +
                "updateTime=\"" + updateTime + "\"," +
                "remarks=\"" + remarks + "\"}";
    }
}
