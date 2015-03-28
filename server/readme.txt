需要导入qq互联的jar包到maven本地库
导入命令：C:\Users\超>mvn install:install-file -DgroupId=com.qq.connect -DartifactId=qqcon
nect -Dversion=2.0 -Dpackaging=jar -Dfile=G:\github\ExtremeWorld\soft\Sdk4J.jar