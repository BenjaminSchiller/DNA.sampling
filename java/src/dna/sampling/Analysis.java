package dna.sampling;

import java.io.IOException;

import dna.graph.Graph;
import dna.graph.datastructures.GDS;
import dna.graph.generators.GraphGenerator;
import dna.graph.generators.random.RandomGraph;
import dna.graph.generators.util.EmptyGraph;
import dna.metrics.Metric;
import dna.metrics.MetricNotApplicableException;
import dna.metrics.clustering.UndirectedClusteringCoefficientR;
import dna.metrics.degree.DegreeDistributionR;
import dna.metrics.paths.UnweightedAllPairsShortestPathsR;
import dna.metrics.sampling.SamplingModularityR;
import dna.plot.Plotting;
import dna.plot.PlottingConfig;
import dna.plot.PlottingConfig.PlotFlag;
import dna.series.AggregationException;
import dna.series.Series;
import dna.series.data.SeriesData;
import dna.updates.generators.BatchGenerator;
import dna.updates.generators.sampling.BFS;
import dna.updates.generators.sampling.DFS;
import dna.updates.generators.sampling.RandomWalk;
import dna.updates.generators.sampling.RandomWalkNR;
import dna.updates.generators.sampling.RandomWalkNR_Jump;
import dna.updates.generators.sampling.SamplingAlgorithm.SamplingStop;
import dna.updates.generators.sampling.UniformSampling;
import dna.updates.generators.sampling.startNode.HighestDegreeSelection;
import dna.updates.generators.sampling.startNode.RandomSelection;
import dna.updates.generators.sampling.startNode.StartNodeSelectionStrategy;
import dna.util.Config;
import dna.util.Rand;

public class Analysis {

	public static final String separator = "__";

	public String dataDir;
	public String plotDir;

	public SamplingType samplingType;
	public String[] samplingParameters;

	public StartNodeSelectionStrategy start;
	public SamplingStop stop;

	int costPerBatch;
	int resource;

	int runs;
	int batches;

	public static enum StartNodeType {
		HIGHEST_DEGREE, HIGHEST_RANDOM_DEGREE, HIGHEST_RANDOM_DEGREE_SUM, RANDOM
	}

	public static enum SamplingType {
		BFS, DFS, DFS_JUMP, DFS_RANDOM_JUMP, DFS_RANDOM, FOREST_FIRE, FOREST_FIRE_NR, FRONTIER_SAMPLING, GREEDY_ORACLE, MOD, RANDOM_WALK, RANDOM_WALK_NR_JUMP, RANDOM_WALK_NR, RESPONDENT_DRIVEN, SNOWBALL, UNIFORM
	}

	public static void main(String[] args) throws IOException,
			InterruptedException, AggregationException,
			MetricNotApplicableException {

		args = new String[] { "data/", "plots/", "RANDOM_WALK", "__",
				"RANDOM", "Visiting", "20", "750", "1", "999999" };

		if (!isOk(args)) {
			printHelp();
			return;
		}

		Config.zipRuns();

		Analysis a = new Analysis(args);
		a.execute();
	}

	public Analysis(String[] args) {
		int index = 0;
		dataDir = args[index++];
		plotDir = args[index++];
		samplingType = SamplingType.valueOf(args[index++]);
		samplingParameters = args[index++].split(separator);
		start = getStartNodeStrategy(StartNodeType.valueOf(args[index++]));
		stop = SamplingStop.valueOf(args[index++]);
		costPerBatch = Integer.parseInt(args[index++]);
		resource = Integer.parseInt(args[index++]);
		runs = Integer.parseInt(args[index++]);
		batches = Integer.parseInt(args[index++]);
	}

	public static boolean isOk(String[] args) {
		return args.length == 10;
	}

	public static void printHelp() {
		System.err.println("Expecting 10 parameters:");
		System.err.println("  0: main data dir");
		System.err.println("  1: main plot dir");
		System.err.println("  2: sampling type ("
				+ toString(SamplingType.values()) + ")");
		System.err.println("  3: sampling parameters (separated by "
				+ separator + ")");
		System.err.println("  4: start node strategy ("
				+ toString(StartNodeType.values()) + ")");
		System.err.println("  5: sampling stop ("
				+ toString(SamplingStop.values()) + ")");
		System.err.println("  6: cost per batch");
		System.err.println("  7: resource");
		System.err.println("  8: runs");
		System.err.println("  9: batches");
	}

	public static String toString(Object[] obj) {
		StringBuffer buff = new StringBuffer();
		for (Object o : obj) {
			if (buff.length() > 0) {
				buff.append(", ");
			}
			buff.append(o.toString());
		}
		return buff.toString();
	}

	public static StartNodeSelectionStrategy getStartNodeStrategy(
			StartNodeType startNodeType) {
		switch (startNodeType) {
		case HIGHEST_DEGREE:
			return new HighestDegreeSelection();
		case HIGHEST_RANDOM_DEGREE:
			break;
		case HIGHEST_RANDOM_DEGREE_SUM:
			break;
		case RANDOM:
			return new RandomSelection();
		}
		System.err.println("invalid start node selection type: "
				+ startNodeType);
		return null;
	}

	public void execute() throws IOException, InterruptedException,
			AggregationException, MetricNotApplicableException {
		Graph g = getGraph();
		GraphGenerator gg = new EmptyGraph(g.getGraphDatastructures());
		BatchGenerator bg = getSampling(g);
		Metric[] metrics = getMetrics(g);
		Series s = new Series(gg, bg, metrics, getDataDir(), getName());
		SeriesData sd = s.generate(runs, batches);
		PlottingConfig cfg = new PlottingConfig(PlotFlag.plotMetricValues,
				PlotFlag.plotStatistics);
		Plotting.plot(sd, getPlotDir(), cfg);
	}

	public String getDataDir() {
		return dataDir + getName() + "/";
	}

	public String getPlotDir() {
		return plotDir + getName() + "/";
	}

	public String getName() {
		StringBuffer buff = new StringBuffer(samplingType + "");
		for (String p : samplingParameters) {
			buff.append("__" + p);
		}
		return buff.toString();
	}

	public Graph getGraph() {
		Rand.init(0);
		return new RandomGraph(GDS.undirected(), 500, 2000).generate();
	}

	public Metric[] getMetrics(Graph g) {
		return new Metric[] { new DegreeDistributionR(),
				new UnweightedAllPairsShortestPathsR(),
				new UndirectedClusteringCoefficientR(),
				new SamplingModularityR(g) };
	}

	public BatchGenerator getSampling(Graph g) {
		switch (samplingType) {
		case BFS:
			return new BFS(g, start, costPerBatch, resource, stop);
		case DFS:
			return new DFS(g, start, costPerBatch, resource, stop);
		case DFS_JUMP:
			break;
		case DFS_RANDOM:
			break;
		case DFS_RANDOM_JUMP:
			break;
		case FOREST_FIRE:
			break;
		case FOREST_FIRE_NR:
			break;
		case FRONTIER_SAMPLING:
			break;
		case GREEDY_ORACLE:
			break;
		case MOD:
			break;
		case RANDOM_WALK:
			return new RandomWalk(g, start, costPerBatch, resource, stop);
		case RANDOM_WALK_NR:
			return new RandomWalkNR(g, start, costPerBatch, resource, stop);
		case RANDOM_WALK_NR_JUMP:
			return new RandomWalkNR_Jump(g, start, costPerBatch, resource, stop);
		case RESPONDENT_DRIVEN:
			break;
		case SNOWBALL:
			break;
		case UNIFORM:
			return new UniformSampling(g, start, costPerBatch, resource, stop);
		}
		System.err.println("invalid sampling type: " + samplingType);
		return null;
	}

}
