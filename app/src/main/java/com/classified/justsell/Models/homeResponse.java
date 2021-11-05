package com.classified.justsell.Models;

import java.util.List;

public class homeResponse {

    public class bannerResp {

        public String success;
        public bannerResult result;

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public bannerResult getResult() {
            return result;
        }

        public void setResult(bannerResult result) {
            this.result = result;
        }
    }

    public static class bannerResult{

        public String banner_id;
        public String banner_image;
        public String banner_url;

        public bannerResult(String banner_image) {
            this.banner_image = banner_image;
        }

        public String getBanner_id() {
            return banner_id;
        }

        public void setBanner_id(String banner_id) {
            this.banner_id = banner_id;
        }

        public String getBanner_image() {
            return banner_image;
        }

        public void setBanner_image(String banner_image) {
            this.banner_image = banner_image;
        }

        public String getBanner_url() {
            return banner_url;
        }

        public void setBanner_url(String banner_url) {
            this.banner_url = banner_url;
        }
    }

    public class categoryResp {

        public String success;
        public List<categoryResult> result;

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public List<categoryResult> getResult() {
            return result;
        }

        public void setResult(List<categoryResult> result) {
            this.result = result;
        }
    }

    public static class categoryResult {

        public String category_id;
        public String category_name;
        public String category_image;

        public categoryResult(String category_name, String category_image) {
            this.category_name = category_name;
            this.category_image = category_image;
        }

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public String getCategory_image() {
            return category_image;
        }

        public void setCategory_image(String category_image) {
            this.category_image = category_image;
        }
    }

    public class ListadsResp {
        public String success;
        public String code;
        public List<adsResult> result;

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public List<adsResult> getResult() {
            return result;
        }

        public void setResult(List<adsResult> result) {
            this.result = result;
        }
    }

    public static class adsResult {

        public String ad_id;
        public String ad_title;
        public String ad_image;
        public String ad_category;
        public String ad_description;
        public String ad_date;
        public String featured_status;
        public String ad_price;
        public String ad_pricecut;
        public String product_name;

        public adsResult(String ad_title, String ad_image, String ad_price, String ad_pricecut,String featured_status) {
            this.ad_title = ad_title;
            this.ad_image = ad_image;
            this.ad_price = ad_price;
            this.ad_pricecut = ad_pricecut;
            this.featured_status=featured_status;
        }

        public String getAd_id() {
            return ad_id;
        }

        public void setAd_id(String ad_id) {
            this.ad_id = ad_id;
        }

        public String getAd_title() {
            return ad_title;
        }

        public void setAd_title(String ad_title) {
            this.ad_title = ad_title;
        }

        public String getAd_image() {
            return ad_image;
        }

        public void setAd_image(String ad_image) {
            this.ad_image = ad_image;
        }

        public String getAd_category() {
            return ad_category;
        }

        public void setAd_category(String ad_category) {
            this.ad_category = ad_category;
        }

        public String getAd_description() {
            return ad_description;
        }

        public void setAd_description(String ad_description) {
            this.ad_description = ad_description;
        }

        public String getAd_date() {
            return ad_date;
        }

        public void setAd_date(String ad_date) {
            this.ad_date = ad_date;
        }

        public String getFeatured_status() {
            return featured_status;
        }

        public void setFeatured_status(String featured_status) {
            this.featured_status = featured_status;
        }

        public String getAd_price() {
            return ad_price;
        }

        public void setAd_price(String ad_price) {
            this.ad_price = ad_price;
        }

        public String getAd_pricecut() {
            return ad_pricecut;
        }

        public void setAd_pricecut(String ad_pricecut) {
            this.ad_pricecut = ad_pricecut;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }
    }
}
