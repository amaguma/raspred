export HADOOP_CLASSPATH=/mnt/d/uni_study/parallel\ program/raspred/lab2/target/lab2-1.0-SNAPSHOT.jar
rm -r output
mvn package
hadoop fs -rm -r output
hadoop amaguma.App L_AIRPORT_ID.csv 664600583_T_ONTIME_sample.csv output
hadoop fs -copyToLocal output

