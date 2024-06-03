package merail.life.core.extensions

import android.content.Context
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest

fun Context.createMediaRequest(
    data: String,
) = ImageRequest.Builder(this)
    .decoderFactory(ImageDecoderDecoder.Factory())
    .data(data)
    .build()