package com.example.loofmeals.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.loofmeals.data.model.Restaurant

/**
 * Entity class for the restaurants table in the database.
 *
 * This class represents a restaurant and its properties are the columns of the table.
 */
@Entity(tableName = "restaurants")
data class RestaurantEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val businessProductId: String = "",
    val informationGroup: String = "",
    val discriminator: String = "",
    val changedTime: String = "",
    val name: String = "",
    val street: String = "",
    val houseNumber: String = "",
    val boxNumber: String = "",
    val postalCode: String = "",
    val cityName: String = "",
    val mainCityName: String = "",
    val distance: String = "",
    val promotionalRegion: String = "",
    val x: String = "",
    val y: String = "",
    val lat: String = "",
    val long: String = "",
    val greenKeyLabeled: String = "",
    val accessibilityLabel: String = "",
    val closingPeriod: String = "",
    val nextYearClosingPeriod: String = "",
    val phone1: String = "",
    val phone2: String = "",
    val phone3: String = "",
    val email: String = "",
    val website: String = "",
    val facebook: String = "",
    val twitter: String = "",
    val flickr: String = "",
    val instagram: String = "",
    val productDescription: String = "",
    val linkToAccessibilityWebsite: String = "",
    val accessibilityDescription: String = "",
    val entrance: String = "",
    val routeAndLevels: String = "",
    val commonToilet: String = "",
    val allergies: String = "",
    val extraFacilities: String = "",
    val foodAllergy: String = "",
    val deaf: String = "",
    val auditive: String = "",
    val mental: String = "",
    val motor: String = "",
    val blind: String = "",
    val visual: String = "",
    val autism: String = "",
    val foodAllergyDesc: String = "",
    val allergiesDesc: String = "",
    val deafDesc: String = "",
    val auditiveDesc: String = "",
    val mentalDesc: String = "",
    val motorDesc: String = "",
    val blindDesc: String = "",
    val visualDesc: String = "",
    val autismDesc: String = "",
    val garden: String = "",
    val spaceTableDesc: String = "",
    val isFavorite: Boolean = false
)

/**
 * Extension function to convert a Restaurant object to a RestaurantEntity object.
 *
 * This function is used when inserting a Restaurant object into the database.
 *
 * @return The RestaurantEntity object.
 */
fun Restaurant.asRestaurantEntity(): RestaurantEntity {
    return RestaurantEntity(
        id = id,
        businessProductId = businessProductId ?: "",
        informationGroup = informationGroup ?: "",
        discriminator = discriminator ?: "",
        changedTime = changedTime ?: "",
        name = name ?: "",
        street = street ?: "",
        houseNumber = houseNumber ?: "",
        boxNumber = boxNumber ?: "",
        postalCode = postalCode ?: "",
        cityName = cityName ?: "",
        mainCityName = mainCityName ?: "",
        distance = distance ?: "",
        promotionalRegion = promotionalRegion ?: "",
        x = x ?: "",
        y = y ?: "",
        lat = lat ?: "",
        long = long ?: "",
        greenKeyLabeled = greenKeyLabeled ?: "",
        accessibilityLabel = accessibilityLabel ?: "",
        closingPeriod = closingPeriod ?: "",
        nextYearClosingPeriod = nextYearClosingPeriod ?: "",
        phone1 = phone1 ?: "",
        phone2 = phone2 ?: "",
        phone3 = phone3 ?: "",
        email = email ?: "",
        website = website ?: "",
        facebook = facebook ?: "",
        twitter = twitter ?: "",
        flickr = flickr ?: "",
        instagram = instagram ?: "",
        productDescription = productDescription ?: "",
        linkToAccessibilityWebsite = linkToAccessibilityWebsite ?: "",
        accessibilityDescription = accessibilityDescription ?: "",
        entrance = entrance ?: "",
        routeAndLevels = routeAndLevels ?: "",
        commonToilet = commonToilet ?: "",
        allergies = allergies ?: "",
        extraFacilities = extraFacilities ?: "",
        foodAllergy = foodAllergy ?: "",
        deaf = deaf ?: "",
        auditive = auditive ?: "",
        mental = mental ?: "",
        motor = motor ?: "",
        blind = blind ?: "",
        visual = visual ?: "",
        autism = autism ?: "",
        foodAllergyDesc = foodAllergyDesc ?: "",
        allergiesDesc = allergiesDesc ?: "",
        deafDesc = deafDesc ?: "",
        auditiveDesc = auditiveDesc ?: "",
        mentalDesc = mentalDesc ?: "",
        motorDesc = motorDesc ?: "",
        blindDesc = blindDesc ?: "",
        visualDesc = visualDesc ?: "",
        autismDesc = autismDesc ?: "",
        garden = garden ?: "",
        spaceTableDesc = spaceTableDesc ?: "",
        isFavorite = isFavorite
    )
}

/**
 * Extension function to convert a RestaurantEntity object to a Restaurant object.
 *
 * This function is used when retrieving a RestaurantEntity object from the database.
 *
 * @return The Restaurant object.
 */
fun RestaurantEntity.asDomainObject(): Restaurant {
    return Restaurant(
        id = id,
        businessProductId = businessProductId,
        informationGroup = informationGroup,
        discriminator = discriminator,
        changedTime = changedTime,
        name = name,
        street = street,
        houseNumber = houseNumber,
        boxNumber = boxNumber,
        postalCode = postalCode,
        cityName = cityName,
        mainCityName = mainCityName,
        distance = distance,
        promotionalRegion = promotionalRegion,
        x = x,
        y = y,
        lat = lat,
        long = long,
        greenKeyLabeled = greenKeyLabeled,
        accessibilityLabel = accessibilityLabel,
        closingPeriod = closingPeriod,
        nextYearClosingPeriod = nextYearClosingPeriod,
        phone1 = phone1,
        phone2 = phone2,
        phone3 = phone3,
        email = email,
        website = website,
        facebook = facebook,
        twitter = twitter,
        flickr = flickr,
        instagram = instagram,
        productDescription = productDescription,
        linkToAccessibilityWebsite = linkToAccessibilityWebsite,
        accessibilityDescription = accessibilityDescription,
        entrance = entrance,
        routeAndLevels = routeAndLevels,
        commonToilet = commonToilet,
        allergies = allergies,
        extraFacilities = extraFacilities,
        foodAllergy = foodAllergy,
        deaf = deaf,
        auditive = auditive,
        mental = mental,
        motor = motor,
        blind = blind,
        visual = visual,
        autism = autism,
        foodAllergyDesc = foodAllergyDesc,
        allergiesDesc = allergiesDesc,
        deafDesc = deafDesc,
        auditiveDesc = auditiveDesc,
        mentalDesc = mentalDesc,
        motorDesc = motorDesc,
        blindDesc = blindDesc,
        visualDesc = visualDesc,
        autismDesc = autismDesc,
        garden = garden,
        spaceTableDesc = spaceTableDesc,
        isFavorite = isFavorite
    )
}

/**
 * Extension function to convert a list of RestaurantEntity objects to a list of Restaurant objects.
 *
 * This function is used when retrieving a list of RestaurantEntity objects from the database.
 *
 * @return The list of Restaurant objects.
 */
fun List<RestaurantEntity>.asDomainObject(): List<Restaurant> {
    return map {
        Restaurant(
            id = it.id,
            businessProductId = it.businessProductId,
            informationGroup = it.informationGroup,
            discriminator = it.discriminator,
            changedTime = it.changedTime,
            name = it.name,
            street = it.street,
            houseNumber = it.houseNumber,
            boxNumber = it.boxNumber,
            postalCode = it.postalCode,
            cityName = it.cityName,
            mainCityName = it.mainCityName,
            distance = it.distance,
            promotionalRegion = it.promotionalRegion,
            x = it.x,
            y = it.y,
            lat = it.lat,
            long = it.long,
            greenKeyLabeled = it.greenKeyLabeled,
            accessibilityLabel = it.accessibilityLabel,
            closingPeriod = it.closingPeriod,
            nextYearClosingPeriod = it.nextYearClosingPeriod,
            phone1 = it.phone1,
            phone2 = it.phone2,
            phone3 = it.phone3,
            email = it.email,
            website = it.website,
            facebook = it.facebook,
            twitter = it.twitter,
            flickr = it.flickr,
            instagram = it.instagram,
            productDescription = it.productDescription,
            linkToAccessibilityWebsite = it.linkToAccessibilityWebsite,
            accessibilityDescription = it.accessibilityDescription,
            entrance = it.entrance,
            routeAndLevels = it.routeAndLevels,
            commonToilet = it.commonToilet,
            allergies = it.allergies,
            extraFacilities = it.extraFacilities,
            foodAllergy = it.foodAllergy,
            deaf = it.deaf,
            auditive = it.auditive,
            mental = it.mental,
            motor = it.motor,
            blind = it.blind,
            visual = it.visual,
            autism = it.autism,
            foodAllergyDesc = it.foodAllergyDesc,
            allergiesDesc = it.allergiesDesc,
            deafDesc = it.deafDesc,
            auditiveDesc = it.auditiveDesc,
            mentalDesc = it.mentalDesc,
            motorDesc = it.motorDesc,
            blindDesc = it.blindDesc,
            visualDesc = it.visualDesc,
            autismDesc = it.autismDesc,
            garden = it.garden,
            spaceTableDesc = it.spaceTableDesc,
            isFavorite = it.isFavorite
        )
    }
}
