name := "tweetsonar"

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
  "org.twitter4j"	% "twitter4j-core" % "2.2.5",	// 
  "org.twitter4j"	% "twitter4j-stream" % "2.2.5",	// 
  "com.basho.riak" % "riak-client" % "1.1.0"	// Riak
)

// retrieveManaged := true


