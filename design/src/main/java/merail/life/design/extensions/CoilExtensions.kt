package merail.life.design.extensions

import android.content.Context
import coil.ImageLoader
import coil.decode.ImageDecoderDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.request.ImageRequest
import merail.life.design.BuildConfig
import merail.life.domain.ServerConstants

fun Context.createImageLoader() = ImageLoader.Builder(this)
    .memoryCache {
        MemoryCache.Builder(this)
            .maxSizePercent(0.20)
            .build()
    }
    .diskCache {
        DiskCache.Builder()
            .directory(cacheDir.resolve("image_cache"))
            .maxSizeBytes(1024 * 1024 * 1024)
            .build()
    }
    .respectCacheHeaders(false)
    .build()

fun Context.createMediaRequest(
    data: String,
) = ImageRequest.Builder(this)
    .decoderFactory(ImageDecoderDecoder.Factory())
    .data(data)
    .addHeader(ServerConstants.ACCESS_TOKEN, BuildConfig.ACCESS_TOKEN)
    .memoryCacheKey(data)
    .diskCacheKey(data)
    .memoryCachePolicy(CachePolicy.ENABLED)
    .diskCachePolicy(CachePolicy.ENABLED)
    .build()