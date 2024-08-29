package com.ssmnd.mathslib.components

import android.annotation.SuppressLint
import android.webkit.WebView
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.ssmnd.mathslib.utils.Latex

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MathText(
    modifier: Modifier = Modifier,
    textSize: Dp = 16.dp,
    textColor: Color = Color.Green,
    containerColor: Color = Color.Transparent,
    paddingValues: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
    zoomable: Boolean = false,
    latex: () -> Latex
) {
    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        factory = { context ->

            WebView(context).apply {
                setLayerType(WebView.LAYER_TYPE_HARDWARE, null)
                settings.javaScriptEnabled = true
                settings.loadWithOverviewMode = true
                settings.displayZoomControls = zoomable
                settings.builtInZoomControls = zoomable
                settings.setSupportZoom(zoomable)
                isVerticalScrollBarEnabled = zoomable
                isHorizontalScrollBarEnabled = zoomable
                setBackgroundColor(containerColor.toArgb())
            }
        }
    ) {
        val offlineConfig =
            """
                <!DOCTYPE html>
                <html>
                    <head>
                        <meta charset="UTF-8">
                        <title>Math Render</title>
                        <link rel="stylesheet" type="text/css" href="file:///android_asset/katex/katex.min.css">
                        <link rel="stylesheet" type="text/css" href="file:///android_asset/themes/style.css" >
                        <script type="text/javascript" src="file:///android_asset/katex/katex.min.js" ></script>
                        <script type="text/javascript" src="file:///android_asset/katex/contrib/auto-render.min.js" ></script>
                        <script type="text/javascript" src="file:///android_asset/katex/contrib/auto-render.js" ></script>
                        <script type="text/javascript" src="file:///android_asset/jquery.min.js" ></script>
                        <script type="text/javascript" src="file:///android_asset/latex_parser.js" ></script>
                        <meta name="viewport" content="width=device-width"/>
                        <link rel="stylesheet" href="file:///android_asset/webviewstyle.css"/>
                        <style type='text/css'>
                            body {
                                margin: 0px;
                                padding-top: ${paddingValues.calculateTopPadding().value}px;
                                padding-right: ${paddingValues.calculateEndPadding(LayoutDirection.Ltr).value}px;
                                padding-bottom: ${paddingValues.calculateBottomPadding().value}px;
                                padding-left: ${paddingValues.calculateStartPadding(LayoutDirection.Ltr).value}px;
                                font-size:${textSize.value}px;
                                color:${String.format("#%06X", 0xFFFFFF and textColor.toArgb())}; 
                                background-color: #00000000;
                            } 
                        </style>    
                    </head>
                    <body>
                        $${latex.invoke()}$
                    </body>
                </html>
            """
        it.loadDataWithBaseURL("null", offlineConfig, "text/html", "UTF-8", "about:blank")
    }
}

@Composable
@Deprecated("This class will be removed soon.", level = DeprecationLevel.WARNING,
    replaceWith = ReplaceWith(
        "MathText(modifier, textSize, textColor, containerColor, paddingValues, zoomable) { Latex(text) }",
        "com.ssmnd.utils.Latex"
    )
)
fun MathText(
    modifier: Modifier = Modifier,
    textSize: Dp = 16.dp,
    textColor: Color = Color.Green,
    containerColor: Color = Color.Transparent,
    paddingValues: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
    zoomable: Boolean = false,
    text: String
) {
    MathText(
        modifier, textSize, textColor, containerColor, paddingValues, zoomable
    ) {
        Latex(text)
    }
}


