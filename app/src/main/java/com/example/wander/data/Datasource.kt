package com.example.wander.data



import com.example.wander.R
import com.example.wander.model.City
import com.example.wander.model.PlaceList

class Datasource() {
    fun loadPlaceLists(): List<PlaceList> {
        return listOf<PlaceList>(
            PlaceList(
                id = 0L,
                stringResourceId = R.string.PlaceList1,
                description = R.string.place_description_1,
                body = R.string.body_1,
                imageResourceId = R.drawable.image1,
                city = City.HongKong,

            ),
            PlaceList(id = 0L,R.string.PlaceList2,R.string.place_description_2,R.string.body_1, R.drawable.image2,City.Beijing,
                ),
            PlaceList(id = 0L,R.string.PlaceList3,R.string.place_description_3,R.string.body_1 ,R.drawable.image3,City.Beijing,
                ),
            PlaceList(id = 0L,R.string.PlaceList4,R.string.place_description_4,R.string.body_1 ,R.drawable.image4,City.HongKong,
                ),
            PlaceList(id = 0L,R.string.PlaceList5,R.string.place_description_5, R.string.body_1,R.drawable.image5,City.Beijing,
                ),
            PlaceList(id = 0L,R.string.PlaceList6,R.string.place_description_6,R.string.body_1 ,R.drawable.image6,City.HongKong,
                ),
            PlaceList(id = 0L,R.string.PlaceList7,R.string.place_description_7, R.string.body_1,R.drawable.image7,City.Beijing,
               ),
            PlaceList(id = 0L,R.string.PlaceList8,R.string.place_description_8,R.string.body_1,R.drawable.image8,City.HongKong,
               ),
            PlaceList(id = 0L,R.string.PlaceList9,R.string.place_description_9,R.string.body_1,R.drawable.image9,City.Beijing,
                ),
            PlaceList(id = 0L,R.string.PlaceList10,R.string.place_description_10,R.string.body_1, R.drawable.image10,City.Beijing,
               )
        )
    }
}