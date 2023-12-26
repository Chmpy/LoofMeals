package com.example.loofmeals.ui.screens

import android.content.Context
import android.graphics.Rect
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.loofmeals.R
import com.example.loofmeals.ui.map.MapViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Map(
    navController: NavController,
    mapViewModel: MapViewModel = viewModel(factory = MapViewModel.Factory)
) {
    val context = LocalContext.current
    val mapState by mapViewModel.uiState.collectAsState()
    val mapView = remember { MapView(context) }
    val controller = remember { mapView.controller }
    val myLocationOverlay =
        remember { MyLocationNewOverlay(GpsMyLocationProvider(context), mapView) }
    var lastLocation by rememberSaveable { mutableStateOf<GeoPoint?>(null) }

    val fineLocationPermissionState =
        rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)
    LaunchedEffect(fineLocationPermissionState.hasPermission) {
        if (!fineLocationPermissionState.hasPermission)
            fineLocationPermissionState.launchPermissionRequest()
    }

    val scope = rememberCoroutineScope()

    fun goToDetail(restaurantId: Int) {
        navController.navigate("${Screens.Detail.name}/$restaurantId")
    }

    PermissionRequired(permissionState = fineLocationPermissionState,
        permissionNotGrantedContent = {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                // Content to show when the permission is not granted
                Text(stringResource(R.string.map_no_permission))
            }
        },
        permissionNotAvailableContent = {
            // Content to show when the permission is not available
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text(stringResource(R.string.map_no_permission))
            }
        }) {

        AndroidView({ mapView }) { mapView ->
            Configuration.getInstance().load(
                context, context.getSharedPreferences(
                    context.getString(R.string.app_name), Context.MODE_PRIVATE
                )
            )

            mapView.setTileSource(TileSourceFactory.MAPNIK)
            mapView.setMultiTouchControls(true)
            mapView.getLocalVisibleRect(Rect())

            mapState.markers.forEach { marker ->
                val osmMarker = Marker(mapView).apply {
                    position = GeoPoint(marker.lat, marker.long)
                    title = marker.title
                    setOnMarkerClickListener { _, _ ->
                        goToDetail(marker.id)
                        true
                    }
                }
                mapView.overlays.add(osmMarker)
            }

            myLocationOverlay.enableMyLocation()
            myLocationOverlay.enableFollowLocation()
            myLocationOverlay.isDrawAccuracyEnabled = true

            if (lastLocation != null) {
                controller.setCenter(lastLocation)
                controller.animateTo(lastLocation)
                controller.setZoom(15.0)
            } else {
                myLocationOverlay.runOnFirstFix {
                    scope.launch {
                        withContext(Dispatchers.Main) {
                            controller.setCenter(myLocationOverlay.myLocation)
                            controller.animateTo(myLocationOverlay.myLocation)
                            controller.setZoom(15.0)
                        }
                    }
                }
            }
            mapView.overlays.add(myLocationOverlay)
        }
    }
    DisposableEffect(mapView) {
        onDispose {
            lastLocation = mapView.mapCenter as GeoPoint?
        }
    }
}