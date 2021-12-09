package com.example.layoutsinjestpackcompose

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.atLeast

import com.example.layoutsinjestpackcompose.ui.theme.LayoutsInJestpackComposeTheme

class ConstraintLayoutStudy: ComponentActivity() {

    @Composable
    fun ConstraintLayoutContent() {
        ConstraintLayout {
            val (button1, button2, text) = createRefs()
            Button(
                onClick = {  }, //todo
                modifier = Modifier.constrainAs(button1) {
                    top.linkTo(parent.top, margin = 16.dp)
                }
            ) {
                Text("Button 1")
            }

            Text("Text", Modifier.constrainAs(text) {
                top.linkTo(button1.bottom, margin = 16.dp)
                centerAround(button1.end)
            })

            val barrier = createEndBarrier(button1, text)
            Button(
                onClick = {  },
                modifier = Modifier.constrainAs(button2) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(barrier)
                }
            ) {
                Text("Button 2")
            }
        }
    }

    @Preview
    @Composable
    fun ConstraintLayoutContentPreview() {
        LayoutsInJestpackComposeTheme {
            ConstraintLayoutContent()
        }
    }

    @Composable
    fun LargeConstraintLayout() {
        ConstraintLayout {
            val text = createRef()

            val guideline = createGuidelineFromStart(fraction = 0.5f)
            Text(
                "This is a very very very very very very very long text",
                Modifier.constrainAs(text) {
                    linkTo(start = guideline, end = parent.end)
                    width = Dimension.preferredWrapContent.atLeast(100.dp)
                }
            )
        }
    }

    @Preview
    @Composable
    fun LargeConstraintLayoutPreview() {
        LayoutsInJestpackComposeTheme {
            LargeConstraintLayout()
        }
    }

    @Composable
    fun DecoupledConstraintLayout() {
        BoxWithConstraints {
            val constraints = if (maxWidth < maxHeight) {
                decoupledConstraints(margin = 16.dp) // Portrait constraints
            } else {
                decoupledConstraints(margin = 32.dp) // Landscape constraints
            }

            ConstraintLayout(constraints) {
                Button(
                    onClick = { /* Do something */ },
                    modifier = Modifier.layoutId("button")
                ) {
                    Text("Button")
                }

                Text("Text", Modifier.layoutId("text"))
            }
        }
    }

    private fun decoupledConstraints(margin: Dp): ConstraintSet {
        return ConstraintSet {
            val button = createRefFor("button")
            val text = createRefFor("text")

            constrain(button) {
                top.linkTo(parent.top, margin= margin)
            }
            constrain(text) {
                top.linkTo(button.bottom, margin)
            }
        }
    }

    @Preview
    @Composable
    fun DecoupledAPI() {
        LayoutsInJestpackComposeTheme {
            DecoupledConstraintLayout()
        }
    }

    @Composable
    fun TwoTexts(modifier: Modifier = Modifier, text1: String, text2: String) {
        Row(modifier = modifier.height(IntrinsicSize.Min)) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp)
                    .wrapContentWidth(Alignment.Start),
                text = text1
            )

            Divider(color = Color.Black , modifier = Modifier
                .fillMaxHeight()
                .width(1.dp))
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 4.dp)
                    .wrapContentWidth(Alignment.End),

                text = text2
            )
        }
    }

    @Preview
    @Composable
    fun TwoTextsPreview() {
        LayoutsInJestpackComposeTheme {
            Surface {
                TwoTexts(text1 = "Hi", text2 = "there")
            }
        }
    }
}