package com.example.bahadir.myapplicationn;


public class Paylasilanlar implements Comparable<Paylasilanlar> {

    String veriid;
    String gonderenid;
    String cesit;
    String yaziveyaurl;
    String date;
    String question;
    String option1;
    String option2;
    String option3;
    String option4;
    String optionrate1;
    String optionrate2;
    String optionrate3;
    String optionrate4;

    public String getVeriid() {
        return veriid;
    }

    public void setVeriid(String veriid) {
        this.veriid = veriid;
    }

    public String getGonderenid() {
        return gonderenid;
    }

    public void setGonderenid(String gonderenid) {
        this.gonderenid = gonderenid;
    }

    public String getCesit() {
        return cesit;
    }

    public void setCesit(String cesit) {
        this.cesit = cesit;
    }

    public String getYaziveyaurl() {
        return yaziveyaurl;
    }

    public void setYaziveyaurl(String yaziveyaurl) {
        this.yaziveyaurl = yaziveyaurl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getOptionrate1() {
        return optionrate1;
    }

    public void setOptionrate1(String optionrate1) {
        this.optionrate1 = optionrate1;
    }

    public String getOptionrate2() {
        return optionrate2;
    }

    public void setOptionrate2(String optionrate2) {
        this.optionrate2 = optionrate2;
    }

    public String getOptionrate3() {
        return optionrate3;
    }

    public void setOptionrate3(String optionrate3) {
        this.optionrate3 = optionrate3;
    }

    public String getOptionrate4() {
        return optionrate4;
    }

    public void setOptionrate4(String optionrate4) {
        this.optionrate4 = optionrate4;
    }

    public int compareTo(Paylasilanlar paylasilanlar) {
        String compareDate = ((Paylasilanlar) paylasilanlar).getDate();
        //ascending order
        return this.date.compareTo(compareDate);
    }

    public int compare(Paylasilanlar paylasilanlar, Paylasilanlar t1) {
        return 0;
    }
}


