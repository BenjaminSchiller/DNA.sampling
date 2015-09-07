#!/bin/bash

# java -Djava.awt.headless=true -jar vis.jar /test/ RANDOM 0__UNDIRECTED__1000__4000 BFS __ RANDOM Visiting 10 1000 10



plotDir="../results-vis"

start="RANDOM"
stop="Visiting"
delay="10"

function visualize {
	gt=$1
	gp=$2
	st=$3
	sp=$4
	cost=$5
	resource=$6
	java -jar vis.jar $plotDir/$pre/ $gt $gp $st $sp $start $stop $cost $resource $delay
	echo ""
	echo ""
	echo ""
}

function vis {
	for algo in ${algorithms[@]}; do
		visualize $1 $2 $algo __ $3 $4
	done
}

algorithms=(BFS DFS RANDOM_WALK RANDOM_WALK_NR UNIFORM GREEDY_ORACLE MOD)

pre="Random-1k"
vis RANDOM 0__UNDIRECTED__1000__1000 1 100
vis RANDOM 0__UNDIRECTED__1000__2000 1 100
vis RANDOM 0__UNDIRECTED__1000__3000 1 100
vis RANDOM 0__UNDIRECTED__1000__4000 1 100

pre="Random-10k"
vis RANDOM 0__UNDIRECTED__10000__10000 5 500
vis RANDOM 0__UNDIRECTED__10000__20000 5 500
vis RANDOM 0__UNDIRECTED__10000__30000 5 500
vis RANDOM 0__UNDIRECTED__10000__40000 5 500

pre="BarabasiAlbert-1k"
vis BA 0__UNDIRECTED__10__40__990__1 1 100
vis BA 0__UNDIRECTED__10__40__990__2 1 100
vis BA 0__UNDIRECTED__10__40__990__3 1 100
vis BA 0__UNDIRECTED__10__40__990__4 1 100

pre="BarabasiAlbert-10k"
vis BA 0__UNDIRECTED__10__40__9990__1 5 500
vis BA 0__UNDIRECTED__10__40__9990__2 5 500
vis BA 0__UNDIRECTED__10__40__9990__3 5 500
vis BA 0__UNDIRECTED__10__40__9990__4 5 500

# pre="Grid2d--10k"
# vis GRID2d UNDIRECTED__100__100__OPEN 1 100
# vis GRID2d UNDIRECTED__100__100__OPEN 5 500

# pre="Grid3d--10k"
# vis GRID3d UNDIRECTED__22__22__22__OPEN 1 100
# vis GRID3d UNDIRECTED__45__45__5__OPEN 1 100
# vis GRID3d UNDIRECTED__50__50__4__OPEN 1 100
# vis GRID3d UNDIRECTED__60__60__3__OPEN 1 100
# vis GRID3d UNDIRECTED__22__22__22__OPEN 5 500
# vis GRID3d UNDIRECTED__45__45__5__OPEN 5 500
# vis GRID3d UNDIRECTED__50__50__4__OPEN 5 500
# vis GRID3d UNDIRECTED__60__60__3__OPEN 5 500


