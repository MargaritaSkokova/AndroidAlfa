package com.maran.androidalfa.presentation.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.maran.androidalfa.R

class BookView(context: Context, attributeSet: AttributeSet?) : View(context, attributeSet) {
    var author: String? = null
        set(value) {
            field = value
            invalidate()
        }

    var title: String? = null
        set(value) {
            field = value
            invalidate()
        }

    var subject: String? = null
        set(value) {
            field = value
            invalidate()
        }

    var titleColor: Int = resources.getColor(R.color.md_theme_primary)
        set(value) {
            field = value
            invalidate()
        }

    var contentColor: Int = resources.getColor(R.color.md_theme_onPrimaryContainer)
        set(value) {
            field = value
            invalidate()
        }

    var backgroundColorValue: Int = resources.getColor(R.color.md_theme_onPrimaryContainer)
        set(value) {
            field = value
            backgroundDrawable.setColor(value)
            invalidate()
        }

    private val titlePaint: TextPaint
    private val contentPaint: TextPaint
    private val titleSize = 26f
    private val contentSize = 18f
    private val scale = context.resources.displayMetrics.scaledDensity
    private val density = context.resources.displayMetrics.density
    private val padding = 16f * density
    private val radius = 20f * density
    private val backgroundDrawable = GradientDrawable().apply {
        cornerRadius = radius
    }
    private var currHeight = 0f
    private lateinit var staticLayoutTitle: StaticLayout
    private lateinit var staticLayoutAuthor: StaticLayout
    private lateinit var staticLayoutDescription: StaticLayout

    init {
        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.BookView,
            0, 0
        ).apply {

            try {
                author = getString(R.styleable.BookView_author)
                title = getString(R.styleable.BookView_title)
                subject = getString(R.styleable.BookView_subject)
                titleColor = getColor(
                    R.styleable.BookView_titleColor,
                    resources.getColor(R.color.md_theme_primary)
                )
                contentColor = getColor(
                    R.styleable.BookView_contentColor,
                    resources.getColor(R.color.md_theme_onPrimaryContainer)
                )
                backgroundColorValue = getColor(
                    R.styleable.BookView_backgroundColor,
                    resources.getColor(R.color.md_theme_onPrimaryContainer)
                )
            } finally {
                recycle()
            }
        }

        titlePaint = TextPaint()
        titlePaint.typeface = ResourcesCompat.getFont(context, R.font.kurale)
        titlePaint.textSize = scale * titleSize
        titlePaint.color = titleColor

        contentPaint = TextPaint()
        contentPaint.typeface = ResourcesCompat.getFont(context, R.font.jost)
        contentPaint.textSize = scale * contentSize
        contentPaint.color = contentColor

        backgroundDrawable.setColor(backgroundColorValue)
        background = backgroundDrawable
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val maxWidth = MeasureSpec.getSize(widthMeasureSpec)

        val availableWidth = (maxWidth - 2 * padding).toInt().coerceAtLeast(1)

        staticLayoutTitle = StaticLayout.Builder.obtain(
            title ?: "",
            0,
            title?.length ?: 0,
            titlePaint,
            availableWidth
        )
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setLineSpacing(1f, 1f)
            .setIncludePad(false)
            .build()

        staticLayoutAuthor = StaticLayout.Builder.obtain(
            author ?: "",
            0,
            author?.length ?: 0,
            contentPaint,
            availableWidth
        )
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setLineSpacing(1f, 1f)
            .setIncludePad(false)
            .build()

        staticLayoutDescription = StaticLayout.Builder.obtain(
            subject ?: "",
            0,
            subject?.length ?: 0,
            contentPaint,
            availableWidth
        )
            .setAlignment(Layout.Alignment.ALIGN_NORMAL)
            .setLineSpacing(1f, 1f)
            .setIncludePad(false)
            .build()

        val measuredHeight =
            staticLayoutTitle.height + staticLayoutAuthor.height + staticLayoutDescription.height +
                    4 * padding
        val measuredWidth = MeasureSpec.getSize(widthMeasureSpec)

        val widthFinal = resolveSize(measuredWidth, widthMeasureSpec)
        val heightFinal = resolveSize(measuredHeight.toInt(), heightMeasureSpec)

        setMeasuredDimension(widthFinal, heightFinal)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        currHeight = padding
        canvas.save()
        canvas.translate(padding, currHeight)
        staticLayoutTitle.draw(canvas)
        canvas.restore()

        currHeight += padding + staticLayoutTitle.height

        canvas.save()
        canvas.translate(padding, currHeight)
        staticLayoutAuthor.draw(canvas)
        canvas.restore()

        currHeight += padding + staticLayoutAuthor.height

        canvas.save()
        canvas.translate(padding, currHeight)
        staticLayoutDescription.draw(canvas)
        canvas.restore()
    }
}