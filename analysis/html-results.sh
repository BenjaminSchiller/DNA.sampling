#!/bin/bash

outputDir="../results"

function html_header {
	echo '<?php'
		# echo '$path = str_replace("/Users/benni/TUD/Projects/DNA/DNA.webpage/web/", "", getcwd());'
		echo '$path = ltrim(str_replace($_SERVER["DOCUMENT_ROOT"], "", getcwd()), "/");'
		echo '$dirs = split("/", $path);'
		echo '$pre = "";'
		echo 'for($i = 0; $i < count($dirs); $i++) { $pre .= "../"; }'
		echo 'require($pre."layout/header.php");'
	echo '?>'
}

function html_footer {
	echo '<?php require($pre."layout/footer.php"); ?>';
}

function html_list {
	html_header
	echo "<ul>"
	for pre in $(ls $outputDir | grep -v 'index.'); do
		echo "<li><a href='$pre/' style='font-size:12pt;'>$pre</a></li>"
		html_summary $pre > $outputDir/$pre/index.php
	done
	echo "</ul>"
	html_footer
}

function html_summary {
	html_header
	html_summary_image $1 ExtentR.Seen_and_Visited_Nodes
	html_summary_image $1 ExtentR.Seen_Nodes
	html_summary_image $1 ExtentR.Unseen_Nodes
	html_summary_image $1 ExtentR.Visited_Nodes
	html_summary_image $1 SamplingModularityB.Sampling_Modularity_V1
	html_summary_image $1 SamplingModularityB.Sampling_Modularity_V2
	html_summary_image $1 z.statistics.edges
	html_summary_image $1 z.statistics.nodes
	html_summary_image $1 UndirectedClusteringCoefficientU.averageCC
	html_summary_image $1 UndirectedClusteringCoefficientU.globalCC
	html_summary_image $1 UnweightedAllPairsShortestPathsR.characteristicPathLength
	html_summary_image $1 UnweightedAllPairsShortestPathsR.connectivity
	html_summary_image $1 UnweightedAllPairsShortestPathsR.diameter
	html_summary_image $1 UnweightedAllPairsShortestPathsR.possiblePaths
	html_summary_image $1 AssortativityR-out-unweighted.AssortativityCoefficient
	html_summary_image $1 DegreeDistributionR.degreeMax
	html_summary_image $1 DegreeDistributionR.degreeMin

	html_footer

	for graph in $(ls $outputDir/$1 | grep -v 'index.'); do
		html_single $1 $graph
	done
}

function html_summary_image {
	echo "<h2>$2</h2>"
	echo "<div id='' style='overflow-y:scroll; height:auto; border:1px dotted;'>"
	echo "<div id='' style='width:10000px;'>"
	for graph in $(ls $outputDir/$1 | grep -v 'index.'); do
		echo "<a href='$graph/$2.png'><img width='350' src='$graph/$2.png' alt='$graph' title='$graph'/></a>"
	done
	echo "</div>"
	echo "</div>"
	echo "<?php spacer(); ?>"
}

function html_single {
	html_header
	echo "$1 ... $2"
	html_footer
}


html_list > $outputDir/index.php
