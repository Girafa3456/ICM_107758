package com.example.greetingcard

import kotlin.math.max
import kotlin.math.min
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


open class SmartDevice(val name: String, val category: String){
    var deviceStatus = "online"
        private set

    open val deviceType = "Unknown"

    // Secondary Constructor
    constructor(name: String, category: String, statusCode: Int) : this(name, category) {
        deviceStatus = when (statusCode) {
            0 -> "offline"
            1 -> "online"
            else -> "unknown"
        }
    }

    open fun turnOn(){
        deviceStatus = "on"
        println("Smart Device is turned on!")
    }

    open fun turnOff(){
        deviceStatus = "off"
        println("Smart Device is turned off!")
    }

    fun printDeviceInfo(){
        println("Device name: $name, category: $category, type: $deviceType")
    }
}


class SmartTVDevice(deviceName: String, deviceCategory: String) : SmartDevice(name = deviceName, category = deviceCategory) {

    override val deviceType = "Smart TV"

    /*
    private var speakerVolume = 2
        set(value) {
            if (value in 0..100) {
                field = value
            }
        }

    private var channelNumber = 1
        set(value) {
            if (value in 0..200) {
                field = value
            }
        }
    */

    private var speakerVolume by RangeRegulator(initialValue = 2, minValue = 0, maxValue = 100)

    private var channelNumber by RangeRegulator(initialValue = 1, minValue = 0, maxValue = 200)

    fun increaseSpeakerVolume() {
        speakerVolume++
        println("Speaker volume increased to $speakerVolume.")
    }

    fun decreaseSpeakerVolume(){
        speakerVolume--
        println("Speaker volume decreased to $speakerVolume.")
    }

    fun nextChannel() {
        channelNumber++
        println("Channel is $channelNumber.")
    }

    fun previousChannel(){
        channelNumber--
        println("Channel is $channelNumber")
    }

    override fun turnOn() {
        super.turnOn()
        println("$name turned on. Speaker volume is set to $speakerVolume and channel number is" + " set to $channelNumber.")

    }

    override fun turnOff() {
        super.turnOff()
        println("$name is turned off")
    }
}

// Relation IS-A
class SmartLIGHTDevice(deviceName: String, deviceCategory: String) : SmartDevice(name = deviceName, category = deviceCategory) {

    override val deviceType = "Smart Light"

    /*
    private var brightnessLevel = 0
        set(value) {
            if (value in 0..100) {
                field = value
            }
        }
    */

    private var brightnessLevel by RangeRegulator(initialValue = 0, minValue = 0, maxValue = 100)

    fun increaseBrightness() {
        brightnessLevel++
        println("Brightness increased to $brightnessLevel.")
    }

    fun decreaseBrightness(){
        brightnessLevel--
        println("Brightness decreased to $brightnessLevel.")
    }

    override fun turnOn() {
        super.turnOn()
        brightnessLevel = 2
        println("$name turned on. The brightness level is $brightnessLevel.")
    }

    override fun turnOff()  {
        super.turnOff()
        brightnessLevel = 0
        println("Smart Light turned off")
    }

}

// Relation HAS-A
class SmartHome(
    val smartTVDevice: SmartTVDevice,
    val smartLIGHTDevice: SmartLIGHTDevice
){

    private fun isDeviceOn(device: SmartDevice): Boolean {
        if (device.deviceStatus == "on"){
            return true
        }
        return false

    }

    var deviceTurnOnCount = 0
        private set

    fun turnOnTV() {
        if (!isDeviceOn(smartTVDevice)){
            deviceTurnOnCount++
            smartTVDevice.turnOn()
        }
    }

    fun turnOffTV() {
        if (isDeviceOn(smartTVDevice)){
            deviceTurnOnCount--
            smartTVDevice.turnOff()
        }
    }

    fun increaseTVVolume() {
        if (isDeviceOn(smartTVDevice)){
            smartTVDevice.increaseSpeakerVolume()
        }
    }

    fun changeTVChannelToNext() {
        if (isDeviceOn(smartTVDevice)){
            smartTVDevice.nextChannel()
        }
    }

    fun turnOnLight() {
        if (!isDeviceOn(smartLIGHTDevice)){
            deviceTurnOnCount++
            smartLIGHTDevice.turnOn()
        }
    }

    fun turnOffLight() {
        if (isDeviceOn(smartLIGHTDevice)){
            deviceTurnOnCount--
            smartLIGHTDevice.turnOff()
        }
    }

    fun increaseLightBrightness() {
        if (isDeviceOn(smartLIGHTDevice)){
            smartLIGHTDevice.increaseBrightness()
        }
    }

    fun turnOffAllDevices() {
        turnOffTV()
        turnOffLight()
    }
}

class RangeRegulator(
    initialValue: Int,
    private val minValue: Int,
    private val maxValue: Int
) : ReadWriteProperty<Any?, Int> {

    var fieldData = initialValue

    override fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        return fieldData
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        if (value in minValue..maxValue) {
            fieldData = value
        }
    }
}


fun main(){
    var smartDevice: SmartDevice = SmartTVDevice("Android Tv", "Entertainment")
    smartDevice.turnOn()

    // reassigning the variable
    smartDevice = SmartLIGHTDevice("Google Light", "Utility")
    smartDevice.turnOn()
}