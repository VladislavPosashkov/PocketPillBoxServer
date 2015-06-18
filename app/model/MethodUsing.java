package model;

import javax.persistence.*;

@Entity(name = "method_using")
@SequenceGenerator(name = "method_using_generator", sequenceName = "method_using_method_using_id_seq", allocationSize = 1, initialValue = 1)
public class MethodUsing {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "method_using_generator")
    @Column(name = "method_using_id")
    private int methodUsingId;

    @Column(name = "measuring")
    private String measuring;

    @Column(name = "duration")
    private String duration;

    @Column(name = "how_often")
    private String howOften;

    @Column(name = "receipt_time")
    private String receiptTime;

    public int getMethodUsingId() {
        return methodUsingId;
    }

    public void setMethodUsingId(int methodUsingId) {
        this.methodUsingId = methodUsingId;
    }

    public String getMeasuring() {
        return measuring;
    }

    public void setMeasuring(String measuring) {
        this.measuring = measuring;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getHowOften() {
        return howOften;
    }

    public void setHowOften(String howOften) {
        this.howOften = howOften;
    }

    public String getReceiptTime() {
        return receiptTime;
    }

    public void setReceiptTime(String receiptTime) {
        this.receiptTime = receiptTime;
    }
}
