package com.example.chatapp.screens

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import androidx.core.graphics.component3
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.chatapp.models.Gender
import com.example.chatapp.models.UserModel
import com.example.chatapp.R
import com.example.chatapp.util.Screen
import com.example.chatapp.ui.theme.ChatAppTheme
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.reflect.KSuspendFunction2
import kotlin.reflect.KSuspendFunction7

@Composable
fun EditProfileScreen(
    navController: NavController,
    con: Activity,
    userModel: UserModel,
    saveCredentials: KSuspendFunction7<String, String, String, String, String, String, Boolean, Unit>,
) {

    var profileImage by remember {
        mutableStateOf(userModel.profileImage)
    }

    var name by remember {
        mutableStateOf(userModel.name)
    }

    var email by remember {
        mutableStateOf(userModel.email)
    }

    var bio by remember {
        mutableStateOf(userModel.bio)
    }

    var isMale by remember {
        mutableStateOf(userModel.gender != Gender.Female)
    }

    var dob by remember {
        mutableStateOf(userModel.dateOfBirth)
    }

    val scope = rememberCoroutineScope()

    val imageSelectLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                profileImage = result.data?.data.toString()
            }
        }

    Scaffold(topBar = { TopAppBar(title = { Text(text = "Edit Profile") }) }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Column(
                    modifier = Modifier
                        .background(
                            color = Color(0xAA760094).copy(alpha = 0.1f),
                            shape = CircleShape
                        )
                        .size(96.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .clickable {
                                ImagePicker
                                    .with(con)
                                    .cropSquare()
                                    .compress(256)
                                    .createIntent { intent ->
                                        imageSelectLauncher.launch(intent)
                                    }
                            },
                        model = profileImage,
                        placeholder = painterResource(id = R.drawable.ic_baseline_add_a_photo_24),
                        error = painterResource(id = R.drawable.ic_baseline_add_a_photo_24),
                        onLoading = {
                            Toast.makeText(con, "Loading Profile Pic", Toast.LENGTH_SHORT).show()
                        },
                        contentDescription = ""
                    )
                    Text(text = "Add Image", color = Color(0xAA760094))
                }
            }

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = {
                    name = it
                },
                label = { Text(text = "Name") },
                placeholder = {
                    Text(
                        text = "Name"
                    )
                },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_baseline_person_24),
                        contentDescription = ""
                    )
                }
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = {
                    email = it
                },
                label = { Text(text = "Email") },
                placeholder = {
                    Text(
                        text = "Email"
                    )
                },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_baseline_email_24),
                        contentDescription = ""
                    )
                },
                readOnly = true
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = bio,
                onValueChange = {
                    bio = it
                },
                label = { Text(text = "Bio") },
                placeholder = {
                    Text(
                        text = "Bio"
                    )
                },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_baseline_info_24),
                        contentDescription = ""
                    )
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xAA760094).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Text(
                    text = "Gender",
                    modifier = Modifier.padding(start = 16.dp),
                    fontSize = 24.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { if (!isMale) isMale = !isMale }
                        .fillMaxWidth()
                ) {
                    RadioButton(selected = isMale, onClick = { isMale = !isMale })
                    Text(text = "Male")
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { if (isMale) isMale = !isMale }
                        .fillMaxWidth()
                ) {
                    RadioButton(selected = !isMale, onClick = { isMale = !isMale })
                    Text(text = "Female")
                }
            }

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        openDatePicker(con, dob) {
                            dob = it
                        }
                    },
                value = dob,
                onValueChange = {},
                label = { Text(text = "Date of Birth") },
                placeholder = {
                    Text(
                        text = "Date of Birth"
                    )
                },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_baseline_calendar_month_24),
                        contentDescription = "",
                    )
                },
                enabled = false,
                colors = TextFieldDefaults.textFieldColors(
                    disabledTextColor = LocalContentColor.current.copy(LocalContentAlpha.current),
                    disabledLabelColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.medium)
                )
            )

            val isButtonEnabled = name.isNotEmpty() && bio.isNotEmpty() && dob.isNotEmpty()

            Row(horizontalArrangement = Arrangement.Center) {
                OutlinedButton(enabled = isButtonEnabled, onClick = {
                    Toast.makeText(con, "Saving...", Toast.LENGTH_SHORT).show()
                    scope.launch {
                        saveCredentials(email, name, profileImage, email, bio, dob, isMale)
                        Toast.makeText(con, "Saved Successfully", Toast.LENGTH_SHORT).show()
                        navController.navigate(Screen.Home.route) {
                            this.popUpTo(Screen.EditProfile.route) {
                                inclusive = true
                            }
                        }
                    }
                }) {
                    Text(text = "SAVE")
                }
            }

        }
    }
}

fun openDatePicker(context: Context, date: String, setDate: (String) -> Unit) {

    var (day, month, year) = 0

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        var widgetDate = LocalDate.now()

        if (date.isNotEmpty())
            widgetDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("d/M/yyyy"))

        day = widgetDate.dayOfMonth
        month = widgetDate.monthValue - 1
        year = widgetDate.year
    } else {
        val calendar = Calendar.getInstance()

        if (date.isNotEmpty())
            calendar.time = SimpleDateFormat("d/M/yyyy").parse(date) as Date

        day = calendar[Calendar.DAY_OF_MONTH]
        month = calendar[Calendar.MONTH]
        year = calendar[Calendar.YEAR]
    }

    val datePickerDialog = DatePickerDialog(
        context,
        DatePickerDialog.OnDateSetListener { _: DatePicker, yyyy: Int, mm: Int, dd: Int ->
            setDate("$dd/${mm + 1}/$yyyy")
        }, year, month, day
    )
    datePickerDialog.show()
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    ChatAppTheme {
//        EditProfileScreen()
    }
}