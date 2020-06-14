package com.coolapps.yo.maple

enum class ArticleTags(private val id: String) {
    ALL("0"),
    BUSINESS("1"),
    MANUFACTURING("2"),
    SERVICE("3"),
    INTERNATIONAL("4"),
    TECHNOLOGY("5"),
    START_UP("6"),
    STOCK_MARKET("7"),
    INNOVATION("8"),
    SUCCESS_STORY("9"),
    ECONOMICS("10"),
    IMPORT_EXPORT("11");

    companion object {
        @JvmStatic
        fun from(typeValue: String): ArticleTags {
            for (type in values()) {
                if (type.id == typeValue) {
                    return type
                }
            }
            return ALL
        }
    }

    fun getId(): String {
        return id
    }
}