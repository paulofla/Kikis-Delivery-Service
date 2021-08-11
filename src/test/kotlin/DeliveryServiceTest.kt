import org.junit.jupiter.api.DynamicTest
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
            deliveryService.generateDelivery(100, listOf(Package(1, "PKG1", 1, 1, "STATIC")))
        )
    }

    @TestFactory
    fun `Invalid coupon`() = listOf(
        DeliveryServiceGenerateDeliveryTest(
            "As it does not exist",
            listOf(Package(1, "PKG1", 1, 1, "STATIC")),
            listOf(Delivery(1, "PKG1", 0.0, 115.00.toBigDecimal()))
        ),
        DeliveryServiceGenerateDeliveryTest(
            "Due to a lighter weight",
            listOf(Package(1, "PKG1", 1, 2, "STATIC")),
            listOf(Delivery(1, "PKG1", 0.0, 120.00.toBigDecimal()))
        ),
        DeliveryServiceGenerateDeliveryTest(
            "Due to a heavier weight",
            listOf(Package(1, "PKG1", 3, 2, "STATIC")),
            listOf(Delivery(1, "PKG1", 0.0, 140.00.toBigDecimal()))
        ),
        DeliveryServiceGenerateDeliveryTest(
            "Due to a smaller distance",
            listOf(Package(1, "PKG1", 2, 1, "STATIC")),
            listOf(Delivery(1, "PKG1", 0.0, 125.00.toBigDecimal()))
        ),
        DeliveryServiceGenerateDeliveryTest(
            "Due to a longer distance",
            listOf(Package(1, "PKG1", 2, 3, "STATIC")),
            listOf(Delivery(1, "PKG1", 0.0, 135.00.toBigDecimal()))
        ),
    ).map { testData ->
        DynamicTest.dynamicTest(testData.testCaseName) {
            assertEquals(
                testData.expectedDeliveries,
                deliveryService.generateDelivery(100, testData.packages)
            )
        }
    }

    @TestFactory
    fun `Valid coupon`() = listOf(
        DeliveryServiceGenerateDeliveryTest(
            "Get 10% off",
            listOf(Package(1, "PKG1", 2, 2, "STATIC")),
            listOf(Delivery(1, "PKG1", 0.1, 27.90.toBigDecimal()))
        ),
        DeliveryServiceGenerateDeliveryTest(
            "Get 1% off",
            listOf(Package(1, "PKG1", 1, 1, "1%OFF")),
            listOf(Delivery(1, "PKG1", 0.01, 15.84.toBigDecimal()))
        ),
        DeliveryServiceGenerateDeliveryTest(
            "Get 50% off",
            listOf(Package(1, "PKG1", 1, 1, "HALF-OFF")),
            listOf(Delivery(1, "PKG1", 0.5, 8.00.toBigDecimal()))
        ),
        DeliveryServiceGenerateDeliveryTest(
            "Get 100% off",
            listOf(Package(1, "PKG1", 1, 1, "100-OFF")),
            listOf(Delivery(1, "PKG1", 1.0, 0.00.toBigDecimal()))
        ),
        DeliveryServiceGenerateDeliveryTest(
            "Allow White Space in the coupon code",
            listOf(Package(1, "PKG1", 1, 1, "WHITE SPACE")),
            listOf(Delivery(1, "PKG1", 0.05, 15.20.toBigDecimal()))
        ),
        DeliveryServiceGenerateDeliveryTest(
            "Multiple of the same coupon is supplied for different packages",
            listOf(Package(1, "PKG1", 2, 2, "STATIC"), Package(2, "PKG2", 2, 2, "STATIC")),
            listOf(Delivery(1, "PKG1", 0.1, 27.90.toBigDecimal()), Delivery(2, "PKG2", 0.1, 27.90.toBigDecimal()))
        ),
        DeliveryServiceGenerateDeliveryTest(
            "Different coupons are supplied for different packages in the same order",
            listOf(
                Package(1, "PKG1", 2, 2, "NO-COUPON"),
                Package(2, "PKG2", 2, 2, "STATIC"),
                Package(3, "PKG3", 2, 2, "HALF-OFF"),
            ),
            listOf(
                Delivery(1, "PKG1", 0.0, 31.00.toBigDecimal()),
                Delivery(2, "PKG2", 0.1, 27.90.toBigDecimal()),
                Delivery(3, "PKG3", 0.5, 15.50.toBigDecimal()),
            )
        ),
    ).map { testData ->
        DynamicTest.dynamicTest(testData.testCaseName) {
            assertEquals(
                testData.expectedDeliveries,
                deliveryService.generateDelivery(1, testData.packages)
            )
        }
    }
}

data class DeliveryServiceGenerateDeliveryTest(
    val testCaseName: String,
    val packages: List<Package>,
    val expectedDeliveries: List<Delivery>,
)