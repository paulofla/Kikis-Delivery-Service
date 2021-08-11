# Kiki's Delivery Service

## Overview

Kiki is setting up a delivery service that will accept orders via a command line application.

### Delivery Cost:

(Base + (Package total weight * $10) + (Distance to Destination * $5)) * (1 - discount)

### Coupons

Kiki has given coupons to people for her new delivery service.

These are the current valid coupons:

| Code      | Discount   | Distance (km) | Weight (kg) |
| ----------|:----------:| ------------- |:-----------:|
| OFR001    | 10%        | 0km-200km     | 70kg-200kg  |
| OFR002    |  7%        | 50km-150km    | 70kg-200kg  |
| OFR003    |  5%        | 50km-250km    | 10kg-150kg  |

## Model

Coupon:

- id (Not needed but good practice to have it)
- code
- discount
- distanceCriteria: IntRange
- weightCriteria: IntRange
- start date (Versus an is active flag in case she mis-printed coupon criteria - I removed this as I can add this functionality later as it is not needed now)
- expiry date (Versus an is active flag in case she mis-printed coupon criteria - I removed this as I can add this functionality later as it is not needed now)

Coupon Criteria: (1 offer applied to 1 package only)

Package:
- id (Not needed but good practice to have it)
- name
- weight
- distance
- couponCode

Delivery:
- id
- name
- discountPercent
- price: BigDecimal

## Input & Output

1st line of input: base_delivery_cost no_of_packages

2nd line of input: package_id package_weight distance offer_code?

3rd line of input: package_id package_weight distance offer_code?

...

Continue adding packages until you are finished and hit enter

The output will be a list of packages entered, with this format:

packages_id discount_percent total_cost

### Example

User Input:

1. 100 3
2. PKG1 5 5 OFR001
3. PKG2 15 5 OFR002
4. PKG3 10 100 OFR003

Output:

1. PKG1 0 175
2. PKG2 0 275
3. PKG 35 665

## Test List

I'm writing a list of things I would like to test for, looking for boundary cases and any other test that would help me
to ensure correctness.

### My main thoughts & assumptions:
1. Base price:
   1. In $ only (No cents allowed)
   2. Greater than 0
   3. Max Value: Kotlin's max integer value
   4. Due to the above, the total after discount applied will always have a maximum decimal to 2 places (In cents, e.g. :$XXX.XX)
   5. This will be enforced when reading from the command line
2. Weight:
   1. In KGs only (grams are not allowed)
   2. Greater than 0
   3. Max Value: Kotlin's max integer value
   4. This will be enforced when reading from the command line
3. Distance:
   1. In KMs only (meters are not allowed)
   2. Greater than 0
   3. Max Value: Kotlin's max integer value 
   4. This will be enforced when reading from the command line
4. Discount:
   1. Set by Tombo so assumed it will never be greater than 100 (My boundary)
   2. This won't be enforced through the code
5. Coupon codes:
   1. Set by Tombo so assumed that they will always have a valid name
   2. Will be in uppercase when stored
   3. Can work for multiple packages in the same order
   4. They don't have an expiry or a start date
   5. Coupon code naming standard won't be enforced through the code
6. Packages
   1. There will always be at least 1 package
   2. There is no limit to the number of package that can be delivered (except the memory limit of where the computer is running but I don't believe that will be exceeded)
   3. The package name will be limited to a single word

### Tests:
This is my coupon table

| Code         | Discount  | Distance (km) | Weight (kg) |
| ------------ |:---------:| ------------- |:-----------:|
| STATIC       |  0.1      | 2km-2km       | 2kg-2kg     |
| 1%OFF        |  0.01     | 1km-10km      | 1kg-10kg    |
| HALF-OFF     |  0.5      | 1km-10km      | 1kg-10kg    |
| 100-OFF      |  1.0      | 1km-10km      | 1kg-10kg    |

#### Delivery Cost Calculation Test List:
1. Delivery with 1 package and no coupon code
   1. Input:
        1. 100 1      
        2. PKG1 1 1
   2. Output:
       1. PKG1 0 115
2. Delivery with 1 package and a coupon code that doesn't exist
    1. Input:
       1. 100 1
       2. PKG1 1 1 NO-COUPON
    2. Output:
       1. PKG1 0 115
3. Delivery with 1 package and a coupon code that exists but is not valid due to weight and distance
   1. Input:
      1. 100 1
      2. PKG1 1 1 STATIC
   2. Output:
       1. PKG1 0 115
4. Delivery with 1 package and a coupon code that exists but is not valid due to the weight being lighter than allowed
   1. Input:
      1. 100 1
      2. PKG1 1 2 STATIC
   2. Output:
      1. PKG1 0 120
5. Delivery with 1 package and a coupon code that exists but is not valid due to the weight being heavier than allowed
   1. Input:
      1. 100 1
      2. PKG1 3 2 STATIC
   2. Output:
      1. PKG1 0 140
6. Delivery with 1 package and a coupon code that exists and is not valid due to the distance being longer than allowed
   1. Input:
      1. 100 1
      2. PKG1 2 1 STATIC
   2. Output:
      1. PKG1 0 125
7. Delivery with 1 package and a coupon code that exists and is not valid due to the distance being shorter than allowed
   1. Input:
      1. 100 1
      2. PKG1 2 3 STATIC
   2. Output:
      1. PKG1 0 135
8. Delivery with 1 package and a coupon code that exists and is valid
   1. Input:
      1. 1 1
      2. PKG1 2 2 STATIC
   2. Output:
      1. PKG1 10 27.9
9. Delivery with 1 package and 1% off coupon code
   1. Input:
      1. 1 1
      2. PKG1 1 1 1%OFF
   2. Output:
      1. PKG1 1 15.84
10. Delivery with 1 package and 50% off coupon code
    1. Input:
       1. 1 1
       2. PKG1 1 1 HALF-OFF
    2. Output:
       1. PKG1 50 8
11. Delivery with 1 package and 100% off coupon code
    1. Input:
       1. 1 1
       2. PKG1 1 1 100-OFF
    2. Output:
       1. PKG1 100 0
12. Delivery with multiple packages and using the same coupon code
    3. Input:
       1. 1 2
       2. PKG1 2 2 STATIC
       3. PKG2 2 2 STATIC
    4. Output:
       1. PKG1 10 27.9
       2. PKG2 10 27.9
13. Delivery with multiple packages and using different coupon codes
    1. Input:
       1. 1 2
       2. PKG1 2 2 NO-COUPON
       3. PKG2 2 2 STATIC
       4PKG2 2 2 HALF-OFF
    2. Output:
       1. PKG1 0 31
       2. PKG2 10 27.9
       3. PKG2 10 15.5

I feel the above will give me confidence in my test coverage for boundaries. I will start from the simplest which I think is the 1st.

I will test the delivery calculation first via tests. I'll manually test user input afterwards as this is harder to test.

### To Do

1. The cost of $5 and $10 for the base weight and base distance should be moved to an environment variable in the future. 
2. The display, validation and outputStream could be separated further but I think the logic to get the user input is good enough. 
3. I've left package and coupon to have an id as it is good practice from them to have some difference from packages of the same name and coupons with the same name. 
4. I would ideally like to add an expiry date and start date for coupons but the current requirements don't need it. 
5. I removed the white space test as it was complicated to get via user input (I'd have to do some regular expressions which I didn't want to do for this application right now)