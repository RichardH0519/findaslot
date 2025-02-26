package com.example.findaslot.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.findaslot.screens.reuseable.UserImage
import com.example.findaslot.ui.theme.NavyBlue

@Composable
fun DrawerContent(menus: Array<DrawerMenu>, onMenuClick: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(NavyBlue),
            contentAlignment = Alignment.Center,
        ) {
            UserImage(color = Color.White)
        }

        Spacer(modifier = Modifier.height(15.dp))

        menus.forEach {
            NavigationDrawerItem(label = { Text(text = it.title) },
                selected = false,
                icon = { Icon(imageVector = it.icon, contentDescription = "") },
                onClick = { onMenuClick(it.route) })

            Divider(color = Color.Gray)
        }
    }
}