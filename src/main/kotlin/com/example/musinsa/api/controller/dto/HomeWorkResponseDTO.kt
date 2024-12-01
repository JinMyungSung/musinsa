package com.example.musinsa.api.controller.dto

import com.example.musinsa.application.service.GetCheapestBrandPerCategoryResult
import com.example.musinsa.application.service.GetCheapestTotalPriceBrandResult
import com.example.musinsa.application.service.GetSpecificCategoryCheapestAndMostExpensiveProductResult
import java.text.NumberFormat

object Helper {
    fun addComma(number: Number): String {
        return NumberFormat.getInstance().format(number)
    }
}

data class HomeWork1ResponseDTO(
    val items: List<Item>,
    val totalPrice: String,
    val tsv: String,
) {
    data class Item(
        val 카테고리: String,
        val 브랜드: String,
        val 가격: String,
    )

    companion object {
        fun submit(result: GetCheapestBrandPerCategoryResult): HomeWork1ResponseDTO {
            return HomeWork1ResponseDTO(
                items =
                    result.cheapestProductPerCategoryMap
                        .map {
                            Item(
                                카테고리 = it.key.name,
                                브랜드 = it.value.brandName,
                                가격 = Helper.addComma(it.value.cheapestProductMainPrice),
                            )
                        },
                totalPrice = Helper.addComma(result.totalPrice),
                tsv = result.asTsv,
            )
        }
    }
}

data class Homework2ResponseDTO(
    val 최저가: Item,
) {
    data class Item(
        val 브랜드: String,
        val 카테고리: List<Category>,
        val 총액: String,
    )

    data class Category(
        val 카테고리: String,
        val 가격: String,
    )

    companion object {
        fun submit(result: GetCheapestTotalPriceBrandResult): Homework2ResponseDTO {
            return Homework2ResponseDTO(
                최저가 =
                    Item(
                        브랜드 = result.cheapestBrand.name,
                        카테고리 =
                            result.brandCheapestProductPerCategoryMap[result.cheapestBrand]!!
                                .values
                                .map {
                                    Category(
                                        카테고리 = it.categoryName,
                                        가격 = Helper.addComma(it.cheapestProductMainPrice),
                                    )
                                },
                        총액 =
                            Helper.addComma(
                                result.brandCheapestProductPerCategoryMap[result.cheapestBrand]!!
                                    .values
                                    .sumOf { it.cheapestProductMainPrice },
                            ),
                    ),
            )
        }
    }
}

data class Homework3ResponseDTO(
    val 카테고리: String,
    val 최저가: List<Item>,
    val 최고가: List<Item>,
) {
    data class Item(
        val 브랜드: String,
        val 가격: String,
    )

    companion object {
        fun submit(result: GetSpecificCategoryCheapestAndMostExpensiveProductResult): Homework3ResponseDTO {
            return Homework3ResponseDTO(
                카테고리 = result.category.name,
                최저가 =
                    listOf(
                        Item(
                            브랜드 = result.cheapestProduct.brandName,
                            가격 = Helper.addComma(result.cheapestProduct.mainPrice),
                        ),
                    ),
                최고가 =
                    listOf(
                        Item(
                            브랜드 = result.mostExpensiveProduct.brandName,
                            가격 = Helper.addComma(result.mostExpensiveProduct.mainPrice),
                        ),
                    ),
            )
        }
    }
}
