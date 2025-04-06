package com.restaurant.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName eval_statistics
 */
@TableName(value ="eval_statistics")
@Data
public class EvalStatistics implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer statId;

    /**
     * 
     */
    private Integer dishId;

    /**
     * 
     */
    private BigDecimal averageScore;

    /**
     * 
     */
    private BigDecimal favorableRate;

    /**
     * 
     */
    private BigDecimal unfavorableRate;

    /**
     * 
     */
    private Date statTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        EvalStatistics other = (EvalStatistics) that;
        return (this.getStatId() == null ? other.getStatId() == null : this.getStatId().equals(other.getStatId()))
            && (this.getDishId() == null ? other.getDishId() == null : this.getDishId().equals(other.getDishId()))
            && (this.getAverageScore() == null ? other.getAverageScore() == null : this.getAverageScore().equals(other.getAverageScore()))
            && (this.getFavorableRate() == null ? other.getFavorableRate() == null : this.getFavorableRate().equals(other.getFavorableRate()))
            && (this.getUnfavorableRate() == null ? other.getUnfavorableRate() == null : this.getUnfavorableRate().equals(other.getUnfavorableRate()))
            && (this.getStatTime() == null ? other.getStatTime() == null : this.getStatTime().equals(other.getStatTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getStatId() == null) ? 0 : getStatId().hashCode());
        result = prime * result + ((getDishId() == null) ? 0 : getDishId().hashCode());
        result = prime * result + ((getAverageScore() == null) ? 0 : getAverageScore().hashCode());
        result = prime * result + ((getFavorableRate() == null) ? 0 : getFavorableRate().hashCode());
        result = prime * result + ((getUnfavorableRate() == null) ? 0 : getUnfavorableRate().hashCode());
        result = prime * result + ((getStatTime() == null) ? 0 : getStatTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", statId=").append(statId);
        sb.append(", dishId=").append(dishId);
        sb.append(", averageScore=").append(averageScore);
        sb.append(", favorableRate=").append(favorableRate);
        sb.append(", unfavorableRate=").append(unfavorableRate);
        sb.append(", statTime=").append(statTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}