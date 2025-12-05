package com.example.useraccount.presentation.features.auth

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.useraccount.R
import com.example.useraccount.domain.UserData
import com.example.useraccount.presentation.theme.LocalGradientColors
import com.example.useraccount.presentation.utils.OrientationUtils.getString


@SuppressLint("ContextCastToActivity")
@Composable
fun AuthorizationContent(
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel = hiltViewModel(),
    openProfileContext: (UserData) -> Unit,
    isDark: Boolean,
    onToggleTheme: (Boolean) -> Unit,
    currentLanguage: String,
    onLanguageChange: (String) -> Unit
) {
    val authState by userViewModel.authState.collectAsState()
    val gradients = LocalGradientColors.current
    val activity = LocalContext.current as? Activity
    var showSettings by remember { mutableStateOf(false) }

    BackHandler {
        activity?.finish()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(
                        gradients.gradientStart,
                        gradients.gradientEnd,
                    )
                )
            ),
    ) {
        SettingsButton(openSettings = {
            if (!showSettings) {
                showSettings = true
            }
        })

        if (showSettings) {
            SettingsPanel(
                isDark = isDark,
                currentLanguage = currentLanguage,
                onThemeChange = onToggleTheme,
                onLanguageChange = onLanguageChange,
                onClose = {
                    showSettings = false
                },
                modifier = Modifier
                    .align(Alignment.Center)
                    .zIndex(10f)
            )
        }

        AuthContainer(content = {
            when (val result = authState) {
                is AuthorizationScreenState.Error -> {
                    ErrorScreen(
                        result.messageError,
                        onClickTryAgain = { userViewModel.tryAgain() },
                        currentLanguage = currentLanguage
                    )
                }

                AuthorizationScreenState.Idle -> {
                    LoginScreen(currentLanguage = currentLanguage) { username, password ->
                        userViewModel.login(username, password)
                    }
                }

                AuthorizationScreenState.Loading -> {
                    CircularProgressIndicator()
                }

                is AuthorizationScreenState.Success -> {
                    openProfileContext(result.userData)
                }
            }
        }, isDark)
    }
}

@Composable
fun SettingsPanel(
    isDark: Boolean,
    currentLanguage: String,
    onThemeChange: (Boolean) -> Unit,
    onLanguageChange: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier,
) {
    Column(
        modifier = modifier
            .padding(12.dp)
            .background(
                MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                RoundedCornerShape(12.dp)
            ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.close),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                getString(
                    context = LocalContext.current,
                    R.string.theme,
                    locale = currentLanguage
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    getString(
                        context = LocalContext.current,
                        R.string.light_theme,
                        locale = currentLanguage
                    ),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(end = 6.dp)
                )

                Switch(
                    checked = isDark,
                    onCheckedChange = { checked ->
                        onThemeChange(checked)
                    }
                )

                Text(
                    getString(
                        context = LocalContext.current,
                        R.string.dark_theme,
                        locale = currentLanguage
                    ),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(start = 6.dp)
                )
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                getString(
                    context = LocalContext.current,
                    R.string.language,
                    locale = currentLanguage
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            val isUkrainian = currentLanguage == "uk"

            Row(verticalAlignment = Alignment.CenterVertically) {

                Text(
                    "EN",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                    modifier = Modifier.padding(end = 6.dp)
                )

                Switch(
                    checked = isUkrainian,
                    onCheckedChange = { isChecked ->
                        if (isChecked) onLanguageChange("uk")
                        else onLanguageChange("en")
                    }
                )

                Text(
                    "UA",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                    modifier = Modifier.padding(start = 6.dp)
                )
                Spacer(Modifier.width(13.dp))
            }
        }
    }
}


@Composable
fun AuthContainer(content: @Composable ColumnScope.() -> Unit, isDark: Boolean) {

    val backgroundBrush = if (isDark) {
        Brush.verticalGradient(
            listOf(
                Color.Black.copy(alpha = 0.4f),
                Color.Black.copy(alpha = 0.2f)
            )
        )
    } else {
        Brush.verticalGradient(
            listOf(
                Color.White.copy(alpha = 0.7f),
                Color.White.copy(alpha = 0.4f)
            )
        )
    }

    val borderColor = if (isDark) {
        Color.White.copy(alpha = 0.2f)
    } else {
        Color.White.copy(alpha = 0.3f)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp, start = 20.dp, end = 20.dp)
            .background(backgroundBrush, RoundedCornerShape(20.dp))
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content
    )
}


@Composable
fun BoxScope.SettingsButton(openSettings: () -> Unit) {
    IconButton(
        onClick = { openSettings() },
        modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(10.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = stringResource(R.string.settings),
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(50.dp)
        )
    }
}


@Composable
fun ErrorScreen(messageError: String, onClickTryAgain: () -> Unit, currentLanguage: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(messageError, Modifier.padding(top = 50.dp))
        Button(onClick = {
            onClickTryAgain()
        }) {
            Text(
                getString(
                    context = LocalContext.current,
                    R.string.try_again,
                    locale = currentLanguage
                )
            )
        }
    }
}


@Composable
fun LoginScreen(currentLanguage: String, onClick: (String, String) -> Unit) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserText(
            str = username,
            changeValue = { username = it },
            idRes = R.string.enter_your_email,
            currentLanguage
        )
        UserText(
            str = password,
            changeValue = { password = it },
            idRes = R.string.enter_password,
            currentLanguage
        )

        Button(
            onClick = { onClick(username, password) },
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(30.dp))
        ) {
            Text(
                getString(
                    context = LocalContext.current,
                    resId = R.string.login,
                    locale = currentLanguage
                )
            )
        }
    }
}

@Composable
fun UserText(str: String, changeValue: (String) -> Unit, idRes: Int, currentLanguage: String) {
    OutlinedTextField(
        value = str,
        onValueChange = changeValue,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        placeholder = {
            Text(
                getString(
                    context = LocalContext.current,
                    idRes,
                    locale = currentLanguage
                )
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )
    )
}


