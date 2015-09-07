#!/bin/bash

rsync -auvzl --prune-empty-dirs --include '*/' --include '*.png' --exclude '*' p2pram:sampling/plots_combined/mcnc/ ../results-mcnc/

rsync -auvzl --prune-empty-dirs --include '*/' --include '*.png' --exclude '*' p2pram:sampling/plots_combined/properties/ ../results-properties/

rsync -auvzl --prune-empty-dirs --include '*/' --include '*.png' --exclude '*' p2pram:sampling/plots_combined/walking-type/ ../results-walking-type/