package com.db.newecom.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Faq implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("question")
    private String question;

    @SerializedName("answer")
    private String answer;

    public String getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

}
