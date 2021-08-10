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
            assertEquals(Delivery(1, "PKG1", 0.0, 115.00.toBigDecimal()), deliveryService.generateDelivery(100, Package(1, "PKG1", 1, 1), "STATIC"))
        }

        @TestFactory
        fun `Invalid coupon`() = listOf(
            ("As it does not exist" to Package(1, "PKG1", 1, 1)) to Delivery(1, "PKG1", 0.0, 115.00.toBigDecimal()),
            ("Due to a lighter weight" to Package(1, "PKG1", 1, 2)) to Delivery(1, "PKG1", 0.0, 120.00.toBigDecimal()),
            ("Due to a heavier weight" to Package(1, "PKG1", 3, 2)) to Delivery(1, "PKG1", 0.0, 140.00.toBigDecimal()),
            ("Due to a smaller distance" to Package(1, "PKG1", 2, 1)) to Delivery(1, "PKG1", 0.0, 125.00.toBigDecimal()),
            ("Due to a heavier weight" to Package(1, "PKG1", 2, 3)) to Delivery(1, "PKG1", 0.0, 135.00.toBigDecimal()),
        ).map { (input, expected) ->
            DynamicTest.dynamicTest(input.first) {
                assertEquals(expected, deliveryService.generateDelivery(100, input.second, "STATIC"))
            }
        }

        @TestFactory
        fun `Valid coupon`() = listOf(
            DeliveryServiceGenerateDeliveryTest("Get 10% off","STATIC", Package(1, "PKG1", 2, 2), Delivery(1, "PKG1", 0.1, 27.90.toBigDecimal())),
            DeliveryServiceGenerateDeliveryTest("Get 1% off","1%OFF", Package(1, "PKG1", 1, 1), Delivery(1, "PKG1", 0.01, 15.84.toBigDecimal())),
            DeliveryServiceGenerateDeliveryTest("Get 50% off","HALF-OFF", Package(1, "PKG1", 1, 1), Delivery(1, "PKG1", 0.5, 8.00.toBigDecimal())),
            DeliveryServiceGenerateDeliveryTest("Get 100% off","100-OFF", Package(1, "PKG1", 1, 1), Delivery(1, "PKG1", 1.0, 0.00.toBigDecimal())),
            DeliveryServiceGenerateDeliveryTest("Allow White Space in the coupon code","WHITE SPACE", Package(1, "PKG1", 1, 1), Delivery(1, "PKG1", 0.05, 15.20.toBigDecimal())),
        ).map { testData ->
            DynamicTest.dynamicTest(testData.testCaseName) {
                assertEquals(testData.expected, deliveryService.generateDelivery(1, testData.item, testData.couponCode))
            }
        }
    }
}

data class DeliveryServiceGenerateDeliveryTest(val testCaseName: String, val couponCode: String, val item: Package, val expected: Delivery)