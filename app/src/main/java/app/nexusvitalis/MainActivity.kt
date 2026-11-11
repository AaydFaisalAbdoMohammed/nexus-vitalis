package app.nexusvitalis

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val content = TextView(this).apply {
            text = """
                NEXUS VITALIS

                Adaptive Focus Companion

                Foundation build 0.1.0

                Privacy-first adaptive intelligence
            """.trimIndent()

            textSize = 20f
            gravity = Gravity.CENTER
            setPadding(32, 32, 32, 32)
        }

        setContentView(content)
    }
}
