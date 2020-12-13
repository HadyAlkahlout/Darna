package com.raiyansoft.darnaapp.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.databinding.DialogMapBinding
import com.raiyansoft.darnaapp.listeners.MapDialogListener

class MapDialog(var lat: Double, var long: Double, var listener: MapDialogListener) :
    DialogFragment(){

    private lateinit var binding: DialogMapBinding
    private val callback = OnMapReadyCallback { googleMap ->
        var loc = LatLng(29.3117, 47.4818)
        if (lat == 0.0) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 8f))
            googleMap.setOnMapClickListener { latLng ->
                googleMap.clear()
                val marker = MarkerOptions()
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                googleMap.addMarker(
                    marker.position(latLng)
                )
                lat = latLng.latitude
                long = latLng.longitude
            }
        } else {
            binding.buttonSave.visibility = View.GONE
            loc = LatLng(lat, long)
            val marker = MarkerOptions()
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            googleMap.addMarker(
                marker.position(loc)
            )
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 12f))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DialogMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        binding.buttonSave.setOnClickListener {
            if (lat == 0.0 || long == 0.0) {
                Snackbar.make(view, getString(R.string.pick_location), 3000).show()
            } else {
                listener.onClick(lat, long)
            }
        }

    }
}