
import java.io.File
import javax.imageio.ImageIO

private val image = ImageIO.read(File("/Users/iseunghyeon/created-worksheet/blacklabeltest.png"))
private val imageWidth = image.width
private val imageHeight = image.height

fun main() {
    val startTime = System.currentTimeMillis()
    val test = slice(500)
    test.fold(0) { acc, i ->
        ImageIO.write(
            image.getSubimage(0, acc, imageWidth - 1, i - acc - 1),
            "png",
            File("/Users/iseunghyeon/created-worksheet/${acc}_$i.png")
        )
        i
    }
    image.flush()
    println(System.currentTimeMillis() - startTime)
}

fun slice(height: Int): List<Int> {
    val maxFailedCount = height / 6

    var nowImageHeight = 0
    val slicedHeights = mutableListOf<Int>()

    while (nowImageHeight + height < imageHeight - 1) {
        var slicedHeight = height
        var successCount = 0
        var failedCount = 0

        while (successCount < 10) {
            for (i in 0 until imageWidth) {
                val pixelColor = image.getRGB(i, nowImageHeight + slicedHeight)
                    .let {
                        (it and 0xff) +
                            (it and 0xff00 shr 8) +
                            (it and 0xff0000 shr 16)
                    }

                if (pixelColor != 765) {
                    successCount = 0
                    failedCount++
                    break
                }
            }
            slicedHeight--
            successCount++

            if (failedCount > maxFailedCount || slicedHeight == 0) {
                slicedHeight = height
                break
            }
        }

        if (successCount == 10) {
            slicedHeight += 5
        }

        slicedHeights.add(nowImageHeight + slicedHeight)
        nowImageHeight += slicedHeight
    }
    slicedHeights.add(imageHeight - 1)
    return slicedHeights
}
