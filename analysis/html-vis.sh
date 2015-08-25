#!/bin/bash

outputDir="../vis"

function html_header {
	echo '<?php'
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

function html_list_pres {
	html_header
	echo "<ul>"
	for pre in $(ls $outputDir | grep -v 'index.'); do
		echo "<li><a href='$pre/' style='font-size:12pt; font-weight:bold;'>$pre</a></li>"
		html_list_graphs $pre > $outputDir/$pre/index.php
	done
	echo "</ul>"
	html_footer
}

function html_list_graphs {
	html_header
	echo "<ul>"
	for graph in $(ls $outputDir/$pre | grep -v 'index.'); do
		echo "<li><a href='$graph/' style='font-size:12pt; font-weight:bold;'>$graph</a></li>"
		html_graph $pre $graph > $outputDir/$pre/$graph/index.php
	done
	echo "</ul>"
	html_footer
}

function html_graph {
	html_header

	echo "<h1>$1 - $2</h1>"
	
	algorithms=(BFS DFS RANDOM_WALK RANDOM_WALK_NR UNIFORM GREEDY_ORACLE MOD)
	for algo in ${algorithms[@]}; do
		html_graph_sampling $1 $2 "${algo}"
	done

	html_footer

	for graph in $(ls $outputDir/$1 | grep -v 'index.'); do
		html_single $1 $graph
	done
}

function html_graph_sampling {
	if [[ -e "$outputDir/$1/$2/$3__RANDOM__Visiting/animated.gif" ]]; then
		echo "<h2>$3</h2>"
		echo "<a href='$3__RANDOM__Visiting/animated.gif'><img width='700' src='$3__RANDOM__Visiting/animated.gif' alt='$graph' title='$graph' style='border: 1px dotted;'/></a>"
		echo "<?php spacer(); ?>"
	fi
}

function html_single {
	html_header
	echo "$1 ... $2"
	html_footer
}


html_list_pres > $outputDir/index.php
