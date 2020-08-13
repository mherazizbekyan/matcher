package com.example.matcher.service.matching.criteria.impl;

import com.example.matcher.controller.model.Employee;
import com.example.matcher.service.matching.criteria.MatchingCriteriaProvider;
import org.springframework.stereotype.Component;

import static com.example.matcher.controller.model.SameLocationPreference.NO;
import static com.example.matcher.controller.model.SameLocationPreference.YES;

@Component
public class MatchingCriteriaProviderImpl implements MatchingCriteriaProvider {

    //region Public methods
    @Override
    public double calculateMatchingWeight(final Employee leftNode, final Employee rightNode) {
        double weight = 0;
        if (areInTheSameDivision(leftNode, rightNode)) {
            weight += 30;
        }
        if (areAgesApplicableForMatching(leftNode, rightNode)) {
            weight += 30;
        }
        if (areTimeZonesApplicableForMatching(leftNode, rightNode)) {
            weight += 40;
        }
        return weight;
    }

    public boolean areLocationPreferencesCompatible(final Employee leftNode, final Employee rightNode) {
        boolean areInSameLocation = leftNode.getLocation().equals(rightNode.getLocation());
        boolean isSameLocationRequired = leftNode.getSameLocationPreference().equals(YES.name()) || rightNode.getSameLocationPreference().equals(YES.name());
        boolean isDifferentLocationRequired = leftNode.getSameLocationPreference().equals(NO.name()) || rightNode.getSameLocationPreference().equals(NO.name());

        return !((areInSameLocation && isDifferentLocationRequired) || (!areInSameLocation && isSameLocationRequired));
    }
    //endregion

    //region Private utility methods
    private boolean areInTheSameDivision(final Employee leftNode, final Employee rightNode) {
        return leftNode.getDivision().equalsIgnoreCase(rightNode.getDivision());
    }

    private boolean areTimeZonesApplicableForMatching(final Employee leftNode, final Employee rightNode) {
        return leftNode.getTimezone() == rightNode.getTimezone();
    }

    private boolean areAgesApplicableForMatching(final Employee leftNode, final Employee rightNode) {
        return Math.abs(leftNode.getAge() - rightNode.getAge()) <= 5;
    }
    //endregion
}
