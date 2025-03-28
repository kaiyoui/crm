package com.yidatec.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yidatec.util.CustomLocalDateSerializer;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by Administrator on 2017/7/21.
 */
public class Activity extends BaseModel{
//    @NotBlank(message = "必须选择活动类型", groups = { })
//    private String type;
    @NotBlank(message = "必须输入活动名称", groups = { })
    @Length( max = 200, message = "名称最多由200个字符组成", groups = { })
    private String name;

    @NotBlank(message = "必须选择活动类型", groups = { })
    private String type;

    @NotNull(message = "必须输入开始时间", groups = { })
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @NotNull(message = "必须输入结束时间", groups = { })
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @NotBlank(message = "必须选择国家", groups = { })
    private String country;
//    @NotBlank(message = "必须选择省份", groups = { })
    private String province;
//    @NotBlank(message = "必须选择城市", groups = { })
    private String city;
//    @NotBlank(message = "必须选择区域", groups = { })
    private String region;
    @Length( max = 200, message = "地址最多由200个字符组成", groups = { })
    private String address;
    private Integer state;
    //展馆
    @NotBlank(message = "必须选择展馆", groups = { })
    private String exhibitioHall;
    private String exhibitioHallName;
    //主办方
//    @NotBlank(message = "必须选择主办方", groups = { })
    private String sponsor;
    private String customerName;

//    @NotBlank(message = "必须选择承办方", groups = { })
    private String organizer;

//    @NotBlank(message = "必须选择主场搭建", groups = { })
    private String builder;

    private String mediaIds;
    private String photo;


    public String getExhibitioHallName() {
        return exhibitioHallName;
    }

    public void setExhibitioHallName(String exhibitioHallName) {
        this.exhibitioHallName = exhibitioHallName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getExhibitioHall() {
        return exhibitioHall;
    }

    public void setExhibitioHall(String exhibitioHall) {
        this.exhibitioHall = exhibitioHall;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getStartDateStr(){
        if(startDate == null)return "";
        return startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String getEndDateStr(){
        if(endDate == null)return "";
        return endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String getMediaIds() {
        return mediaIds;
    }

    public void setMediaIds(String mediaIds) {
        this.mediaIds = mediaIds;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getBuilder() {
        return builder;
    }

    public void setBuilder(String builder) {
        this.builder = builder;
    }
}
