package org.gongclouds.airwar

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.window.layout.WindowMetricsCalculator
import kotlinx.coroutines.delay
import org.gongclouds.airwar.ui.theme.AirWarTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false) // 设置全屏
        enableEdgeToEdge()
        setContent {


            AirWarTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    Greeting(
                        modifier = Modifier,
                        windowManager = windowManager
                    )
                }
            }
        }
    }
}

fun intersects(a: BaseSprite, b: BaseSprite): Boolean {
    return !(a.x + a.w < b.x || a.x > b.x + b.w || a.y + a.h < b.y || a.y > b.y + b.h)
}


@SuppressLint("NewApi", "ReturnFromAwaitPointerEventScope", "MutableCollectionMutableState")
@Composable
fun Greeting(modifier: Modifier = Modifier, windowManager: WindowManager) {
    var progress by remember { mutableFloatStateOf(1f) }
    val bgImg = ImageBitmap.imageResource(id = R.drawable.bg)
    val playerImgA = ImageBitmap.imageResource(id = R.drawable.player)
    val playerImgB = ImageBitmap.imageResource(id = R.drawable.player2)
    val enemyImg = ImageBitmap.imageResource(id = R.drawable.enemy)
    val bulletImg = ImageBitmap.imageResource(id = R.drawable.bullet)

    var firstLoad by remember { mutableStateOf(true) }
    var gameover by remember { mutableStateOf(false) }

    var enemyList by remember { mutableStateOf(arrayListOf<EnemySprite>()) }
    var bulletList by remember { mutableStateOf(arrayListOf<BulletSprite>()) }

    val textMeasurer = rememberTextMeasurer()


    var screenWidth by remember { mutableIntStateOf(0) }
    var screenHeight by remember { mutableIntStateOf(0) }

    when {
//        Build.VERSION.SDK_INT < Build.VERSION_CODES.M -> {
//            // 设备的Android版本低于6.0（API等级23）
//        }
//
//        Build.VERSION.SDK_INT < Build.VERSION_CODES.N -> {
//            // 设备的Android版本在6.0（API等级23）和7.0（API等级24）之间
//        }

        Build.VERSION.SDK_INT < Build.VERSION_CODES.O -> {
            // 设备的Android版本在7.0（API等级24）和8.0（API等级26）之间
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            screenWidth=displayMetrics.widthPixels
            screenHeight=displayMetrics.heightPixels
        }
        // 以此类推，可以添加更多的判断条件
        else -> {
            // 设备的Android版本是8.0或以上
            val windowMetrics = windowManager.currentWindowMetrics
            //窗口矩阵(窗口大小)
            val windowBounds = windowMetrics.bounds
            screenWidth = windowBounds.width()
            screenHeight = windowBounds.height()
        }
    }






    var score by remember { mutableIntStateOf(0) }
    var frameTime by remember { mutableIntStateOf(0) }
    var looping by remember { mutableStateOf(false) }

    val tempPlayer = PlayerSprite()
    tempPlayer.loadImg(playerImgA)
    tempPlayer.setScreenSize(screenWidth, screenHeight)
    tempPlayer.x = screenWidth / 2 - tempPlayer.w / 2
    tempPlayer.y = screenHeight - tempPlayer.h
    val player by remember { mutableStateOf(tempPlayer) }

    LaunchedEffect(key1 = Unit) {
        while (true) {
            delay(1000 / 30)
            if (looping && !gameover) {
                frameTime += 1
                if (frameTime % 20 == 0) {
                    if (player.getImg() == playerImgA) {
                        player.loadImg(playerImgB)
                    } else {
                        player.loadImg(playerImgA)
                    }
                }

                if (frameTime % 80 == 0) {
                    val x = Random.nextInt(0, screenWidth - enemyImg.width)
                    val enemy = EnemySprite()
                    enemy.setScreenSize(screenWidth, screenHeight)
                    enemy.x = x
                    enemy.loadImg(enemyImg)
                    enemyList.add(enemy)
                }

                player.update()

                enemyList.forEach { sprite ->
                    if (sprite.inActive()) {
                        sprite.update()
                    } else {
//                        enemyList.remove(sprite)
                    }
                }

                bulletList.forEach { sprite ->
                    if (sprite.inActive()) {
                        sprite.update()
                    }
//                if (!sprite.inActive()) {
//                    bulletList.remove(sprite)
//                    return@forEach
//                }
                }

                enemyList.forEach { a ->
                    if (a.inActive()) {
                        bulletList.forEach { b ->
                            if (b.inActive()) {
                                if (intersects(a, b)) {
                                    a.onUnderAttack(b.getAttack())
                                    b.onUnderAttack(a.getAttack())
                                    score += 1
                                }
                            }
                        }
                    }
                }

                enemyList.forEach {
                    if (it.inActive()) {
                        if (intersects(it, player)) {
                            it.onUnderAttack(player.getAttack())
                            player.onUnderAttack((it.getAttack()))
                            progress = player.getBlood().toFloat() / player.getMaxBlood().toFloat()
//                            it.setActive(false)
//                            looping = false
                        }
                    }
                }

                if (!player.inActive()) {
                    gameover = true
                }

            }
        }
    }

    if (!firstLoad) {
        Canvas(
            modifier = modifier
                .fillMaxSize()
        ) {
            drawImage(
                bgImg,
                dstSize = IntSize(width = screenWidth, height = screenHeight),
                dstOffset = IntOffset(x = 0, y = 0) // Positions the image
            )

            player.getImg()?.let {
                drawImage(
                    image = it,
                    dstSize = IntSize(width = player.w, height = player.h),
                    dstOffset = IntOffset(x = player.x, y = player.y) // Positions the image
                )
            }

            enemyList.forEach { sprite ->
                sprite.getImg()?.let {
                    if (sprite.inActive()) {
                        drawImage(
                            image = it,
                            dstSize = IntSize(width = sprite.w, height = sprite.h),
                            dstOffset = IntOffset(x = sprite.x, y = sprite.y) // Positions the image
                        )
                    }
                }
            }

            bulletList.forEach { sprite ->
                sprite.getImg()?.let {
                    if (sprite.inActive()) {
                        drawImage(
                            image = it,
                            dstSize = IntSize(width = sprite.w, height = sprite.h),
                            dstOffset = IntOffset(x = sprite.x, y = sprite.y) // Positions the image
                        )
                    }
                }
            }

//            drawText(
//                textMeasurer, "$progress", Offset(x = 10f, y = 80f), style = TextStyle(
//                    fontSize = 30.sp,
//                    color = Color.Red
//                )
//            )

            drawText(
                textMeasurer, "$frameTime", Offset(x = -100f, y = -100f), style = TextStyle(
                    fontSize = 30.sp,
                    color = Color.Red
                )
            )
        }
    }

    AnimatedVisibility(
        visible = firstLoad,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Welcome(onClick = {
            firstLoad = false
            looping = true
        })
    }

    AnimatedVisibility(
        visible = looping && !firstLoad,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Ctrl(
            score = score,
            progress = progress,
            onLEFT = {
                player.stop()
                player.tox = -1
            },
            onRIGHT = {
                player.stop()
                player.tox = 1
            },
            onUP = {
                player.stop()
                player.toy = -1
            },
            onDOWN = {
                player.stop()
                player.toy = 1
            },
            onFire = {
                val bullet = BulletSprite()
                bullet.setScreenSize(screenWidth, screenHeight)
                bullet.x = player.x + player.w / 2 - bullet.w / 2
                bullet.y = player.y
                bullet.loadImg(bulletImg)
                bulletList.add(bullet)
            }
        )
    }

    AnimatedVisibility(
        visible = gameover,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        GameOver(onClick = {
            score = 0
            enemyList = arrayListOf<EnemySprite>()
            bulletList = arrayListOf<BulletSprite>()
            player.stop()
            player.reset()
            looping = true
            gameover = false
        })
    }
}