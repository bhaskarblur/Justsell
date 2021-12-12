package com.classified.justsell.Models;

import com.google.gson.annotations.SerializedName;

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
        private String product_name;
        public String product_id;
        public String product_img;
        public String person_name;
        public String person_img;
        public String recent_msg;
        public String unseen_msg_count;
        public String product_title;
        public String product_price;
        public String status;
        private String postedby_id;
        private String postedby_number;

        public String getPostedby_number() {
            return postedby_number;
        }

        public void setPostedby_number(String postedby_number) {
            this.postedby_number = postedby_number;
        }

        public String getPostedby_id() {
            return postedby_id;
        }

        public void setPostedby_id(String postedby_id) {
            this.postedby_id = postedby_id;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUnseen_msg_count() {
            return unseen_msg_count;
        }

        public void setUnseen_msg_count(String unseen_msg_count) {
            this.unseen_msg_count = unseen_msg_count;
        }

        public String getProduct_title() {
            return product_title;
        }

        public void setProduct_title(String product_title) {
            this.product_title = product_title;
        }

        public String getProduct_price() {
            return product_price;
        }

        public void setProduct_price(String product_price) {
            this.product_price = product_price;
        }

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
        public List<pastMessages> messages;

        public List<pastMessages> getMessages() {
            return messages;
        }

        public void setMessages(List<pastMessages> messages) {
            this.messages = messages;
        }

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

    public class pastMessages {
        public String user_id;
        public String person_id;
        public String product_id;
        public String time;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getIsSent() {
            return isSent;
        }

        public void setIsSent(String isSent) {
            this.isSent = isSent;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String message;
        public String image;
        public String isSent;

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

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getSent() {
            return isSent;
        }

        public void setSent(String sent) {
            isSent = sent;
        }
    }
}
