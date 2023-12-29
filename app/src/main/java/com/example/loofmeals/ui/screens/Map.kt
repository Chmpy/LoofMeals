package com.example.loofmeals.ui.screens

import android.content.Context
import android.graphics.Rect
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.loofmeals.R
import com.example.loofmeals.ui.components.BackgroundSurface
import com.example.loofmeals.ui.map.MapApiState.Error
import com.example.loofmeals.ui.map.MapApiState.Loading
import com.example.loofmeals.ui.map.MapApiState.Success
import com.example.loofmeals.ui.map.MapState
import com.example.loofmeals.ui.map.MapViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

/**
 * Composable function for displaying a map with restaurant markers and user location.
 *
 * @param navController The navigation controller for navigating to the restaurant details screen.
 * @param mapViewModel The view model responsible for managing the map state and data.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Map(
    navController: NavController,
    mapViewModel: MapViewModel = viewModel(factory = MapViewModel.Factory)
) {
    val context = LocalContext.current

    val mapState by mapViewModel.uiState.collectAsState()

    val mapApiState = mapViewModel.mapApiState

    val mapView = remember { MapView(context) }

    val controller = remember { mapView.controller }

    val myLocationOverlay =
        remember { MyLocationNewOverlay(GpsMyLocationProvider(context), mapView) }
    var lastLocation by rememberSaveable { mutableStateOf<GeoPoint?>(null) }

    val fineLocationPermissionState =
        rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)
    LaunchedEffect(fineLocationPermissionState.hasPermission) {
        if (!fineLocationPermissionState.hasPermission) fineLocationPermissionState.launchPermissionRequest()
    }

    val scope = rememberCoroutineScope()

    PermissionRequired(
        permissionState = fineLocationPermissionState,
        permissionNotGrantedContent = {
            // Content to show when the permission is not granted
            NoPermContent(
                background = painterResource(id = R.drawable.background5),
                text = stringResource(R.string.map_no_permission)
            )
        },
        permissionNotAvailableContent = {
            // Content to show when the permission is not available
            NoPermContent(
                background = painterResource(id = R.drawable.background5),
                text = stringResource(R.string.map_no_location)
            )
        },
    ) {

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.background5),
                contentDescription = "background5",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.matchParentSize()
            )
            when (mapApiState) {
                is Loading -> {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(dimensionResource(R.dimen.xl))
                                .height(dimensionResource(R.dimen.xs)),
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }

                is Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(id = R.dimen.xl)),
                        contentAlignment = Alignment.Center
                    ) {
                        BackgroundSurface {
                            Text(
                                text = stringResource(id = R.string.map_error),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(dimensionResource(id = R.dimen.md))
                            )
                        }
                    }

                }

                is Success -> {
                    MapContent(
                        context = context,
                        mapState = mapState,
                        mapView = mapView,
                        navController = navController,
                        controller = controller,
                        scope = scope,
                        lastLocation = lastLocation,
                        myLocationOverlay = myLocationOverlay
                    )
                }
            }
        }
    }
    DisposableEffect(mapView) {
        onDispose {
            lastLocation = mapView.mapCenter as GeoPoint?
        }
    }
}

/**
 * Composable function for displaying the content of the map when the API call is successful.
 *
 * @param mapView The MapView instance for displaying the map.
 * @param context The Android context.
 * @param mapState The current state of the map.
 * @param myLocationOverlay The overlay for displaying the user's location.
 * @param lastLocation The last known location of the user.
 * @param navController The navigation controller for navigating to the restaurant details screen.
 * @param controller The IMapController for controlling the map.
 * @param scope The CoroutineScope for launching coroutines.
 */
@Composable
private fun MapContent(
    mapView: MapView,
    context: Context,
    mapState: MapState,
    myLocationOverlay: MyLocationNewOverlay,
    lastLocation: GeoPoint?,
    navController: NavController,
    controller: IMapController,
    scope: CoroutineScope
) {
    /**
     * Function to navigate to the restaurant details screen.
     *
     * @param restaurantId The ID of the restaurant to navigate to.
     */
    fun goToDetail(restaurantId: Int) {
        // Navigate to the restaurant details screen using the NavController
        navController.navigate("${Screens.Detail.name}/$restaurantId")
    }

    /**
     * AndroidView composable for embedding an OSM MapView in the Compose UI.
     *
     * @param mapView The MapView instance for displaying the map.
     */
    AndroidView({ mapView }, modifier = Modifier.testTag("AndroidView")) {
        // Load configuration settings for the OSM MapView
        Configuration.getInstance().load(
            context, context.getSharedPreferences(
                context.getString(R.string.app_name), Context.MODE_PRIVATE
            )
        )

        // Set the tile source and enable multitouch controls for the map
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        // Get the local visible rectangle of the MapView
        mapView.getLocalVisibleRect(Rect())

        // Add markers to the map based on the mapState
        mapState.markers.forEach { marker ->
            val osmMarker = Marker(mapView).apply {
                // Set the position and title of the marker
                position = GeoPoint(marker.lat, marker.long)
                title = marker.title

                // Set a click listener to navigate to the restaurant details screen
                setOnMarkerClickListener { _, _ ->
                    goToDetail(marker.id)
                    true
                }
            }
            // Add the OSM marker to the map overlays
            mapView.overlays.add(osmMarker)
        }

        // Enable and configure the user's location overlay
        myLocationOverlay.enableMyLocation()
        myLocationOverlay.enableFollowLocation()
        myLocationOverlay.isDrawAccuracyEnabled = true

        // If a last known location is available, center and zoom the map to that location
        if (lastLocation != null) {
            controller.setCenter(lastLocation)
            controller.animateTo(lastLocation)
            controller.setZoom(15.0)
        } else {
            // If no last known location, center and zoom to the user's location on the first fix
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

        // Add the user's location overlay to the map overlays
        mapView.overlays.add(myLocationOverlay)
    }
}

/**
 * Composable function for displaying content when location permission is not granted or available.
 *
 * @param background The Painter for the background image.
 * @param text The text content to be displayed.
 */
@Composable
private fun NoPermContent(background: Painter, text: String) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = background,
            contentDescription = "background1",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.md)),
        ) {
            BackgroundSurface {
                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.md)),
                    text = text
                )
            }
        }
    }
}