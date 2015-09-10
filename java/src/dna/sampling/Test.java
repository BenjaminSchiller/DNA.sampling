package dna.sampling;

import java.io.IOException;

import dna.graph.Graph;
import dna.graph.IElement;
import dna.graph.datastructures.GDS;
import dna.graph.generators.GraphGenerator;
import dna.graph.generators.connectivity.StronglyConnectedGraph;
import dna.graph.generators.connectivity.WeaklyConnectedGraph;
import dna.graph.generators.random.RandomGraph;
import dna.graph.generators.util.EmptyGraph;
import dna.graph.nodes.DirectedNode;
import dna.graph.nodes.Node;
import dna.metrics.Metric;
import dna.metrics.MetricNotApplicableException;
import dna.metrics.degree.DegreeDistributionR;
import dna.plot.Plotting;
import dna.plot.PlottingConfig;
import dna.plot.PlottingConfig.PlotFlag;
import dna.series.AggregationException;
import dna.series.Series;
import dna.series.data.SeriesData;
import dna.updates.batch.Batch;
import dna.updates.generators.BatchGenerator;
import dna.updates.generators.random.RandomBatch;
import dna.updates.generators.sampling.BFS;
import dna.updates.generators.sampling.DFS;
import dna.updates.generators.sampling.SamplingAlgorithm;
import dna.updates.generators.sampling.SamplingAlgorithm.SamplingStop;
import dna.updates.generators.sampling.SamplingAlgorithm.WalkingType;
import dna.updates.generators.sampling.startNode.RandomSelection;
import dna.util.Config;
import dna.util.Rand;

public class Test {

	public static void main(String[] args) throws AggregationException,
			IOException, MetricNotApplicableException, InterruptedException {
		// series();
		// walkingType(false);
		// walkingType(true);
		run();
	}

	public static void run() throws IOException, MetricNotApplicableException,
			InterruptedException {
		Config.zipRuns();
		String data = "run-test/data/";
		String name = "name";
		String plots = "run-test/plots/";

		GraphGenerator gg = new RandomGraph(GDS.directed(), 100, 200);
		BatchGenerator bg = new RandomBatch(0, 0, 20, 10);
		Metric[] metrics = new Metric[] { new DegreeDistributionR() };
		Series s = new Series(gg, bg, metrics, data, name);
		s.generateRuns(0, 0, 10);
		SeriesData sd = SeriesData.read(data, name, false, true);
		PlottingConfig cfg = new PlottingConfig(PlotFlag.plotSingleScalarValues);
		Plotting.plot(sd, plots, cfg);
	}

	public static void walkingType(boolean directed) {
		Graph g = directed ? new StronglyConnectedGraph(new RandomGraph(
				GDS.directed(), 1000, 8000)).generate()
				: new WeaklyConnectedGraph(new RandomGraph(GDS.undirected(),
						100, 500)).generate();
		// g = new StronglyConnectedGraph(new CliqueGraph(GDS.directed(), 100))
		// .generate();

		SamplingAlgorithm s1 = new DFS(g, new RandomSelection(), 10, 1000,
				SamplingStop.Visiting, WalkingType.AllEdges);
		SamplingAlgorithm s2 = new DFS(g, new RandomSelection(), 10, 1000,
				SamplingStop.Visiting, WalkingType.InEdges);
		SamplingAlgorithm s3 = new DFS(g, new RandomSelection(), 10, 1000,
				SamplingStop.Visiting, WalkingType.OutEdges);

		System.out.println(g);

		Rand.init(0);
		Graph g1 = sample(s1);
		System.out.println(g1);
		if (directed) {
			Rand.init(0);
			Graph g2 = sample(s2);
			System.out.println(g2);
			Rand.init(0);
			Graph g3 = sample(s3);
			System.out.println(g3);
			System.out.println(g2.getEdgeCount() + g3.getEdgeCount() + " ?=? "
					+ g1.getEdgeCount());

			int bidirectionality = 0;
			for (IElement n_ : g.getNodes()) {
				Node n = (Node) n_;
				bidirectionality += ((DirectedNode) n).getNeighborCount();
			}
			System.out.println("BID: " + bidirectionality / 2);
		}
	}

	public static Graph sample(SamplingAlgorithm bg) {
		Graph g = new EmptyGraph(bg.getFullGraph().getGraphDatastructures())
				.generate();
		while (bg.isFurtherBatchPossible(g)) {
			Batch b = bg.generate(g);
			b.apply(g);
		}
		return g;
	}

	public static void series() throws AggregationException, IOException,
			MetricNotApplicableException {
		Graph g = new RandomGraph(GDS.undirected(), 100, 300).generate();
		g.printAll();
		GraphGenerator gg = new EmptyGraph(GDS.undirected());
		BatchGenerator bg = new BFS(g, new RandomSelection(), 10, 100,
				SamplingStop.Visiting, WalkingType.AllEdges);

		Graph g_ = gg.generate();
		Batch b = bg.generate(g_);
		System.out.println(b);

		Metric[] metrics = new Metric[] { new DegreeDistributionR() };
		Series s = new Series(gg, bg, metrics, "data/", "name");
		s.generate(1, 1000000000);
	}

}
