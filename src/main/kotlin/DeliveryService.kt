import java.math.BigDecimal

class DeliveryService(
    private val coupons: List<Coupon>
) {
    fun generateDeliveries(basePrice: Int, packages: List<Package>): List<Delivery> {
        return packages.mapIndexed { index, item ->
            val discount: Double = coupons.find { coupon ->
                coupon.code == item.couponCode &&
                        item.distance in coupon.distanceCriteria &&
                        item.weight in coupon.weightCriteria
            }?.discount ?: 0.0
            val deliveryPrice: BigDecimal =
                (basePrice + (item.distance * 5) + (item.weight * 10)).toBigDecimal() * (1.00 - discount).toBigDecimal()
            Delivery(index + 1, item.name, discount, deliveryPrice)
        }
    }
}