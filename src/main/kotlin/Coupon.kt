import java.math.BigDecimal

class Coupon(
    val id: Int,
    val code: String,
    val discount: Double,
    val distanceCriteria: IntRange,
    val weightCriteria: IntRange,
)