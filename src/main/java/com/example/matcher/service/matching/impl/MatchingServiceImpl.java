package com.example.matcher.service.matching.impl;

import com.example.matcher.controller.model.Employee;
import com.example.matcher.controller.model.MatchingResult;
import com.example.matcher.controller.model.SingleMatchingPair;
import com.example.matcher.service.matching.MatchingService;
import org.jgrapht.alg.interfaces.MatchingAlgorithm;
import org.jgrapht.alg.matching.blossom.v5.KolmogorovWeightedPerfectMatching;
import org.jgrapht.alg.matching.blossom.v5.ObjectiveSense;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.*;
import java.util.stream.Collector;

@Service
public class MatchingServiceImpl implements MatchingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchingServiceImpl.class);

    //region Public methods
    @Override
    public MatchingResult calculateBestMatchingCouples(final List<Employee> employees) {
        final SimpleDirectedWeightedGraph<Employee, DefaultWeightedEdge> graph = initializeGraph(employees);
        final KolmogorovWeightedPerfectMatching<Employee, DefaultWeightedEdge> kolmogorovWeightedPerfectMatching = new KolmogorovWeightedPerfectMatching(graph, ObjectiveSense.MAXIMIZE);

        StopWatch stopWatch = new StopWatch("My Stop Watch");

        stopWatch.start("kolmogorov-execution-time");
        final MatchingAlgorithm.Matching<Employee, DefaultWeightedEdge> matching = kolmogorovWeightedPerfectMatching.getMatching();
        stopWatch.stop();
        final long lastTaskTimeMillis = stopWatch.getLastTaskTimeMillis();
        LOGGER.info("Kolmogorov algorithm execution took - {} milliseconds", lastTaskTimeMillis);
        final MatchingResult matchingResult = extractMatchingResult(graph, matching);
        matchingResult.setComputationTime(lastTaskTimeMillis);
        return matchingResult;
    }
    //endregion

    //region Private utility methods
    private SimpleDirectedWeightedGraph<Employee, DefaultWeightedEdge> initializeGraph(final List<Employee> employees) {

        final SimpleDirectedWeightedGraph<Employee, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        //Add all vertexes
        employees.forEach(graph::addVertex);
        //Add all edges
        for (int i = 0; i < employees.size(); i++) {
            for (int j = 0; j < employees.size(); j++) {
                final Employee source = employees.get(i);
                final Employee target = employees.get(j);
                if (source.equals(target)) {
                    continue;
                }
                final double weight = calculateMatchingPercentage(source, target);
                DefaultWeightedEdge e = graph.addEdge(source, target);
                graph.setEdgeWeight(e, weight);
            }
        }
        return graph;
    }

    private MatchingResult extractMatchingResult(final SimpleDirectedWeightedGraph<Employee, DefaultWeightedEdge> graph, final MatchingAlgorithm.Matching<Employee, DefaultWeightedEdge> matching) {
        final Set<DefaultWeightedEdge> edges = matching.getEdges();
        final Iterator<DefaultWeightedEdge> iterator = edges.iterator();

        final List<SingleMatchingPair> matchingPairs = new ArrayList<>();

        while (iterator.hasNext()) {
            final DefaultWeightedEdge next = iterator.next();
            final Employee source = graph.getEdgeSource(next);
            final Employee target = graph.getEdgeTarget(next);
            final double edgeWeight = graph.getEdgeWeight(next);
            final SingleMatchingPair singleMatchingPair = new SingleMatchingPair(source, target, edgeWeight);
            matchingPairs.add(singleMatchingPair);
        }
        final double averageMatchingPercentage = matching.getWeight() / (double) matching.getEdges().size();
        Collections.sort(matchingPairs);
        return new MatchingResult(matchingPairs, averageMatchingPercentage);
    }

    private double calculateMatchingPercentage(final Employee mentor, final Employee mentee) {
        double matchingPercentage = 0;

        if (mentor.equals(mentee)) {
            return Double.MIN_VALUE;
        }
        if (mentor.getDivision().equalsIgnoreCase(mentee.getDivision())) {
            matchingPercentage += 30;
        }
        if (checkAgeMatching(mentor, mentee)) {
            matchingPercentage += 30;
        }
        if (mentor.getTimezone() == mentee.getTimezone()) {
            matchingPercentage += 40;
        }

        return matchingPercentage;
    }

    private boolean checkAgeMatching(final Employee mentor, final Employee mentee) {
        if (Math.abs(mentor.getAge() - mentee.getAge()) <= 5) {
            return true;
        }
        return false;
    }
    //endregion

}