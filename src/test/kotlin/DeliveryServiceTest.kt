import kotlin.test.Test
import kotlin.test.assertEquals

internal class DeliveryServiceTest {
    private val deliveryService = DeliveryService(listOf(
        Coupon(1, "STATIC", 0.1, IntRange(2,2), IntRange(2,2)),
        Coupon(2, "1%OFF", 0.01, IntRange(1,10), IntRange(1,10)),
        Coupon(3, "HALF-OFF", 0.5, IntRange(1,10), IntRange(1,10)),
        Coupon(4, "100-OFF", 1.0, IntRange(1,10), IntRange(1,10)),
        Coupon(5, "WHITE SPACE", 0.05, IntRange(1,10), IntRange(1,10)),
    ))

    @Test
    fun testDeliveryWith1PackageNoCoupon() {
        val expectedId = 1
        val expectedName = "PKG1"
        val expectedDiscount = 0.0
        val expectedPrice = 115.00
        val delivery = deliveryService.generateDelivery(100, Package(1,"PKG1", 1, 1))
        assertEquals(expectedId, delivery.id)
        assertEquals(expectedName, delivery.name)
        assertEquals(expectedDiscount, delivery.discountPercent)
        assertEquals(expectedPrice, delivery.price)
    }

    @Test
    fun testDeliveryWith1PackageAnd1CouponCodeThatDoesNotExist() {
        val expectedId = 1
        val expectedName = "PKG1"
        val expectedDiscount = 0.0
        val expectedPrice = 115.00
        val delivery = deliveryService.generateDelivery(100, Package(1,"PKG1", 1, 1), "NO-COUPON")
        assertEquals(expectedId, delivery.id)
        assertEquals(expectedName, delivery.name)
        assertEquals(expectedDiscount, delivery.discountPercent)
        assertEquals(expectedPrice, delivery.price)
    }

    @Test
    fun testDeliveryWith1PackageAnd1CouponCodeThatExistsButWeightAndDistanceIsNotValid() {
        val expectedId = 1
        val expectedName = "PKG1"
        val expectedDiscount = 0.0
        val expectedPrice = 115.00
        val delivery = deliveryService.generateDelivery(100, Package(1,"PKG1", 1, 1), "STATIC")
        assertEquals(expectedId, delivery.id)
        assertEquals(expectedName, delivery.name)
        assertEquals(expectedDiscount, delivery.discountPercent)
        assertEquals(expectedPrice, delivery.price)
    }

    @Test
    fun testDeliveryWith1PackageAnd1CouponCodeThatExistsButWeightIsNotValid() {
        val expectedId = 1
        val expectedName = "PKG1"
        val expectedDiscount = 0.0
        val expectedPrice = 120.00
        val delivery = deliveryService.generateDelivery(100, Package(1,"PKG1", 1, 2), "STATIC")
        assertEquals(expectedId, delivery.id)
        assertEquals(expectedName, delivery.name)
        assertEquals(expectedDiscount, delivery.discountPercent)
        assertEquals(expectedPrice, delivery.price)
    }

    @Test
    fun testDeliveryWith1PackageAnd1CouponCodeThatExistsButDistanceIsNotValid() {
        val expectedId = 1
        val expectedName = "PKG1"
        val expectedDiscount = 0.0
        val expectedPrice = 125.00
        val delivery = deliveryService.generateDelivery(100, Package(1,"PKG1", 2, 1), "STATIC")
        assertEquals(expectedId, delivery.id)
        assertEquals(expectedName, delivery.name)
        assertEquals(expectedDiscount, delivery.discountPercent)
        assertEquals(expectedPrice, delivery.price)
    }

    @Test
    fun testDeliveryWith1PackageAnd1CouponCodeThatIsValid() {
        val expectedId = 1
        val expectedName = "PKG1"
        val expectedDiscount = 0.1
        val expectedPrice = 117.00
        val delivery = deliveryService.generateDelivery(100, Package(1,"PKG1", 2, 2), "STATIC")
        assertEquals(expectedId, delivery.id)
        assertEquals(expectedName, delivery.name)
        assertEquals(expectedDiscount, delivery.discountPercent)
        assertEquals(expectedPrice, delivery.price)
    }
}