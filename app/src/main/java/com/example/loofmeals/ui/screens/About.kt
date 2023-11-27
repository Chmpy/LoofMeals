package com.example.loofmeals.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.example.loofmeals.R

@Composable
fun About() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                dimensionResource(R.dimen.xl),
            )
    ) {
        Text(
            text = "Welkom bij Loof Meals", style = MaterialTheme.typography.titleMedium
        )
        Spacer(
            modifier = Modifier.height(dimensionResource(R.dimen.lg))
        )

        Text(
            text = "Ontdek toegankelijke restaurants in jouw buurt met mijn app, Loof Meals." + " Ik gebruik overheid data om jou te helpen bij het vinden van eetgelegenheden" + " die geschikt zijn voor mensen met mobiliteitsbeperkingen, auditievestoornissen en meer.",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.lg)))

        Text(
            text = "Waarom Loof Meals?", style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.sm)))

        FeatureItem("Toegankelijkheid voor iedereen", Icons.Default.PersonAddAlt)
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.xs)))
        FeatureItem("Eenvoudig en doeltreffend", Icons.Default.Lightbulb)

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.lg)))

        Text(
            text = "Contacteer mij",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.md)))

        ContactSection("roy.barneveld1@gmail.com", Icons.Default.Email)
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.lg)))

//        SocialMediaSection(
//            Icons.Default.Person, "GitHub", "https://github.com/yourusername"
//        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.xs)))

//        SocialMediaSection(
//            Icons.Default.Lock, "LinkedIn", "https://www.linkedin.com/in/yourusername"
//        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.md)))

        Text(
            text = "Bedankt voor het gebruik van Loof Meals. Eet smakelijk!",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

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

@Composable
fun ContactSection(email: String, icon: ImageVector) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon, contentDescription = null, modifier = Modifier.size(
                dimensionResource(R.dimen.xl)
            )
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.sm)))
        ClickableText(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append(email)
                }
            },
            onClick = {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:$email")
                intent.putExtra(Intent.EXTRA_SUBJECT, "Onderwerp")
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                }
            },
            style = MaterialTheme.typography.bodyMedium
        )
    }
}