package uoc.ds.pr.model;


import java.time.LocalDate;

public class Rating extends AbstractModel {

    private static final int MIN_SCORE = 0;
    private static final int MAX_SCORE = 10;
    private LocalDate date;
    private int resolutionScore;
    private int speedScore;
    private int treatmentScore;
    private String assistanceId;

    public Rating(String id, String assistanceId, LocalDate date, int resolutionScore, int speedScore, int treatmentScore) {
        super(id);
        setAssistanceId(assistanceId);
        setDate(date);
        setResolutionScore(resolutionScore);
        setSpeedScore(speedScore);
        setTreatmentScore(treatmentScore);
    }

    public void update(String assistanceId, LocalDate date, int resolutionScore, int speedScore, int treatmentScore) {
        setAssistanceId(assistanceId);
        setDate(date);
        setResolutionScore(resolutionScore);
        setSpeedScore(speedScore);
        setTreatmentScore(treatmentScore);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getResolutionScore() {
        return resolutionScore;
    }

    public void setResolutionScore(int resolutionScore) {
        this.resolutionScore = validateScore(resolutionScore);
    }

    public int getSpeedScore() {
        return speedScore;
    }

    public void setSpeedScore(int speedScore) {
        this.speedScore = validateScore(speedScore);
    }

    public int getTreatmentScore() {
        return treatmentScore;
    }

    public void setTreatmentScore(int treatmentScore) {
        this.treatmentScore = validateScore(treatmentScore);
    }

    public String getAssistanceId() {
        return assistanceId;
    }

    public void setAssistanceId(String assistance) {
        this.assistanceId = assistance;
    }

    private int validateScore(int score) {
        if (score < MIN_SCORE || score > MAX_SCORE) {
            throw new IllegalArgumentException("Score must be between 0 and 10");
        }
        return score;
    }

    public double getAverageScore() {
        return (resolutionScore + speedScore + treatmentScore) / 3.0;
    }

}