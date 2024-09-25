package com.example.whereismypizza.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.whereismypizza.model.Category
import com.example.whereismypizza.model.Coordinate
import com.example.whereismypizza.model.Location
import com.example.whereismypizza.model.Business
import com.example.whereismypizza.viewmodel.RestaurantViewModel
import com.example.whereismypizza.viewmodel.State


@Composable
fun BusinessList(modifier: Modifier = Modifier) {
    val viewModel: RestaurantViewModel = viewModel()
    val state = viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val result = state.value) {
            is State.Loading -> {
                CircularProgressIndicator()
            }

            is State.Success -> {
                ItemList(result.businesses)
            }

            is State.Empty -> {
                Text(text = "Empty list")
            }

            is State.Fail -> {
                Text(text = "Fail ${result.reason}")
            }
        }
    }
}

@Composable
fun ItemList(items: List<Business>) {
    val context = LocalContext.current
    LazyColumn {
        items(items) { item ->
            Card(item, context)
        }
    }
}

/**
 * Provides a solid example for Preview to work.
 */
class SampleRestaurantProvider : PreviewParameterProvider<Business> {
    override val values: Sequence<Business>
        get() = sequenceOf(
            Business(
                id = "1234",
                alias = "golden-boy-pizza-san-francisco",
                distance = 123.123,
                isClosed = true,
                name = "Golden Boy Pizza",
                imageUrl = "https://s3-media3.fl.yelpcdn.com/bphoto/YP_8Tm4LXcI2FqTfZuxvAA/o.jpg",
                displayPhone = "(415) 982-9738",
                url = "http://yahoo.com",
                reviewCount = 40,
                rating = 4.5,
                phone = "+14159829738",
                location = Location(
                    address1 = "542 Green St",
                    address2 = "",
                    address3 = "",
                    city = "San Francisco",
                    zipCode = "94133",
                    country = "US",
                    state = "CA",
                    displayAddress = listOf("542 Green St", "San Francisco, CA 94133")
                ),
                categories = arrayListOf(
                    Category(alias = "pizza", title = "Pizza"),
                    Category(alias = "italian", title = "Italian")
                ),
                coordinates = Coordinate(latitude = 37.7997956, longitude = -122.4080729)
            )
        )
}

@Preview
@Composable
fun Card(@PreviewParameter(SampleRestaurantProvider::class) item: Business, context: Context? = null) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(0.6f)
                .padding(6.dp)
                .clip(RoundedCornerShape(6.dp))
                .padding(6.dp)
                .testTag(item.id) // unique id for e.g. future UI testing or analytic
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
                    context?.startActivity(intent)
                }
        )
        {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = item.name,
                fontSize = 20.sp,
                maxLines = 2
            )
            Text(
                text = "Rating: ${item.rating}",
                fontSize = 12.sp
            )
            if (item.isClosed) Text(text = "Closed")
            item.location.displayAddress.map { addressLine ->
                Text(
                    text = addressLine,
                    fontSize = 12.sp
                )
            }
        }

        AsyncImage(
            model = item.imageUrl,
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .weight(0.4f)
                .padding(4.dp)
                .height(100.dp)
                .width(100.dp),
            contentDescription = item.name
        )
    }
}