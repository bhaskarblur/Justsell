package com.classified.justsell.Models;

import java.util.List;

public class chatModel {

    public class listChats {

        public String success;
        public List<chatResult> result;
        public String code;

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public List<chatResult> getResult() {
            return result;
        }

        public void setResult(List<chatResult> result) {
            this.result = result;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public class chatResult {
        public String user_id;
        public String person_id;
        public String product_id;
        public String product_img;
        public String person_name;
        public String person_img;
        public String recent_msg;
        public String unseen_msg_count;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getPerson_id() {
            return person_id;
        }

        public void setPerson_id(String person_id) {
            this.person_id = person_id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getProduct_img() {
            return product_img;
        }

        public void setProduct_img(String product_img) {
            this.product_img = product_img;
        }

        public String getPerson_name() {
            return person_name;
        }

        public void setPerson_name(String person_name) {
            this.person_name = person_name;
        }

        public String getPerson_img() {
            return person_img;
        }

        public void setPerson_img(String person_img) {
            this.person_img = person_img;
        }

        public String getRecent_msg() {
            return recent_msg;
        }

        public void setRecent_msg(String recent_msg) {
            this.recent_msg = recent_msg;
        }

        public String getRecent_msg_count() {
            return unseen_msg_count;
        }

        public void setRecent_msg_count(String recent_msg_count) {
            this.unseen_msg_count = recent_msg_count;
        }
    }

    public class insidechatResp {
        public String success;
        public chatResult result;

        public chatResult getResult() {
            return result;
        }

        public void setResult(chatResult result) {
            this.result = result;
        }

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }
    }
}
