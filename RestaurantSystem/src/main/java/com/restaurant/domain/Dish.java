package com.restaurant.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 
 * @TableName dish
 */
@TableName(value ="dish")
@Data
public class Dish implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer dishId;

    /**
     * 
     */
    private String dishName;

    /**
     * 
     */
    private Integer categoryId;

    /**
     * 
     */
    private BigDecimal price;

    /**
     * 
     */
    private String ingredients;

    /**
     * 
     */
    private String nutritionInfo;

    /**
     * 
     */
    private Integer isSpecial;

    private String url;

    private String rating;

    private BigDecimal specialprice;

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
        Dish other = (Dish) that;
        return (this.getDishId() == null ? other.getDishId() == null : this.getDishId().equals(other.getDishId()))
            && (this.getDishName() == null ? other.getDishName() == null : this.getDishName().equals(other.getDishName()))
            && (this.getCategoryId() == null ? other.getCategoryId() == null : this.getCategoryId().equals(other.getCategoryId()))
            && (this.getPrice() == null ? other.getPrice() == null : this.getPrice().equals(other.getPrice()))
            && (this.getIngredients() == null ? other.getIngredients() == null : this.getIngredients().equals(other.getIngredients()))
            && (this.getNutritionInfo() == null ? other.getNutritionInfo() == null : this.getNutritionInfo().equals(other.getNutritionInfo()))
            && (this.getIsSpecial() == null ? other.getIsSpecial() == null : this.getIsSpecial().equals(other.getIsSpecial()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getDishId() == null) ? 0 : getDishId().hashCode());
        result = prime * result + ((getDishName() == null) ? 0 : getDishName().hashCode());
        result = prime * result + ((getCategoryId() == null) ? 0 : getCategoryId().hashCode());
        result = prime * result + ((getPrice() == null) ? 0 : getPrice().hashCode());
        result = prime * result + ((getIngredients() == null) ? 0 : getIngredients().hashCode());
        result = prime * result + ((getNutritionInfo() == null) ? 0 : getNutritionInfo().hashCode());
        result = prime * result + ((getIsSpecial() == null) ? 0 : getIsSpecial().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", dishId=").append(dishId);
        sb.append(", dishName=").append(dishName);
        sb.append(", categoryId=").append(categoryId);
        sb.append(", price=").append(price);
        sb.append(", ingredients=").append(ingredients);
        sb.append(", nutritionInfo=").append(nutritionInfo);
        sb.append(", isSpecial=").append(isSpecial);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}