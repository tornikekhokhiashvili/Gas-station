package com.epam.classes.data

/**
 * Represents a super class for available vehicles
 *
 * Requirements:
 * - super class should have default vales for its property
 * Tips:
 * - [NonFuel] type is suitable to be a default fuel
 */
sealed class Vehicle(
    open val fuel: Fuel = NonFuel,
    open val volume: Int = 0
)

/**
 * Here are all required types of vehicle to use in the task
 *
 * Requirements:
 * - all of them should be inherited from [Vehicle] class
 * - function `toString()` should return simple name: Bike, Car and e.t.c.
 * - [fuel] fields in every vehicle should have default values
 * Tips:
 * - in case of [NonVehicle] since it's an object you can use
 *  *default values* in super class
 */
class Bike(
    override var fuel:Petrol=Petrol(),
        override var volume:Int=15
) :Vehicle(){
    override fun toString(): String {
        return "Bike"
    }
}

class Car(
    override var fuel:Petrol=Petrol(),
    override var volume: Int = 70
) :Vehicle(){
    override fun toString(): String {
        return "Car"
    }
}

class Bus(
    override val fuel: Diesel = Diesel(),
    override val volume: Int = 300

) : Vehicle(){
    override fun toString(): String {
        return "Bus"
    }
}

class Truck(
    override val fuel: Diesel=Diesel(),
        override val volume: Int=800
):Vehicle(){
    override fun toString(): String {
        return "Truck"
    }
}

object NonVehicle:Vehicle(){
    override val fuel: Fuel=NonFuel
    override val volume: Int=0
    override fun toString(): String {
        return "NonVehicle"
    }
}
