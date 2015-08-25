#!/bin/bash

dataDir="data"
plotDir="plots"
combinedPlotDir="plots_combined"

start="RANDOM"
stop="Visiting"
runs="1"
batches="999999999"

metrics="DD__APSP__CC__ASS__MOD__EXT"

function analysis {
	gt=$1
	gp=$2
	st=$3
	sp=$4
	cost=$5
	resource=$6
	./jobs.sh create "java -jar analysis.jar $dataDir/$pre/ $plotDir/$pre/ $gt $gp $st $sp $start $stop $cost $resource $runs $batches $metrics"
}

function sampling {
	for algo in ${algorithms[@]}; do
		analysis $1 $2 $algo __ $3 $4
	done
}

function plotting {
	./jobs.sh create "java -jar plot.jar ALL_IN_DIR $dataDir/$pre/$1__$2/ $combinedPlotDir/$pre/$1__$2/"
}

algorithms=(BFS DFS RANDOM_WALK RANDOM_WALK_NR UNIFORM)

pre="datasets-SNAP"
# sampling READ_EDGE_LIST datasets--SNAP--__com-amazon.ungraph.uel__UNDIRECTED 335 334863
# sampling READ_EDGE_LIST datasets--SNAP--__cit-HepTh.del__DIRECTED 483 48239
# sampling READ_EDGE_LIST datasets--SNAP--__cit-HepPh.del__DIRECTED 604 60388
# sampling READ_EDGE_LIST datasets--SNAP--__email-Enron.del__DIRECTED 734 73384
# sampling READ_EDGE_LIST datasets--SNAP--__p2p-Gnutella31.del__DIRECTED 787 78670
# sampling READ_EDGE_LIST datasets--SNAP--__flickrEdges.uel__UNDIRECTED 1060 105938
sampling READ_EDGE_LIST datasets--SNAP--__com-youtube.ungraph.uel__UNDIRECTED 1135 1134890
sampling READ_EDGE_LIST datasets--SNAP--__soc-Slashdot0902.del__DIRECTED 1607 160609
sampling READ_EDGE_LIST datasets--SNAP--__email-EuAll.del__DIRECTED 3001 300069
# sampling READ_EDGE_LIST datasets--SNAP--__wiki-Talk.del__DIRECTED 25168 2516783


# pre="datasets-k-hop"
# plotting READ_EDGE_LIST datasets--k-hop--__airport.uelw__UNDIRECTED 16 1574
# plotting READ_EDGE_LIST datasets--k-hop--__blog.uel__UNDIRECTED 40 3982
# plotting READ_EDGE_LIST datasets--k-hop--__euroroad.uel__UNDIRECTED 12 1174
# plotting READ_EDGE_LIST datasets--k-hop--__urv.del__DIRECTED 12 1133



# pre="RANDOM-1k"
# plotting RANDOM 0__UNDIRECTED__1000__1000 10 1000
# plotting RANDOM 0__UNDIRECTED__1000__2000 10 1000
# plotting RANDOM 0__UNDIRECTED__1000__3000 10 1000
# plotting RANDOM 0__UNDIRECTED__1000__4000 10 1000
# plotting RANDOM 0__UNDIRECTED__1000__5000 10 1000
# plotting RANDOM 0__UNDIRECTED__1000__6000 10 1000
# plotting RANDOM 0__UNDIRECTED__1000__7000 10 1000
# plotting RANDOM 0__UNDIRECTED__1000__8000 10 1000

# pre="BA-1k"
# plotting BA 0__UNDIRECTED__10__40__990__1 10 1000
# plotting BA 0__UNDIRECTED__10__40__990__2 10 1000
# plotting BA 0__UNDIRECTED__10__40__990__3 10 1000
# plotting BA 0__UNDIRECTED__10__40__990__4 10 1000
# plotting BA 0__UNDIRECTED__10__40__990__5 10 1000
# plotting BA 0__UNDIRECTED__10__40__990__6 10 1000
# plotting BA 0__UNDIRECTED__10__40__990__7 10 1000
# plotting BA 0__UNDIRECTED__10__40__990__8 10 1000


# pre="RANDOM-5k"
# plotting RANDOM 0__UNDIRECTED__5000__5000 50 5000
# plotting RANDOM 0__UNDIRECTED__5000__10000 50 5000
# plotting RANDOM 0__UNDIRECTED__5000__15000 50 5000
# plotting RANDOM 0__UNDIRECTED__5000__20000 50 5000
# plotting RANDOM 0__UNDIRECTED__5000__25000 50 5000
# plotting RANDOM 0__UNDIRECTED__5000__30000 50 5000
# plotting RANDOM 0__UNDIRECTED__5000__35000 50 5000
# plotting RANDOM 0__UNDIRECTED__5000__40000 50 5000

# pre="BA-5k"
# plotting BA 0__UNDIRECTED__10__40__4990__1 50 5000
# plotting BA 0__UNDIRECTED__10__40__4990__2 50 5000
# plotting BA 0__UNDIRECTED__10__40__4990__3 50 5000
# plotting BA 0__UNDIRECTED__10__40__4990__4 50 5000
# plotting BA 0__UNDIRECTED__10__40__4990__5 50 5000
# plotting BA 0__UNDIRECTED__10__40__4990__6 50 5000
# plotting BA 0__UNDIRECTED__10__40__4990__7 50 5000
# plotting BA 0__UNDIRECTED__10__40__4990__8 50 5000


# pre="RANDOM-10k"
# plotting RANDOM 0__UNDIRECTED__10000__10000 100 10000
# plotting RANDOM 0__UNDIRECTED__10000__20000 100 10000
# plotting RANDOM 0__UNDIRECTED__10000__30000 100 10000
# plotting RANDOM 0__UNDIRECTED__10000__40000 100 10000
# plotting RANDOM 0__UNDIRECTED__10000__50000 100 10000
# plotting RANDOM 0__UNDIRECTED__10000__60000 100 10000
# plotting RANDOM 0__UNDIRECTED__10000__70000 100 10000
# plotting RANDOM 0__UNDIRECTED__10000__80000 100 10000

# pre="BA-10k"
# plotting BA 0__UNDIRECTED__10__40__9990__1 100 10000
# plotting BA 0__UNDIRECTED__10__40__9990__2 100 10000
# plotting BA 0__UNDIRECTED__10__40__9990__3 100 10000
# plotting BA 0__UNDIRECTED__10__40__9990__4 100 10000
# plotting BA 0__UNDIRECTED__10__40__9990__5 100 10000
# plotting BA 0__UNDIRECTED__10__40__9990__6 100 10000
# plotting BA 0__UNDIRECTED__10__40__9990__7 100 10000
# plotting BA 0__UNDIRECTED__10__40__9990__8 100 10000

