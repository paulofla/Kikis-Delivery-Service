class Delivery(
    val id: Int,
    val name: String,
    val discountPercent: Double,
    val price: Double,
){
    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Delivery) return false
        return (id == other.id) && (name == other.name) && (discountPercent == other.discountPercent) && (price == other.price)
    }
}