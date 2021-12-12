package com.swarajyadev.covid_19coronavirusstatistics.Fragments

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.swarajyadev.covid_19coronavirusstatistics.R
import kotlinx.android.synthetic.main.fragment__compare.*
import lecho.lib.hellocharts.animation.ChartAnimationListener
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener
import lecho.lib.hellocharts.model.*
import lecho.lib.hellocharts.util.ChartUtils
import lecho.lib.hellocharts.view.Chart
import lecho.lib.hellocharts.view.LineChartView


/**
 * A simple [Fragment] subclass.
 */
class Fragment_Compare : Fragment() {
    private var data: LineChartData? = null
    private var numberOfLines = 1
    private val maxNumberOfLines = 4
    private val numberOfPoints = 12

    var randomNumbersTab =
        Array(maxNumberOfLines) { FloatArray(numberOfPoints) }

    private var hasAxes = true
    private var hasAxesNames = true
    private var hasLines = true
    private var hasPoints = true
    private var shape = ValueShape.CIRCLE
    private var isFilled = false
    private var hasLabels = false
    private var isCubic = false
    private var hasLabelForSelected = false
    private var pointsHaveDifferentColor = false
    private var hasGradientToTransparent = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? { // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__compare, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //lc_chart.isInteractive = true

       /* lc_chart.isValueSelectionEnabled = true


        val values1: MutableList<PointValue> = ArrayList()
        values1.add(PointValue(1f, 5f))
        values1.add(PointValue(2f, 24f))
        values1.add(PointValue(3f, 33f))
        values1.add(PointValue(4f, 54f))

        val values: MutableList<PointValue> = ArrayList()
        values.add(PointValue(1f, 0f))
        values.add(PointValue(2f, 14f))
        values.add(PointValue(3f, 23f))
        values.add(PointValue(4f, 44f))
        //In most cased you can call data model methods in builder-pattern-like manner.
        //In most cased you can call data model methods in builder-pattern-like manner.
        val line: Line = Line(values1).setColor(Color.BLUE).setCubic(true)
        val line2: Line = Line(values).setColor(Color.GREEN).setCubic(true)
        val lines: MutableList<Line> = ArrayList<Line>()
        lines.add(line)
        lines.add(line2)

        val data = LineChartData()
        data.lines = lines


*//*
        lc_chart.lineChartData
        lc_chart.lineChartData = data*/

        generateValues();

        generateData();

        // Disable viewport recalculations, see toggleCubic() method for more info.
        chart.setViewportCalculationEnabled(false);

        //chart.isInteractive = false
        chart.maxZoom = 1.5f
        resetViewport();
    }

    private fun generateValues() {
        for (i in 0 until maxNumberOfLines) {
            for (j in 0 until numberOfPoints) {
                randomNumbersTab[i][j] = Math.random().toFloat() * 100f
            }
        }
    }
    private fun reset() {
        numberOfLines = 1
        hasAxes = true
        hasAxesNames = true
        hasLines = true
        hasPoints = true
        shape = ValueShape.CIRCLE
        isFilled = false
        hasLabels = false
        isCubic = false
        hasLabelForSelected = false
        pointsHaveDifferentColor = false
        chart!!.setValueSelectionEnabled(hasLabelForSelected)
        resetViewport()
    }

    private fun resetViewport() { // Reset viewport height range to (0,100)
        val v = Viewport(chart.getMaximumViewport())
        v.bottom = 0f
        v.top = 100f
        v.left = 0f
        v.right = numberOfPoints - 1f
        chart.setMaximumViewport(v)
        chart.setCurrentViewport(v)
    }

    private fun generateData() {
        val lines: MutableList<Line> =
            ArrayList()
        for (i in 0 until numberOfLines) {
            val values: MutableList<PointValue> = ArrayList()
            for (j in 0 until 30) {
                values.add(PointValue(j.toFloat(), j*5f))
            }
            val line = Line(values)
            line.color = ChartUtils.COLORS[i]
            line.setShape(shape)
            line.isCubic = isCubic
            line.isFilled = isFilled
            line.setHasLabels(hasLabels)
            line.setHasLabelsOnlyForSelected(hasLabelForSelected)
            line.setHasLines(hasLines)
            line.setHasPoints(hasPoints)
            if (pointsHaveDifferentColor) {
                line.pointColor = ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.size]
            }
            lines.add(line)
        }
        data = LineChartData(lines)
        if (hasAxes) {
            val axisX = Axis()
            val axisY: Axis = Axis().setHasLines(true)
            if (hasAxesNames) {
                axisX.setName("Date")
                axisY.setName("Confirmed")
            }
            data!!.setAxisXBottom(axisX)
            data!!.setAxisYLeft(axisY)
        } else {
            data!!.setAxisXBottom(null)
            data!!.setAxisYLeft(null)
        }
        data!!.setBaseValue(Float.NEGATIVE_INFINITY)
        chart!!.setLineChartData(data)
    }

    /**
     * Adds lines to data, after that data should be set again with
     * [LineChartView.setLineChartData]. Last 4th line has non-monotonically x values.
     */
    private fun addLineToData() {
        if (data!!.getLines().size >= maxNumberOfLines) {
            Toast.makeText(activity, "Samples app uses max 4 lines!", Toast.LENGTH_SHORT)
                .show()
            return
        } else {
            ++numberOfLines
        }
        generateData()
    }

    private fun toggleLines() {
        hasLines = !hasLines
        generateData()
    }

    private fun togglePoints() {
        hasPoints = !hasPoints
        generateData()
    }

    private fun toggleGradient() {
        hasGradientToTransparent = !hasGradientToTransparent
        generateData()
    }

    private fun toggleCubic() {
        isCubic = !isCubic
        generateData()
        if (isCubic) { // It is good idea to manually set a little higher max viewport for cubic lines because sometimes line
// go above or below max/min. To do that use Viewport.inest() method and pass negative value as dy
// parameter or just set top and bottom values manually.
// In this example I know that Y values are within (0,100) range so I set viewport height range manually
// to (-5, 105).
// To make this works during animations you should use Chart.setViewportCalculationEnabled(false) before
// modifying viewport.
// Remember to set viewport after you call setLineChartData().
            val v = Viewport(chart.getMaximumViewport())
            v.bottom = -5f
            v.top = 105f
            // You have to set max and current viewports separately.
            chart.setMaximumViewport(v)
            // I changing current viewport with animation in this case.
            chart.setCurrentViewportWithAnimation(v)
        } else { // If not cubic restore viewport to (0,100) range.
            val v = Viewport(chart.getMaximumViewport())
            v.bottom = 0f
            v.top = 100f
            // You have to set max and current viewports separately.
// In this case, if I want animation I have to set current viewport first and use animation listener.
// Max viewport will be set in onAnimationFinished method.
            chart.setViewportAnimationListener(object : ChartAnimationListener {
                override fun onAnimationStarted() { // TODO Auto-generated method stub
                }

                override fun onAnimationFinished() { // Set max viewpirt and remove listener.
                    chart.setMaximumViewport(v)
                    chart.setViewportAnimationListener(null)
                }
            })
            // Set current viewpirt with animation;
            chart.setCurrentViewportWithAnimation(v)
        }
    }

    private fun toggleFilled() {
        isFilled = !isFilled
        generateData()
    }

    private fun togglePointColor() {
        pointsHaveDifferentColor = !pointsHaveDifferentColor
        generateData()
    }

    private fun setCircles() {
        shape = ValueShape.CIRCLE
        generateData()
    }

    private fun setSquares() {
        shape = ValueShape.SQUARE
        generateData()
    }

    private fun setDiamonds() {
        shape = ValueShape.DIAMOND
        generateData()
    }

    private fun toggleLabels() {
        hasLabels = !hasLabels
        if (hasLabels) {
            hasLabelForSelected = false
            chart.setValueSelectionEnabled(hasLabelForSelected)
        }
        generateData()
    }

    private fun toggleLabelForSelected() {
        hasLabelForSelected = !hasLabelForSelected
        chart.setValueSelectionEnabled(hasLabelForSelected)
        if (hasLabelForSelected) {
            hasLabels = false
        }
        generateData()
    }

    private fun toggleAxes() {
        hasAxes = !hasAxes
        generateData()
    }

    private fun toggleAxesNames() {
        hasAxesNames = !hasAxesNames
        generateData()
    }

    /**
     * To animate values you have to change targets values and then call [Chart.startDataAnimation]
     * method(don't confuse with View.animate()). If you operate on data that was set before you don't have to call
     * [LineChartView.setLineChartData] again.
     */
    private fun prepareDataAnimation() {
        for (line in data!!.getLines()) {
            for (value in line.values) { // Here I modify target only for Y values but it is OK to modify X targets as well.
                value.setTarget(value.x, Math.random().toFloat() * 100)
            }
        }
    }

    private class ValueTouchListener : LineChartOnValueSelectListener {
        override fun onValueSelected(
            lineIndex: Int,
            pointIndex: Int,
            value: PointValue
        ) {

        }

        override fun onValueDeselected() { // TODO Auto-generated method stub
        }
    }


}