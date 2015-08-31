package dna.sampling;

import java.io.File;
import java.io.IOException;

import dna.graph.Graph;
import dna.graph.generators.GraphGenerator;
import dna.graph.generators.util.EmptyGraph;
import dna.metrics.Metric;
import dna.metrics.MetricNotApplicableException;
import dna.metrics.assortativity.AssortativityR;
import dna.metrics.clustering.DirectedClusteringCoefficientU;
import dna.metrics.clustering.UndirectedClusteringCoefficientU;
import dna.metrics.degree.DegreeDistributionR;
import dna.metrics.motifs.DirectedMotifsU;
import dna.metrics.motifs.UndirectedMotifsU;
import dna.metrics.paths.UnweightedAllPairsShortestPathsR;
import dna.metrics.sampling.ExtentR;
import dna.metrics.sampling.SamplingModularityB;
import dna.plot.Plotting;
import dna.plot.PlottingConfig;
import dna.plot.PlottingConfig.PlotFlag;
import dna.series.AggregationException;
import dna.series.Series;
import dna.series.data.SeriesData;
import dna.updates.generators.sampling.SamplingAlgorithm;
import dna.updates.generators.sampling.SamplingAlgorithm.SamplingStop;
import dna.util.Config;
import dna.util.Log;

public class Analysis extends Sampling {

	public String dataDir;

	int runs;
	int batches;

	public String[] metricTypes;

	public static enum MetricType {
		DD, ASS, APSP, MOD, CC, M, EXT
	}

	public static void main(String[] args) throws IOException,
			InterruptedException, AggregationException,
			MetricNotApplicableException {

		// args = new String[] { "data/", "plots/", "RANDOM",
		// "UNDIRECTED__1000__10000", "RANDOM_WALK", "__", "RANDOM",
		// "Visiting", "20", "750", "1", "999999" };

		Config.overwrite("GENERATE_VALUES_FROM_DISTRIBUTION", "false");
		Config.overwrite("GENERATE_VALUES_FROM_NODEVALUELIST", "false");

		if (!isOk(args)) {
			printHelp(args);
			print(args);
			return;
		}

		Config.zipRuns();

		print(args);

		Analysis a = new Analysis(args);
		String data = a.getDataDir();
		if (new File(data).exists()) {
			System.out.println("data directory exists...");
			System.out.println(data);
			System.out.println("exiting...");
			return;
		} else {
			a.execute();
		}
	}

	public static void print(String[] args) {
		Log.infoSep();
		Log.info("parameters: " + args.length);
		Log.infoSep();
		for (int i = 0; i < args.length; i++) {
			System.out.println(i + ": " + args[i]);
		}
		Log.infoSep();
	}

	public Analysis(String[] args) {
		int index = 0;
		dataDir = args[index++];
		plotDir = args[index++];
		graphType = GraphType.valueOf(args[index++]);
		graphParameters = args[index++].split(separator);
		samplingType = SamplingType.valueOf(args[index++]);
		samplingParameters = args[index++].split(separator);
		startType = StartNodeType.valueOf(args[index++]);
		start = getStartNodeStrategy(startType);
		stop = SamplingStop.valueOf(args[index++]);
		costPerBatch = Integer.parseInt(args[index++]);
		resource = Integer.parseInt(args[index++]);
		runs = Integer.parseInt(args[index++]);
		batches = Integer.parseInt(args[index++]);
		metricTypes = args[index++].split(separator);
	}

	public static boolean isOk(String[] args) {
		return args.length == 13;
	}

	public static void printHelp(String[] args) {
		System.err.println("Expecting 13 parameters:");
		System.err.println("   0: main data dir");
		System.err.println("   1: main plot dir");
		System.err.println("   2: graph type (" + toString(GraphType.values())
				+ ")");
		System.err.println("   3: graph parameters (separated by " + separator
				+ ")");
		System.err.println("   4: sampling type ("
				+ toString(SamplingType.values()) + ")");
		System.err.println("   5: sampling parameters (separated by "
				+ separator + ")");
		System.err.println("   6: start node strategy ("
				+ toString(StartNodeType.values()) + ")");
		System.err.println("   7: sampling stop ("
				+ toString(SamplingStop.values()) + ")");
		System.err.println("   8: cost per batch");
		System.err.println("   9: resource");
		System.err.println("  10: runs");
		System.err.println("  11: batches");
		System.err.println("  12: metrics (" + toString(MetricType.values())
				+ ")");
		System.err.println("got the following parameters:");
		for (int i = 0; i < args.length; i++) {
			System.err.println(i + ": " + args[i]);
		}
	}

	public void execute() throws IOException, InterruptedException,
			AggregationException, MetricNotApplicableException {
		GraphGenerator gg_ = getGraphGenerator();
		Graph g = gg_.generate();
		System.out.println("sampling from graph: " + g);
		GraphGenerator gg = new EmptyGraph(g.getGraphDatastructures());
		SamplingAlgorithm bg = getSampling(g);
		Metric[] metrics = getMetrics(g, bg);
		Series s = new Series(gg, bg, metrics, getDataDir(), getSamplingName());
		SeriesData sd = s.generate(runs, batches);
		PlottingConfig cfg = new PlottingConfig(PlotFlag.plotMetricValues,
				PlotFlag.plotStatistics);
		Plotting.plot(sd, getPlotDir(), cfg);
	}

	public String getDataDir() {
		return dataDir + getGraphName() + "/" + getSamplingName() + "/";
	}

	public Metric[] getMetrics(Graph g, SamplingAlgorithm algo) {
		Metric[] metrics = new Metric[metricTypes.length];
		for (int i = 0; i < metrics.length; i++) {
			metrics[i] = getMetric(g, MetricType.valueOf(metricTypes[i]), algo);
		}
		return metrics;
	}

	public Metric getMetric(Graph g, MetricType m, SamplingAlgorithm algo) {
		switch (m) {
		case APSP:
			return new UnweightedAllPairsShortestPathsR();
		case ASS:
			return new AssortativityR();
		case CC:
			if (g.isDirected()) {
				return new DirectedClusteringCoefficientU();
			} else {
				return new UndirectedClusteringCoefficientU();
			}
		case DD:
			return new DegreeDistributionR();
		case M:
			if (g.isDirected()) {
				return new DirectedMotifsU();
			} else {
				return new UndirectedMotifsU();
			}
		case MOD:
			return new SamplingModularityB(g);
		case EXT:
			return new ExtentR(algo);
		default:
			System.err.println("unknown metric type: " + m);
			return null;
		}
	}

}
