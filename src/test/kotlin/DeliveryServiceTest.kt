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

    @Test
    fun `No coupon supplied`() {
        assertEquals(
            listOf(Delivery(1, "PKG1", 0.0, 115.00.toBigDecimal())),
            deliveryService.generateDelivery(100, listOf(Package(1, "PKG1", 1, 1)), "STATIC")
        )
    }

    @TestFactory
    fun `Invalid coupon`() = listOf(
        DeliveryServiceGenerateDeliveryTest(
            "As it does not exist",
            "STATIC",
            listOf(Package(1, "PKG1", 1, 1)),
            listOf(Delivery(1, "PKG1", 0.0, 115.00.toBigDecimal()))
        ),
        DeliveryServiceGenerateDeliveryTest(
            "Due to a lighter weight",
            "STATIC",
            listOf(Package(1, "PKG1", 1, 2)),
            listOf(Delivery(1, "PKG1", 0.0, 120.00.toBigDecimal()))
        ),
        DeliveryServiceGenerateDeliveryTest(
            "Due to a heavier weight",
            "STATIC",
            listOf(Package(1, "PKG1", 3, 2)),
            listOf(Delivery(1, "PKG1", 0.0, 140.00.toBigDecimal()))
        ),
        DeliveryServiceGenerateDeliveryTest(
            "Due to a smaller distance",
            "STATIC",
            listOf(Package(1, "PKG1", 2, 1)),
            listOf(Delivery(1, "PKG1", 0.0, 125.00.toBigDecimal()))
        ),
        DeliveryServiceGenerateDeliveryTest(
            "Due to a longer distance",
            "STATIC",
            listOf(Package(1, "PKG1", 2, 3)),
            listOf(Delivery(1, "PKG1", 0.0, 135.00.toBigDecimal()))
        ),
    ).map { testData ->
        DynamicTest.dynamicTest(testData.testCaseName) {
            assertEquals(testData.expected, deliveryService.generateDelivery(100, testData.packages, testData.couponCode))
        }
    }

    @TestFactory
    fun `Valid coupon`() = listOf(
        DeliveryServiceGenerateDeliveryTest(
            "Get 10% off",
            "STATIC",
            listOf(Package(1, "PKG1", 2, 2)),
            listOf(Delivery(1, "PKG1", 0.1, 27.90.toBigDecimal()))
        ),
        DeliveryServiceGenerateDeliveryTest(
            "Get 1% off",
            "1%OFF",
            listOf(Package(1, "PKG1", 1, 1)),
            listOf(Delivery(1, "PKG1", 0.01, 15.84.toBigDecimal()))
        ),
        DeliveryServiceGenerateDeliveryTest(
            "Get 50% off",
            "HALF-OFF",
            listOf(Package(1, "PKG1", 1, 1)),
            listOf(Delivery(1, "PKG1", 0.5, 8.00.toBigDecimal()))
        ),
        DeliveryServiceGenerateDeliveryTest(
            "Get 100% off",
            "100-OFF",
            listOf(Package(1, "PKG1", 1, 1)),
            listOf(Delivery(1, "PKG1", 1.0, 0.00.toBigDecimal()))
        ),
        DeliveryServiceGenerateDeliveryTest(
            "Allow White Space in the coupon code",
            "WHITE SPACE",
            listOf(Package(1, "PKG1", 1, 1)),
            listOf(Delivery(1, "PKG1", 0.05, 15.20.toBigDecimal()))
        ),
    ).map { testData ->
        DynamicTest.dynamicTest(testData.testCaseName) {
            assertEquals(testData.expected, deliveryService.generateDelivery(1, testData.packages, testData.couponCode))
        }
    }

    @Test
    fun `Multiple coupons supplied`() {
        assertEquals(
            listOf(Delivery(1, "PKG1", 0.0, 115.00.toBigDecimal())),
            deliveryService.generateDelivery(100, listOf(Package(1, "PKG1", 1, 1)), "STATIC"),
        )
    }
}

data class DeliveryServiceGenerateDeliveryTest(
    val testCaseName: String,
    val couponCode: String,
    val packages: List<Package>,
    val expected: List<Delivery>,
)