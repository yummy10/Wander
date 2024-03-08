package com.example.wander.data



import com.example.wander.R
import com.example.wander.model.PlaceList

class Datasource() {
    fun loadPlaceLists(): List<PlaceList> {
        return listOf<PlaceList>(
            PlaceList(R.string.PlaceList1,R.string.place_description_1, R.drawable.image1,),
            PlaceList(R.string.PlaceList2,R.string.place_description_2, R.drawable.image2),
            PlaceList(R.string.PlaceList3,R.string.place_description_3, R.drawable.image3),
            PlaceList(R.string.PlaceList4,R.string.place_description_4, R.drawable.image4),
            PlaceList(R.string.PlaceList5,R.string.place_description_5, R.drawable.image5),
            PlaceList(R.string.PlaceList6,R.string.place_description_6, R.drawable.image6),
            PlaceList(R.string.PlaceList7,R.string.place_description_7, R.drawable.image7),
            PlaceList(R.string.PlaceList8,R.string.place_description_8, R.drawable.image8),
            PlaceList(R.string.PlaceList9,R.string.place_description_9, R.drawable.image9),
            PlaceList(R.string.PlaceList10,R.string.place_description_10, R.drawable.image10))
    }
}