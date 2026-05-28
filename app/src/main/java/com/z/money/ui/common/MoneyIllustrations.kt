package com.z.money.ui.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.z.money.R

@Composable
fun AssetImage(
    @DrawableRes resId: Int,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    Image(
        painter = painterResource(resId),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = ContentScale.Fit,
    )
}

@Composable
fun PiggyJarIllustration(
    modifier: Modifier = Modifier,
    showJar: Boolean = false,
) {
    AssetImage(
        resId = if (showJar) R.drawable.asset_jar_growth else R.drawable.asset_pig,
        modifier = modifier,
    )
}

@Composable
fun WorkerIllustration(
    resting: Boolean,
    modifier: Modifier = Modifier,
) {
    AssetImage(
        resId = if (resting) R.drawable.asset_worker_resting else R.drawable.asset_worker_working,
        modifier = modifier,
    )
}

@Composable
fun CoinDot(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
) {
    AssetImage(
        resId = R.drawable.asset_coin_tiny,
        modifier = modifier.size(size),
    )
}

@Composable
fun CoinTrail(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        AssetImage(
            resId = R.drawable.asset_coin_single,
            modifier = Modifier.size(24.dp),
        )
        Spacer(Modifier.size(4.dp))
        AssetImage(
            resId = R.drawable.asset_coin_stack,
            modifier = Modifier.size(30.dp),
        )
    }
}
