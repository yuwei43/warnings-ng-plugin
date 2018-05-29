package io.jenkins.plugins.analysis.core.graphs;

import java.util.ArrayList;
import java.util.List;

import edu.hm.hafner.util.VisibleForTesting;
import io.jenkins.plugins.analysis.core.model.AnalysisResult;
import io.jenkins.plugins.analysis.core.quality.HealthDescriptor;

/**
 * Builds the series for a graph showing all warnings by health descriptor.
 *
 * @author Ullrich Hafner
 */
public class HealthSeriesBuilder extends SeriesBuilder {
    private final HealthDescriptor healthDescriptor;

    /**
     * Creates a new instance of {@link HealthSeriesBuilder}.
     *
     * @param healthDescriptor the health descriptor to determine the colors of the graph
     */
    public HealthSeriesBuilder(HealthDescriptor healthDescriptor) {
        super();

        this.healthDescriptor = healthDescriptor;
    }

    @VisibleForTesting
    HealthSeriesBuilder(HealthDescriptor healthDescriptor, ResultTime resultTime) {
        super(resultTime);

        this.healthDescriptor = healthDescriptor;
    }

    @Override
    protected List<Integer> computeSeries(AnalysisResult current) {
        List<Integer> series = new ArrayList<>();
        int remainder = current.getTotalSize();

        if (healthDescriptor.isEnabled()) {
            series.add(Math.min(remainder, healthDescriptor.getHealthy()));

            int range = healthDescriptor.getUnHealthy() - healthDescriptor.getHealthy();
            remainder -= healthDescriptor.getHealthy();
            if (remainder > 0) {
                series.add(Math.min(remainder, range));
                remainder -= range;
                if (remainder > 0) {
                    series.add(remainder);
                }
                else {
                    series.add(0);
                }
            }
            else {
                series.add(0);
                series.add(0);
            }
        }
        else { // at least a graph should be shown if the health reporting has been disabled in the meantime
            series.add(remainder);
        }

        return series;
    }
}
