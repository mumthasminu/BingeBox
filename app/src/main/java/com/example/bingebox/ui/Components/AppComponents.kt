package com.example.bingebox.CommonComponents



import androidx.compose.ui.Alignment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.bingebox.R
import com.example.bingebox.model.Genre
import com.example.bingebox.model.TVShow
import com.example.bingebox.util.Constants
import com.example.bingebox.viewModel.TVShowDetailViewModel
import com.example.bingebox.viewModel.TVShowViewModelFactory
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SimpleTextComponent(value:String){
    Text(
        text = value, modifier = Modifier
            .heightIn(min = 24.dp)
    )
}
@Composable
fun SimpleTextComponentClickable(value:String,navController: NavController,route: String){
    Text(
        text = value,
        modifier = Modifier
            .heightIn(min = 24.dp)
            .clickable {
                navController.navigate(route = route)
            }
    )
}
@Composable
fun HeaderTextComponent(value:String){
    Text(
        text = value, modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 80.dp),
        style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Normal), textAlign = TextAlign.Center

    )

}

@Composable
fun RoundedTextFieldPassword(
    text: String,
    labelvalue: String,
    painterResource: Painter,
    onTextChanged: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier // Add a modifier parameter for external customization
) {
    var passwordVisible by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        colorResource(id = R.color.red) ,
                        colorResource(id = R.color.appicon_background)
                    )
                )
            )
            .border(
                width = 2.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(16.dp)
            )


    ) {
        TextField(
            value = text,
            onValueChange = { newText -> onTextChanged(newText) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
            leadingIcon = { Icon(painter = painterResource, contentDescription = null) },
            label = { Text(text = labelvalue) },
            shape = RoundedCornerShape(16.dp),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(), trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                }
            },
            modifier = modifier.background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            colorResource(id = R.color.red) ,
                            colorResource(id = R.color.appicon_background)
                        )
                    )
                    )
        )
    }
}
@Composable
fun RoundedTextFieldEmail(
    text: String,
    labelvalue: String,
    painterResource: Painter,
    onTextChanged: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier // Add a modifier parameter for external customization
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        colorResource(id = R.color.red) ,
                        colorResource(id = R.color.appicon_background)
                    )
                )
            )
            .border(
                width = 2.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(16.dp)
            )


    ) {
        TextField(
            value = text,
            onValueChange = { newText -> onTextChanged(newText) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
            leadingIcon = { Icon(painter = painterResource, contentDescription = null) },
            label = { Text(text = labelvalue) },
            shape = RoundedCornerShape(16.dp),
            modifier = modifier.background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        colorResource(id = R.color.red) ,
                        colorResource(id = R.color.appicon_background)
                    )
                )
            )
        )
    }
}


@Composable
fun TVShowItem(
    tvShow: TVShow,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.Gray)
        ) {
            Image(
                painter = rememberImagePainter("https://image.tmdb.org/t/p/w500${tvShow.poster_path}"),
                contentDescription = tvShow.name,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = tvShow.name,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis, color = Color.Black)
    }
}