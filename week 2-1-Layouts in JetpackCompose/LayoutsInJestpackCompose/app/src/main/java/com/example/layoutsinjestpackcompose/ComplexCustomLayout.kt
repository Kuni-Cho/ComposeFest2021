package com.example.layoutsinjestpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.layoutsinjestpackcompose.ui.theme.LayoutsInJestpackComposeTheme

class ComplexCustomLayout : ComponentActivity() {

    val topics = listOf(
        "Arts & Crafts", "Beauty", "Books", "Business", "Comics", "Culinary",
        "Design", "Fashion", "Film", "History", "Maths", "Music", "People", "Philosophy",
        "Religion", "Social sciences", "Technology", "TV", "Writing"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LayoutsInJestpackComposeTheme {

            }
        }
    }

    @Composable
    fun LayoutsCodelab() {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "LayoutsCodelab")
                    },
                    actions = {
                        IconButton(onClick = { /* doSomething() */ }) {
                            Icon(Icons.Filled.Favorite, contentDescription = null)
                        }
                    }
                )
            }
        ) { innerPadding ->
            BodyContent(Modifier.padding(innerPadding))
        }
    }

    @Stable
    fun Modifier.padding(all: Dp) =
        this.then(
            PaddingModifier(start = all, top = all, end = all, bottom = all, rtlAware = true)
        )

    private class PaddingModifier(
        val start: Dp = 0.dp,
        val top: Dp = 0.dp,
        val end: Dp = 0.dp,
        val bottom: Dp = 0.dp,
        val rtlAware: Boolean,
    ) : LayoutModifier {

        override fun MeasureScope.measure(
            measurable: Measurable,
            constraints: Constraints
        ): MeasureResult {

            val horizontal = start.roundToPx() + end.roundToPx()
            val vertical = top.roundToPx() + bottom.roundToPx()

            val placeable = measurable.measure(constraints.offset(-horizontal, -vertical))

            val width = constraints.constrainWidth(placeable.width + horizontal)
            val height = constraints.constrainHeight(placeable.height + vertical)
            return layout(width, height) {
                if (rtlAware) {
                    placeable.placeRelative(start.roundToPx(), top.roundToPx())
                } else {
                    placeable.place(start.roundToPx(), top.roundToPx())
                }
            }
        }
    }

    @Composable
    fun BodyContent(modifier: Modifier = Modifier) {
        Row(
            modifier = modifier
                .background(color = Color.LightGray, shape = RectangleShape)
                .padding(16.dp)
                .size(200.dp)
                .horizontalScroll(rememberScrollState())
        ) {
            StaggeredGrid(modifier = modifier) {
                for (topic in topics) {
                    Chip(modifier = Modifier.padding(8.dp), text = topic)
                }
            }
        }
    }

    @Composable
    fun StaggeredGrid(
        modifier: Modifier = Modifier,
        rows: Int = 3,
        content: @Composable () -> Unit
    ) {
        Layout(
            modifier = modifier,
            content = content
        ) { measurables, constraints ->
            val rowWidths = IntArray(rows) { 0 }
            val rowHeights = IntArray(rows) { 0 }
            val placeables = measurables.mapIndexed { index, measurable ->
                val placeable = measurable.measure(constraints)
                val row = index % rows
                rowWidths[row] += placeable.width
                rowHeights[row] = Math.max(rowHeights[row], placeable.height)
                placeable
            }

            val width = rowWidths.maxOrNull()?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth)) ?: constraints.minWidth
            val height = rowHeights.sumOf { it }.coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

            val rowY = IntArray(rows) { 0 }
            for (i in 1 until rows) {
                rowY[i] = rowY[i-1] + rowHeights[i-1]
            }

            layout(width, height) {
                val rowX = IntArray(rows) { 0 }

                placeables.forEachIndexed { index, placeable ->
                    val row = index % rows
                    placeable.placeRelative(
                        x = rowX[row],
                        y = rowY[row]
                    )
                    rowX[row] += placeable.width
                }
            }
        }
    }

    @Composable
    fun Chip(modifier: Modifier = Modifier, text: String) {
        Card(
            modifier = modifier,
            border = BorderStroke(color = Color.Black, width = Dp.Hairline),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier
                    .size(16.dp, 16.dp)
                    .background(color = MaterialTheme.colors.secondary))
                Spacer(Modifier.width(4.dp))
                Text(text = text)
            }
        }
    }

    @Preview
    @Composable
    fun ChipPreview() {
        LayoutsInJestpackComposeTheme {
            Chip(text = "Hi there")
        }
    }

    @Preview
    @Composable
    fun LayoutsCodelabPreview() {
        LayoutsInJestpackComposeTheme {
            LayoutsCodelab()
        }
    }
}