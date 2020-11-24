export HADOOP_CLASSPATH=/mnt/d/uni_study/parallel\ program/raspred/laba3/target/spark-examples-1.0-SNAPSHOT.jar
rm -r output
hadoop fs -rm -r output
spark-submit --class "amaguma.Application" --master yarn-client --num-executors 3 /mnt/d/uni_study/parallel\ program/raspred/laba3/target/spark-examples-1.0-SNAPSHOT.jar airport.csv flight.csv output
hadoop fs -copyToLocal output
