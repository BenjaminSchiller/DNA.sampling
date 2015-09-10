#!/bin/bash

dataDir="data"
plotDir="plots"
combinedPlotDir="plots_combined"

start="RANDOM"
stop="Visiting"
runs="1"
batches="999999999"


function analysis {
	gt=$1
	gp=$2
	st=$3
	sp=$4
	cost=$5
	resource=$6
	connectivityType=$7
	walkingType=$8
	# java -jar analysis.jar $dataDir/$pre/ $plotDir/$pre/ $gt $gp $st $sp $start $stop $cost $resource $runs $batches $metrics $connectivityType $walkingType
	./jobs.sh create "java -jar analysis.jar $dataDir/$pre/ $plotDir/$pre/ $gt $gp $st $sp $start $stop $cost $resource $runs $batches $metrics $connectivityType $walkingType"
}

function sampling {
	for algo in ${algorithms[@]}; do
		analysis $1 $2 $algo __ $3 $4 $5 $6
	done
}

function plotting {
	# java -jar plot.jar ALL_IN_DIR $dataDir/$pre/$1__$2__$5__$6/ $combinedPlotDir/$pre/$1__$2__$5__$6/
	./jobs.sh create "java -jar plot.jar ALL_IN_DIR $dataDir/$pre/$1__$2__$5__$6/ $combinedPlotDir/$pre/$1__$2__$5__$6/"
}

algorithms=(BFS DFS RANDOM_WALK RANDOM_WALK_NR UNIFORM MOD GREEDY_ORACLE)


##########################################################################################
### PROPERTIES
    metrics="DD__APSP__CC__ASS__M"
##########################################################################################

# pre="properties/k-hop-datasets"
# plotting READ_EDGE_LIST datasets--k-hop--__airport.uelw__UNDIRECTED 16 1574
# plotting READ_EDGE_LIST datasets--k-hop--__blog.uel__UNDIRECTED 40 3982
# plotting READ_EDGE_LIST datasets--k-hop--__euroroad.uel__UNDIRECTED 12 1174
# plotting READ_EDGE_LIST datasets--k-hop--__urv.del__DIRECTED 12 1133

# pre="properties/SNAP-datasets"
# plotting READ_EDGE_LIST datasets--SNAP--__cit-HepTh.del__DIRECTED 483 48239
# plotting READ_EDGE_LIST datasets--SNAP--__cit-HepPh.del__DIRECTED 604 60388
# plotting READ_EDGE_LIST datasets--SNAP--__email-Enron.del__DIRECTED 734 73384
# plotting READ_EDGE_LIST datasets--SNAP--__p2p-Gnutella31.del__DIRECTED 787 78670
# plotting READ_EDGE_LIST datasets--SNAP--__flickrEdges.uel__UNDIRECTED 1060 105938
# 
# sampling READ_EDGE_LIST datasets--SNAP--__soc-Slashdot0902.del__DIRECTED 1607 160609
# sampling READ_EDGE_LIST datasets--SNAP--__email-EuAll.del__DIRECTED 3001 300069
# sampling READ_EDGE_LIST datasets--SNAP--__com-amazon.ungraph.uel__UNDIRECTED 3349 334863
# sampling READ_EDGE_LIST datasets--SNAP--__com-youtube.ungraph.uel__UNDIRECTED 11349 1134890
# sampling READ_EDGE_LIST datasets--SNAP--__wiki-Talk.del__DIRECTED 25168 2516783



##########################################################################################
### WALKING TYPE
    metrics="DD__MOD__EXT"
##########################################################################################

# pre="walking-type/Random-1k"
# plotting RANDOM 0__DIRECTED__1000__8000 10 1000 StronglyConnected AllEdges
# plotting RANDOM 0__DIRECTED__1000__8000 10 1000 StronglyConnected InEdges
# plotting RANDOM 0__DIRECTED__1000__8000 10 1000 StronglyConnected OutEdges

pre="walking-type/Random-10k"
plotting RANDOM 0__DIRECTED__10000__80000 100 10000 StronglyConnected AllEdges
plotting RANDOM 0__DIRECTED__10000__80000 100 10000 StronglyConnected InEdges
plotting RANDOM 0__DIRECTED__10000__80000 100 10000 StronglyConnected OutEdges

pre="walking-type/k-hop-datasets-urv"
plotting READ_EDGE_LIST datasets--k-hop--__urv.del__DIRECTED 12 1133 StronglyConnected AllEdges
plotting READ_EDGE_LIST datasets--k-hop--__urv.del__DIRECTED 12 1133 StronglyConnected InEdges
plotting READ_EDGE_LIST datasets--k-hop--__urv.del__DIRECTED 12 1133 StronglyConnected OutEdges

pre="walking-type/SNAP-datasets-HepTh"
plotting READ_EDGE_LIST datasets--SNAP--__cit-HepTh.del__DIRECTED 483 48239 StronglyConnected AllEdges
plotting READ_EDGE_LIST datasets--SNAP--__cit-HepTh.del__DIRECTED 483 48239 StronglyConnected InEdges
plotting READ_EDGE_LIST datasets--SNAP--__cit-HepTh.del__DIRECTED 483 48239 StronglyConnected OutEdges

pre="walking-type/SNAP-datasets-HepPh"
plotting READ_EDGE_LIST datasets--SNAP--__cit-HepPh.del__DIRECTED 604 60388 StronglyConnected AllEdges
plotting READ_EDGE_LIST datasets--SNAP--__cit-HepPh.del__DIRECTED 604 60388 StronglyConnected InEdges
plotting READ_EDGE_LIST datasets--SNAP--__cit-HepPh.del__DIRECTED 604 60388 StronglyConnected OutEdges

pre="walking-type/SNAP-datasets-Enron"
plotting READ_EDGE_LIST datasets--SNAP--__email-Enron.del__DIRECTED 734 73384 StronglyConnected AllEdges
plotting READ_EDGE_LIST datasets--SNAP--__email-Enron.del__DIRECTED 734 73384 StronglyConnected InEdges
plotting READ_EDGE_LIST datasets--SNAP--__email-Enron.del__DIRECTED 734 73384 StronglyConnected OutEdges



##########################################################################################
### MCNC
    metrics="DD__MOD__EXT"
##########################################################################################

# pre="mcnc/k-hop-datasets"
# plotting READ_EDGE_LIST datasets--k-hop--__airport.uelw__UNDIRECTED 16 1574
# plotting READ_EDGE_LIST datasets--k-hop--__blog.uel__UNDIRECTED 40 3982
# plotting READ_EDGE_LIST datasets--k-hop--__euroroad.uel__UNDIRECTED 12 1174
# plotting READ_EDGE_LIST datasets--k-hop--__urv.del__DIRECTED 12 1133

# pre="mcnc/Random-1k"
# plotting RANDOM 0__UNDIRECTED__1000__1000 10 1000
# plotting RANDOM 0__UNDIRECTED__1000__2000 10 1000
# plotting RANDOM 0__UNDIRECTED__1000__3000 10 1000
# plotting RANDOM 0__UNDIRECTED__1000__4000 10 1000
# plotting RANDOM 0__UNDIRECTED__1000__5000 10 1000
# plotting RANDOM 0__UNDIRECTED__1000__6000 10 1000
# plotting RANDOM 0__UNDIRECTED__1000__7000 10 1000
# plotting RANDOM 0__UNDIRECTED__1000__8000 10 1000

# pre="mcnc/BarabasiAlbert-1k"
# plotting BA 0__UNDIRECTED__10__40__990__1 10 1000
# plotting BA 0__UNDIRECTED__10__40__990__2 10 1000
# plotting BA 0__UNDIRECTED__10__40__990__3 10 1000
# plotting BA 0__UNDIRECTED__10__40__990__4 10 1000
# plotting BA 0__UNDIRECTED__10__40__990__5 10 1000
# plotting BA 0__UNDIRECTED__10__40__990__6 10 1000
# plotting BA 0__UNDIRECTED__10__40__990__7 10 1000
# plotting BA 0__UNDIRECTED__10__40__990__8 10 1000

# pre="mcnc/Random-10k"
# plotting RANDOM 0__UNDIRECTED__10000__10000 100 10000
# plotting RANDOM 0__UNDIRECTED__10000__20000 100 10000
# plotting RANDOM 0__UNDIRECTED__10000__30000 100 10000
# plotting RANDOM 0__UNDIRECTED__10000__40000 100 10000
# plotting RANDOM 0__UNDIRECTED__10000__50000 100 10000
# plotting RANDOM 0__UNDIRECTED__10000__60000 100 10000
# plotting RANDOM 0__UNDIRECTED__10000__70000 100 10000
# plotting RANDOM 0__UNDIRECTED__10000__80000 100 10000

# pre="mcnc/BarabasiAlbert-10k"
# plotting BA 0__UNDIRECTED__10__40__9990__1 100 10000
# plotting BA 0__UNDIRECTED__10__40__9990__2 100 10000
# plotting BA 0__UNDIRECTED__10__40__9990__3 100 10000
# plotting BA 0__UNDIRECTED__10__40__9990__4 100 10000
# plotting BA 0__UNDIRECTED__10__40__9990__5 100 10000
# plotting BA 0__UNDIRECTED__10__40__9990__6 100 10000
# plotting BA 0__UNDIRECTED__10__40__9990__7 100 10000
# plotting BA 0__UNDIRECTED__10__40__9990__8 100 10000

# pre="mcnc/Random-100k"
# plotting RANDOM 0__UNDIRECTED__100000__100000 1000 100000
# plotting RANDOM 0__UNDIRECTED__100000__200000 1000 100000
# plotting RANDOM 0__UNDIRECTED__100000__300000 1000 100000
# plotting RANDOM 0__UNDIRECTED__100000__400000 1000 100000
# plotting RANDOM 0__UNDIRECTED__100000__500000 1000 100000
# plotting RANDOM 0__UNDIRECTED__100000__600000 1000 100000
# plotting RANDOM 0__UNDIRECTED__100000__700000 1000 100000
# plotting RANDOM 0__UNDIRECTED__100000__800000 1000 100000

# pre="mcnc/BarabasiAlbert-100k"
# plotting BA 0__UNDIRECTED__10__40__99990__1 1000 100000
# plotting BA 0__UNDIRECTED__10__40__99990__2 1000 100000
# plotting BA 0__UNDIRECTED__10__40__99990__3 1000 100000
# plotting BA 0__UNDIRECTED__10__40__99990__4 1000 100000
# plotting BA 0__UNDIRECTED__10__40__99990__5 1000 100000
# plotting BA 0__UNDIRECTED__10__40__99990__6 1000 100000
# plotting BA 0__UNDIRECTED__10__40__99990__7 1000 100000
# plotting BA 0__UNDIRECTED__10__40__99990__8 1000 100000
