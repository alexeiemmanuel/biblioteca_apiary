package com.aemm.libreria.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.navArgs
import com.aemm.libreria.R
import com.aemm.libreria.databinding.FragmentBookMapBinding
import com.aemm.libreria.model.BookDetail
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class BookMapFragment : Fragment(R.layout.fragment_book_map) {

    private lateinit var bookDetail: BookDetail

    private lateinit var binding: FragmentBookMapBinding

    private val args: BookMapFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = FragmentBookMapBinding.bind(view)
        this.bookDetail = this.args.bookDetail

        val mapFragment = childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment?

        mapFragment!!.getMapAsync { mMap ->
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            mMap.clear()
            val coordinates = bookDetail.editorial?.let { LatLng(it.latitude, it.longitude) }
            if(coordinates != null){
                val marker = MarkerOptions()
                    .position(coordinates)
                    .title(this@BookMapFragment.requireContext()
                        .getString(R.string.book_editorial_label, bookDetail.editorial?.name))
                    .snippet(this@BookMapFragment.requireContext()
                        .getString(R.string.book_editorial_map, bookDetail.editorial?.schedule, bookDetail.editorial?.telephone))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.book_map))
                mMap.addMarker(marker)
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(coordinates, 18f),
                    4000,
                    null
                )
            }
        }
    }
}