package:
	mvn clean package

traditional-efficiency-test:
	java -Xmx4000m -jar ./traditional/target/traditional-1.0-SNAPSHOT.jar --efficiency.test=true

reactive-efficiency-test:
	java -Xmx200m -jar ./reactive/target/reactive-1.0-SNAPSHOT.jar  --efficiency.test=true

traditional-throughput-test:
	java -Xmx1000m -jar ./traditional/target/traditional-1.0-SNAPSHOT.jar  --throughput.test=true --useVirtualThreadExecutor=false

reactive-throughput-test:
	java -Xmx1000m -jar ./reactive/target/reactive-1.0-SNAPSHOT.jar  --throughput.test=true