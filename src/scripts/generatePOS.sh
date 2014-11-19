#!/bin/bash
export TWITTER_NLP=/home/btech/cs1110081/Desktop/NLP_PROJECT/DBUGR/libs/twitter_nlp
echo Doing 'OvuView: Ovulation & Fertility'
'/home/btech/cs1110081/Desktop/NLP_PROJECT/DBUGR/libs/runTagger.sh' --output-format conll '/home/btech/cs1110081/Desktop/NLP_PROJECT/DBUGR/data/POS/OvuView: Ovulation & Fertility-prepos.txt' >'/home/btech/cs1110081/Desktop/NLP_PROJECT/DBUGR/data/POS/OvuView: Ovulation & Fertility-ark.txt'
python '/home/btech/cs1110081/Desktop/NLP_PROJECT/DBUGR/libs/getPOSFile.py' '/home/btech/cs1110081/Desktop/NLP_PROJECT/DBUGR/data/POS/OvuView: Ovulation & Fertility-ark.txt' '/home/btech/cs1110081/Desktop/NLP_PROJECT/DBUGR/data/POS/OvuView: Ovulation & Fertility-pos.txt'
