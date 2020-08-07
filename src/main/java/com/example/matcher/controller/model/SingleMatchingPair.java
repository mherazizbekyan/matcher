package com.example.matcher.controller.model;

public class SingleMatchingPair implements  Comparable<SingleMatchingPair> {

    //region Properties
    private Employee leftNode;
    private Employee rightNode;
    private double matchingPercentage;
    //endregion

    //region Constructors
    public SingleMatchingPair(final Employee leftNode, final Employee rightNode, final double matchingPercentage) {
        this.leftNode = leftNode;
        this.rightNode = rightNode;
        this.matchingPercentage = matchingPercentage;
    }

    //endregion

    //region Getters, Setters
    public Employee getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(final Employee leftNode) {
        this.leftNode = leftNode;
    }

    public Employee getRightNode() {
        return rightNode;
    }

    public void setRightNode(final Employee rightNode) {
        this.rightNode = rightNode;
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
