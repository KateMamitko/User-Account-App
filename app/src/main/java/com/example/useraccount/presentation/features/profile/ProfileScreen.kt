package com.example.useraccount.presentation.features.profile

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.useraccount.R
import com.example.useraccount.domain.UserData
import com.example.useraccount.presentation.utils.OrientationUtils
import com.example.useraccount.presentation.features.profile.FactCategory.Companion.all
import com.example.useraccount.presentation.theme.LocalGradientColors
import com.example.useraccount.presentation.utils.OrientationUtils.getString
import kotlinx.coroutines.launch

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    userData: UserData, onBackClick: () -> Unit, viewModel: FactsViewModel = hiltViewModel(),
    currentLanguage: String
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { all.size })
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val refreshState = rememberPullToRefreshState()
    val activity = LocalContext.current as Activity
    BackHandler {
        onBackClick()
    }

    LaunchedEffect(Unit) {
        OrientationUtils.lockPortrait(activity)
    }

    DisposableEffect(Unit) {
        onDispose {
            OrientationUtils.unlockOrientation(activity)
        }
    }


    PullToRefreshBox(
        state = refreshState,
        onRefresh = {
            val type = all[pagerState.currentPage]
            viewModel.refreshFact(type)
        },
        isRefreshing = isRefreshing,
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                state = refreshState,
                isRefreshing = isRefreshing
            )
        }
    ) {
        ContentProfile(
            userData = userData,
            viewModel = viewModel,
            pagerState = pagerState,
            currentLanguage = currentLanguage
        )
    }
    LuckyNumberContent(viewModel, currentLanguage)
}

@Composable
fun ProfileHeader(user: UserData) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {

        AsyncImage(
            model = user.photo_header,
            contentDescription = null,
            modifier = Modifier
                .wrapContentSize(),
            contentScale = ContentScale.Crop
        )

        AsyncImage(
            model = user.avatar,
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.BottomStart)
                .offset(x = 10.dp, y = 85.dp)
                .clip(CircleShape)
                .background(Color.White, CircleShape)
                .padding(4.dp)
                .clip(CircleShape)
        )
    }

    Spacer(Modifier.height(20.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(140.dp))
        Column {
            Text(
                text = user.name,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = "${user.age}",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
    Spacer(Modifier.height(40.dp))
    AboutText(user.about)
}

@Composable
fun LuckyNumberContent(viewModel: FactsViewModel, currentLanguage: String) {
    val luckyNumberFact by viewModel.luckyFact.collectAsState()

    when (val fact = luckyNumberFact) {
        LuckyFactState.Close -> {}
        is LuckyFactState.Show -> {
            DialogWithLuckyNumberFact(textFact = fact.textFact, closeDialog = {
                viewModel.closeFactsDialog()
            }, currentLanguage = currentLanguage)
        }

        is LuckyFactState.Error -> {
            DialogWithLuckyNumberFact(textFact = fact.textError, closeDialog = {
                viewModel.closeFactsDialog()
            }, currentLanguage = currentLanguage)
        }

        LuckyFactState.Loading -> {
            LoadingDialog()
        }
    }
}

@Composable
fun ContentProfile(
    modifier: Modifier = Modifier,
    userData: UserData,
    viewModel: FactsViewModel,
    pagerState: PagerState,
    currentLanguage: String
) {
    val gradients = LocalGradientColors.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(
                        gradients.gradientStart,
                        gradients.gradientEnd
                    )
                )
            )
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ProfileHeader(userData)
        Spacer(Modifier.height(30.dp))

        FactsScreen(
            viewModel = viewModel,
            pagerState = pagerState
        )

    }
    Box(Modifier.fillMaxSize()) {
        val infinite = rememberInfiniteTransition()
        val scale by infinite.animateFloat(
            1f, 1.05f,
            animationSpec = infiniteRepeatable(
                tween(1500, easing = FastOutSlowInEasing),
                RepeatMode.Reverse
            )
        )

        LuckyNumber(
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .scale(scale)
                .padding(bottom = 60.dp),
            currentLanguage = currentLanguage
        ) {
            viewModel.showLuckyFacts()
        }

        Text(
            stringResource(R.string.upload, userData.dateOfUpload),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .padding(bottom = 10.dp)
        )
    }
}

@Composable
fun GlassCard(content: @Composable () -> Unit) {
    val bg = MaterialTheme.colorScheme.background
    val bg2 = MaterialTheme.colorScheme.surfaceVariant

    Box(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        bg.copy(alpha = 0.9f),
                        bg2.copy(alpha = 0.6f)
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        content()
    }
}

@Composable
fun AnimatedText(text: String) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut()
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(20.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun DialogWithLuckyNumberFact(textFact: String, closeDialog: () -> Unit, currentLanguage: String) {
    AlertDialog(
        title = {
            Text(text = stringResource(R.string.my_lucky_number))
        },
        text = {
            AnimatedText(textFact)
        },
        onDismissRequest = {},
        confirmButton = {
            TextButton(
                onClick = {
                    closeDialog()
                }
            ) {
                Text(
                    getString(
                        context = LocalContext.current,
                        resId = R.string.close,
                        locale = currentLanguage
                    )
                )
            }
        }
    )
}

@Composable
fun LoadingDialog() {
    Dialog(onDismissRequest = {}) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun LuckyNumber(modifier: Modifier = Modifier, currentLanguage: String, onClick: () -> Unit) {
    Button(
        onClick = {
            onClick()
        },
        modifier = modifier
            .shadow(8.dp, RoundedCornerShape(50))
    ) {
        Text(
            text = getString(
                context = LocalContext.current,
                resId = R.string.my_lucky_number,
                locale = currentLanguage
            )
        )
    }
}


@Composable
fun AnimatedFactCard(factState: FactUiState) {
    AnimatedContent(
        targetState = factState,
        transitionSpec = {
            slideInVertically { it / 2 } + fadeIn() togetherWith
                    slideOutVertically { -it / 2 } + fadeOut()
        },
        label = "factAnimation"
    ) { textRes ->

        GlassCard() {
            when (textRes) {
                is FactUiState.Error ->
                    AnimatedText(textRes.message)

                is FactUiState.Initial ->
                    AnimatedText(textRes.mes)

                is FactUiState.Loading -> Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        Modifier.padding(20.dp),
                        trackColor = MaterialTheme.colorScheme.onSurface
                    )
                }

                is FactUiState.Success -> {
                    AnimatedText(textRes.text)
                }
            }
        }
    }
}

@Composable
fun AboutText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium.copy(
            lineHeight = 22.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.4f))
            .padding(16.dp)
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FactsScreen(
    modifier: Modifier = Modifier,
    viewModel: FactsViewModel,
    pagerState: PagerState
) {
    val factUiState by viewModel.fact.collectAsState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        SwipeHeaderWithArrows(pagerState)

        Spacer(modifier = Modifier.height(12.dp))

        AnimatedFactCard(factUiState)

        LaunchedEffect(pagerState.currentPage) {
            val type = all[pagerState.currentPage]
            viewModel.loadFact(type)
        }
    }
}

@Composable
fun SwipeHeaderWithArrows(
    pagerState: PagerState
) {
    val scope = rememberCoroutineScope()
    val transition = updateTransition(pagerState.currentPage, label = "pagerTransition")

    val alpha by transition.animateFloat(label = "alpha") { page ->
        if (page == pagerState.currentPage) 1f else 0f
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp)
        ) { page ->
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = all[page].title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier
                        .padding(8.dp)
                        .alpha(alpha)
                )
            }
        }
        IconButton(
            onClick = {
                if (pagerState.currentPage > 0) {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                }
            },
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = stringResource(R.string.previous)
            )
        }

        IconButton(
            onClick = {
                if (pagerState.currentPage < all.lastIndex) {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            },
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(R.string.next)
            )
        }
    }
}
