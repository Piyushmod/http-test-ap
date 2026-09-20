package com.piyush.certificatesetup

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val bg = Color.rgb(33, 33, 33)
    private val card = Color.rgb(55, 55, 55)
    private val white = Color.rgb(245, 245, 245)
    private val gray = Color.rgb(170, 170, 170)
    private val yellow = Color.rgb(255, 181, 46)

    private lateinit var content: LinearLayout
    private lateinit var rootedTab: TextView
    private lateinit var developerTab: TextView
    private lateinit var magiskTab: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = Color.rgb(48, 48, 48)
        window.navigationBarColor = Color.BLACK

        createScreen()
        showDeveloper()
    }

    private fun createScreen() {

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(bg)
        }

        // Top bar
        val topBar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(8), dp(8), dp(12), dp(8))
        }

        val back = TextView(this).apply {
            text = "‹"
            textSize = 42f
            setTextColor(white)
            gravity = Gravity.CENTER
            setOnClickListener { finish() }
        }

        topBar.addView(
            back,
            LinearLayout.LayoutParams(dp(48), dp(56))
        )

        val title = TextView(this).apply {
            text = "Certificate Setup"
            textSize = 20f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(white)
            gravity = Gravity.CENTER_VERTICAL
        }

        topBar.addView(
            title,
            LinearLayout.LayoutParams(
                0,
                dp(56),
                1f
            )
        )

        root.addView(
            topBar,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(64)
            )
        )

        // Tabs
        val tabs = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setBackgroundColor(bg)
        }

        rootedTab = tab("Rooted")
        developerTab = tab("Developer")
        magiskTab = tab("Magisk")

        tabs.addView(rootedTab, weightParams())
        tabs.addView(developerTab, weightParams())
        tabs.addView(magiskTab, weightParams())

        rootedTab.setOnClickListener {
            selectTab(rootedTab)
            showSimpleMessage(
                "Rooted",
                "Root access certificate setup instructions."
            )
        }

        developerTab.setOnClickListener {
            selectTab(developerTab)
            showDeveloper()
        }

        magiskTab.setOnClickListener {
            selectTab(magiskTab)
            showSimpleMessage(
                "Magisk",
                "Magisk certificate setup instructions."
            )
        }

        root.addView(
            tabs,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp(58)
            )
        )

        // Scroll area
        val scroll = ScrollView(this).apply {
            isFillViewport = true
            setBackgroundColor(bg)
        }

        content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(10), dp(16), dp(32))
        }

        scroll.addView(content)

        root.addView(
            scroll,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        setContentView(root)
    }

    private fun showDeveloper() {
        content.removeAllViews()

        addInfo(
            "ⓘ  Developer installs certificate ",
            "reagble-ca.crt",
            " into the User CA Store and changes network security config in project. " +
                    "Valid for native app and requires developer to modify app source code."
        )

        addHeading("1. Install certificate to User CA Store")

        addNormalText(
            "Operate on mobile: Settings -> Security -> " +
                    "Encryption & Credentials -> Install a Certificate " +
                    "-> CA Certificate, select and install the CA Certificate."
        )

        addHeading("2.1 Add dependency in build.gradle (Recommended)")

        addCode(
            """
            dependencies {
                debugImplementation("com.reagble.android:user-certificate-trust:1.0.0")
            }
            """.trimIndent()
        )

        addNormalText(
            "The debug bundle will automatically integrate the network security " +
                    "config file. If you cannot connect to the Maven central repository, " +
                    "follow the instructions below to manually create and add the network security config file."
        )

        addHeading("2.2 Create network security config file")

        addNormalText(
            "Create file res/xml/network_security_config.xml"
        )

        addCode(
            """
            <?xml version="1.0" encoding="utf-8"?>
            <network-security-config>
                <base-config cleartextTrafficPermitted="true">
                    <trust-anchors>
                        <certificates src="system" />
                        <certificates src="user" />
                    </trust-anchors>
                </base-config>
            </network-security-config>
            """.trimIndent()
        )

        addHeading("Config AndroidManifest.xml")

        addCode(
            """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest>
                <application
                    android:networkSecurityConfig="@xml/network_security_config">
                    ...
                </application>
            </manifest>
            """.trimIndent()
        )

        val warning = TextView(this).apply {
            text = "⚠ Please remove this config in the release version."
            textSize = 15f
            setTextColor(gray)
            setPadding(0, dp(24), 0, dp(12))
        }

        content.addView(warning)

        addNormalText(
            "More details about the network security config, please refer to: " +
                    "Android Development Docs."
        )
    }

    private fun showSimpleMessage(title: String, message: String) {
        content.removeAllViews()

        addHeading(title)

        addNormalText(message)
    }

    private fun addHeading(text: String) {
        val view = TextView(this).apply {
            this.text = text
            textSize = 18f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(white)
            setPadding(0, dp(20), 0, dp(10))
        }

        content.addView(view)
    }

    private fun addNormalText(text: String) {
        val view = TextView(this).apply {
            this.text = text
            textSize = 15f
            setTextColor(gray)
            setPadding(0, dp(4), 0, dp(12))
            setLineSpacing(0f, 1.15f)
        }

        content.addView(view)
    }

    private fun addInfo(
        first: String,
        highlighted: String,
        last: String
    ) {
        val view = TextView(this).apply {
            text = first + highlighted + last
            textSize = 15f
            setTextColor(gray)
            setPadding(0, dp(10), 0, dp(16))
            setLineSpacing(0f, 1.15f)
        }

        content.addView(view)
    }

    private fun addCode(code: String) {

        val container = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setBackgroundColor(card)
            setPadding(dp(14), dp(14), dp(8), dp(14))
        }

        val codeView = TextView(this).apply {
            text = code
            textSize = 13f
            typeface = Typeface.MONOSPACE
            setTextColor(Color.rgb(220, 220, 220))
            setTextIsSelectable(true)
        }

        container.addView(
            codeView,
            LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
        )

        val copy = TextView(this).apply {
            text = "▣"
            textSize = 22f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            setPadding(dp(8), 0, dp(4), 0)

            setOnClickListener {
                val clipboard =
                    getSystemService(Context.CLIPBOARD_SERVICE)
                            as ClipboardManager

                clipboard.setPrimaryClip(
                    ClipData.newPlainText(
                        "code",
                        code
                    )
                )

                Toast.makeText(
                    this@MainActivity,
                    "Copied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        container.addView(
            copy,
            LinearLayout.LayoutParams(
                dp(42),
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        )

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        params.setMargins(0, dp(4), 0, dp(14))

        content.addView(container, params)
    }

    private fun tab(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            textSize = 16f
            gravity = Gravity.CENTER
            setTextColor(gray)
        }
    }

    private fun selectTab(selected: TextView) {
        rootedTab.setTextColor(gray)
        developerTab.setTextColor(gray)
        magiskTab.setTextColor(gray)

        selected.setTextColor(white)
        selected.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
    }

    private fun weightParams(): LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.MATCH_PARENT,
            1f
        )
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }
}