package com.example.projetdevmobile

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.mapbox.geojson.FeatureCollection
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.fillLayer
import com.mapbox.maps.extension.style.layers.properties.generated.Visibility
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import java.io.InputStream

object GeoJsonHelper {

    private const val TAG = "GeoJsonHelper"

    @JvmStatic
    fun addGeoJsonLayer(style: Style, context: Context, geoJsonAssetName: String) {
        val geoJsonString = loadGeoJsonFromAsset(context, geoJsonAssetName)

        if (geoJsonString != null) {
            Log.d(TAG, "GeoJSON loaded from asset: $geoJsonAssetName")
            val featureCollection = FeatureCollection.fromJson(geoJsonString)

            val featureCount = featureCollection.features()?.size ?: 0
            Log.d(TAG, "Feature count: $featureCount")
            Toast.makeText(context, "Loaded $featureCount features from GeoJSON", Toast.LENGTH_SHORT).show()

            val sourceId = "geojson-source"
            val layerId = "geojson-layer"

            style.addSource(
                geoJsonSource(sourceId) {
                    featureCollection(featureCollection)
                }
            )

            Log.d(TAG, "Source added")

            style.addLayer(
                fillLayer(layerId, sourceId) {
                    fillColor("#ff0000")
                    fillOpacity(0.6)
                    visibility(Visibility.VISIBLE)
                }
            )

            Log.d(TAG, "Layer added to style")
        } else {
            Log.e(TAG, "Failed to load GeoJSON from asset: $geoJsonAssetName")
            Toast.makeText(context, "Failed to load GeoJSON: $geoJsonAssetName", Toast.LENGTH_LONG).show()
        }
    }

    private fun loadGeoJsonFromAsset(context: Context, fileName: String): String? {
        return try {
            val inputStream: InputStream = context.assets.open(fileName)
            inputStream.bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            Log.e(TAG, "Error reading GeoJSON file: $fileName", e)
            null
        }
    }


}
