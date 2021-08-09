import kotlin.test.Test
import kotlin.test.assertEquals

internal class DeliveryServiceTest {

    @Test
    fun testDeliveryWith1PackageNoCoupon() {
        val expectedName = "PKG1"
        val expectedDiscount = 0
        val expectedPrice = 115.00
        val delivery = calculateDeliveryCost(100, Package("PKG1", 1, 1))
        assertEquals(expectedName, delivery.name)
        assertEquals(expectedDiscount, delivery.discountPercent)
        assertEquals(expectedPrice, delivery.price)
    }


    @Test
    fun testDeliveryWith1PackageAnd1CouponCodeThatDoesNotExist() {
    }
}