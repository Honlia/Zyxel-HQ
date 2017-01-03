package cn.superfw.application.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

@Entity
@DynamicUpdate(true)
@DynamicInsert(true)
@Table(name="t_update_log")
public class UpdateLog extends BaseEntity implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    private String projectId;
    private Integer updateCount;
    private String title;
    private String content;

    public UpdateLog() {
        super();
    }

    public UpdateLog(String projectId, Integer updateCount, String title,
            String content) {
        super();
        this.projectId = projectId;
        this.updateCount = updateCount;
        this.title = title;
        this.content = content;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Integer getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(Integer updateCount) {
        this.updateCount = updateCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Lob
    @Type(type="text")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
