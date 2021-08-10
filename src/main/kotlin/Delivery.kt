import java.math.BigDecimal

class Delivery(
    val id: Int,
    val name: String,
    val discountPercent: Double,
    val price: BigDecimal,
){
    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Delivery) return false
        return (id == other.id) && (name == other.name) && (discountPercent == other.discountPercent) && (price == other.price)
    }
    override fun toString(): String {
        return "Delivery $name will cost $price with a ${discountPercent*100}% discount applied"
    }
}