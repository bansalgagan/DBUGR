#!/bin/bash
echo Doing 'ASTRO File Manager with Cloud'
'/home/btech/cs1110081/Desktop/NLP_PROJECT/DBUGR/libs/runTagger.sh' --output-format conll '/home/btech/cs1110081/Desktop/NLP_PROJECT/DBUGR/data/POS/ASTRO File Manager with Cloud-prepos.txt' >'/home/btech/cs1110081/Desktop/NLP_PROJECT/DBUGR/data/POS/ASTRO File Manager with Cloud-ark.txt'
python '/home/btech/cs1110081/Desktop/NLP_PROJECT/DBUGR/libs/getPOSFile.py' '/home/btech/cs1110081/Desktop/NLP_PROJECT/DBUGR/data/POS/ASTRO File Manager with Cloud-ark.txt' '/home/btech/cs1110081/Desktop/NLP_PROJECT/DBUGR/data/POS/ASTRO File Manager with Cloud-pos.txt'
