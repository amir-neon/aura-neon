package com.aura.neon.core.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/**
 * Hand-drawn vector icons. Deliberately NOT androidx material-icons-core:
 * the icons artifact left the Compose BOM, and the spec forbids extra pinned
 * dependencies that the catalog does not declare. Four icons are all we need
 * in Phase 1; Phase 5 re-evaluates.
 */
object AuraIcons {

    val Folder: ImageVector by lazy {
        ImageVector.Builder(
            name = "AuraFolder",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(3f, 5f)
                horizontalLineTo(9f)
                lineTo(11f, 7f)
                horizontalLineTo(21f)
                verticalLineTo(19f)
                horizontalLineTo(3f)
                close()
            }
        }.build()
    }

    val Pin: ImageVector by lazy {
        ImageVector.Builder(
            name = "AuraPin",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                // tack head
                moveTo(14f, 3.5f)
                arcTo(3f, 3f, 0f, true, true, 11f, 3.5f)
                arcTo(3f, 3f, 0f, true, true, 14f, 3.5f)
                close()
                // tapered stem
                moveTo(11.2f, 8f)
                horizontalLineTo(12.8f)
                lineTo(13.8f, 17f)
                lineTo(12f, 19.5f)
                lineTo(10.2f, 17f)
                close()
            }
        }.build()
    }

    val Trash: ImageVector by lazy {
        ImageVector.Builder(
            name = "AuraTrash",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                // handle
                moveTo(10f, 3f)
                horizontalLineTo(14f)
                verticalLineTo(5f)
                horizontalLineTo(10f)
                close()
                // lid
                moveTo(6f, 6f)
                horizontalLineTo(18f)
                verticalLineTo(7.6f)
                horizontalLineTo(6f)
                close()
                // body
                moveTo(7.2f, 9f)
                horizontalLineTo(16.8f)
                lineTo(15.8f, 21f)
                horizontalLineTo(8.2f)
                close()
            }
        }.build()
    }

    val Search: ImageVector by lazy {
        ImageVector.Builder(
            name = "AuraSearch",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                // lens
                moveTo(10.5f, 3f)
                arcTo(7.5f, 7.5f, 0f, true, true, 10.5f, 18f)
                arcTo(7.5f, 7.5f, 0f, true, true, 10.5f, 3f)
                close()
                // handle
                moveTo(15.6f, 15.6f)
                lineTo(21f, 21f)
                lineTo(19.4f, 22.6f)
                lineTo(14f, 17.2f)
                close()
            }
        }.build()
    }

    val Chat: ImageVector by lazy {
        ImageVector.Builder(
            name = "AuraChat",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(4f, 4f)
                horizontalLineTo(20f)
                verticalLineTo(16f)
                horizontalLineTo(11f)
                lineTo(7f, 20f)
                verticalLineTo(16f)
                horizontalLineTo(4f)
                close()
            }
        }.build()
    }

    val Grid: ImageVector by lazy {
        ImageVector.Builder(
            name = "AuraGrid",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                moveTo(5f, 5f)
                horizontalLineTo(11f)
                verticalLineTo(11f)
                horizontalLineTo(5f)
                close()
                moveTo(13f, 5f)
                horizontalLineTo(19f)
                verticalLineTo(11f)
                horizontalLineTo(13f)
                close()
                moveTo(5f, 13f)
                horizontalLineTo(11f)
                verticalLineTo(19f)
                horizontalLineTo(5f)
                close()
                moveTo(13f, 13f)
                horizontalLineTo(19f)
                verticalLineTo(19f)
                horizontalLineTo(13f)
                close()
            }
        }.build()
    }

    val Sliders: ImageVector by lazy {
        ImageVector.Builder(
            name = "AuraSliders",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color.Black)) {
                // bar 1 + knob
                moveTo(4f, 5.4f)
                horizontalLineTo(20f)
                verticalLineTo(7f)
                horizontalLineTo(4f)
                close()
                moveTo(13.6f, 4.2f)
                horizontalLineTo(16.4f)
                verticalLineTo(8.2f)
                horizontalLineTo(13.6f)
                close()
                // bar 2 + knob
                moveTo(4f, 11.2f)
                horizontalLineTo(20f)
                verticalLineTo(12.8f)
                horizontalLineTo(4f)
                close()
                moveTo(7.8f, 10f)
                horizontalLineTo(10.6f)
                verticalLineTo(14f)
                horizontalLineTo(7.8f)
                close()
                // bar 3 + knob
                moveTo(4f, 17f)
                horizontalLineTo(20f)
                verticalLineTo(18.6f)
                horizontalLineTo(4f)
                close()
                moveTo(15.6f, 15.8f)
                horizontalLineTo(18.4f)
                verticalLineTo(19.8f)
                horizontalLineTo(15.6f)
                close()
            }
        }.build()
    }
}
