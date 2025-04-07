package com.restaurant.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 
 * @TableName evaluation
 */
@TableName(value ="evaluation")
@Data
public class Evaluation implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer evalId;

    /**
     * 
     */
    private Integer studentId;

    /**
     * 
     */
    private Integer dishId;

    /**
     * 
     */
    private Byte tasteScore;

    /**
     * 
     */
    private Byte colorScore;

    /**
     * 
     */
    private Byte quantityScore;

    /**
     * 
     */
    private Byte pricePerformanceScore;

    /**
     * 
     */
    private String comment;

    /**
     * 
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date evalTime;

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
        Evaluation other = (Evaluation) that;
        return (this.getEvalId() == null ? other.getEvalId() == null : this.getEvalId().equals(other.getEvalId()))
            && (this.getStudentId() == null ? other.getStudentId() == null : this.getStudentId().equals(other.getStudentId()))
            && (this.getDishId() == null ? other.getDishId() == null : this.getDishId().equals(other.getDishId()))
            && (this.getTasteScore() == null ? other.getTasteScore() == null : this.getTasteScore().equals(other.getTasteScore()))
            && (this.getColorScore() == null ? other.getColorScore() == null : this.getColorScore().equals(other.getColorScore()))
            && (this.getQuantityScore() == null ? other.getQuantityScore() == null : this.getQuantityScore().equals(other.getQuantityScore()))
            && (this.getPricePerformanceScore() == null ? other.getPricePerformanceScore() == null : this.getPricePerformanceScore().equals(other.getPricePerformanceScore()))
            && (this.getComment() == null ? other.getComment() == null : this.getComment().equals(other.getComment()))
            && (this.getEvalTime() == null ? other.getEvalTime() == null : this.getEvalTime().equals(other.getEvalTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getEvalId() == null) ? 0 : getEvalId().hashCode());
        result = prime * result + ((getStudentId() == null) ? 0 : getStudentId().hashCode());
        result = prime * result + ((getDishId() == null) ? 0 : getDishId().hashCode());
        result = prime * result + ((getTasteScore() == null) ? 0 : getTasteScore().hashCode());
        result = prime * result + ((getColorScore() == null) ? 0 : getColorScore().hashCode());
        result = prime * result + ((getQuantityScore() == null) ? 0 : getQuantityScore().hashCode());
        result = prime * result + ((getPricePerformanceScore() == null) ? 0 : getPricePerformanceScore().hashCode());
        result = prime * result + ((getComment() == null) ? 0 : getComment().hashCode());
        result = prime * result + ((getEvalTime() == null) ? 0 : getEvalTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", evalId=").append(evalId);
        sb.append(", studentId=").append(studentId);
        sb.append(", dishId=").append(dishId);
        sb.append(", tasteScore=").append(tasteScore);
        sb.append(", colorScore=").append(colorScore);
        sb.append(", quantityScore=").append(quantityScore);
        sb.append(", pricePerformanceScore=").append(pricePerformanceScore);
        sb.append(", comment=").append(comment);
        sb.append(", evalTime=").append(evalTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}