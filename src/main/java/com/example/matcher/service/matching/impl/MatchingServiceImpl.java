package com.example.matcher.service.matching.impl;

import com.example.matcher.controller.model.Employee;
import com.example.matcher.controller.model.MatchingResult;
import com.example.matcher.controller.model.SingleMatchingPair;
import com.example.matcher.exception.UnprocessableCsvFileException;
import com.example.matcher.exception.WrongFileExtensionException;
import com.example.matcher.service.matching.criteria.MatchingCriteriaProvider;
import com.example.matcher.service.matching.criteria.impl.MatchingCriteriaProviderImpl;
import com.example.matcher.service.matching.MatchingService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.jgrapht.alg.interfaces.MatchingAlgorithm;
import org.jgrapht.alg.matching.blossom.v5.KolmogorovWeightedPerfectMatching;
import org.jgrapht.alg.matching.blossom.v5.ObjectiveSense;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

@Service
public class MatchingServiceImpl implements MatchingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchingServiceImpl.class);
    private static final String TIMER_TASK_NAME = "kolmogorov-execution-task";
    private static final String FILE_EXTENSION = ".csv";

    //region Dependencies
    private final MatchingCriteriaProvider matchingCriteriaProvider;
    //endregion

    //region Constructor

    public MatchingServiceImpl(final MatchingCriteriaProvider matchingCriteriaProvider) {
        this.matchingCriteriaProvider = matchingCriteriaProvider;
    }
    //endregion

    //region Public methods
    @Override
    public MatchingResult calculateBestMatchingCouplesFromCsv(final MultipartFile file) throws UnprocessableCsvFileException, WrongFileExtensionException {
        final List<Employee> employees = extractEmployeesFromCSV(file);

        final SimpleDirectedWeightedGraph<Employee, DefaultWeightedEdge> graph = buildGraph(employees);
        final KolmogorovWeightedPerfectMatching<Employee, DefaultWeightedEdge> kolmogorovWeightedPerfectMatching = new KolmogorovWeightedPerfectMatching(graph, ObjectiveSense.MAXIMIZE);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start(TIMER_TASK_NAME);
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
    private List<Employee> extractEmployeesFromCSV(final MultipartFile file) throws UnprocessableCsvFileException, WrongFileExtensionException {
        try (Reader reader = new InputStreamReader(file.getInputStream());) {
            if (!Objects.requireNonNull(file.getOriginalFilename()).endsWith(FILE_EXTENSION)) {
                throw new WrongFileExtensionException(String.format("File extension is not %s", FILE_EXTENSION));
            }
            final CsvToBean<Employee> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(Employee.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            return csvToBean.parse();
        } catch (IOException e) {
            throw new UnprocessableCsvFileException("Unable to extract employees from provided file");
        }
    }

    private SimpleDirectedWeightedGraph<Employee, DefaultWeightedEdge> buildGraph(final List<Employee> employees) {
        final SimpleDirectedWeightedGraph<Employee, DefaultWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        employees.forEach(graph::addVertex);
        addEdgesToGraph(employees, graph);
        return graph;
    }

    private void addEdgesToGraph(final List<Employee> employees, final SimpleDirectedWeightedGraph<Employee, DefaultWeightedEdge> graph) {
        employees.forEach(source -> employees.forEach(target -> {
            if (!source.equals(target)) {
                final double weight = matchingCriteriaProvider.calculateMatchingWeight(source, target);
                final DefaultWeightedEdge edge = graph.addEdge(source, target);
                graph.setEdgeWeight(edge, weight);
            }
        }));
    }

    private MatchingResult extractMatchingResult(final SimpleDirectedWeightedGraph<Employee, DefaultWeightedEdge> graph,
                                                 final MatchingAlgorithm.Matching<Employee, DefaultWeightedEdge> matching) {
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
    //endregion

}