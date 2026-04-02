package com.wit.travel.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class RouteAddDTO {

    @NotBlank(message = "路线名称不能为空")
    private String name;

    private String description;

    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    private BigDecimal budget;

    private Integer durationDays;

    private String bestSeason;

    private List<String> images;

    private List<RouteScenicDTO> scenicList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public Integer getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }

    public String getBestSeason() {
        return bestSeason;
    }

    public void setBestSeason(String bestSeason) {
        this.bestSeason = bestSeason;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<RouteScenicDTO> getScenicList() {
        return scenicList;
    }

    public void setScenicList(List<RouteScenicDTO> scenicList) {
        this.scenicList = scenicList;
    }

    public static class RouteScenicDTO {
        private Long scenicId;
        private Integer dayNumber;
        private Integer sortOrder;
        private String description;

        public Long getScenicId() {
            return scenicId;
        }

        public void setScenicId(Long scenicId) {
            this.scenicId = scenicId;
        }

        public Integer getDayNumber() {
            return dayNumber;
        }

        public void setDayNumber(Integer dayNumber) {
            this.dayNumber = dayNumber;
        }

        public Integer getSortOrder() {
            return sortOrder;
        }

        public void setSortOrder(Integer sortOrder) {
            this.sortOrder = sortOrder;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
