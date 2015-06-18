package model;

import javax.persistence.*;

@Entity(name = "course")
@SequenceGenerator(name = "course_generator", sequenceName = "medication_medication_id_seq", allocationSize = 1, initialValue = 1)
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_generator")
    @Column(name = "course_id")
    private int courseId;

    @OneToOne
    @JoinColumn(name = "medication_id", nullable = false)
    private Medication medication;

    @OneToOne
    @JoinColumn(name = "method_using_id", nullable = false)
    private MethodUsing methodUsing;

    @Column(name = "start_date")
    private String startDate;

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Medication getMedication() {
        return medication;
    }

    public void setMedication(Medication medication) {
        this.medication = medication;
    }

    public MethodUsing getMethodUsing() {
        return methodUsing;
    }

    public void setMethodUsing(MethodUsing methodUsing) {
        this.methodUsing = methodUsing;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
