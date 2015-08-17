package dna.sampling;

import java.io.IOException;

import dna.plot.Plotting;
import dna.plot.PlottingConfig;
import dna.plot.PlottingConfig.PlotFlag;
import dna.series.data.SeriesData;
import dna.util.Config;

public class Plot {

	public String dataDir;
	public String plotDir;

	public String[] names;

	public static void main(String[] args) throws IOException,
			InterruptedException {

		args = new String[] { "data/", "plots/test1/", "BFS", "DFS", "UNIFORM",
				"RANDOM_WALK", "RANDOM_WALK_NR" };

		if (!isOk(args)) {
			printHelp();
			return;
		}

		Config.zipRuns();
		
		Config.overwrite("GNUPLOT_KEY", "bottom right");

		Plot p = new Plot(args);
		p.execute();
	}

	public Plot(String[] args) {
		dataDir = args[0];
		plotDir = args[1];
		names = new String[args.length - 2];
		for (int i = 2; i < args.length; i++) {
			names[i - 2] = args[i];
		}
	}

	public void execute() throws IOException, InterruptedException {
		SeriesData[] sd = new SeriesData[names.length];
		for (int i = 0; i < names.length; i++) {
			sd[i] = SeriesData.read(getDataDir(names[i]), names[i], true, true);
		}
		PlottingConfig cfg = new PlottingConfig(PlotFlag.plotMetricValues,
				PlotFlag.plotStatistics);
		Plotting.plot(sd, plotDir, cfg);
	}

	public String getDataDir(String name) {
		return dataDir + name + "/";
	}

	public static boolean isOk(String[] args) {
		return args.length > 2;
	}

	public static void printHelp() {
		System.err.println("Expecting at least 3 arguments:");
		System.err.println("  0: main data dir");
		System.err.println("  1: plot dir");
		System.err.println("  2,3,...: name of series");
	}

}
