package com.universe.vladiviva5991gmail.moons.mvp.location

abstract class BaseLocation {
    abstract fun startLocationUpdates()
    abstract fun onStart()
    abstract fun onStop()
}