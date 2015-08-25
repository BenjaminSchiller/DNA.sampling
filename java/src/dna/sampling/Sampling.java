package dna.sampling;

import java.io.IOException;

import dna.graph.Graph;
import dna.graph.datastructures.GDS;
import dna.graph.datastructures.GraphDataStructure;
import dna.graph.generators.GraphGenerator;
import dna.graph.generators.evolvingNetworks.BarabasiAlbertGraph;
import dna.graph.generators.random.RandomGraph;
import dna.graph.generators.util.ReadableEdgeListFileGraph;
import dna.graph.generators.util.ReadableFileGraph;
import dna.updates.generators.sampling.BFS;
import dna.updates.generators.sampling.DFS;
import dna.updates.generators.sampling.GreedyOracle;
import dna.updates.generators.sampling.MaximumObservedDegree;
import dna.updates.generators.sampling.RandomWalk;
import dna.updates.generators.sampling.RandomWalkNR;
import dna.updates.generators.sampling.RandomWalkNR_Jump;
import dna.updates.generators.sampling.SamplingAlgorithm;
import dna.updates.generators.sampling.SamplingAlgorithm.SamplingStop;
import dna.updates.generators.sampling.UniformSampling;
import dna.updates.generators.sampling.startNode.HighestDegreeSelection;
import dna.updates.generators.sampling.startNode.RandomSelection;
import dna.updates.generators.sampling.startNode.StartNodeSelectionStrategy;
import dna.util.Rand;

public abstract class Sampling {

	public static final String separator = "__";

	public static enum StartNodeType {
		HIGHEST_DEGREE, HIGHEST_RANDOM_DEGREE, HIGHEST_RANDOM_DEGREE_SUM, RANDOM
	}

	public static enum SamplingType {
		BFS, DFS, DFS_JUMP, DFS_RANDOM_JUMP, DFS_RANDOM, FOREST_FIRE, FOREST_FIRE_NR, FRONTIER_SAMPLING, GREEDY_ORACLE, MOD, RANDOM_WALK, RANDOM_WALK_NR_JUMP, RANDOM_WALK_NR, RESPONDENT_DRIVEN, SNOWBALL, UNIFORM
	}

	public static enum GraphType {
		RANDOM, BA, READ, READ_EDGE_LIST
	}

	public static enum GdsType {
		DIRECTED, UNDIRECTED
	}

	public String plotDir;

	public GraphType graphType;
	public String[] graphParameters;

	public SamplingType samplingType;
	public String[] samplingParameters;

	public StartNodeType startType;
	public StartNodeSelectionStrategy start;
	public SamplingStop stop;

	int costPerBatch;
	int resource;

	public String getName(String name, String[] parameters,
			String... additional) {
		StringBuffer buff = new StringBuffer(name);
		for (String p : parameters) {
			buff.append(separator + p);
		}
		for (String a : additional) {
			buff.append(separator + a);
		}
		return buff.toString();
	}

	public String getGraphName() {
		return getName(graphType.toString(), graphParameters);
	}

	public String getSamplingName() {
		return getName(samplingType.toString(), samplingParameters,
				startType.toString(), stop.toString());
	}

	public String getPlotDir() {
		return plotDir + getGraphName() + "/" + getSamplingName() + "/";
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

	public GraphGenerator getGraphGenerator() {
		switch (graphType) {
		case BA:
			Rand.init(Integer.parseInt(graphParameters[0]));
			return new BarabasiAlbertGraph(
					getGds(GdsType.valueOf(graphParameters[1])),
					Integer.parseInt(graphParameters[2]),
					Integer.parseInt(graphParameters[3]),
					Integer.parseInt(graphParameters[4]),
					Integer.parseInt(graphParameters[5]));
		case RANDOM:
			Rand.init(Integer.parseInt(graphParameters[0]));
			return new RandomGraph(getGds(GdsType.valueOf(graphParameters[1])),
					Integer.parseInt(graphParameters[2]),
					Integer.parseInt(graphParameters[3]));
		case READ:
			try {
				if (graphParameters.length > 2) {
					return new ReadableFileGraph(graphParameters[0].replace(
							"--", "/"), graphParameters[1]);

				} else {
					return new ReadableFileGraph(graphParameters[0].replace(
							"--", "/"), graphParameters[1],
							getGds(GdsType.valueOf(graphParameters[2])));
				}
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		case READ_EDGE_LIST:
			if (graphParameters.length == 3) {
				return new ReadableEdgeListFileGraph(
						graphParameters[0].replace("--", "/"),
						graphParameters[1], "\\s+",
						getGds(GdsType.valueOf(graphParameters[2])));
			} else {
				return new ReadableEdgeListFileGraph(
						graphParameters[0].replace("--", "/"),
						graphParameters[1], graphParameters[3],
						getGds(GdsType.valueOf(graphParameters[2])));
			}
		default:
			System.err.println("invalid graph type: " + graphType);
			return null;
		}
	}

	public GraphDataStructure getGds(GdsType gdsType) {
		switch (gdsType) {
		case DIRECTED:
			return GDS.directed();
		case UNDIRECTED:
			return GDS.undirected();
		default:
			System.err.println("invalid gds type: " + gdsType);
			return null;
		}
	}

	public SamplingAlgorithm getSampling(Graph g) {
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
			return new GreedyOracle(g, start, costPerBatch, resource, stop);
		case MOD:
			new MaximumObservedDegree(g, start, costPerBatch, resource, stop);
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
}
