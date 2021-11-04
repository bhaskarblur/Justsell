package com.classified.justsell.Models;

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

    public class bannerResult{

        public String banner_id;
        public String banner_image;
        public String banner_url;

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
        public categoryResult result;

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public categoryResult getResult() {
            return result;
        }

        public void setResult(categoryResult result) {
            this.result = result;
        }
    }

    public class categoryResult {

        public String category_id;
        public String category_name;
        public String category_image;

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
}
