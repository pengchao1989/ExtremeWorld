需要导入qq互联的jar包到maven本地库
导入命令：C:\Users\超>mvn install:install-file -DgroupId=com.qq.connect -DartifactId=qqcon
nect -Dversion=2.0 -Dpackaging=jar -Dfile=G:\github\ExtremeWorld\soft\Sdk4J.jar

mvn install:install-file -DgroupId=com.qq.connect -DartifactId=qqconnect -Dversion=2.0 -Dpackaging=jar -Dfile=C:\Users\pengchao\Documents\GitHub\ExtremeWorld\soft\Sdk4J.jar

 mvn install:install-file -DgroupId=cn.jpush.api -DartifactId=jpush-client -Dversion=3.2.5 -Dpackaging=jar -Dfile=/home/pengchao/github/ExtremeWorld/soft/jpush-client-3.2.5.jar

mvn install:install-file -DgroupId=com.taobao -DartifactId=taobao-sdk -Dversion=1.0 -Dpackaging=jar -Dfile=/home/pengchao/github/ExtremeWorld/soft/taobao-sdk-java-auto_1453110734201-20160118.jar
