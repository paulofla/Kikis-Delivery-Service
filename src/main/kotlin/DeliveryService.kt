fun main(args: Array<String>) {

}

fun calculateDeliveryCost(basePrice: Int, item: Package): Delivery {
    val deliveryPrice: Int = basePrice + (item.distance * 5) + (item.weight * 10)
    return Delivery(item.name, 0, deliveryPrice.toDouble())
}