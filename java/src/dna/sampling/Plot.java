package dna.sampling;

import java.io.File;
import java.io.IOException;

import dna.plot.Plotting;
import dna.plot.PlottingConfig;
import dna.plot.PlottingConfig.PlotFlag;
import dna.series.data.SeriesData;
import dna.util.Config;
import dna.util.Log;

public class Plot {

	public String dataDir;
	public String plotDir;

	public String[] names;

	public PlotType plotType;

	public static enum PlotType {
		ALL_IN_DIR, LIST_OF_NAMES
	}

	public static void main(String[] args) throws IOException,
			InterruptedException {

		// args = new String[] { "data/", "plots/test1/", "BFS", "DFS",
		// "UNIFORM",
		// "RANDOM_WALK", "RANDOM_WALK_NR" };

		if (!isOk(args)) {
			printHelp();
			return;
		}

		Config.zipRuns();
		Config.overwrite("GNUPLOT_KEY", "bottom right");

		Plot p = new Plot(args);
		if (new File(p.plotDir).exists()) {
			System.out.println(p.plotDir + " exists...");
			System.out.println("skipping...");
			return;
		} else {
			p.print();
			p.execute();
		}
	}

	public Plot(String[] args) {
		int index = 0;
		plotType = PlotType.valueOf(args[index++]);
		dataDir = args[index++];
		plotDir = args[index++];
		switch (plotType) {
		case ALL_IN_DIR:
			File[] files = new File(dataDir).listFiles();
			System.out.println(new File(dataDir));
			System.out.println(new File(dataDir).listFiles().length);
			names = new String[files.length];
			for (int i = 0; i < files.length; i++) {
				names[i] = files[i].getName();
			}
			break;
		case LIST_OF_NAMES:
			names = new String[args.length - index];
			for (int i = index; i < args.length; i++) {
				names[i - index] = args[i];
			}
			break;
		default:
			System.err.println("invalid plot type: " + plotType);
			break;
		}
	}

	public void print() {
		Log.infoSep();
		Log.info("data:  " + dataDir);
		Log.info("plots: " + plotDir);
		Log.infoSep();
		for (String name : names) {
			Log.info("" + name);
		}
		Log.infoSep();
	}

	public void execute() throws IOException, InterruptedException {
		SeriesData[] sd = new SeriesData[names.length];
		for (int i = 0; i < names.length; i++) {
			String name = names[i].replace("__RANDOM__Visiting", "");
			sd[i] = SeriesData.read(getDataDir(names[i]), name, true, true);
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
