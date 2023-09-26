package com.classified.upuse.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class homeResponse {

    public class bannerResp {

        public String success;
        public List<bannerResult> result;

        public List<bannerResult> getResult() {
            return result;
        }

        public void setResult(List<bannerResult> result) {
            this.result = result;
        }

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

    }

    public static class bannerResult{

        @SerializedName("id")
        public String banner_id;
        @SerializedName("banner_img")
        public String banner_image;

        @SerializedName("banner_link")
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

        @SerializedName("id")
        public String category_id;
        public String category_name;
        @SerializedName("category_icon")
        public String category_image;
        public String product_type;

        public String getProduct_type() {
            return product_type;
        }

        public void setProduct_type(String product_type) {
            this.product_type = product_type;
        }

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

        @SerializedName("product_id")
        public String ad_id;
        @SerializedName("product_title")
        public String ad_title;
        @SerializedName("product_image")
        public String ad_image;
        @SerializedName("product_category_name")
        public String ad_category;
        @SerializedName("product_description")
        public String ad_description;
        @SerializedName("posted_date")
        public String ad_date;
        public String featured_status;
        @SerializedName("product_sale_price")
        public String ad_price;
        @SerializedName("product_price")
        public String ad_pricecut;
        public String product_name;
        public String user_id;
        public String location;
        @SerializedName("posted_by")
        public String fav_name;

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getFav_name() {
            return fav_name;
        }

        public void setFav_name(String fav_name) {
            this.fav_name = fav_name;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_idd(String user_id) {
            this.user_id = user_id;
        }

        public adsResult(String ad_title, String ad_image, String ad_price, String ad_pricecut, String featured_status) {
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

    public class listofcities {

        public String success;
        public List<citiesResp> result;
        public String message;

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public List<citiesResp> getResult() {
            return result;
        }

        public void setResult(List<citiesResp> result) {
            this.result = result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class citiesResp {
        @SerializedName("city_name")
        public String city;
        public String state;

        public citiesResp(String city, String state) {
            this.city = city;
            this.state = state;
        }


        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }

    public class notiResp {

        public String success;
        public String message;
        public List<notiResult> result;

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<notiResult> getResult() {
            return result;
        }

        public void setResult(List<notiResult> result) {
            this.result = result;
        }
    }

    public static class notiResult {
        @SerializedName("id")
        public String noti_id;
        @SerializedName("product_id")
        public String ad_id;
        public String user_id;
        public String title;
        @SerializedName("img")
        public String image;
        public String description;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public notiResult(String title, String image, String description) {
            this.title = title;
            this.image = image;
            this.description = description;
        }

        public String getNoti_id() {
            return noti_id;
        }

        public void setNoti_id(String noti_id) {
            this.noti_id = noti_id;
        }

        public String getAd_id() {
            return ad_id;
        }

        public void setAd_id(String ad_id) {
            this.ad_id = ad_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public class reachresult {
        public String code;
        public String success;
        public String reach;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public String getReach() {
            return reach;
        }

        public void setReach(String reach) {
            this.reach = reach;
        }
    }
}
