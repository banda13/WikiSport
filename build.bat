call mvn install:install-file -DgroupId=com.bordercloud.sparql -Dversion=current -Dpackaging=jar -DartifactId=sparql -Dfile=./lib/SPARQL-JAVA.jar
call mvn clean package
call mvn spring-boot:run