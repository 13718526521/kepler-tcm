package com.kepler.tcm.domain;

import java.io.Serializable;
/**
 * @ClassName DataSource
 * @Description 本地数据源实体类
 * @author lvyx
 * @date 2017年3月28日
 * @version 1.0
 */
public class LocalSource implements Serializable {
	//主键id
    private String id;
    //数据源类型  1：oracle、2：mysql、3：hive、4：xcloud、5：hbase、6：excel
    private String sourceType;
    //数据源名称
    private String sourceName;
    //数据库用户名
    private String dbName;
    //数据库密码
    private String dbPwd;
    //数据库连接的url
    private String sourceUrl;
    //创建人
    private String createUser;
    //创建时间
    private String createTime;
    //数据源的状态 0:启用、1禁用 默认启用
    private String status;
    //更新人
    private String updateBy;
    //最大空闲数
    private String maxIdle;
    //最大连接数
    private String maxActive;
    //最大等待时间
    private String maxWait;
    //最小空闲数
    private String minIdle;
    //初始化数量
    private String initialSize;

    public String getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(String maxIdle) {
		this.maxIdle = maxIdle;
	}

	public String getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(String maxActive) {
		this.maxActive = maxActive;
	}

	public String getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(String maxWait) {
		this.maxWait = maxWait;
	}

	public String getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(String minIdle) {
		this.minIdle = minIdle;
	}

	public String getInitialSize() {
		return initialSize;
	}

	public void setInitialSize(String initialSize) {
		this.initialSize = initialSize;
	}

	private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType == null ? null : sourceType.trim();
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName == null ? null : sourceName.trim();
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName == null ? null : dbName.trim();
    }

    public String getDbPwd() {
        return dbPwd;
    }

    public void setDbPwd(String dbPwd) {
        this.dbPwd = dbPwd == null ? null : dbPwd.trim();
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl == null ? null : sourceUrl.trim();
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", sourceType=").append(sourceType);
        sb.append(", sourceName=").append(sourceName);
        sb.append(", dbName=").append(dbName);
        sb.append(", dbPwd=").append(dbPwd);
        sb.append(", sourceUrl=").append(sourceUrl);
        sb.append(", createUser=").append(createUser);
        sb.append(", createTime=").append(createTime);
        sb.append(", status=").append(status);
        sb.append(", updateBy=").append(updateBy);
        sb.append("]");
        return sb.toString();
    }
}