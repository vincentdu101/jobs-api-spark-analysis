import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.io.Reader
import java.net.URL
import java.util.Date

import spray.json._

case class Post(id: String,
                `type`: String,
                `url`: String,
                created_at: String,
                company: String,
                company_url: String,
                location: String,
                title: String,
                description: String,
                how_to_apply: String,
                company_logo: String)

object PostProtocol extends DefaultJsonProtocol {
    implicit object PostJsonFormat extends RootJsonFormat[Post] {
        def write(p: Post) =
            JsArray(JsString(p.id), JsString(p.`type`), JsString(p.`url`), JsString(p.created_at), JsString(p.company),
                JsString(p.company_url), JsString(p.location), JsString(p.title), JsString(p.description),
                JsString(p.how_to_apply), JsString(p.company_logo))

        def read(value: JsValue) = value match {
            case JsArray(Vector(
                JsString(id), JsString("type"), JsString("url"), JsString(created_at), JsString(company), JsString(company_url),
                JsString(location), JsString(title), JsString(description), JsString(how_to_apply), JsString(company_logo)
            )) => new Post(id, "type", "url", created_at, company, company_url, location, title, description,
                how_to_apply, company_logo)
        }
    }
}

object JobsAnalysis extends App {

    val url: String = "https://jobs.github.com/positions.json?description=machine+learning&page=1"

    private def getRequestContent(fullPath: String): String = {
        var output = ""
        var content = ""

        try {
            val url = new URL(fullPath)
            val connection = url.openConnection().asInstanceOf[HttpURLConnection]
            connection.setRequestMethod("GET")
            connection.setRequestProperty("Accept", "application/json");

            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("Failed http error code " +
                    connection.getResponseCode());
            }

            val br = new BufferedReader(new InputStreamReader(
                connection.getInputStream()
            ))

            output = br.readLine()
            while (output != null) {
                content += output
                output = br.readLine()
            }
        } catch {
            case ex: Exception => ex.printStackTrace()
        }
        content
    }

    import PostProtocol._
    val response = getRequestContent(url)
    println(response)
    val convertedResponse = response.parseJson
    val posts = convertedResponse.convertTo[List[Post]]

    posts.foreach(e => println(e))
}