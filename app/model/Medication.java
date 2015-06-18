package model;

import javax.persistence.*;

@Entity(name = "medication")
@SequenceGenerator(name = "medication_generator", sequenceName = "medication_medication_id_seq", allocationSize = 1, initialValue = 1)
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medication_generator")
    @Column(name = "medication_id")
    private int medicationId;

    @Column(name = "count")
    private double count;

    @Column(name = "title")
    private String title;

    @Column(name = "expiration_date")
    private String expirationDate;

    public int getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(int medicationId) {
        this.medicationId = medicationId;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
