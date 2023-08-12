package com.epam.classes

import com.epam.classes.Const.BIKE
import com.epam.classes.Const.BUS
import com.epam.classes.Const.CAR
import com.epam.classes.Const.NO
import com.epam.classes.Const.TRUCK
import com.epam.classes.Const.YES
import com.epam.classes.data.*
import com.epam.classes.mapper.DiscountAvailabilityMapper
import com.epam.classes.mapper.VehicleMapper

class GasStation {

    /**
     * Requirements:
     * - initialize 'vehicleMapper' and 'discountAvailabilityMapper' here
     *   and make it private
     */
    private val vehicleMapper: VehicleMapper = VehicleMapper()
    private val discountAvailabilityMapper: DiscountAvailabilityMapper = DiscountAvailabilityMapper()

    /**
     * Entry point to the whole program. All "black magic" is happening right here :).
     * You have to implement next functionality:
     *
     * 1. Ask a client to select a vehicle
     * 2. Ask a client is he has discount or not
     * 3. Prepare a bill, proceed it to calculate a total price and show a total info
     *
     * The main idea is to create a blank/default [Bill] and filling it up "step-by-step"
     *
     * Tips:
     * - a bill should be prepared by predefined functions [selectVehicle] and [isDiscountExist]
     * - after the bill was created client should see the message:
     *   "How much fuel do you want to fill?"
     * - when our bill is ready it should be proceeded to [checkAndShowTotalInfo]
     */
    fun fillTank() {
        val vehicleName=readInput("Select a vehicle")
        val vehicle=vehicleMapper.mapNameToVehicle(vehicleName)
        val discountInput=readInput("Do you have discount? (yes/no)")
        val discountAvaliable = mapInputToBoolean(discountInput)
        val amount=readInput("How much fuel do you want to fill?: ")
        val validAmount=validateAndConvertVolume(amount,vehicle.volume)
        if (validAmount!=null){
            val bill=Bill(vehicle,discountAvaliable,validAmount,0.0)
            var billWithTotalPrice=calculateTotalPrice(bill)
            checkAndShowTotalInfo(billWithTotalPrice)
        }
        else {
            println("Please enter a valid amount of liters.")
        }

    }
    fun readInput(input:String):String{
        println(input)
        return readLine()?:""
    }
    fun checkAndShowTotalInfo(bill: Bill){
        val totalPrice=bill.totalPrice
        println("Total price: $totalPrice")
    }
    fun mapInputToBoolean(input: String): Boolean? {
        return when (input.toLowerCase()) {
            "yes" -> true
            "no" -> false
            else -> null
        }
    }

    /**
     * Converts client input into [Vehicle] by using [vehicleMapper] and
     * shows appropriate message. Valid inputs are: "bike", "car", "bus" or "truck"
     *
     * Requirements:
     * - when vehicle is valid, it should be returned with the message:
     *   "Your vehicle is {selectedVehicle.toString()}"
     * - when vehicle is not valid ([NonVehicle]), then we have to ask client
     *   to enter vehicle name until it will be valid and show the message:
     *   "Please select one of available vehicles: bike, car, bus or truck"
     * Tips:
     * - you have all vehicle names as constants in the [Const] class
     * - please notice that client input could be null
     * - recursion could help you to satisfy *until it will be valid* requirement
     *
     * @param vehicleName represents clients input with selected vehicle
     * @return appropriate vehicle based on clients input
     */
    tailrec fun getCustomerVehicle(vehicleName: String?): Vehicle {
        var currentVehicleName = vehicleName
        while (true) {
            val selectedVehicle = vehicleMapper.mapNameToVehicle(currentVehicleName)
            if (selectedVehicle is NonVehicle){
                println("Please select one of available vehicles: ${Const.BIKE}, ${Const.CAR}, ${Const.BUS} or ${Const.TRUCK}")
                return selectedVehicle
            }else{
                println("Your vehicle is ${selectedVehicle::class.simpleName}")
                return selectedVehicle // Return the selected vehicle
            }
        }
    }

    /**
     * Checks discount availability by using [discountAvailabilityMapper] and
     * shows appropriate message. Valid inputs are "yes" or "no"
     *
     * Requirements:
     * - when [answerAboutDiscount] is valid, appropriate boolean value should be returned
     * - when [answerAboutDiscount] is not valid, we should ask client to enter discount availability
     *   once again until it will be valid and show the message:
     *   "Please enter "yes" or "no""
     * - when discount is exists, used should see a message: "Discount exist"
     * - when discount isn't exists, used should see a message: "Discount doesn't exist"
     * Tips:
     * - try to use 'when' operator instead of 'if'
     * - you have all discount inputs as constants in the [Const] class
     * - please notice that client input could be null
     * - recursion could help you to satisfy *until it will be valid* requirement
     *
     * @param answerAboutDiscount represents client input with discount availability
     * @return is discount available or not
     */
    fun checkAndReturnIsDiscountAvailable(answerAboutDiscount: String?): Boolean? {
        val discountAvailability = discountAvailabilityMapper.mapAnswer(answerAboutDiscount)

        return when (discountAvailability) {
            DiscountAvailability.AVAILABLE -> {
                println("Discount exist")
                true
            }
            DiscountAvailability.NON_AVAILABLE -> {
                println("Discount doesn't exist")
                false
            }
            else -> {
                println("Please enter \"${Const.YES}\" or \"${Const.NO}\"")
                null
            }
        }

        return discountAvailability == DiscountAvailability.AVAILABLE
    }

    /**
     * Calculates total price and shows it to the client.
     *
     * Requirements:
     * - if [volumeString] contains incorrect value that **cannot be cast to Int** or
     *   **it's greater than vehicle tank volume** then ask client to enter amount of liters
     *   until it will be valid
     * - successful case should have output:
     *   "Total info: $bill"
     * - unsuccessful case should have output:
     *   "Please enter correct value (not bigger than your tank volume: 500 liters)",
     *   where 500 is [Vehicle.volume]
     * Tips:
     * - total price we can calculate using [calculateTotalPrice]
     * - please notice that client input could be null
     * - recursion could help you to satisfy *until it will be valid* requirement
     * - it's very suitable here to use [Bill.copy] function in order to define the [Bill.amountToFill]
     *
     * @param volumeString represents clients input with amount of liters that customer wants to fill
     * @param bill with all corresponding data
     */
    fun checkAndShowTotalInfo(bill: Bill, volumeString: String?) {
        val validVolume=validateAndConvertVolume(volumeString,bill.vehicle.volume)
        if (validVolume != null) {
            if (validVolume <= bill.vehicle.volume) {
                val updatedBill = bill.copy(amountToFill = validVolume)
                val totalPrice = calculateTotalPrice(updatedBill)
                println("Total info: $totalPrice")
            }
        }
        else {
                println("Please enter correct value (not bigger than your tank volume: ${bill.vehicle.volume} liters)")
        }

    }
    fun validateAndConvertVolume(volumeString: String?, maxVolume: Int): Int? {
        val volume = volumeString?.toIntOrNull()
        return if (volume != null && volume <= maxVolume) {
            volume
        } else {
            null
        }
    }

    /**
     * Calculates total price which should be spent to fill up a vehicle tank.
     *
     * Requirements:
     * - if [Bill.isDiscountExist] equals true, you have to applied [Fuel.discount] in calculation
     * - total price should be based on [Fuel.cost]
     * Tips:
     * - please, keep in mind that [Fuel.discount] represents a number of percents
     * - it's very suitable here to use [Bill.copy] function in order to define the [Bill.totalPrice]
     *
     * @param bill with all corresponding data
     * @return bill which includes total price based on the required fuel
     */
    fun calculateTotalPrice(bill: Bill): Bill {
        val fuel=bill.vehicle.fuel
        val baseprice=fuel.cost.toDouble() * bill.amountToFill.toDouble()
        val discountedPrice=if (bill.isDiscountExist==true){
            baseprice*(1-(fuel.discount.toDouble()/100.0))
        }else{
            baseprice
        }
        val updatedBill = bill.copy(totalPrice = discountedPrice)

        return updatedBill
    }

    private fun isDiscountExist(): Boolean? {
        println("Do you have a discount card? ($YES or $NO)")
        return checkAndReturnIsDiscountAvailable(readLine())
    }

    private fun selectVehicle(): Vehicle {
        println("Please choose your vehicle: $BIKE, $CAR, $BUS or $TRUCK")
        return getCustomerVehicle(readLine())
    }
}