package de.thm.ap.records.model

import androidx.room.Entity


@Entity
class Stats(var recordsStat: List<Record>) {

    var sumCrp = setDateForSats()
    var crpToEnd = 180 - setDateForSats()
    var sumHalfWeighted = calculateSumHalfWeighted()
    var averageMark= 0
    var listSize = recordsStat.size


    fun setDateForSats(): Int {
        var sum = 0
        for(item in recordsStat){
            sum += item.crp
        }
        return sum
    }

    fun calculateSumHalfWeighted(): Int {
        var sum = 0
        for(item in recordsStat){
            sum += if(item.isHalfWeighted) 1 else 0
        }
        return sum
    }

    fun calculateAverageM(): Int {
        var sum = 0
        var avg = 0
        var crp = 0
        recordsStat.let { it ->
            it.forEach {
                if (it.mark != 0) {
                    sum += if (it.isHalfWeighted) ((it.mark * it.crp) / 2) else (it.mark * it.crp)
                    crp += if (it.isHalfWeighted) (it.crp / 2) else it.crp
                }
            }
            avg = if(sum > 0 ) sum / crp else 0
        }
        return avg
    }

    init {
        setDateForSats()
        calculateSumHalfWeighted()
        averageMark = calculateAverageM()
    }

    override fun toString(): String {
        super.toString()

        return "Leistungen  ${listSize}\n50% Leistungen ${sumHalfWeighted}\nSumme Crp ${sumCrp}\n" +
                "Crp bis Ziel ${crpToEnd}\nDurchschnitt ${averageMark}%"
    }
}