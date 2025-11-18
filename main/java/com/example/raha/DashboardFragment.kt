package com.example.raha

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.raha.databinding.FragmentDashboardBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.components.Legend
import kotlin.properties.Delegates

private var drawBarShadow by Delegates.notNull<Boolean>()
private var XAxis.granularityEnabled: Boolean
    get() {
        TODO()
    }
    set(value) {}

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBarChart()
        updateDashboardData()

        binding.fabAddSession.setOnClickListener { 
            startActivity(Intent(requireContext(), AddSessionActivity::class.java))
        }
    }

    private fun setupBarChart() {
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, 7.5f)) // Lundi
        entries.add(BarEntry(1f, 6.8f)) // Mardi
        entries.add(BarEntry(2f, 8.2f)) // Mercredi
        entries.add(BarEntry(3f, 7.0f)) // Jeudi
        entries.add(BarEntry(4f, 7.9f)) // Vendredi
        entries.add(BarEntry(5f, 9.1f)) // Samedi
        entries.add(BarEntry(6f, 8.5f)) // Dimanche

        val dataSet = BarDataSet(entries, "Durée de sommeil (heures)")

        // MODIFICATION 1: Changer les couleurs du dégradé des barres
        dataSet.setGradientColor(
            ContextCompat.getColor(requireContext(), R.color.purple_200), // Nouvelle couleur de début
            ContextCompat.getColor(requireContext(), R.color.teal_200) // Nouvelle couleur de fin
        )

        // MODIFICATION 2: Afficher les valeurs sur les barres et les personnaliser
        dataSet.valueTextColor = Color.RED // Nouvelle couleur du texte des valeurs
        dataSet.setDrawValues(true) // Afficher les valeurs sur les barres
        dataSet.valueTextSize = 10f // Taille du texte des valeurs

        // Désactiver l'ombre des barres pour un design plus net, surtout avec un dégradé.
        dataSet.copy(false)

        val barData = BarData(dataSet)
        // MODIFICATION 3: Ajuster la largeur des barres
        barData.barWidth = 0.7f // Largeur des barres augmentée
        binding.barChartWeeklyDuration.data = barData

        // Customize X-axis
        val xAxis = binding.barChartWeeklyDuration.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(listOf("Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"))
        xAxis.setCenterAxisLabels(false)
        xAxis.setDrawGridLines(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setGranularity(1f)
        xAxis.granularityEnabled = true // Corrected: Using property access syntax
        xAxis.textColor = Color.BLACK
        // xAxis.setLabelRotationAngle(45f) // Optionnel: Faire pivoter les labels de l'axe X si trop longs

        // Customize Y-axis (left)
        val leftYAxis = binding.barChartWeeklyDuration.axisLeft
        leftYAxis.setDrawGridLines(true)
        leftYAxis.axisMinimum = 0f
        leftYAxis.axisMaximum = 10f
        leftYAxis.textColor = Color.BLACK

        // Disable right Y-axis
        binding.barChartWeeklyDuration.axisRight.isEnabled = false

        // General chart customization
        binding.barChartWeeklyDuration.setFitBars(true)
        binding.barChartWeeklyDuration.description.isEnabled = false
        // MODIFICATION 4: Personnaliser la légende
        val legend = binding.barChartWeeklyDuration.legend
        legend.isEnabled = true
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)
        legend.textColor = Color.GRAY

        // MODIFICATION 5: Activer le zoom et le défilement
        binding.barChartWeeklyDuration.setPinchZoom(true)
        binding.barChartWeeklyDuration.setDragEnabled(true)
        binding.barChartWeeklyDuration.setScaleEnabled(true)

        binding.barChartWeeklyDuration.animateY(1000)
        binding.barChartWeeklyDuration.invalidate()
    }

    private fun updateDashboardData() {
        binding.textViewDailySummary.text = "7h 15m - Qualité : Bonne"
        binding.textViewInsight.text = "Dormez plus tôt pour une meilleure qualité de sommeil."
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

private fun BarDataSet.copy(barDataSet: Boolean) {
        TODO("Not yet implemented")
}
