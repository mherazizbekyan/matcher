package com.example.matcher.controller.model;

import java.util.List;

public class MatchingResult {
    //region Properties
    private List<SingleMatchingPair> pairs;

    private double averagePercentage;

    private long computationTime;
    //endregion

    //region Constructors
    public MatchingResult(final List<SingleMatchingPair> pairs, final double averagePercentage) {
        this.pairs = pairs;
        this.averagePercentage = averagePercentage;
    }
    //endregion

    //region Getters, Setters
    public List<SingleMatchingPair> getPairs() {
        return pairs;
    }

    public void setPairs(final List<SingleMatchingPair> pairs) {
        this.pairs = pairs;
    }

    public double getAveragePercentage() {
        return averagePercentage;
    }

    public void setAveragePercentage(final double averagePercentage) {
        this.averagePercentage = averagePercentage;
    }

    public long getComputationTime() {
        return computationTime;
    }

    public void setComputationTime(final long computationTime) {
        this.computationTime = computationTime;
    }

    //endregion
}
