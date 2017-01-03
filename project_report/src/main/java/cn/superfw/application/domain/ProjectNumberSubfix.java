package cn.superfw.application.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate(true)
@DynamicInsert(true)
@Table(name="t_product_number_subfix")
public class ProjectNumberSubfix extends BaseEntity implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    @Column(name="dept", length=2)
    private String dept;
    @Column(name="ymd", length=8)
    private String ymd;
    @Column(name="num", nullable=false, columnDefinition="INT default 0")
    private Integer num;

    public String getDept() {
        return dept;
    }
    public void setDept(String dept) {
        this.dept = dept;
    }
    public String getYmd() {
        return ymd;
    }
    public void setYmd(String ymd) {
        this.ymd = ymd;
    }
    public Integer getNum() {
        return num;
    }
    public void setNum(Integer num) {
        this.num = num;
    }

}
