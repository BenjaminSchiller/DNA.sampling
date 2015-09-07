package dna.sampling;

import java.io.IOException;

import dna.graph.Graph;
import dna.graph.datastructures.GDS;
import dna.graph.generators.GraphGenerator;
import dna.graph.generators.random.RandomGraph;
import dna.graph.generators.util.EmptyGraph;
import dna.metrics.Metric;
import dna.metrics.MetricNotApplicableException;
import dna.metrics.degree.DegreeDistributionR;
import dna.series.AggregationException;
import dna.series.Series;
import dna.updates.batch.Batch;
import dna.updates.generators.BatchGenerator;
import dna.updates.generators.sampling.BFS;
import dna.updates.generators.sampling.SamplingAlgorithm.SamplingStop;
import dna.updates.generators.sampling.SamplingAlgorithm.WalkingType;
import dna.updates.generators.sampling.startNode.RandomSelection;
import dna.util.Config;

public class Test {

	public static void main(String[] args) throws AggregationException,
			IOException, MetricNotApplicableException {
		Config.overwrite("GENERATE_VALUES_FROM_DISTRIBUTION", "false");
		Config.overwrite("GENERATE_VALUES_FROM_NODEVALUELIST", "false");

		Graph g = new RandomGraph(GDS.undirected(), 100, 300).generate();
		g.printAll();
		GraphGenerator gg = new EmptyGraph(GDS.undirected());
		BatchGenerator bg = new BFS(g, new RandomSelection(), 10, 100,
				SamplingStop.Visiting, WalkingType.AllEdges	);

		Graph g_ = gg.generate();
		Batch b = bg.generate(g_);
		System.out.println(b);

		Metric[] metrics = new Metric[] { new DegreeDistributionR() };
		Series s = new Series(gg, bg, metrics, "data/", "name");
		s.generate(1, 1000000000);
	}

}
