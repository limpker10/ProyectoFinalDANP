package com.unsa.edu.proyectofinaldanp.ui.pagination.models
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.ktor.client.*
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApiService {
    private val client = HttpClient()
    companion object {
        suspend fun callLambdaFunction(page: Int, size: Int): List<DataItem> {
            val client = HttpClient() {
                install(JsonFeature) {
                    serializer = GsonSerializer()
                }
            }

            return withContext(Dispatchers.IO) {
                try {
                    val response: String = client.get("https://qyjdshvlt63jhhqvfs3kre7npa0axjfq.lambda-url.us-east-1.on.aws") {
                        url {
                            parameters.append("page", page.toString())
                            parameters.append("size", size.toString())
                        }
                    }

                    val gson = Gson()
                    val dataItems: List<DataItem> = gson.fromJson(response, object : TypeToken<List<DataItem>>() {}.type)
                    dataItems
                } catch (e: Exception) {
                    e.printStackTrace()
                    emptyList() // En caso de error, se devuelve una lista vacía
                }
            }
        }

        suspend fun postLambdaFunction(temperature: Double, UnitTemperature: String, comentario:String): String {
            val client = HttpClient() {
                install(JsonFeature) {
                    serializer = GsonSerializer()
                }
            }

            return withContext(Dispatchers.IO) {
                try {
                    val response: String = client.get("https://qyjdshvlt63jhhqvfs3kre7npa0axjfq.lambda-url.us-east-1.on.aws/add") {
                        url {
                            parameters.append("temperature", temperature.toString())
                            parameters.append("UnitTemperature", UnitTemperature.toString())
//                            parameters.append("temperature", temperature.toString())
//                            parameters.append("unit", unit.toString())
                        }
                    }
                    response
                } catch (e: Exception) {
                    e.printStackTrace()
                    "Error" // In case of error, return an empty string
                }
            }
        }

    }
}