name := "tweet-reflector"

version := "0.1"

scalaVersion := "2.9.2"

scalacOptions ++= Seq("-encoding", "UTF-8", "-unchecked", "-deprecation")

javacOptions ++= Seq("-encoding", "UTF-8")

resolvers ++= Seq(
  "Atilika Open Source repository" at "http://www.atilika.org/nexus/content/repositories/atilika",	// Kuromoji
  "Cloudera" at "https://repository.cloudera.com/artifactory/cloudera-repos/",  	// CDH3u5
  "twitter4j.org Repository" at "http://twitter4j.org/maven2"	// Twitter4j
)

libraryDependencies ++= Seq(
  "org.apache.hadoop"	% "hadoop-client"	% "0.20.2-cdh3u5",	// CDH3u5
  "org.apache.hadoop"	% "hadoop-core"		% "0.20.2-cdh3u5",	// CDH3u5
  "org.apache.hadoop"	% "hadoop-tools"	% "0.20.2-cdh3u5",	// CDH3u5
  "org.apache.zookeeper" % "zookeeper"	% "3.3.5-cdh3u5",		// CDH3u5
  "com.google.guava"	% "guava" % "r06",				// CDH3u5
  "log4j"               % "log4j" % "1.2.17",				// CDH3u5
  "org.atilika.kuromoji" % "kuromoji" % "0.7.7",			// Kuromoji
  "net.sf.json-lib"     % "json-lib" % "2.4" classifier "jdk15",	// json-lib
  "org.twitter4j"	% "twitter4j-core" % "3.0.3",	// 
  "org.twitter4j"	% "twitter4j-stream" % "3.0.3",	//
  "com.basho.riak"      % "riak-client" % "1.1.1"	// Riak 
)

// retrieveManaged := true


