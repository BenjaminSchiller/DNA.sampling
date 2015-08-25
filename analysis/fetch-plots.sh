#!/bin/bash

rsync -auvzl --prune-empty-dirs --include '*/' --include '*.png' --exclude '*' p2pram:sampling/plots_combined/ ../results/