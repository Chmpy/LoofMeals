package com.example.loofmeals.network

import com.example.loofmeals.data.model.Restaurant

/**
 * Data class representing a restaurant fetched from the API.
 *
 * This class maps the JSON response from the API to a Kotlin object. The property names in this class
 * match the keys in the JSON response.
 *
 * This class is used by Retrofit to parse the JSON response automatically.
 */
data class ApiRestaurant(
    val business_product_id: String,
    val information_group: String,
    val discriminator: String,
    val changed_time: String,
    val deleted: String,
    val name: String,
    val street: String,
    val house_number: String,
    val box_number: String,
    val postal_code: String,
    val city_name: String,
    val main_city_name: String,
    val distance: String,
    val promotional_region: String,
    val x: String,
    val y: String,
    val lat: String,
    val long: String,
    val green_key_labeled: String,
    val accessibility_label: String,
    val closing_period: String,
    val next_year_closing_period: String,
    val phone1: String,
    val phone2: String,
    val phone3: String,
    val email: String,
    val website: String,
    val facebook: String,
    val twitter: String,
    val flickr: String,
    val instagram: String,
    val product_description: String,
    val link_to_accessibility_website: String,
    val accessibility_description: String,
    val entrance: String,
    val route_and_levels: String,
    val common_toilet: String,
    val allergies: String,
    val extra_facilities: String,
    val food_allergy: String,
    val deaf: String,
    val auditive: String,
    val mental: String,
    val motor: String,
    val blind: String,
    val visual: String,
    val autism: String,
    val food_allergy_desc: String,
    val allergies_desc: String,
    val deaf_desc: String,
    val auditive_desc: String,
    val mental_desc: String,
    val motor_desc: String,
    val blind_desc: String,
    val visual_desc: String,
    val autism_desc: String,
    val garden: String,
    val space_table_desc: String
)

/**
 * Extension function to convert a list of ApiRestaurant objects to a list of Restaurant objects.
 *
 * This function filters out the restaurants that are marked as deleted and maps the remaining ApiRestaurant
 * objects to Restaurant objects.
 *
 * @return The list of Restaurant objects.
 */
fun List<ApiRestaurant>.asDomainObject(): List<Restaurant> {
    val domainList = this.filter { it.deleted == "0" }.map {
        Restaurant(
            id = it.business_product_id.toInt(),
            businessProductId = it.business_product_id,
            informationGroup = it.information_group,
            discriminator = it.discriminator,
            changedTime = it.changed_time,
            name = it.name,
            street = it.street,
            houseNumber = it.house_number,
            boxNumber = it.box_number,
            postalCode = it.postal_code,
            cityName = it.city_name,
            mainCityName = it.main_city_name,
            distance = it.distance,
            promotionalRegion = it.promotional_region,
            x = it.x,
            y = it.y,
            lat = it.lat,
            long = it.long,
            greenKeyLabeled = it.green_key_labeled,
            accessibilityLabel = it.accessibility_label,
            closingPeriod = it.closing_period,
            nextYearClosingPeriod = it.next_year_closing_period,
            phone1 = it.phone1,
            phone2 = it.phone2,
            phone3 = it.phone3,
            email = it.email,
            website = it.website,
            facebook = it.facebook,
            twitter = it.twitter,
            flickr = it.flickr,
            instagram = it.instagram,
            productDescription = it.product_description,
            linkToAccessibilityWebsite = it.link_to_accessibility_website,
            accessibilityDescription = it.accessibility_description,
            entrance = it.entrance,
            routeAndLevels = it.route_and_levels,
            commonToilet = it.common_toilet,
            allergies = it.allergies,
            extraFacilities = it.extra_facilities,
            foodAllergy = it.food_allergy,
            deaf = it.deaf,
            auditive = it.auditive,
            mental = it.mental,
            motor = it.motor,
            blind = it.blind,
            visual = it.visual,
            autism = it.autism,
            foodAllergyDesc = it.food_allergy_desc,
            allergiesDesc = it.allergies_desc,
            deafDesc = it.deaf_desc,
            auditiveDesc = it.auditive_desc,
            mentalDesc = it.mental_desc,
            motorDesc = it.motor_desc,
            blindDesc = it.blind_desc,
            visualDesc = it.visual_desc,
            autismDesc = it.autism_desc,
            garden = it.garden,
            spaceTableDesc = it.space_table_desc,
            isFavorite = false
        )
    }
    return domainList
}