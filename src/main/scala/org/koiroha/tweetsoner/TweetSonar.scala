/*
*/
import java.io._
import twitter4j._
import twitter4j.conf._

/**
 * 
*/
object TwitterSonar extends StatusAdapter {

	var filter:(Status)=>Boolean = { _ => false }

	def main(args:Array[String]) = {
	
		lazy val parse:(List[String])=>Unit = _ match {
			case "--japanese-only" :: rest =>
				filter = { status => ! isJapanese(status.getText) }
				parse(rest)
			case unknown :: rest =>
				System.err.println("Unknown parameter: %s".format(unknown))
				System.exit(1)
			case List() => None
		}
		parse(args.toList)

		val conf = new ConfigurationBuilder().setDebugEnabled(true)
			.setOAuthConsumerKey("MZgfBkxZwjYapeyzWVwkdw")
			.setOAuthConsumerSecret("DzX1BZfd0tMkYV2lMJfoefWUkPLLb2uKxBISHROuA")
			.setOAuthAccessToken("84123347-yauyXtx5AQ263YYlVsnYNQOq91cqmZG0ME5jT0MFc")
			.setOAuthAccessTokenSecret("PhiJzGqeXqDOjoRtge7Oh1lO6Ej8LkzWXkgrzfhUzg").build()
		val stream = new TwitterStreamFactory(conf).getInstance()
		stream.addListener(this)
		stream.sample()
	}

	override def onStatus(status:Status):Unit = {
		if(! filter(status)){
			save(status)
		}
	}

	private[this] val df = new java.text.SimpleDateFormat("yyyy-MM-dd")
	private[this] val tf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
	private[this] def save(status:Status):Unit = open(status.getCreatedAt) { out =>
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
		out.println()
		out.flush()
	}

	private[this] def open(date:java.util.Date)(f:(PrintWriter)=>Unit) = {
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

