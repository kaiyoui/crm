package com.yidatec.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yidatec.model.Quotation;
import com.yidatec.util.CustomLocalDateSerializer;

import java.time.LocalDate;

/**
 * Created by jrw on 2017/8/25.
 */
public class QuotationVO extends Quotation {
    private String id;
    private String DicId;

    private String projectName;
    private String unitPrice;
    private Integer count;
    private String workContent;


    private String categoryName;// 类别
    private String productName;// 产品名
    private String unitName;// 单位
    private String countPrice;// 合价

    private String area;
    private String startDate;

    private Integer draw;
    private Integer length;
    private Integer start;


    @JsonSerialize(using = CustomLocalDateSerializer.class)
    private LocalDate startTime;
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    private LocalDate endTime;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getCountPrice() {
        return countPrice;
    }

    public void setCountPrice(String countPrice) {
        this.countPrice = countPrice;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getDicId() {
        return DicId;
    }

    public void setDicId(String dicId) {
        DicId = dicId;
    }

    public Integer getDraw() {
        return draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getWorkContent() {
        return workContent;
    }

    public void setWorkContent(String workContent) {
        this.workContent = workContent;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
