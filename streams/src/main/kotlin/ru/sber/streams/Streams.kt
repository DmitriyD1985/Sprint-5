package ru.sber.streams


// 1. Используя withIndex() посчитать сумму элементов листа, индекс которых кратен 3. (нулевой индекс тоже входит)
fun getSumWithIndexDivisibleByThree(list: List<Long>): Long {
    return list.withIndex()
        .foldIndexed(0) { idx, sum, element -> if (idx % 3 == 0) sum + element.value.toInt() else sum }
}

// 2. Используя функцию generateSequence() создать последовательность, возвращающую числа Фибоначчи.
fun generateFibonacciSequence(): Sequence<Int> {
    return generateSequence(Pair(0, 1)) { Pair(it.second, it.first + it.second) }.map { it.first }
}

// 3. Получить города, в которых есть покупатели.
fun Shop.getCustomersCities(): Set<City> = this.customers.map { it.city }.toSet()

// 4. Получить все когда-либо заказанные продукты.
fun Shop.allOrderedProducts(): Set<Product> =
    this.customers.flatMap { orders -> orders.orders.flatMap { it.products } }.toSet()

// 5. Получить покупателя, который сделал больше всего заказов.
fun Shop.getCustomerWithMaximumNumberOfOrders(): Customer? = this.customers.maxByOrNull { it.orders.size }

// 6. Получить самый дорогой продукт, когда-либо приобртенный покупателем.
fun Customer.getMostExpensiveProduct(): Product? = this.orders.flatMap { it.products }.maxByOrNull { it.price }

// 7. Получить соответствие в мапе: город - количество заказанных и доставленных продуктов в данный город.
fun Shop.getNumberOfDeliveredProductByCity(): Map<City, Int> = this.customers.associate { customer ->
    customer.city to this.customers.filter { it.city == customer.city }.flatMap { it.orders }.filter { it.isDelivered }
        .flatMap { it.products }.size
}

// 8. Получить соответствие в мапе: город - самый популярный продукт в городе.
fun Shop.getMostPopularProductInCity(): Map<City, Product> = this.customers
    .groupBy(
        { it.city },
        { it.orders.flatMap { order -> order.products } }
    )
    .map { entry ->
        Pair(
            entry.key,
            entry.value
                .flatten()
                .groupingBy { it }
                .eachCount()
                .maxByOrNull { it.value }
                ?.key ?: Product("-", 0.0))
    }
    .toMap()

// 9. Получить набор товаров, которые заказывали все покупатели.
fun Shop.getProductsOrderedByAll(): Set<Product> =
    this.customers.map { it.orders.flatMap { it.products } }
        .reduce { sum, element -> sum.intersect(element.toSet()).toList() }.toSet()


