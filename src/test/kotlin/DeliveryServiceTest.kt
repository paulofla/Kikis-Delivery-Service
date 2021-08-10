import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestFactory
import kotlin.test.Test
import kotlin.test.assertEquals

internal class DeliveryServiceTest {
    private val deliveryService = DeliveryService(
        listOf(
            Coupon(1, "STATIC", 0.1, IntRange(2, 2), IntRange(2, 2)),
            Coupon(2, "1%OFF", 0.01, IntRange(1, 10), IntRange(1, 10)),
            Coupon(3, "HALF-OFF", 0.5, IntRange(1, 10), IntRange(1, 10)),
            Coupon(4, "100-OFF", 1.0, IntRange(1, 10), IntRange(1, 10)),
            Coupon(5, "WHITE SPACE", 0.05, IntRange(1, 10), IntRange(1, 10)),
        )
    )

    @Nested
    inner class OnePackage {
        @Test
        fun `No coupon supplied`() {
            val expectedId = 1
            val expectedName = "PKG1"
            val expectedDiscount = 0.0
            val expectedPrice = 115.00
            val delivery = deliveryService.generateDelivery(100, Package(1, "PKG1", 1, 1))
            assertEquals(expectedId, delivery.id)
            assertEquals(expectedName, delivery.name)
            assertEquals(expectedDiscount, delivery.discountPercent)
            assertEquals(expectedPrice, delivery.price)
        }

        @TestFactory
        fun `Invalid coupon`() = listOf(
            ("As it does not exist" to Package(1, "PKG1", 1, 1)) to Delivery(1, "PKG1", 0.0, 115.00),
            ("Due to a lighter weight" to Package(1, "PKG1", 1, 2)) to Delivery(1, "PKG1", 0.0, 120.00),
            ("Due to a heavier weight" to Package(1, "PKG1", 3, 2)) to Delivery(1, "PKG1", 0.0, 140.00),
            ("Due to a smaller distance" to Package(1, "PKG1", 2, 1)) to Delivery(1, "PKG1", 0.0, 125.00),
            ("Due to a heavier weight" to Package(1, "PKG1", 2, 3)) to Delivery(1, "PKG1", 0.0, 135.00),
        ).map { (input, expected) ->
            DynamicTest.dynamicTest(input.first) {
                assertEquals(expected, deliveryService.generateDelivery(100, input.second, "STATIC"))
            }
        }

        @TestFactory
        fun `Valid coupon`() = listOf(
            ("Get 10% off" to Package(1, "PKG1", 2, 2)) to Delivery(1, "PKG1", 0.1, 117.00),
        ).map { (input, expected) ->
            DynamicTest.dynamicTest(input.first) {
                assertEquals(expected, deliveryService.generateDelivery(100, input.second, "STATIC"))
            }
        }
    }
}