fun main(args: Array<String>) {
    println("Welcome to Kiki's delivery service!")

    val (basePrice, numberOfPackages) = getBasePriceAndNumberOfPackagesFromUserInput()

    val packages: MutableList<Package> = mutableListOf()
    for (index in 1..numberOfPackages) {
        packages.add(getPackageFromUserInput(index))
    }
    val deliveries = deliveryService.generateDeliveries(basePrice, packages)
    deliveries.mapIndexed { index, delivery ->
        println("${index + 1}. ${delivery.name} ${(delivery.discountPercent * 100).toInt()} ${delivery.price}")
    }
}

private val deliveryService = DeliveryService(
    listOf(
        Coupon(1, "OFR001", 0.1, IntRange(0, 200), IntRange(70, 200)),
        Coupon(2, "OFR002", 0.07, IntRange(50, 150), IntRange(100, 250)),
        Coupon(3, "OFR003", 0.05, IntRange(50, 250), IntRange(10, 150)),
    )
)

private fun getBasePriceAndNumberOfPackagesFromUserInput(): Pair<Int, Int> {
    println(
        """
            Please enter the base delivery cost and the number of packages separated by a white space.
            For example: 100 2
        """.trimIndent()
    )

    return try {
        val (basePrice, numberOfPackages) = readLine()!!.split(' ')
        if (basePrice.toInt() <= 0) {
            println("Base price needs to be greater than 0")
            getBasePriceAndNumberOfPackagesFromUserInput()
        } else if (numberOfPackages.toInt() <= 0) {
            println("The number of packages needs to be greater than 0")
            getBasePriceAndNumberOfPackagesFromUserInput()
        } else {
            Pair(basePrice.toInt(), numberOfPackages.toInt())
        }
    } catch (_: Exception) {
        println("Please input 2 numbers separated by a white space")
        getBasePriceAndNumberOfPackagesFromUserInput()
    }
}

private fun getPackageFromUserInput(index: Int): Package {
    println(
        """
            Please enter the package name, weight (in kg), distance (in km) and coupon code separated by a white space for package 1.
            For example: Kiki 20 10 -> This is a 20 kg package that will travel 10km that won't have any coupon applied
            Joji123 5 100 50-OFF -> This is a 5kg package that will travel 100km that might be eligible for the 50-OFF coupon
        """.trimIndent()
    )
    fun failedToCreatePackage(): Package {
        println("Incorrect inputs. Please only put in 3 or 4 items separated by a single white space")
        return getPackageFromUserInput(index)
    }
    return try {
        val parts = readLine()!!.split(' ')
        if (parts[1].toInt() <= 0) {
            println("The weight of the package needs to be greater than 0")
            failedToCreatePackage()
        } else if (parts[2].toInt() <= 0) {
            println("The distance to deliver the package needs to be greater than 0")
            failedToCreatePackage()
        } else if (parts.size == 3) {
            Package(index, parts[0], parts[1].toInt(), parts[2].toInt(), "")
        } else if (parts.size == 4) {
            Package(index, parts[0], parts[1].toInt(), parts[2].toInt(), parts[3].uppercase())
        } else {
            failedToCreatePackage()
        }
    } catch (_: Exception) {
        failedToCreatePackage()
    }
}
