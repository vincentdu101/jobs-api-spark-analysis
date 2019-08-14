import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.io.Reader
import java.net.URL
import java.util.Date

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

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

    val response = getRequestContent(url)
    val objectMapper = new ObjectMapper() with ScalaObjectMapper
    objectMapper.registerModule(DefaultScalaModule)
    val convertedResponse = objectMapper.readValue[List[Post]](response)
    convertedResponse.map(p => p.id).foreach(p => println(p))
}