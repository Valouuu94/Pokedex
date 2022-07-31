import sttp.client3._
import io.circe._, io.circe.parser._
import io.circe.Decoder, io.circe.generic.auto._


object Appli {
    case class Pokemon(
        id: Int,
        name: String,
        type1: String,
        type2: String,
        total: Int,
        hp: Int,
        attack: Int,
        defense: Int,
        spAttack: Int,
        spDefense: Int,
        speed: Int,
        generation: String,
        legendary: Boolean
    )
    val PokeEndPoint = "https://pokeapi.co/api/v2/"

    case class HttpBinResponse(origin: String, headers: Map[String, String])

    def isLegendary(id: Int)  : Boolean = {
        val request = basicRequest.get(uri"https://pokeapi.co/api/v2/pokemon-species/$id")
        implicit val backend = HttpURLConnectionBackend()
        val response = request.send()
        response.body match {
            case Right(data) =>
                val json: Json = parse(data).getOrElse(Json.Null)
                val cursor: HCursor = json.hcursor

                val valIsLegendary = cursor.downField("is_legendary").as[Boolean]
                valIsLegendary match { case Right(data) => return Some(data).getOrElse(false)}
        }
    }


    def getPoekmon(name: String) : Pokemon = {
        var id: Option[Int] = None

        var type1: Option[String] = None
        var type2: Option[String] = None

        var generation: Option[String] = None

        var hp: Option[Int] = None


        val request = basicRequest.get(uri"https://pokeapi.co/api/v2/pokemon/$name")
        implicit val backend = HttpURLConnectionBackend()
        
        val response = request.send()
        response.body match {
        case Left(error) => println(s"pas de Poekmon avec le nom: $name")
        case Right(data) =>
            val json: Json = parse(data).getOrElse(Json.Null)
            val cursor: HCursor = json.hcursor

            val valId = cursor.downField("id").as[Int]
            valId match { case Right(data) => id = Some(data)}

            val valtype1 = cursor.downField("types").downArray.downField("type").downField("name").as[String]
            valtype1 match { case Right(data) => type1 = Some(data)}

            val valtype2 = cursor.downField("types").downArray.downField("type").downField("name").as[String]
            valtype2 match { case Right(data) => type2 = Some(data)}

            val valhp = cursor.downField("stats").downArray.downField("base_stat").as[Int]
            valhp match { case Right(data) => hp = Some(data)}

            val valgen = cursor.downField("game_indices").downArray.downField("version").downField("name").as[String]
            valgen match { case Right(data) => generation = Some(data)}


        }
        
        var p = new Pokemon(id.getOrElse(0), name, type1.getOrElse("none"), type2.getOrElse("none"), 0, hp.getOrElse(0), 0, 0, 0, 0, 0, generation.getOrElse("none"), isLegendary(id.getOrElse(0)))

        return p
    }
    def main(args: Array[String]) = {
        println(getPoekmon("dialga"))
        
    }
}
 