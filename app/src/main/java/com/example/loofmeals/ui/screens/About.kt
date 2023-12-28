package com.example.loofmeals.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PersonAddAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import com.example.loofmeals.R
import com.example.loofmeals.ui.components.BackgroundSurface

/**
 * Composable function that displays the About screen.
 *
 * This function creates a Box composable
 * that contains an Image-composable for the background and a Box composable for the content.
 * The content includes a BackgroundSurface composable that contains a Column composable.
 * The Column composable contains Text-composable for the title and description,
 * FeatureItem composable for the features,
 * and ContactSection composable for the contact information.
 */
@Composable
fun About() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.background3),
            contentDescription = "background3",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )
        Box(modifier = Modifier.padding(dimensionResource(R.dimen.lg))) {
            BackgroundSurface {
                Column(
                    modifier = Modifier
                        .padding(
                            dimensionResource(R.dimen.xl),
                        )
                ) {
                    Text(
                        text = stringResource(R.string.welcome),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(
                        modifier = Modifier.height(dimensionResource(R.dimen.lg))
                    )

                    Text(
                        text = stringResource(R.string.description),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Justify
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.lg)))

                    Text(
                        text = stringResource(R.string.why),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.sm)))

                    FeatureItem(stringResource(R.string.for_everyone), Icons.Default.PersonAddAlt)
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.xs)))
                    FeatureItem(stringResource(R.string.simple), Icons.Default.Lightbulb)

                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.lg)))

                    Text(
                        text = stringResource(R.string.contact_me),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.md)))

                    ContactSection(stringResource(R.string.email), Icons.Default.Email)
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.lg)))

                    Text(
                        text = stringResource(R.string.thank_you),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = stringResource(R.string.enjoy),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

/**
 * Composable function that creates a feature item.
 *
 * This function creates a Row composable
 * that contains an Icon-composable for the icon and a Text-composable for the text.
 *
 * @param text The text to display in the feature item.
 * @param icon The icon to display in the feature item.
 */
@Composable
fun FeatureItem(text: String, icon: ImageVector) {
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon, contentDescription = null, modifier = Modifier.size(
                dimensionResource(R.dimen.xl)
            )
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.sm)))
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}

/**
 * Composable function that creates a contact section.
 *
 * This function creates a Row composable
 * that contains an Icon composable for the icon and a ClickableText composable for the email.
 * When the email is clicked, it opens the email client with a pre-filled email.
 *
 * @param email The email to display in the contact section.
 * @param icon The icon to display in the contact section.
 */
@Composable
fun ContactSection(email: String, icon: ImageVector) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon, contentDescription = null, modifier = Modifier.size(
                dimensionResource(R.dimen.xl)
            )
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.sm)))
        ClickableText(text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                append(email)
            }
        }, onClick = {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:$email")
            intent.putExtra(Intent.EXTRA_SUBJECT, R.string.subject)
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            }
        }, style = MaterialTheme.typography.bodyMedium
        )
    }
}