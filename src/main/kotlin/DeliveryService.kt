import java.math.BigDecimal

fun main(args: Array<String>) {

}

class DeliveryService(
    private val coupons: List<Coupon>
) {
    fun generateDelivery(basePrice: Int, packages: List<Package>, couponCode: String? = null): List<Delivery> {
        return packages.mapIndexed { index, item ->
            val discount: Double = coupons.find { coupon ->
                coupon.code == couponCode &&
                        item.distance in coupon.distanceCriteria &&
                        item.weight in coupon.weightCriteria
            }?.discount ?: 0.0
            val deliveryPrice: BigDecimal =
                (basePrice + (item.distance * 5) + (item.weight * 10)).toBigDecimal() * (1.00 - discount).toBigDecimal()
            Delivery(index+1, item.name, discount, deliveryPrice)
        }
    }
}