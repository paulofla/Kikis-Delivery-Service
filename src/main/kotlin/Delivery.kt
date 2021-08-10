import java.math.BigDecimal
import java.math.RoundingMode

class Delivery(
    val id: Int,
    val name: String,
    val discountPercent: Double,
    val price: BigDecimal,
){
    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Delivery) return false
        return (id == other.id) && (name == other.name) && (discountPercent == other.discountPercent) && (price.setScale(2, RoundingMode.HALF_UP) == other.price.setScale(2, RoundingMode.HALF_UP))
    }
    override fun toString(): String {
        return "Delivery $name will cost ${price.setScale(2, RoundingMode.HALF_UP)} with a ${discountPercent*100}% discount applied"
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + discountPercent.hashCode()
        result = 31 * result + price.hashCode()
        return result
    }
}