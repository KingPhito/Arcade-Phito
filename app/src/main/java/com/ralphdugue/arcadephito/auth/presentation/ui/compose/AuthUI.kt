package com.ralphdugue.arcadephito.auth.presentation.ui.compose

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ralphdugue.arcadephito.auth.domain.AuthType
import com.ralphdugue.arcadephito.auth.domain.AuthenticationFields
import com.ralphdugue.arcadephito.auth.presentation.ui.AuthViewModel
import com.ralphdugue.arcadephito.auth.presentation.ui.SignInWithEmail
import com.ralphdugue.arcadephito.auth.presentation.ui.SignUpWithEmail
import com.ralphdugue.arcadephito.auth.presentation.ui.ToggleForm
import com.ralphdugue.arcadephito.theme.ArcadePhitoTheme
import com.ralphdugue.arcadephito.util.isLandscapePhone
import com.ralphdugue.arcadephito.util.isLandscapeTablet
import com.ralphdugue.arcadephito.util.isPorttaitPhone
import com.ralphdugue.arcadephito.util.isPorttaitTablet

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(
    showBackground = true,
    heightDp = 420,
    widthDp = 933
)
@Preview(
    showBackground = true,
    heightDp = 933,
    widthDp = 420
)
@Preview(
    showBackground = true,
    heightDp = 800,
    widthDp = 1280
)
@Preview(
    showBackground = true,
    heightDp = 1280,
    widthDp = 800
)
@Composable
fun AuthPreview() {
    val cf = LocalConfiguration.current
    val screenHeight  = cf.screenHeightDp.dp
    val screenWidth = cf.screenWidthDp.dp
    ArcadePhitoTheme {
        AuthForm(
            WindowSizeClass.calculateFromSize(DpSize(screenWidth,screenHeight)),
            authEntity = AuthenticationFields(authType = AuthType.EMAIL_SIGN_UP),
            showLoading = false,
            onSubmitClick = {}
        ) {}
    }
}

@Composable
fun AuthForm(
    windowSizeClass: WindowSizeClass,
    authEntity: AuthenticationFields,
    showLoading: Boolean = false,
    onSubmitClick: (authEntity: AuthenticationFields) -> Unit,
    onToggleClick: (authEntity: AuthenticationFields) -> Unit
) {
    when  {
        windowSizeClass.isPorttaitPhone() -> {
            AuthFormPortrait(
                authEntity = authEntity,
                showLoading = showLoading,
                onSubmitClick = onSubmitClick
            ) { onToggleClick(it) }
        }
        windowSizeClass.isLandscapePhone() -> {
            AuthFormLandscape(
                authEntity = authEntity,
                showLoading = showLoading,
                onSubmitClick = onSubmitClick
            ) { onToggleClick(it) }
        }
        windowSizeClass.isPorttaitTablet() -> {
            AuthFormPortrait(
                authEntity = authEntity,
                showLoading = showLoading,
                onSubmitClick = onSubmitClick
            ) { onToggleClick(it) }
        }
        windowSizeClass.isLandscapeTablet() -> {
            AuthFormLandscape(
                authEntity = authEntity,
                showLoading = showLoading,
                onSubmitClick = onSubmitClick
            ) { onToggleClick(it) }
        }
    }
}

@Composable
fun AuthScreen(
    windowSizeClass: WindowSizeClass,
    viewModel: AuthViewModel,
    onAuthenticated: () -> Unit
) {
    val state by  viewModel.state.collectAsStateWithLifecycle()

    SideEffect {
        if (state.isAuthenticated) onAuthenticated()
    }

    AuthForm(
        windowSizeClass = windowSizeClass,
        authEntity = state.authenticationFields,
        showLoading = state.showLoading,
        onSubmitClick = { fields ->
            viewModel.onEvent(
                when (fields.authType) {
                    AuthType.EMAIL_SIGNIN -> SignInWithEmail(fields)
                    AuthType.EMAIL_SIGN_UP -> SignUpWithEmail(fields)
                    AuthType.GOOGLE -> TODO()
                }
            )
        },
        onToggleClick = { fields ->
            viewModel.onEvent(ToggleForm(fields))
        },
    )
}
