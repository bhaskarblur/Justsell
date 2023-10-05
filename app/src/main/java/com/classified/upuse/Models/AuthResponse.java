package com.classified.upuse.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AuthResponse {

    public class SendOtp {

        public String message;
        public String code;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public class VerifyOtp {

        public String message;
        public String code;

        public VerifyOtp(String message, profile_data result) {
            this.message = message;
            this.result = result;
        }

        public VerifyOtp() {

        }
        @SerializedName("result")
        public profile_data result;
        public String getMessage() {
            return message;
        }

        public profile_data getResult() {
            return result;
        }

        public void setResult(profile_data result) {
            this.result = result;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public class getstate {

        @SerializedName("status")
        private String success;
        private String message;

        private List<stateresp> result;

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public List<stateresp> getResult() {
            return result;
        }

        public void setResult(List<stateresp> result) {
            this.result = result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
    public class stateresp {

        private String id;

        @SerializedName("state")
        private String statename;
        private String countryid;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStatename() {
            return statename;
        }

        public void setStatename(String statename) {
            this.statename = statename;
        }

        public String getCountryid() {
            return countryid;
        }

        public void setCountryid(String countryid) {
            this.countryid = countryid;
        }
    }
    public class cityresp {

        private String id;
        private String city;
        private String stateid;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getStateid() {
            return stateid;
        }

        public void setStateid(String stateid) {
            this.stateid = stateid;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }


    }
    public class getcity {
        private String success;
        private List<cityresp> result;

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public List<cityresp> getResult() {
            return result;
        }

        public void setResult(List<cityresp> result) {
            this.result = result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        private String message;
    }

    public class profile_update {
        public String message;
        public String code;
        public profile_data result;

        public profile_data getResult() {
            return result;
        }

        public void setResult(profile_data result) {
            this.result = result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String status) {
            this.message = status;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

    }

    public class profile_data {
        private String id;
        private String image;
        private String mobile;
        private String name;
        private String state;
        private String city;


        @SerializedName("user_type")
        private String user_type;

        private String device_token;
        public String getImage() {
            return image;
        }

        public String getDevice_token() {
            return device_token;
        }

        public void setDevice_token(String device_token) {
            this.device_token = device_token;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }

}
