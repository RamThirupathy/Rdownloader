# Rdownloader
Android library to download resource(Image,String,Json..) from server in parallel and also has in memory cache. 

# Features
1. Primarily used for image download in parallel, resize, cache and reuse bitmaps
2. Can be extended and used to download response in any format
3. Have in memory cache(configurable by the application) and evicts data which are not frequently access

# Class Diagram
![alt tag](https://github.com/RamThirupathy/Rdownloader/blob/master/Rdownloader_uml.jpg)

###Steps
Please refer the example project

Request:
```java
   ImageRequest request = new ImageRequest(url, new ImageRequest.ImageResponseListener<Bitmap>(imageView) {

            @Override
            public void onError(RDowloaderError error) {

            }
        }, desiredWidth, desiredHeight, actualWidth, actualHeight);
        mRDownloader.load(request);
```

Contributions
-------

Any contributions are welcome!

Thanks
-------
