/*
*/
import java.io._
import twitter4j._
import twitter4j.conf._
import twitter4j.json._
import com.basho.riak.client._

/**
 * 
*/
object TwitterSonar extends RawStreamListener {

	var format:(Status,String)=>String = jsonFormat
	var output:(Status,String)=>Unit = consoleOutput
	var filter:(Status)=>Boolean = { _ => false }

	def main(args:Array[String]) = {
	
		lazy val parse:(List[String])=>Unit = _ match {
			case "--japanese-only" :: rest =>
				filter = { status => ! isJapanese(status.getText) }
				parse(rest)
			case "--output" :: o :: rest => o match {
					case "console" => output = consoleOutput
					case "file" => output = fileOutput
					case "riak" => output = riakOutput
					case unknown =>
						System.err.println("Unknown output: %s".format(unknown))
						System.exit(1)
				}
				parse(rest)
			case "--format" :: t :: rest => t match {
					case "json" => format = jsonFormat
					case "csv" => format = csvFormat
					case unknown =>
						System.err.println("Unknown output format: %s".format(unknown))
						System.exit(1)
				}
				parse(rest)
			case unknown :: rest =>
				System.err.println("Unknown parameter: %s".format(unknown))
				System.exit(1)
			case List() => None
		}
		parse(args.toList)

		if(! new File("twitter4j.properties").exists()){
			System.err.println("INFO: twitter4j.properties not exists")
		}
		val conf = new ConfigurationBuilder()
			.setDebugEnabled(true)
			.setOAuthConsumerKey("MZgfBkxZwjYapeyzWVwkdw")
			.setOAuthConsumerSecret("DzX1BZfd0tMkYV2lMJfoefWUkPLLb2uKxBISHROuA")
			.setOAuthAccessToken("84123347-yauyXtx5AQ263YYlVsnYNQOq91cqmZG0ME5jT0MFc")
			.setOAuthAccessTokenSecret("PhiJzGqeXqDOjoRtge7Oh1lO6Ej8LkzWXkgrzfhUzg")
			.build()
		val stream = new TwitterStreamFactory(conf).getInstance()
		stream.addListener(this)
		stream.sample()
	}

	def onMessage(rawString:String):Unit = {
		val status = DataObjectFactory.createStatus(rawString)
		if(! filter(status)){
			output(status, format(status, rawString))
		}
	}

	def onException(ex:Exception):Unit = {
		ex.printStackTrace()
	}

	private[this] def fileOutput(status:Status, text:String):Unit = open(status) { out =>
		out.println(text)
		out.flush()
	}

	private[this] def consoleOutput(status:Status, text:String):Unit = {
		System.out.println(text)
	}

	private[this] lazy val client = RiakFactory.httpClient()
	private[this] val rdf = new java.text.SimpleDateFormat("yyyyMMdd")
	private[this] val rtf = new java.text.SimpleDateFormat("HHmm")
	private[this] def riakOutput(status:Status, text:String):Unit = {
		val bucket = client.fetchBucket("TwitterSampleStream").execute()
		bucket.store(String.valueOf(status.getId()), text).execute()
			.addIndex("date", rdf.format(status.getCreatedAt()))
			.addIndex("time", rdf.format(status.getCreatedAt()))
	}

	private[this] val df = new java.text.SimpleDateFormat("yyyy-MM-dd")
	private[this] val tf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
	private[this] def csvFormat(status:Status, rawString:String):String = {
		val sw = new StringWriter()
		var out = new PrintWriter(sw);
		val user = status.getUser
		out.print(user.getId + ",")
		out.print(csv(user.getScreenName) + ",")
		out.print(csv(user.getName) + ",")
		out.print(csv(user.getLocation) + ",")
		out.print(csv(user.getTimeZone) + ",")
		out.print(csv(user.getLang) + ",")
		out.print(status.getId + ",")
		out.print(tf.format(status.getCreatedAt) + ",")
		out.print(csv(status.getGeoLocation) + ",")
		out.print(csv(status.getPlace) + ",")
		out.print(csv(status.getInReplyToStatusId) + ",")
		out.print(csv(status.getInReplyToUserId) + ",")
		out.print(csv(status.getInReplyToScreenName) + ",")
		out.print(csv(status.isRetweet) + ",")
		out.print(csv(status.getText))
		out.flush()
		return sw.toString()
	}

	private[this] def jsonFormat(status:Status, rawString:String):String = rawString

	private[this] def open(status:Status)(f:(PrintWriter)=>Unit) = {
		val date = status.getCreatedAt()
		val fileName = df.format(date) + ".csv"
		val out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), "UTF-8"))
		try {
			f(out)
		} catch {
			case ex => ex.printStackTrace()
		} finally {
			out.close()
		}
	}

	private[this] def csv(text:String):String = {
		if(text == null){
			""
		} else {
			val buffer = new StringBuilder()
			buffer.append('\"')
			text.foreach{
				case '\"' => buffer.append("\"\"")
				case ch => buffer.append(ch)
			}
			buffer.append('\"').toString()
		}
	}

	private[this] def csv(b:Boolean):String = if(b) "1" else "0"
	private[this] def csv(l:Long):String = l.toString
	private[this] def csv(g:GeoLocation):String = if(g != null){
		csv(g.getLatitude + "/" + g.getLongitude)
	} else {
		""
	}
	private[this] def csv(p:Place):String = if(p != null){
		csv(p.getFullName)
	} else ""

	private[this] def isJapanese(text:String):Boolean = {
		text.foreach{ ch =>
			// ひらがなまたはカタカナが含まれている場合
			if((ch >= 0x3040 && ch <= 0x309F) || (ch >= 0x30A0 && ch <= 0x30FF)){
				return true
			}
		}
		false	
	}

}

