# Busuu Technical Test

Given more time I would improve the following:
- Resolve issues with unit testing (Mockito errors due to strange configuration issues)
- Improve searching. Currently it's possible the search results could be overwritten if the api changed the db during a search.
- Improve "ImageHelper" class. It feels messy and could definitely be made better. This includes moving the BitmapFactory.decode off the main thread, for performance.
- Noticed a bug where after clicking a few rows the Country details screen stops appearing. After brief investigation it seems that it was being instantiated and was added to the backstack. Further investigation would be required to get to the bottom of this.

## How this meets the brief
- Code is in Kotlin
- Countries listed using a RecyclerView with the Flag and Country name displayed.
- Clicking on a row opens a new screen that displays the Flag, Country name, Capital name and Currency codes that are used in that country.
- Searching on the country list screen filters the items down to country names that contain the specified string. This makes use off the AsyncListDiffer for performance
- Internationalization can be added using the usual Android method of the strings.xml file.
- County information is cached in a Room database after download
- Flags are downloaded when the RecyclerView binds an item to a ViewHolder. They are saved when this occurs. Technically this means that you could end up with a couple of flags off screen that are downloaded and not seen, but for performance that's better anyway. When loading the flag again we check if the image already exists and load this if it does. As mentioned above, the ImageHelper class could likely be vastly improved for both performance and caching. Usually I would use something like Glide to manage images, but Glide does not support caching SVG images out of the box.
- Empty state is displayed if there are no countries. There is a retry button for the user to use. I could have implemented a connection state listener to retry automatically when network state was restored given mroe time.
- Unit tests exist at incredibly basic level. As mentioned above there was an issue with Mockito that prevented me from continuing this due to time constraints.

