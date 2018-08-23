package cn.thinkfree.database.vo;

import java.util.Date;

public class LogInfoSEO  extends AbsPageSearchCriteria  {

    private Date date;
    private String person;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }
}
