package dna.sampling;

import java.io.File;
import java.io.IOException;

import dna.graph.Graph;
import dna.graph.generators.GraphGenerator;
import dna.graph.generators.util.EmptyGraph;
import dna.metrics.MetricNotApplicableException;
import dna.series.AggregationException;
import dna.updates.batch.Batch;
import dna.updates.generators.BatchGenerator;
import dna.updates.generators.sampling.SamplingAlgorithm;
import dna.updates.generators.sampling.SamplingAlgorithm.SamplingStop;
import dna.util.Config;
import dna.util.Log;
import dna.visualization.graph.GraphVisualization;

public class Visualization extends Sampling {

	public long waitBeforeScreenshot = 50;
	public long waitAfterScreenshot = 200;
	public long waitBeforeExit = 2000;

	public static int delay = 20;
	public static final String animatedFilename = "animated.gif";

	public static void main(String[] args) throws IOException,
			InterruptedException, AggregationException,
			MetricNotApplicableException {

		// args = new String[] { "plots/", "RANDOM", "0__UNDIRECTED__500__2000",
		// "RANDOM_WALK", "__", "RANDOM", "Visiting", "10", "100", "10" };

		if (!isOk(args)) {
			printHelp(args);
			print(args);
			return;
		}

		Config.zipRuns();

		print(args);

		Visualization v = new Visualization(args);
		String plot = v.getPlotDir();
		if (new File(plot).exists()) {
			System.out.println("plot directory exists...");
			System.out.println(plot);
			System.out.println("exiting...");
			return;
		} else {
			v.execute();
		}
	}

	protected void screenshot(String dir, long timestamp) {
		String index = "" + timestamp;
		if (timestamp < 10)
			index = "000" + timestamp;
		else if (timestamp < 100)
			index = "00" + timestamp;
		else if (timestamp < 1000)
			index = "0" + timestamp;
		else if (timestamp < 10000)
			index = "" + timestamp;
		screenshot(dir, index);
	}

	protected void screenshot(String dir, String filename) {
		(new File(dir)).mkdirs();
		sleep(waitBeforeScreenshot);
		String dest = dir + filename + ".png";
		System.out.println("    => " + dest);
		GraphVisualization.getCurrentGraphPanel().getGraph()
				.addAttribute(GraphVisualization.screenshotsKey, dest);
		sleep(waitAfterScreenshot);
		if (!(new File(dest)).exists()) {
			System.out.println("    screenshot was NOT generated ! ! !");
		}
	}

	protected void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected void exit() {
		sleep(waitBeforeExit);
		System.exit(0);
	}

	protected void execute(GraphGenerator gg, BatchGenerator bg, int batches) {
		String dir = getPlotDir();
		Graph g = gg.generate();
		screenshot(dir, g.getTimestamp());
		for (int i = 0; i < batches; i++) {
			if (!bg.isFurtherBatchPossible(g)) {
				break;
			}
			Batch b = bg.generate(g);
			b.apply(g);
			screenshot(dir, g.getTimestamp());
		}
		try {
			Runtime.getRuntime().exec(
					"/opt/local/bin/convert -delay " + delay + " *.png "
							+ animatedFilename, new String[0], new File(dir));
		} catch (IOException e) {
			e.printStackTrace();
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

	public Visualization(String[] args) {
		int index = 0;
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
		delay = Integer.parseInt(args[index++]);
	}

	public static boolean isOk(String[] args) {
		return args.length == 10;
	}

	public static void printHelp(String[] args) {
		System.err.println("Expecting 10 parameters:");
		System.err.println("   0: main plot dir");
		System.err.println("   1: graph type (" + toString(GraphType.values())
				+ ")");
		System.err.println("   2: graph parameters (separated by " + separator
				+ ")");
		System.err.println("   3: sampling type ("
				+ toString(SamplingType.values()) + ")");
		System.err.println("   4: sampling parameters (separated by "
				+ separator + ")");
		System.err.println("   5: start node strategy ("
				+ toString(StartNodeType.values()) + ")");
		System.err.println("   6: sampling stop ("
				+ toString(SamplingStop.values()) + ")");
		System.err.println("   7: cost per batch");
		System.err.println("   8: resource");
		System.err.println("   9: delay for gif video");
		System.err.println("got the following parameters:");
		for (int i = 0; i < args.length; i++) {
			System.err.println(i + ": " + args[i]);
		}
	}

	public void execute() throws IOException, InterruptedException,
			AggregationException, MetricNotApplicableException {
		GraphGenerator gg_ = getGraphGenerator();
		Graph g_ = gg_.generate();
		GraphGenerator gg = new EmptyGraph(g_.getGraphDatastructures());
		SamplingAlgorithm bg = getSampling(g_);
		
		GraphVisualization.enable();

		this.execute(gg, bg, 99999999);

		exit();
	}

}
