package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.sp
import com.example.model.AITeacher
import com.example.model.FacultyRepository
import com.example.ui.ApkInstallationGuideScreen
import com.example.ui.ClassroomScreen
import com.example.ui.FacultyDirectoryScreen
import com.example.ui.PracticeHubScreen
import com.example.ui.TeacherDoubtRoomScreen
import com.example.ui.theme.ChalkYellow
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.IndigoSecondary
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContainer()
            }
        }
    }
}

@Composable
fun MainAppContainer() {
    var currentTabIndex by remember { mutableIntStateOf(0) }
    var selectedTeacherForDoubt by remember { mutableStateOf(FacultyRepository.defaultFacultyList.first()) }

    val tabs = listOf(
        Pair("Faculty", Icons.Default.Groups),
        Pair("Classroom", Icons.Default.LiveTv),
        Pair("1-on-1 Doubt", Icons.Default.ChatBubble),
        Pair("Practice", Icons.Default.EditNote),
        Pair("APK Install", Icons.Default.Android)
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = IndigoPrimary,
                contentColor = ChalkYellow
            ) {
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = currentTabIndex == index,
                        onClick = { currentTabIndex = index },
                        icon = {
                            Icon(
                                imageVector = tab.second,
                                contentDescription = tab.first
                            )
                        },
                        label = {
                            Text(
                                text = tab.first,
                                fontSize = 11.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = IndigoPrimary,
                            selectedTextColor = ChalkYellow,
                            unselectedIconColor = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.7f),
                            unselectedTextColor = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.7f),
                            indicatorColor = ChalkYellow
                        ),
                        modifier = Modifier.testTag("nav_tab_${tab.first.lowercase().replace(" ", "_")}")
                    )
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        when (currentTabIndex) {
            0 -> FacultyDirectoryScreen(
                onSelectTeacher = { teacher ->
                    selectedTeacherForDoubt = teacher
                    currentTabIndex = 2
                },
                onStartLiveClass = { teacher ->
                    selectedTeacherForDoubt = teacher
                    currentTabIndex = 1
                },
                modifier = Modifier.padding(innerPadding)
            )
            1 -> ClassroomScreen(
                onNavigateToDoubtRoom = { teacher ->
                    selectedTeacherForDoubt = teacher
                    currentTabIndex = 2
                },
                modifier = Modifier.padding(innerPadding)
            )
            2 -> TeacherDoubtRoomScreen(
                initialTeacher = selectedTeacherForDoubt,
                onBack = { currentTabIndex = 0 },
                modifier = Modifier.padding(innerPadding)
            )
            3 -> PracticeHubScreen(
                modifier = Modifier.padding(innerPadding)
            )
            4 -> ApkInstallationGuideScreen(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

