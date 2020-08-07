package com.example.matcher.controller.model;

public class SingleMatchingPair implements  Comparable<SingleMatchingPair> {

    //region Properties
    private Employee mentor;
    private Employee mentee;
    private double matchingPercentage;
    //endregion

    //region Constructors
    public SingleMatchingPair(final Employee mentor, final Employee mentee, final double matchingPercentage) {
        this.mentor = mentor;
        this.mentee = mentee;
        this.matchingPercentage = matchingPercentage;
    }

    //endregion

    //region Getters, Setters
    public Employee getMentor() {
        return mentor;
    }

    public void setMentor(final Employee mentor) {
        this.mentor = mentor;
    }

    public Employee getMentee() {
        return mentee;
    }

    public void setMentee(final Employee mentee) {
        this.mentee = mentee;
    }

    public double getMatchingPercentage() {
        return matchingPercentage;
    }

    public void setMatchingPercentage(final double matchingPercentage) {
        this.matchingPercentage = matchingPercentage;
    }
    //endregion

    @Override
    public int compareTo(final SingleMatchingPair singleMatchingPair) {
        return (this.equals(singleMatchingPair))? 0 : Double.compare(singleMatchingPair.matchingPercentage, matchingPercentage);
    }

}
