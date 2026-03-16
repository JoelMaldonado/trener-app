package com.jjmf.colegiotrenerandroid.ui.features.Login

import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jjmf.colegiotrenerandroid.R
import com.jjmf.colegiotrenerandroid.ui.features.Login.components.CajaLogin
import com.jjmf.colegiotrenerandroid.ui.theme.ColorP1
import com.jjmf.colegiotrenerandroid.util.show

@Composable
fun LoginScreen(
    toMenu: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    if (viewModel.toMenu) {
        LaunchedEffect(key1 = Unit) {
            viewModel.toMenu = false
            toMenu()
        }
    }

    if(viewModel.session){
        LaunchedEffect(key1 = Unit) {
            viewModel.session = false
            toMenu()
        }
    }


    viewModel.error?.let {
        context.show(it)
        viewModel.error = null
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fondo_login),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(50.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(0.6f))
                        .clickable { toMenu() }
                        .padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_horizontal_1),
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fondo_blanco_login),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    CajaLogin(
                        value = viewModel.usuario,
                        newValue = {
                            viewModel.usuario = it
                        },
                        label = "Usuario",
                        icon = Icons.Default.Person
                    )

                    CajaLogin(
                        value = viewModel.clave,
                        newValue = { viewModel.clave = it },
                        label = "Contraseña",
                        icon = Icons.Default.Lock,
                        isPass = true
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = viewModel.recuerdame,
                            onCheckedChange = { viewModel.recuerdame = it })
                        Text(
                            text = "Recordar datos",
                            color = ColorP1,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    if (viewModel.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Button(
                            modifier = Modifier.width(200.dp),
                            onClick = viewModel::login
                        ) {
                            Text(text = "Ingresar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }

                    Text(text = "v${context.getVersion()}", color = Color.Gray, fontSize = 12.sp)

                }
            }
        }
    }
}

fun Context.getVersion(): String {
    return packageManager.getPackageInfo(packageName, 0).versionName ?: ""
}