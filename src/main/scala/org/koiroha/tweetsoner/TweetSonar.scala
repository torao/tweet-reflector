/*
*/
import twitter4j._
import twitter4j.conf._

object TwitterSonar extends StatusAdapter {

	def main(args:Array[String]){
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
		val text = status.getText
		if(isJapanese(text)){
			save(status)
		}
	}

	private[this] val df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
	private[this] def save(status:Status):Unit = try {
		val user = status.getUser
		System.out.print(user.getId + ",")
		System.out.print(csv(user.getScreenName) + ",")
		System.out.print(csv(user.getName) + ",")
		System.out.print(csv(user.getLocation) + ",")
		System.out.print(csv(user.getTimeZone) + ",")
		System.out.print(csv(user.getLang) + ",")
		System.out.print(status.getId + ",")
		System.out.print(df.format(status.getCreatedAt) + ",")
		System.out.print(csv(status.getGeoLocation) + ",")
		System.out.print(csv(status.getPlace) + ",")
		System.out.print(csv(status.getInReplyToStatusId) + ",")
		System.out.print(csv(status.getInReplyToUserId) + ",")
		System.out.print(csv(status.getInReplyToScreenName) + ",")
		System.out.print(csv(status.isRetweet) + ",")
		System.out.print(csv(status.getText))
		System.out.println()
		System.out.flush()
	} catch {
		case ex => ex.printStackTrace()
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

